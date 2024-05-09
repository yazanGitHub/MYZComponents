/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;

import static com.myz.bundle.MYZResorceBundle.BUNDLE;

import com.myz.log.logWriter;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * @author yazan
 */
public class myzLabel extends Label implements myzComponent
{
    myzScene  m_scene       = null;
    String    m_captionKey  = null;
    Pane      m_parentPane  = null;
    String    m_caption     = "";

    public void buttonPressed(){}

    public myzLabel()
    {
        super();
    }
    public myzLabel(String text)
    {
        super();
        setText(text);
        setFont(Font.font(null , FontWeight.SEMI_BOLD , 16));
    }


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

    @Override
	public void setCaption(String key)
    {
        m_captionKey = key;
        String str = null;
        try
        {
            str = BUNDLE.getString( key );
        }
        catch( Exception ex )
        {
            logWriter.write(ex);
        }
        if ( str != null)
            setText(str);
        else
            setText(key);
    }
    @Override
    public void removeData()
    {
        //Do nothing
    }
    @Override
    public void refreshCaption()
    {
        if (m_captionKey !=  null)
        {
            String str = BUNDLE.getString(m_captionKey);
            setText(str);
        }
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

    }

    @Override
    public String getFieldName()
    {
        return "";
    }

    @Override
    public String getCaption()
    {
        return "";
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
