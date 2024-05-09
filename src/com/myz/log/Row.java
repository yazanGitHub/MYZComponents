package com.myz.log;

import java.util.Calendar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Montazar
 */
public class Row
{

    //Static
    public static String ERROR = "error" , WARNING = "warning" , INFORMATION = "information"  ,STATISTIC = "statistic" ,EMERGENCY = "emergency" , MISSING = "missing" , EXCEPTION = "exception" ;

    //members
    String [] m_splittedLine ;
    String    m_rowType    ;
    int       m_colLength  = 0 ;
    String    m_newLine = "\r\n";

    //Constructors
    Row ()
    {
    }

    Row( String line )
    {
       this( line, "information" );
    }
    Row (String line , String type )
    {
        m_splittedLine  = splitLine ( line );
        m_colLength     = m_splittedLine.length;
        if( type == null )
        {
           type = "information";
        }
        m_rowType       = type ;
    }

    //methods
    public static String [] splitLine (String line)
    {
        String [] splittedLine = line.split("&", 0);
        return splittedLine;

    }
    boolean equals (Row row)
    {
       return (m_rowType.equalsIgnoreCase(row.m_rowType) && m_colLength == row.m_colLength );
    }

    public String[] getDateTime()
    {
       String[] dateTime = new String[ 2 ];
       Calendar calendar = Calendar.getInstance();
       int year      = calendar.get( Calendar.YEAR );
       int month     = calendar.get( Calendar.MONTH ) + 1;
       int day       = calendar.get( Calendar.DAY_OF_MONTH );
       dateTime[ 0 ] = year + "-" + ( month < 10 ? "0" + month : "" + month ) + "-" + ( day < 10 ? "0" + day : "" + day );
       int hour      = calendar.get( Calendar.HOUR_OF_DAY );
       int minute    = calendar.get( Calendar.MINUTE );
       int second    = calendar.get( Calendar.SECOND );
       dateTime[ 1 ] =  ( hour < 10 ? "0" + hour : "" + hour ) + ":" + ( minute < 10 ? "0" + minute : "" + minute ) + ":" + ( second < 10 ? "0" + second : "" + second );
       return dateTime;

    }

    ////////////////////////////////////////////////////////////////////////////
    //
    // A P I (s)    U S E D  I N  W R I T E R
    // -------------------------------------------------------------------------
    // constructor
    // getText
    // getHtml
    //
    ////////////////////////////////////////////////////////////////////////////
    public String getText( Row lastRow )
    {
       String[] dt = getDateTime();
       String line = "";
       line += dt[ 0 ] + " " + dt[ 1 ] + " ";
       for (String element : m_splittedLine) {
          line += element + " ";
       }
       return line;
    }
    public String getHtml( Row lastRow )
    {
       StringBuffer html = new StringBuffer();
       if( lastRow != null && !lastRow.equals( this ) )
       {
          html.append( "</table>" + m_newLine );
       }
       if( lastRow == null || ( lastRow != null && !lastRow.equals( this ) ) )
       {
          html.append( "<table class= \" " + m_rowType.toUpperCase() + "\" >" + m_newLine );
       }
       String[] dt = getDateTime();
       html.append("<tr>" + m_newLine);
       html.append("<td style=\"width: 150px; \">" + dt[ 0 ] + "</td>" + m_newLine );
       html.append("<td style=\"width: 150px\">" + dt[ 1 ] + "</td>" + m_newLine );
       for (String element : m_splittedLine) {
          html.append("<td>" + element + "</td>" + m_newLine );
       }
       html.append("</tr>" + m_newLine);
       html.append( m_newLine );
       return html.toString();
    }




}
