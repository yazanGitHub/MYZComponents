/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;

import javafx.scene.Node;
import javafx.scene.control.PasswordField;

/**
 *
 * @author user
 */
public class myzPasswordField extends PasswordField implements myzComponent
{

    //Data member
    boolean m_isMandatory = true ;
    String  m_fieldName   = "";
    String  m_caption     = "";


    @Override
    public void refreshCaption()
    {

    }

    @Override
    public void removeData()
    {
        clear();
    }

    @Override
    public void setIsMandatory(boolean isMandatory)
    {
        //Do nothing
    }

    @Override
    public boolean getIsMandatory()
    {
        return true ;
    }

    @Override
    public boolean checkEmptyAndMandatory()
    {
        return true & ("".equals( getText() ) ) ;
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
        return m_fieldName + " = " + "'" + getText() + "'";
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
        return getText();
    }
    @Override
    public Node getNode()
    {
        return this;
    }


}
