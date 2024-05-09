package com.myz.connection;

import java.sql.Connection;

/**
 * -----------------------------------------------------------------------------
 *
 *                 G E N E R A L   I N F O R M A T I O N
 *
 * -----------------------------------------------------------------------------
 * this class is added to help connect.
 * since each program should have its own connection way.
 * the connect() method of the ConnectionContext use this class
 * (ConnectionInfo) to obtain a connection.
 * thus, in each project when we override (ConnectionInfo) we can
 * handle a new connection
 * @author yazan
 */

public abstract class ConnectionInfo
{
   public ConnectionInfo()
   {
   }
   public abstract Connection getConnection();


}
