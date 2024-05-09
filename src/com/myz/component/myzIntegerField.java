/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * @author yazan
 */
public class myzIntegerField extends TextField implements myzComponent
{
    //Member
    Pane      m_parentPane  = null;
    String    m_fieldName   = "";
    String    m_caption     = "";
    boolean   m_isManatory  = false;
    //Constructer
    public myzIntegerField()
    {
        this.textProperty().addListener(new myzIntegerField_actionAdapter(this));
    }


    //Method
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
            prefHeightProperty().bind(m_parentPane.heightProperty());
            prefWidthProperty().bind(m_parentPane.widthProperty());
        }

    }
    public void onValueChange()
    {

    }

    public void setValue(int value)
    {
        setText(String.valueOf(value));
    }
    public int getIntValue()
    {
        if(!"".equals(getText()))
            return  Integer.parseInt(getText());
        else
            return 0 ;
    }

    @Override
    public Object getValue()
    {
        if(!"".equals(getText()))
            return new Integer (Integer.parseInt(getText()));
        else
            return null ;
    }

    @Override
    public void refreshCaption()
    {
    }

    @Override
    public void removeData()
    {
        setText("");
    }

    @Override
    public void setIsMandatory(boolean isMandatory)
    {
        m_isManatory = isMandatory;
    }

    @Override
    public boolean getIsMandatory()
    {
        return m_isManatory;
    }

    @Override
    public boolean checkEmptyAndMandatory()
    {
        boolean resulte = getIsMandatory() && getValue() == null;
        if ( resulte)
        {
            getStyleClass().add(CLASS_ERROR);
        }
        return resulte;
    }

    @Override
    public void resetStyle()
    {
        getStyleClass().removeAll(CLASS_ERROR);
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
        if ( getValue() != null)
        {
            return m_fieldName + " = " + getIntValue();
        }
        return "";
    }
    @Override
    public String getSQLWhereBefor()
    {
        if ( getValue() != null)
        {
            return m_fieldName + " <= " + getIntValue();
        }
        return "";
    }
    @Override
    public String getSQLWhereAfter()
    {
        if ( getValue() != null)
        {
            return m_fieldName + " >= " + getIntValue();
        }
        return "";        }


    @Override
    public Node getNode()
    {
        return this;
    }

}

class myzIntegerField_actionAdapter implements ChangeListener
{
    //Member
    private myzIntegerField m_integerField;

    //Constructer
    myzIntegerField_actionAdapter (myzIntegerField integerField)
    {
        m_integerField = integerField;
    }


    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue)
    {
        String newVal  = (String) newValue;
        String oldVal  = (String) oldValue;

        if (newVal != null && !newVal.matches("\\d*"))
        {
            m_integerField.setText(newVal.replaceAll("[^\\d]", ""));
        }
        m_integerField.onValueChange();
    }


}

