/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.reportComponent;
import java.sql.ResultSet;
/**
 *
 * @author yazan
 */
abstract public class ReportCol implements Comparable
{
    public String m_dbName;
    public int    m_resultSetIndex = -1;
    public String m_caption;
    public int    m_order         = -1;
    public int    m_unit          = 0;

    public ReportCol( String dbName, String caption ,int unit)
    {
        m_dbName  = dbName;
        m_caption = caption;
        m_unit    = unit;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null ) { return false; }
        return ( ( ReportCol )obj ).m_order == this.m_order;
    }

    @Override
    public int compareTo( Object obj )
    {
        if( obj == null ) return -1;
        ReportCol col = ( ReportCol )obj;
        return this.m_order - col.m_order;
    }

    public abstract int    getUnits();
    public abstract String get( ResultSet rs, ManualReportRow r );
}
