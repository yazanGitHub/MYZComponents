package com.myz.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.myz.log.logWriter;


public class DirectConnection extends ConnectionInfo
{
    //Constructors
    public DirectConnection()
    {
        if( m_connectionProperties == null )
            loadProperties( "connection.properties" );
    }

    public DirectConnection( String fileName )
    {
        loadProperties( fileName);
    }

    public DirectConnection( Properties prop )
    {
        m_connectionProperties = prop;
    }

    //Members
    private Properties m_connectionProperties ;

    //Methods
    @Override
    public Connection getConnection()
    {
        Connection conn = null ;
        try
        {
            String strConnection = m_connectionProperties.getProperty( "connection" );
            if( "oracle".equalsIgnoreCase( strConnection ) )
            {
                conn = OracleConnection.getConnection( m_connectionProperties ) ;
                return conn;
            }
            if( "sqlserver".equalsIgnoreCase( strConnection ) )
            {
                conn =  SQLServerConnection.getConnection( m_connectionProperties );
                return conn ;
            }

            conn =  MysqlConnection.getConnection( m_connectionProperties );
            return conn ;
        }
        catch( ClassNotFoundException | SQLException ex )
        {
            logWriter.write(ex);
            //we should close connection if an exception has been accrued
            if( conn != null )
            {
                try
                {
                   conn.close();
                }
                catch( Exception e )
                {
                   logWriter.write(e);
                   System.out.println( "Exception during closing the Connection!!" );
                }
            }
        }
        return null;
    }

    private void loadProperties( String fileName )
    {
        try
        {
            m_connectionProperties = new Properties();
            m_connectionProperties.load( new FileInputStream( fileName ) );
        }
        catch( IOException e )
        {
            try
            {
                //Try to find this file inside my JAR
                m_connectionProperties.load( this.getClass().getResourceAsStream("/" + fileName) );
            }
            catch(Exception ex)
            {
                logWriter.write(e);
                logWriter.write(ex);
            }
        }
   }
}

/*
================================================================================
||                           O R A C L E                                      ||
================================================================================
 */
final class OracleConnection
{
    public static Connection getConnection( Properties connectionProperties ) throws ClassNotFoundException, SQLException
    {
        String user      = connectionProperties.getProperty( "user" );
        String password  = connectionProperties.getProperty( "password" );
        String host      = connectionProperties.getProperty( "host" );
        String database  = connectionProperties.getProperty( "database" );
        String port      = connectionProperties.getProperty( "port");

        if ( port == null || port.equals( "" ))
            port = "1521";

        String url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + database;
        if( port.indexOf( '\\' ) > 0 || port.indexOf( '/' ) > 0 )
        {
            System.setProperty("oracle.net.tns_admin", port );
            url = "jdbc:oracle:thin:@" + database;
        }

        Class.forName ("oracle.jdbc.OracleDriver");
        Class.forName( "oracle.jdbc.driver.OracleDriver" );

        return DriverManager.getConnection( url , user , password  );
    }
}

/*
================================================================================
||                             M Y S Q L                                      ||
================================================================================
 */
final class MysqlConnection
{
    public static Connection getConnection( Properties connectionProperties ) throws ClassNotFoundException, SQLException
    {
        String user     = connectionProperties.getProperty( "user" );
        String password = connectionProperties.getProperty( "password" );
        String host     = connectionProperties.getProperty( "host" );
        String database = connectionProperties.getProperty( "database" );
        String port     = connectionProperties.getProperty( "port");
        if ( port == null || port.equals( "" ))
            port = "3306";


        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" +  user ;
        url += "&characterEncoding=UTF8&characterSetResults=UTF8";
        Class.forName( "com.mysql.jdbc.Driver" ) ;

        return DriverManager.getConnection( url ,  user  ,  password  );
   }
}

/*
================================================================================
||                           S Q L S E R V E R                                ||
================================================================================
 */
final class SQLServerConnection
{
    public static Connection getConnection( Properties connectionProperties ) throws ClassNotFoundException, SQLException
    {
        String user      = connectionProperties.getProperty( "user" );
        String password  = connectionProperties.getProperty( "password" );
        String host      = connectionProperties.getProperty( "host" );
        String database  = connectionProperties.getProperty( "database" );
        String port      = connectionProperties.getProperty( "port");

        if ( port == null || port.equals( "" ))
            port = "1433";

        String url = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + database;
        Class.forName( "com.microsoft.sqlserver.jdbc.SQLServerDriver" );

        return DriverManager.getConnection( url ,  user  ,  password );
   }
}
