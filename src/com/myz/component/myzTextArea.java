/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;

import javafx.scene.Node;
import javafx.scene.control.TextArea;

/**
 *
 * @author Montazar Hamoud
 */
public class myzTextArea extends TextArea implements myzComponent
{
    String m_fieldName = "";
    String m_caption   = "";

    //Constructor
    public myzTextArea()
    {
        super();
    }

    //Data member
    boolean m_isMandatory = false ;

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
        return getIsMandatory() & ("".equals( getText() )) ;
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
        return m_fieldName + " like " + "'" + getText() + "'";
    }


    @Override
    public String getSQLWhereBefor()
    {
        return  m_fieldName + " <= " + "'" + getText() + "'";
    }

    @Override
    public String getSQLWhereAfter()
    {
        return  m_fieldName + " >= " + "'" + getText() + "'";
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
