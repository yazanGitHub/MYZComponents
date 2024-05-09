package com.myz.connection;

public class ConnectRefusedException extends RuntimeException
{

   public ConnectRefusedException( String msg )
   {
       super( msg );
   }

   public ConnectRefusedException()
   {
       super( "Connection refused" );
   }
}
