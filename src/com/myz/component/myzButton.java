/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;


import static com.myz.bundle.MYZResorceBundle.BUNDLE;

import com.myz.bundle.MYZResorceBundle;
import com.myz.log.logWriter;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * @author yazan
 */
public class myzButton extends Button implements myzComponent
{
    //Class member
    public static final String  IDLE_BUTTON_STYLE    = "-fx-background-color: transparent;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;";
    //Data member
    myzScene  m_scene       = null;
    String    m_captionKey  = null;
    Pane      m_parentPane  = null;


    public myzButton()
    {
        super();
        setOnMouseClicked(new myzButton_actionAdapter(this));
        setStyle(IDLE_BUTTON_STYLE);
        setOnMouseEntered(e -> setStyle(HOVERED_BUTTON_STYLE));
        setOnMouseExited(e -> setStyle(IDLE_BUTTON_STYLE));
    }
    public myzButton(String text)
    {
        super();
        setCaption(text);
        setOnMouseClicked(new myzButton_actionAdapter(this));
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
        String str   = null ;
        try
        {
           str = MYZResorceBundle.getCaption(key);
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
        if ( str != null)
            setText(str);
        else
            setText(key);
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

    // override this method on the button you created
    public  void buttonPressed(){}

    @Override
    public void removeData()
    {
        //Do nothing
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
        //do Nothing
    }
    @Override
    public String getFieldName()
    {
        return "";
    }
    @Override
    public String getCaption()
    {
        if(m_captionKey == null)
            return "";

        String str   = null ;
        try
        {
           str = MYZResorceBundle.getCaption(m_captionKey);
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
        if ( str != null)
           return str;
        else
            return m_captionKey;
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


class myzButton_actionAdapter implements EventHandler
{
    private myzButton m_button;
    myzButton_actionAdapter (myzButton button)
    {
        m_button = button;
    }

    @Override
    public void handle(Event event)
    {
        m_button.buttonPressed();
    }


}
