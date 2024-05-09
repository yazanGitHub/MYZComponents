/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import com.myz.connection.ConnectionContext;
import com.myz.connection.arConnectionInfo;
import com.myz.log.logWriter;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author yazan
 */
public class myMultiSelComboBox extends ComboBox implements myzComponent
{

    Pane                               m_parentPane  = null;
    String                             m_sql         = "";
    String                             m_columnName  = "";
    boolean                            m_isMandatory = false;
    private String                     m_fieldName   = "";
    String                             m_caption     = "";

    private Vector                     m_items       = new Vector<>();

    public myMultiSelComboBox()
    {
        valueProperty().addListener((obs, oldVal, newVal)-> {   if (oldVal != newVal) this.selectionChange();  });
        setPrefWidth(180);
        setButtonCell(m_buttonCell);
    }
    public myMultiSelComboBox(String SQL , String columnName)
    {
        valueProperty().addListener((obs, oldVal, newVal)-> {   if (oldVal != newVal) this.selectionChange();  });
        setPrefWidth(180);
        setButtonCell(m_buttonCell);

        m_sql        = SQL;
        m_columnName = columnName;

        ConnectionContext connection   = arConnectionInfo.getConnectionContext() ;
        myzComboBoxItem   comboBoxItem = null ;
        ResultSet         rs           = null ;
        Statement         st           = null ;

        try
        {
            st = connection.m_connection.createStatement() ;
            rs = st.executeQuery(m_sql);
            while(rs.next())
            {
                comboBoxItem = new myzComboBoxItem(rs.getString(m_columnName), rs.getInt("PNR") );
                addItems(comboBoxItem);
            }
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
    }

    //Deep Copy Constructer
    myMultiSelComboBox (myMultiSelComboBox basic)
    {
        this.m_sql        = new String(basic.m_sql);
        this.m_columnName = new String(basic.m_columnName);
        this.m_fieldName  = new String(basic.m_fieldName);
        this.m_caption    = new String(basic.m_caption);
        Vector basicItems = basic.getAllItems();
        for (Object basicItem2 : basicItems) {
            myzComboBoxItem basicItem  = (myzComboBoxItem) basicItem2;
            myzComboBoxItem newItem    = new myzComboBoxItem(basicItem.getValue(),basicItem.getkey() , basicItem.getExtraData());
            this.addItems(newItem);
        }
    }

    //Reload combobox data after insert or update on table
    public void reload()
    {
        arConnectionInfo.CONNECTION    = arConnectionInfo.CONNECTION_DIRECT;
        ConnectionContext connection   = arConnectionInfo.getConnectionContext() ;
        myzComboBoxItem   comboBoxItem = null ;
        ResultSet         rs           = null ;
        Statement         st           = null ;
        //Remove all previouse items
        deleteAllItems();
        try
        {
            st = connection.m_connection.createStatement() ;
            rs = st.executeQuery(m_sql);
            while(rs.next())
            {
                comboBoxItem = new myzComboBoxItem(rs.getString(m_columnName), rs.getInt("PNR") );
                addItems(comboBoxItem);
            }
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
    }
    public void select(int pnr)
    {
        ObservableList items = getItems();
        for(Object object : items)
        {
            myzComboBoxItem comboBoxItem = (myzComboBoxItem) object ;
            if(comboBoxItem.getkey() == pnr)
                getSelectionModel().select(comboBoxItem);
        }
    }
    public void selectionChange(){ }

    public void setParentPane(Pane pane)
    {
        m_parentPane = pane;
    }

    public Pane getParentPane()
    {
        return m_parentPane;
    }

    public void setReSizeOnParentSize(boolean b)
    {
        if (b && m_parentPane != null)
        {
            prefWidthProperty().bind(m_parentPane.widthProperty());
        }
    }

    private ListCell<CheckBox> m_buttonCell = new ListCell<>()
    {
        @Override
		protected void updateItem(CheckBox item, boolean empty)
        {
            super.updateItem(item, empty);
            setText("");
        }
    };

    public void addItems(myzComboBoxItem ... item )
    {
        for(myzComboBoxItem temp : item)
        {
            CheckBox cb = new CheckBox(temp.toString());
            cb.addEventHandler(MouseEvent.MOUSE_CLICKED, onCheckBoxClick);
            m_items.add(temp);
            getItems().addAll(cb);
        }
    }

    public void addItems(Vector items)
    {
        for ( int i = 0 ; i < items.size() ; i++)
        {
            myzComboBoxItem item = (myzComboBoxItem) items.elementAt(i);
            CheckBox cb = new CheckBox(item.toString());
            cb.addEventHandler(MouseEvent.MOUSE_CLICKED, onCheckBoxClick);
            m_items.add(item);
            getItems().addAll(cb);
        }
    }
    public ArrayList getSelectedIntValues()
    {
        ArrayList <Integer> intVal = new ArrayList<>();
        for ( int i = 0; i < getItems().size() ; i++)
        {
            CheckBox checkBox = (CheckBox) getItems().get(i);
            if ( checkBox.isSelected())
            {
                myzComboBoxItem item = (myzComboBoxItem) m_items.get(i);
                intVal.add(item.getkey());
            }
        }
        return intVal;
    }

    public ArrayList getSelectedStringValues()
    {
        ArrayList <String> stringVal = new ArrayList<>();
        for ( int i = 0; i < getItems().size() ; i++)
        {
            CheckBox checkBox = (CheckBox) getItems().get(i);
            if ( checkBox.isSelected())
            {
                myzComboBoxItem item = (myzComboBoxItem) m_items.get(i);
                stringVal.add(item.getValue());
            }
        }
        return stringVal;
    }

    public ArrayList getSelectedExtraDataValues()
    {
        ArrayList <Object> extraDataValues = new ArrayList<>();
        for ( int i = 0; i < getItems().size() ; i++)
        {
            CheckBox checkBox = (CheckBox) getItems().get(i);
            if ( checkBox.isSelected())
            {
                myzComboBoxItem item = (myzComboBoxItem) m_items.get(i);
                extraDataValues.add(item.getExtraData());
            }
        }
        return extraDataValues;
    }

    public ArrayList getSelectedComboBoxItemValues()
    {
        ArrayList <Object> itemValues = new ArrayList<>();
        for ( int i = 0; i < getItems().size() ; i++)
        {
            CheckBox checkBox = (CheckBox) getItems().get(i);
            if ( checkBox.isSelected())
            {
                myzComboBoxItem item = (myzComboBoxItem) m_items.get(i);
                itemValues.add(m_items.get(i));
            }
        }
        return itemValues;
    }

    public Vector getAllItems()
    {
        return m_items;
    }

    public void deleteAllItems()
    {
        m_items.clear();
        getItems().clear();
    }

    @Override
    public void refreshCaption()
    {

    }

    @Override
    public void removeData()
    {
        setValue(null);
    }

    @Override
    public void setIsMandatory(boolean isMandatory)
    {
        m_isMandatory = isMandatory ;
    }

    @Override
    public boolean getIsMandatory()
    {
        return m_isMandatory ;
    }
    @Override
    public void setFieldName(String fieldName)
    {
        m_fieldName = fieldName;
    }
    @Override
    public String getFieldName()
    {
        return m_fieldName;
    }
    @Override
    public void setCaption(String caption)
    {
        m_caption = caption;
    }
    @Override
    public String getCaption()
    {
        return m_caption;
    }
    @Override
    public String getSQLWhere()
    {
        if ( m_fieldName == null || m_fieldName.length() < 1)
            return " 1 = 1 ";

        ArrayList<Integer> intVal = getSelectedIntValues();
        if ( intVal.size() < 1)
            return " 1 = 1 " ;

        String where = "";
        for ( int i = 0 ; i < intVal.size() ; i++ )
        {
            if ( i > 0 )
                where += " , ";
            if ( i == 0)
                where = m_fieldName + " IN ( ";
            where += intVal.get(i).intValue();
        }
        where += " ) ";
        return where;
    }
    @Override
    public String getSQLWhereBefor()
    {
        return getSQLWhere();
    }
    @Override
    public String getSQLWhereAfter()
    {
        return getSQLWhere();
    }

    @Override
    public boolean checkEmptyAndMandatory()
    {
//        boolean result = getIsMandatory() & (getIntValue() == -1) ;
//        if(result)
//            getStyleClass().add(CLASS_ERROR);
//        return result ;
        return false;
    }

    @Override
    public void resetStyle()
    {
        getStyleClass().removeAll("error");
    }

    @Override
    protected Skin<?> createDefaultSkin()
    {
    return new myMultiSelComboBoxSkin(this);
    }




//  setButtonCell(buttonCell);
//    /******************************************
//     * Button Cell
//     ***********************************************************/
//    private ListCell<ComboUiVO> buttonCell = new ListCell<ComboUiVO>()
//    {
//    protected void updateItem(ComboUiVO item, boolean empty)
//    {
//        super.updateItem(item, empty);
//        setText("");
//    }
//    };
//
//

    /**************************************************
     * Combo Check
     ****************************************************/
    private EventHandler<MouseEvent> onCheckBoxClick = new EventHandler<>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            setSelectedItemCaption();
        }
    };

    private void setSelectedItemCaption()
    {
        String caption = "";
        for (Object element : getItems()) {
            CheckBox checkBox = (CheckBox) element;
            if(checkBox.isSelected())
            {
                caption += " ' " + checkBox.getText() + " ' ";
            }
        }
        setValue(caption);
    }

    @Override
    public Node getNode()
    {
        return this;
    }


}

//----------------------------------------------------------------------------------------------------------
class myMultiSelComboBoxSkin extends ComboBoxListViewSkin
{
    public myMultiSelComboBoxSkin(myMultiSelComboBox comboBox)
    {
        super(comboBox);
    }

    @Override
    protected boolean isHideOnClickEnabled()
    {
        return false;
    }
}


