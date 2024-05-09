/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.reportComponent;
import java.sql.ResultSet;

import com.myz.log.logWriter;

/**
 *
 * @author yazan
 */
public class ReportColDate extends ReportCol
{

    public ReportColDate( String dbName, String caption )
    {
        super( dbName, caption ,3);
    }

    public ReportColDate( String dbName, String caption , int unit)
    {
        super( dbName, caption ,unit);
    }

    @Override
    public String get( ResultSet rs,  ManualReportRow r )
    {
        String strVal = null;
        try
        {
            if( m_resultSetIndex >= 0 )
            {
                strVal = rs.getString( m_resultSetIndex );
            }
            else
            {
                strVal = rs.getString( m_dbName );
            }
        }
        catch( Exception ex )
        {
            logWriter.write(ex);
        }
        if (strVal !=null )
        {
            if(strVal.contains("-"))
            {
                strVal = strVal.replaceAll("-","/");
            }
            else if(strVal.contains("."))
            {
                strVal = strVal.replaceAll("\\.","/");
            }
            return  strVal;
        }
        else
        {
            return " ";
        }
    }

    @Override
    public int getUnits()
    {
        return 3;
    }
}
