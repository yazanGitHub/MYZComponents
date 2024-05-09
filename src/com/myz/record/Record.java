package com.myz.record;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.TimeZone;

import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;

/**
*
* @author yazan
*/
public abstract class Record implements Serializable
{
    //Constructor
    public Record( String tableName,  ConnectionContext connection )
    {
        m_connection = connection;
        m_tableName  = tableName;
    }


    //Members
    protected        int               m_pnr       = -1;
    protected        String            m_tableName;
    public transient ConnectionContext m_connection;

    //Statics
    public static final int      ALL_DATA         = 0;
    public static final int      MASTER_ONLY      = 1;
    public static final int      DETAILS_ONLY     = 2;
    public static final int      REINSERT_DETAILS = 3;
    public static final TimeZone GMT              = TimeZone.getTimeZone( "GMT" );

    //Abstract methods
    public abstract void    get( ResultSet rs , int type );
    public abstract boolean insert( int type );
    public abstract boolean update( int type );

    //Methods
    public void get( int pnr )
    {
        get ( pnr , MASTER_ONLY );
    }

    public void get( int pnr , int type )
    {
        String    SQL  = "SELECT * FROM " + m_tableName + " WHERE PNR = " + pnr;
        Statement stmt = null;
        ResultSet rs   = null;
        try
        {
            stmt = m_connection.m_connection.createStatement();
            rs   = stmt.executeQuery( SQL );
            if( rs.next() )
            {
                get( rs, type  );
                m_pnr = pnr;
            }
            rs.close();
            stmt.close();
        }
        catch( Exception ex )
        {
            logWriter.write(ex);
        }
        finally
        {
            try
            {
                if( rs != null ) { rs.close(); }
                if( stmt != null ) { stmt.close(); }
            }
            catch( Exception ex ) { logWriter.write(ex); }
        }
    }

    public void get( ResultSet rs )
    {
        get ( rs, MASTER_ONLY );
    }
    public boolean delete( )
    {
        String SQL = "DELETE FROM " + m_tableName + " WHERE PNR = " + m_pnr;
        return m_connection.executeSQL( SQL );
    }

    public boolean save( int type )
    {
        if ( type != DETAILS_ONLY )
        {
            if ( m_pnr <= 0 )
            {
                return insert( type );
            }
            else
            {
               return update( type );
            }
        }
        else
            return false;
    }

    public boolean save()
    {
        return save( ALL_DATA );
    }

    public int getPnr()
    {
        return m_pnr;
    }

    public void setPnr( int pnr )
    {
        m_pnr = pnr;
    }
/////////////////////////////////////////////////
////////////////////////////////////////////////
    public String getUserName ()
    {
        return "";
    }

    public String getObjectOwner()//the pk (t_name) of the record
    {
        return "";
    }

    public String getObjectSpace() //table name
    {
        return "";
    }

    public long getLastTime()
    {
        return -1;
    }

}
