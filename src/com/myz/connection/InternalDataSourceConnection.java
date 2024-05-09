package com.myz.connection;

import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.myz.log.logWriter;

public class InternalDataSourceConnection extends ConnectionContext
{
    public InternalDataSourceConnection(){}

    //together ( send datasource in request ) we may change this in future..
    private boolean connect( String datasource )
    {
        try
        {
            InitialContext context = new InitialContext();
            DataSource     ds      = ( DataSource ) context.lookup( datasource );
            return super.useConnection( ds.getConnection() );
        }
        catch( NamingException | SQLException ex )
        {
            logWriter.write(ex);
            throw new RuntimeException( ex );
        }
    }

    public static ConnectionContext getConnection( String datasource )
    {
        if( datasource == null )
            throw new ConnectRefusedException( "No datasource : NULL !!!" );

        InternalDataSourceConnection conn = new InternalDataSourceConnection();
        if( !conn.connect( datasource ) )
            throw new ConnectRefusedException();

        return conn;
    }
}
