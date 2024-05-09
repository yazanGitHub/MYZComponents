
package com.myz.connection;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.myz.log.logWriter;


public class ConnectionContext
{
    //Constructor
    public ConnectionContext(){}

    //Members
    public  Connection m_connection;
    private Statement  m_statement;
    private ResultSet  m_resultSet;
    private String     m_databaseProductName = DATABASE_MySQL;
    private byte       m_currentState        = STATE_NEW;

    //Statics
    public static ConnectionContext m_staticConnection = null;//this connection will do all the "JOBS" for view

    //Strings as java.sql.Connection returns them :
    public final static String DATABASE_MySQL     = "MySQL";
    public final static String DATABASE_Oracle    = "Oracle";
    public final static String DATABASE_Sybase    = "Sybase";
    public final static String DATABASE_Sqlserver = "Microsoft SQL Server";

    protected static final byte STATE_NEW          = 0;
    protected static final byte STATE_CONNECTED    = 1;
    protected static final byte STATE_DISCONNECTED = 2;

    /*
     * for execute method mode
     * if MODE_BOOLEAN --> we used the old method
     * if MODE_LAST_ID --> we used the new one.
     */
    public static final int MODE_BOOLEAN = 0;
    public static final int MODE_LAST_ID = 1;

    //Methods
    /*
     * connect: connect to the context
     * call ConnectInternal, ConnectExternal
     * later it might connect to WinDK
     * @return: boolean (true if the connect was succeeded, false if not)
     */
    public boolean connect( ConnectionInfo info )
    {
        return useConnection( info.getConnection() );
    }

    /*
     * this function used to connect if there is no connection
     * @return the ConnectionContext that is created
     */
    public static ConnectionContext getConnection(  ConnectionInfo ci )
    {
        if( m_staticConnection == null )
        {
            m_staticConnection = new ConnectionContext();
            m_staticConnection.connect( ci );
        }
        return m_staticConnection;
    }

    public static ConnectionContext getConnection() //in this function it's necessary that the connection is connected!!!
    {
        return m_staticConnection;
    }

    /*
     * connect using ready java.sql.Connection
     * @param connection connected database connection
     * @return true if everything is ok.
     */
    public boolean useConnection( Connection connection )
    {
        m_connection = connection;

        if( m_connection == null )
            return false;

        //set Connection type from metadata...
        handelDatabase( m_connection );

        setStatus( STATE_CONNECTED );

        return true;
    }

    protected void handelDatabase( Connection connection )
    {
        guessDatabase( m_connection );

        // change date format to 'YYYY-MM-DD'
        //when connection is Oracle.
        if( isOracle() )
            alterOracleSession();
   }

    /*
     * get database connection from connection metadata....
     * @param connection
     */
    protected void guessDatabase( Connection connection )
    {
        try
        {
            m_databaseProductName = m_connection.getMetaData().getDatabaseProductName();
        }
        catch( SQLException ex )
        {
            System.err.println("WARNING: NO DATABASE TYPE WAS SET " );
            logWriter.write(ex);
        }
    }

    /*
     * alter oracle session
     * also used in InternalDataSourceConnection.
     */
    protected void alterOracleSession()
    {
        try
        {
            Statement stm = m_connection.createStatement();
            stm.executeUpdate( "ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD'" );
            stm.executeUpdate( "ALTER SESSION SET NLS_COMP=LINGUISTIC" );
            stm.executeUpdate( "ALTER SESSION SET NLS_SORT=BINARY_AI" );
            stm.close();
        }
        catch( SQLException ex )
        {
            System.out.println( "OracleConnection.ALTER_SESSION: "  + ex );
            logWriter.write(ex);
        }
    }

    public synchronized boolean disConnect()
    {
        try
        {
            if( m_statement != null )
            {
                m_statement.close();
                m_statement = null;
            }
        }
        catch( Exception ex )
        {
	  logWriter.write(ex);
        }
        try
        {
            if( m_resultSet != null )
            {
                m_resultSet.close();
                m_resultSet = null;
            }
        }
        catch( Exception ex )
        {
            logWriter.write(ex);
    	  //do nothing
        }
        try
        {
            if( m_connection != null )
            {
                m_connection.close();
                m_connection = null;
                // ** Firas.R.06.12.2010
                setStatus( STATE_DISCONNECTED );
            }
        }
        catch (Exception ex)
        {
            logWriter.write(ex);
            return false;
        }

        // set static to null too.
        m_staticConnection = null;
        return true;
    }

    public int executeSQL( String SQL, int mode )
    {
        // throw a message in case the connection
        // is not connected or already closed.
        statusCheck();

        int id = -1;
        try
        {
            Statement stmt = m_connection.createStatement();
            stmt.executeUpdate( SQL );
            stmt.close();

            if ( mode == MODE_LAST_ID )
                id = getGeneratedID();
            else
                id = 1;
        }
        catch ( Exception ex )
        {
            logWriter.write(ex);
            System.out.println( "ConnectionContext.executeSQL: " + SQL + " " + ex );
        }

        return id;
    }

    public boolean executeSQL( String SQL )
    {
        return executeSQL( SQL, MODE_BOOLEAN ) == 1;
    }
    public int getGeneratedID()
    {
        String SQL = "SELECT LAST_INSERT_ID() C";
        if( isOracle() )
            SQL = "SELECT LAST_INSERT_ID C FROM DUAL";
        else if( isSqlServer() )
            SQL = "SELECT @@IDENTITY C";

        int id = -1;
        try
        {
            ResultSet rs = getResultSet( SQL );
            if ( rs.next() )
                id = rs.getInt( "C" );
            rs.close();
        }
        catch ( Exception ex )
        {
            logWriter.write(ex);
            System.out.println( "ConnectionContext.getGeneratedID: " + SQL + " " + ex );
        }

        return id;
    }
    public synchronized ResultSet getResultSet( String SQL )
    {

        statusCheck();

        try
        {
            if( m_statement != null )
            {
                m_statement.close();
                m_statement = null;
            }

            if( m_resultSet != null )
            {
                m_resultSet.close();
                m_resultSet = null;
            }

            m_statement = m_connection.createStatement();
            m_resultSet = m_statement.executeQuery( SQL );
            return m_resultSet;
        }
        catch( Exception ex )
        {
            logWriter.write(ex);
            System.out.println( "ConnectionContext.getResultSet: " + SQL + " " + ex  );
            return null;
        }
    }

    private void statusCheck()
    {
        if( m_currentState == STATE_NEW )
            throw new UnsupportedOperationException( "Connection is not yet connected to database!!, use connect() first." );

        if( m_currentState == STATE_DISCONNECTED )
            throw new UnsupportedOperationException( "Connection was already closed !!" );

        if( m_currentState == STATE_CONNECTED && m_connection == null )
            throw new UnsupportedOperationException( "Invalid Connection state, connect method failed to get a connection." );
    }

    public boolean isOracle()
    {
        return isDatabaseProduct( DATABASE_Oracle );
    }

   public boolean isMysql()
    {
        return isDatabaseProduct( DATABASE_MySQL );
    }

    public boolean isSybase()
    {
        return isDatabaseProduct( DATABASE_Sybase );
    }

    public boolean isSqlServer()
    {
        return isDatabaseProduct( DATABASE_Sqlserver );
    }

    public boolean isDatabaseProduct( String productName )
    {
        return productName.equals( m_databaseProductName ) ;
    }

    public String getDatabaseProductName()
    {
        return m_databaseProductName;
    }

    protected void setStatus( byte s )
    {
        m_currentState = s;
    }

}
