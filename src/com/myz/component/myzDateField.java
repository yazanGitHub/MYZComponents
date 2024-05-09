/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;


/**
 *
 * @author user
 */
public class myzDateField extends DatePicker implements myzComponent
{
    //Class member
    public static final DateTimeFormatter DATE_TIME_FORMATER_SAVE =  DateTimeFormatter.ISO_LOCAL_DATE;// format is yyyy-MM-dd


    public static final String            DATE_PATTERN_VIEW       = "dd-MM-yyyy";
    public static final DateTimeFormatter DATE_TIME_FORMATER_VIEW = DateTimeFormatter.ofPattern(DATE_PATTERN_VIEW);

    //Data member
    boolean m_isMandatory = false ;
    String  m_fieldName   = "";
    String  m_caption     = "";

    public myzDateField()
    {
        super();
        this.setConverter(new StringDateConverter(this));
        //Add listener to date picker :
        // 1- on (t) click set today date
        // 2- put (-) char auto
        getEditor().textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                            if ( newValue.length() > 10)
                {
                    getEditor().setText(newValue.substring(0, 10));
                    return;
                }
                if( (newValue.equalsIgnoreCase("t") || newValue.equalsIgnoreCase("ف"))  && oldValue.length() == 0)
                {
                    Date date = new Date(System.currentTimeMillis());
                    setValue(LocalDate.parse( date.toString()));
                }
                else if(newValue.length() == 2 || newValue.length() == 5)
                {
                    getEditor().setText(newValue.concat("-"));
                }

            }
        });
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
    //Methods
    public String getDate()
    {
        String    date  = "";
        LocalDate local = getValue()  ;
        if(local != null)
        {
            date = DATE_TIME_FORMATER_SAVE.format(local) ;
        }
        return date ;
    }
    public void setDate(String stringDate)
    {

        if(stringDate == null || stringDate.trim().isEmpty())
        {
            setValue(null);
            return ;
        }
        // dont need to convert date cuz LocalDate take date as yyyy-MM-dd
        setValue(LocalDate.parse(stringDate));

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
        boolean result = getIsMandatory() & ( "".equals( getDate() )  ) ;
        if(result)
            getStyleClass().add(CLASS_ERROR);
        return result ;
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
        return m_fieldName + " = " + "'" +  getDate() + "'";
    }
    @Override
    public String getSQLWhereBefor()
    {
        return m_fieldName + " <= " + "'" +  getDate() + "'";
    }
    @Override
    public String getSQLWhereAfter()
    {
        return m_fieldName + " >= " + "'" +  getDate() + "'";
    }

    @Override
    public Node getNode()
    {
        return this;
    }

}

class StringDateConverter extends StringConverter
{
    myzDateField m_dateField;
    public StringDateConverter(myzDateField dateField)
    {
        m_dateField = dateField;
        m_dateField.setPromptText(myzDateField.DATE_PATTERN_VIEW.toLowerCase());

    }
    //whats wrtie on the field
    @Override
    public String toString(Object object)
    {
        LocalDate date = (LocalDate) object;
        if (date != null)
        {
           return myzDateField.DATE_TIME_FORMATER_VIEW.format(date);
        }
        else
        {
            return "";
        }
    }

    @Override
    public Object fromString(String string)
    {
        if (string != null && !string.isEmpty())
        {
             return LocalDate.parse(string, myzDateField.DATE_TIME_FORMATER_VIEW);
        }
        else
        {
             return null;
        }
    }
}
