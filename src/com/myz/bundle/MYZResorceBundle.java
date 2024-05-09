/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.bundle;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Montazar Hamoud
 */
public class MYZResorceBundle extends ResourceBundle
{

    //Class members
    public static final ResourceBundle   ARABIC_BUNDLE   =  ResourceBundle.getBundle("captions",new Locale("ar", "sy"));
    public static final ResourceBundle   ENGLISH_BUNDLE  =  ResourceBundle.getBundle("captions",new Locale("en", "en"));
    public static final ResourceBundle   FRENCH_BUNDLE   =  ResourceBundle.getBundle("captions",new Locale("fr", "fr"));

    public static       ResourceBundle   BUNDLE          =  ResourceBundle.getBundle("captions",new Locale("ar", "sy"));

    public static String getCaption(String key)
    {
        String str = "";
        try
        {
            str = BUNDLE.getString(key);
        }
        catch(Exception ex)
        {
//            logWriter.write(ex);
//            System.out.println("Bundle can not find key " + key);
        }
        finally
        {
            if ( str == null || str.length() < 1)
                str = key;
        }
        return str;

    }

    @Override
    protected Object handleGetObject(String key)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enumeration<String> getKeys()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
