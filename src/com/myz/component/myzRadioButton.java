/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;

import javafx.scene.Node;
import javafx.scene.control.RadioButton;

/**
 *
 * @author Montazar Hamoud
 */
public class myzRadioButton extends RadioButton implements myzComponent
{
    String m_fieldName = "";
    String m_caption   = "";
    //constructor
    public myzRadioButton()
    {
        super();
    }
    public myzRadioButton(String text)
    {
        super(text);
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
        return "";
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
        return null;
    }

    @Override
    public Node getNode()
    {
        return this;
    }

}
