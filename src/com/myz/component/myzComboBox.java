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
import com.sun.javafx.collections.ObservableSequentialListWrapper;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;



/**
 * @author yazan
 */
public class myzComboBox extends ComboBox<myzComboBoxItem>  implements myzComponent
{
    Pane    m_parentPane   = null;
    String  m_sql          = "";
    String  m_columnName   = "";
    boolean m_isMandatory  = false ;
    boolean m_isSearchable = false ;
    String  m_caption      = "";
    String  m_fieldName    = "";

    private ObservableList<myzComboBoxItem> m_basicItemsList ;
    // MONTAZAR MODIFY THIS METHOD
    public myzComboBox()
    {
        setBasicsForNewObjects();
    }
    // ALSO THIS JUST TO USE setBasicsForNewObjects METHOD
    public myzComboBox(String SQL , String columnName , boolean isSearchable)
    {
        setBasicsForNewObjects();

        m_sql        = SQL ;
        m_columnName = columnName ;

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

            allowSearchForItem(isSearchable);

//                new AutoCompleteBox(this);
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
        finally
        {
            try
            {
                if(rs != null){rs.close();}
                if(st != null){st.close();}

            }
            catch(Exception ex)
            {
                logWriter.write(ex);
            }
        }
    }
    // MONTAZAR ADD THIS METHOD
    private void setBasicsForNewObjects()
    {
        valueProperty().addListener((obs, oldVal, newVal)-> {   if (oldVal != newVal) this.selectionChange();  });
        setPrefWidth(180);
        m_basicItemsList = new ObservableSequentialListWrapper<>(new ArrayList<>());
        this.setConverter(new myzComboBoxConverter(this) );

    }
    // END SCOPE
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
    // MONTAZAR MODIFY THIS METHOD
    public void addItems(myzComboBoxItem ... item )
    {
        m_basicItemsList.addAll(item);
        getItems().addAll(item);
    }
    // END SCOPE

    public int getIntValue()
    {
        myzComboBoxItem item = getValue();
        if (item != null)
        {
            return item.getkey();
        }
        return -1;
    }

    public String getStringValue()
    {
        myzComboBoxItem item = getValue();
        if (item != null)
        {
            return item.getValue();
        }
        return "";
    }

    public Object getExtraDataValue()
    {
        myzComboBoxItem item = getValue();
        if (item != null)
        {
            return item.getExtraData();
        }
        return null;
    }

    public myzComboBoxItem getItemValue()
    {
        myzComboBoxItem item = getValue();
        if (item != null)
        {
            return item;
        }
        return null;
    }
    // MONTAZAR MODIFY THIS METHOD
    public void deleteAllItems()
    {
        getItems().clear();
        m_basicItemsList.clear();
    }
    // END SCOPE
    //This function use when update on (static tables) then i have to reload combobox data dynamically
    //Reload combobox data after insert or update on table
    public void refreshData()
    {
        ConnectionContext connection        = arConnectionInfo.getConnectionContext() ;
        myzComboBoxItem   comboBoxItem      = null ;
        ResultSet         rs                = null ;
        Statement         st                = null ;
        myzComboBoxItem   selectedItem      = getSelectionModel().getSelectedItem();

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
            //Reselect the choosen item
            if(selectedItem != null)
            {
                getSelectionModel().select(selectedItem);
            }

        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
        finally
        {
            try
            {
                if(rs != null){rs.close();}
                if(st != null){st.close();}

            }
            catch(Exception ex)
            {
                logWriter.write(ex);
            }
        }
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
    public boolean checkEmptyAndMandatory()
    {
        boolean result = getIsMandatory() & (getIntValue() == -1) ;
        if(result)
            getStyleClass().add(CLASS_ERROR);
        return result ;
    }

    @Override
    public void resetStyle()
    {
        getStyleClass().removeAll("error");
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

    public void setIsSearchable(boolean searchable)
    {
        m_isSearchable = searchable ;
    }

    @Override
    public String getCaption()
    {
        return m_caption;
    }

    @Override
    public String getSQLWhere()
    {
        return m_fieldName + " = " + getIntValue();
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

    private boolean getIsSearchable()
    {
        return m_isSearchable ;
    }


    @Override
    public Node getNode()
    {
        return this;
    }

    public void addItems(Vector items)
    {
        for ( int i = 0 ; i < items.size() ; i++)
        {
            myzComboBoxItem item = (myzComboBoxItem) items.elementAt(i);
            addItems(item);
        }
    }

    // MONTAZAR ADD THIS METHOD AND USED WHEN YOU WANT TO MAKE COMBOSEARCHABLE  like allowSearchForItem(true);
    private void allowSearchForItem(boolean allow)
    {
        if ( allow )
        {
            this.setEditable(true);


            this.getEditor().textProperty().addListener((obs, oldValue, newValue) ->
            {
                boolean addCharacter    = newValue.length() > oldValue.length();
                boolean deleteCharacter = newValue.length() < oldValue.length();
                boolean emptyText       = newValue.length() == 0;

                Platform.runLater(() ->
                {

                    if ( emptyText )
                    {
                        this.getItems().clear();
                        this.getItems().addAll(m_basicItemsList);
                    }
                    else if ( addCharacter)
                    {
                        for ( int i = 0 ; i < this.getItems().size() ; i++ )
                        {
                            myzComboBoxItem item = this.getItems().get(i);
                            if (! item.getValue().toLowerCase().startsWith(newValue.toLowerCase()) )
                            {
                                this.getItems().remove(i);
                                i--;
                            }
                        }
                    }
                    else if ( deleteCharacter )
                    {
                        this.getItems().clear();
                        for ( int i = 0 ; i < this.m_basicItemsList.size() ; i++ )
                        {
                            myzComboBoxItem item = m_basicItemsList.get(i);
                            if (item.getValue().toLowerCase().startsWith(newValue.toLowerCase()))
                            {
                                this.getItems().add(item);
                            }
                        }
                    }
                });
            });
        }
        else
        {
            this.setEditable(false);
            // TODO remove the listener
            this.getItems().clear();
            this.getItems().addAll(m_basicItemsList);
        }
    }
    // END SCOPE


}



// MONTAZAR ADD THIS CLASS
class myzComboBoxConverter extends StringConverter<myzComboBoxItem>
{
    myzComboBox  m_comboBox;
    myzComboBoxConverter(myzComboBox combo)
    {
        super();
        m_comboBox = combo;
    }
    @Override
    public String toString(myzComboBoxItem object)
    {
        if ( object != null)
            return object.getValue();
        else
            return null;
    }

    @Override
    public myzComboBoxItem fromString(String string)
    {
       return m_comboBox.getItems().stream().filter(item -> item.getValue().equals(string)).findFirst().orElse(null);
    }

}