package com.myz.log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.util.Calendar;

import myzMessage.myzMessage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Montazar
 */
public class logWriter
{
   //Static
   public String m_fileName;
   public static logWriter LOG_WRITER = null;
   public static String m_newLine     = "\r\n";
   private Row m_currentRow           = null;
   private Row m_prevRow              = null;
   private String m_title             = null ;

   public static final String LOG_BASE_DIR_NAME = "Log";


   ////////////////////////////////////////////////////////////////////////////
   //
   // A P I s - S T A T I C  F U N C T I O N S
   //
   // 1. open( String fileName )
   // 2. close()
   // 3. write( String "data:data2", String type )
   //
   ////////////////////////////////////////////////////////////////////////////
   public static void open( String fileName )
   {
      open( fileName , " " );
   }
   public static void open( String fileName, String title )
   {
      open( null, fileName, title );
   }
   public static void open( String dir, String fileName , String title  )
   {
      Date date       = new Date( Calendar.getInstance().getTimeInMillis() );
      fileName        = fileName + "_" + date;
      String fullName = fileName;
      if( dir != null )
      {
         fullName = dir + File.separator + fileName;

      }
      if( LOG_WRITER != null && LOG_WRITER.m_fileName.equals( fullName ) )
      {
         return;
      }
      if( LOG_WRITER != null )
      {
         close();
      }
      LOG_WRITER = new logWriter( dir, fileName , title );
   }


   public void closeFile()
   {
      StringBuffer html = new StringBuffer();
      try
      {
         if( m_prevRow != null )
         {

            html.append(m_newLine + "</table>" + m_newLine );
         }
         html.append(m_newLine + "</body>" + m_newLine );
         html.append( m_newLine + "</html>" + m_newLine );
         writeHtml( html.toString() );
      }
      catch( Exception ex )
      {
         ex.printStackTrace();
      }
   }
   public static void close()
   {
      if( LOG_WRITER == null )
      {
         return;
      }
      LOG_WRITER.closeFile();
   }

   public static void write( String data, String type )
   {
      LOG_WRITER.log( data, type );
   }

   public static void write( Exception ex )
   {
     LOG_WRITER.log( ex );
   }

   public void log( Exception ex )
   {
      ex.printStackTrace();
      StackTraceElement[] exce = ex.getStackTrace();
       for (StackTraceElement exce1 : exce)
       {
           write("Stack Trace  : " +exce1.toString() , "exception");
       }
       write("Exception String : " + ex.toString() ,"exception");
       myzMessage.alertMessage(ex.getMessage());
   }

    //Constuctor + init html page
    public logWriter( String  fileName, String title )
    {
       this( null, fileName, title );
    }
    public logWriter( String dir, String fileName , String title )
    {
       try
       {
          if( dir != null )
          {
             File fDir = new File( dir );
             fDir.mkdirs();
             fileName = dir + File.separator + fileName;
          }
          m_fileName = fileName;
          m_title    = title ;
          initHtmlPage ();
       }
       catch (Exception ex)
       {
          ex.printStackTrace();
       }
    }

      //Method
     void initHtmlPage ()
     {
        java.util.Date date = new java.util.Date();
        StringBuffer html = new StringBuffer();
        html.append( "<!DOCTYPE html>" + m_newLine );
        html.append( "<html  lang=\"ar\" >" + m_newLine );
        html.append( "<head>" + m_newLine );
        html.append( "<meta charset=\"windows-1256\" >" + m_newLine );
        String title =  ( m_title != null && m_title.length() > 0 ) ? m_title : m_fileName;
        html.append("<title> " + title + "</title>" + m_newLine );
        addCss( html );
        html.append( "<body>" );
        if(m_title != null && m_title.length() > 0 )
            html.append("<h1>" + m_title  + "<hr>" + "</h1>" + m_newLine);
        html.append("<h1>" + "Date:" + date  + "<hr>" + "</h1>" + m_newLine);
        html.append("</head>" + m_newLine);
        html.append("<body  style=\"background-color: #f2f2f2 ;\">" + m_newLine);
        writeHtml( html.toString() );
     }
     void addCss ( StringBuffer html )
     {
        html.append("<style>" + m_newLine);
        html.append(m_newLine + "h1 {color:black ; text-align: center ; font-style: oblique; }" + m_newLine);
        //Table style
        html.append(m_newLine + " table {font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\n" +
                    "border-radius: 2px;"+
                    "width: 100%; "+
                    "color: black ;"+
                    "background-color: #ffffff;"+
                    "border: 1px solid black;\n"+
                    "border-radius: 5px;}"+
                    // td  style
                    m_newLine +
                    "tr:hover {color:#000000; background-color: #ffffff;}"
                    + m_newLine   );
        //typed table's style
        html.append(m_newLine + ".ERROR{background-color:#c00000; color:#ffffff; font-weight: bold; font-style: italic; }" + m_newLine   );
        html.append(m_newLine + ".INFORMATION {color:#000000; background-color:#f0f0f0; font-style: italic; }" + m_newLine   );
        html.append(m_newLine + ".STATISTIC {background-color:#c0c0c0; color:green; font-style: italic; }" + m_newLine   );
        html.append(m_newLine + ".WARNING {color: #e67300 ; font-weight: bold; font-style: italic; }" + m_newLine   );
        html.append(m_newLine + ".EMERGENCY {background-color:black; color:#ff0000 ; font-weight: bold; font-style: italic; ; font-size: 150%; }" + m_newLine   );
        html.append(m_newLine + ".MISSING {color: blue ; font-weight: bold; font-style: italic; }" + m_newLine   );
        html.append(m_newLine + ".EXCEPTION {color: red ; font-weight: bold; font-style: italic; }" + m_newLine   );
        html.append("</style>" + m_newLine );
     }


    public void log( String line , String type  )
    {

       m_currentRow = new Row( line, type );
       String html = m_currentRow.getHtml( m_prevRow );
       String text = m_currentRow.getText( m_prevRow );

       m_prevRow = m_currentRow;
       writeHtml( html );
       writeText( text );
       System.out.println( text );
    }

    void writeText( String text )
    {
       if( text == null )
       {
          text = "";
       }
       text += "\r\n";
       try
       {
          RandomAccessFile w = new RandomAccessFile( m_fileName + ".txt" , "rw" );
          w.seek( w.length() );
          byte[] bytes      = text.getBytes( "windows-1256" );

          w.write( bytes );
          //flush the stream
          w.close();


       }
       catch (IOException ex)
       {
          ex.printStackTrace();
       }
    }
    void writeHtml( String html )
    {
       try
       {
          RandomAccessFile w = new RandomAccessFile( m_fileName + ".html" , "rw" );
          w.seek( w.length() );
          byte[] bytes      = html.getBytes( "windows-1256" );

          w.write( bytes );
          //flush the stream
          w.close();

       }
       catch (IOException ex)
       {
          ex.printStackTrace();
       }
    }

    public static void main( String[] args )
    {
       String fileName = System.getProperty( "user.dir" );
       fileName        = fileName.replaceAll( "\\\\", "*" );
       System.out.println( fileName );

       logWriter.open("D:\\hmde\\hmde\\test");
       logWriter.write("test;test;test", "information");

    }
 }
