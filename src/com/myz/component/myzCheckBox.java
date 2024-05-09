/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;

/**
 *
 * @author user
 */
public class myzCheckBox extends CheckBox implements myzComponent
{
    String m_fieldName = null;
    String m_caption   = null;
    //constructor

    public myzCheckBox(String text)
    {
        super(text);
        m_caption = text;
    }

    @Override
    public void refreshCaption()
    {

    }

    @Override
    public void removeData()
    {
        setSelected(false);
    }

    @Override
    public void setIsMandatory(boolean isMandatory)
    {
        //Do nothing
    }

    @Override
    public boolean getIsMandatory()
    {
        return false ;
    }

    @Override
    public boolean checkEmptyAndMandatory()
    {
        return false ;
    }

    @Override
    public void resetStyle()
    {
        //Do nothing
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
        setText(caption);
    }

    @Override
    public String getCaption()
    {
        return m_caption;
    }
    @Override
    public String getSQLWhere()
    {
        return m_fieldName + " = " +   (isSelected() ? "'1'" : "'0'") ;
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
    public Object getValue()
    {
        return isSelected();
    }
    @Override
    public Node getNode()
    {
        return this;
    }

}
