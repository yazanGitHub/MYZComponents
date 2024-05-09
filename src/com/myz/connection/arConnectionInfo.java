package com.myz.connection;

public class arConnectionInfo
{
    //Constructor
    public arConnectionInfo(int connType)
    {
        CONNECTION   = connType;
    }

    //Statics
    public static final int CONNECTION_DIRECT              = 0;
    public static final int CONNECTION_DATASOURCE          = 1;
    public static final int CONNECTION_INTERNAL_DATASOURCE = 2;

    public    static int    CONNECTION   = CONNECTION_DIRECT;
    public    static String m_dataSource = "java:jboss/datasources/warehouse";

    //together ( send datasource in request ) we may change this in future..
    public static ConnectionContext getConnectionContext()
    {
        ConnectionInfo info    = null;
        if( CONNECTION == CONNECTION_INTERNAL_DATASOURCE )
        {
            return InternalDataSourceConnection.getConnection( m_dataSource );
        }
        else
        {
            info  = new DirectConnection();
        }

        final ConnectionContext cc = ConnectionContext.getConnection( info );
        if( cc != null )
        {
            Runtime.getRuntime().addShutdownHook( new Thread()
                {
                    @Override
                    public void run()
                    {
                        cc.disConnect();
                    }
                }
            );
        }

        return cc;
    }



}
