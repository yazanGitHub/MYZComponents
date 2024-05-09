/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.reportComponent;

import java.sql.ResultSet;
import java.util.Vector;

import com.myz.component.myzComboBoxItem;
import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;

/**
 *
 * @author yazan
 */
public class ReportColCombo extends ReportCol
{
    Vector<myzComboBoxItem> m_vItems = new Vector();
    public ReportColCombo( String dbName, String caption, String[] data )
    {
        super( dbName, caption , 3);
        setItems( data );
    }

    public ReportColCombo( String dbName, String caption, Vector data)
    {
        super( dbName, caption , 3);
        m_vItems = data;
    }

    public ReportColCombo( String dbName, String caption, String SQL, ConnectionContext connection )
    {
        super( dbName, caption , 3);
        setItems( SQL, connection );
    }

    public void setItems( String[] data )
    {
        if(data !=null)
        {
            for( int i = 0; i < data.length; i++ )
            {
                myzComboBoxItem item = new myzComboBoxItem(data[i] , i + 1);
                m_vItems.addElement( item );
            }
        }
    }

    public void setItems( String SQL, ConnectionContext connection  )
    {
        ResultSet rs = null;
        try
        {
            rs = connection.getResultSet( SQL );
            while( rs.next() )
            {
                myzComboBoxItem item = new myzComboBoxItem(rs.getString(2) ,rs.getInt(1) );
                m_vItems.addElement( item );

            }
        }
        catch( Exception ex )
        {
            logWriter.write(ex);
        }
        finally
        {
            try
            {
                if (rs != null)
                    rs.close();
            }
            catch(Exception ex){logWriter.write(ex);}
        }
    }

    @Override
    public String get( ResultSet rs,  ManualReportRow r )
    {
        int val = 0;
        try
        {
            if( m_resultSetIndex >= 0 )
            {
                val = rs.getInt( m_resultSetIndex );
            }
            else
            {
                val = rs.getInt( m_dbName );
            }
        }
        catch( Exception ex )
        {
            logWriter.write(ex);
        }
        if( val <= 0 )
        {
            return "-";
        }
        for( int i = 0; i < m_vItems.size(); i++ )
        {
            myzComboBoxItem item = m_vItems.elementAt( i );
            if( item.getkey() == val )
            {
                return item.getValue();
            }
        }
        return "-";
    }

    @Override
    public int getUnits( )
    {
        return 3;
    }
}
