package com.myz.record;

import java.sql.Date;
import java.sql.ResultSet;

import com.myz.connection.ConnectionContext;
import com.myz.log.logWriter;

/**
 *  setDb ( dataType ) { overloaded function get value from java member set it as DB value }
 *
 * @author Montazar Hamoud
 */
public class DbTools
{



   ////////////////////////////////////////////////////////////////////////////////
   //                       set data as DB value                                 //
   //                                                                            //
   ////////////////////////////////////////////////////////////////////////////////

    public static String setDB ( int i )
   {
      return i == -1 ? "-1": "" + i;
   }

   public static String setDB ( double f )
   {
      return "" + f;
   }

   public static String setDB ( String str )
   {
       return setDB ( str , " " ) ;
   }

   public static String setDB ( String str, String dbProduct )
   {
      if( str == null || str.length() == 0 )
      {
         return "NULL";
      }
      str = str.replaceAll( "'" , "''" );
      // ** is mysql connection...
      if( ConnectionContext.DATABASE_MySQL.equals( dbProduct ) )
      {
         str = str.replaceAll( "\\\\", "\\\\\\\\" );
      }
      return "'" + str + "'";
   }

   public static String setDB ( String str, ConnectionContext connection )
   {
      return setDB ( str, connection.getDatabaseProductName() );
   }

   public static String setDB ( Date date )
   {
      if( date == null )
      {
	  return "NULL";
      }
      //format DD.MM.YYYY from the date
      return "'" + date.toString() + "'";
   }

   public static String setDB ( long i )
   {
      if( i == -1 )
      {
	  return "NULL";
      }
      return "" + i;
   }

   public static String setDB ( boolean b )
   {
      return "'" + ( b ? "1" : "0" ) + "'";
   }

   ///////////////////////////////////////////////////////////////////////////////
   //                       get data from resultSet                             //
   //                                                                           //
   ///////////////////////////////////////////////////////////////////////////////

   protected static int getIndexInResultSet( String name  )
    {
        boolean bIndex = true;
        for( int i = 0 ; i < name.length() ; i++ )
        {
            if( name.charAt( i ) < '0' || name.charAt( i ) > '9' )
            {
                bIndex = false;
                break;
            }
        }
        if( !bIndex )
        {
            return -1;
        }
        int index = -1;
        try
        {
            index = Integer.parseInt( name );
        }
        catch( Exception ex )
        {
            logWriter.write(ex);
        }
        return index;
    }

   public static Date getDate( ResultSet rs, String col )
    {
        Date date  = null;
        try
        {
	    String strDate = getString( rs , col );
	    if( strDate == null || strDate.length() == 0 )
	        date = null;

	    else if( strDate.length() > "xxxx-xx-xx".length() )
	        date = Date.valueOf( strDate.substring( 0, 10 ) );
	    else
	        date = Date.valueOf( strDate );
        }
        catch( Exception ex )
        {
            logWriter.write(ex);
	    System.out.println( "DBTools.getDate( " + col + " ) " + ex );
        }
        return date;
    }

   public static String getString( ResultSet rs, String col )
    {
        String str   = null;
        int    index = getIndexInResultSet( col );
        try
        {
            if( index > 0 )
                str = rs.getString( index ) ;
            else
                str = rs.getString( col );

        }
        catch( Exception ex )
        {
            logWriter.write(ex);
	    System.out.println( "DBTools.getString( " + col + " ) " + ex );
        }
        return str;
   }

   public static int getInt( ResultSet rs , String col )
   {
      int i = -1;
      int index = getIndexInResultSet(col);
      try
      {
          Number num ;
          if ( index > 0 )
             num = ( Number ) rs.getObject( index );
          else
              num = ( Number )rs.getObject( col );
	  if( num != null )
	  {
	     i = num.intValue();
	  }
      }
      catch( Exception ex )
      {
          logWriter.write(ex);
	  System.out.println( "DBTools.getInt( " + col + " ) " + ex );
      }
      return i;
   }

   public static int getTime( ResultSet rs, String col )
   {
      return getInt( rs, col );
   }

   public static double getFloat( ResultSet rs, String col )
   {
      double f  = -1;
      int index = getIndexInResultSet(col) ;
      try
      {
          if ( index > 0 )
              f = rs.getDouble( col );
          else
              f = rs.getDouble( col );
      }
      catch( Exception ex )
      {
          logWriter.write(ex);
	  System.out.println( "DBTools.getFloat( " + col + " ) " + ex );
      }
      return f;
   }

   public static boolean getBoolean( ResultSet rs, String col )
   {
      boolean b = false;
      int index = getIndexInResultSet( col );
      try
      {
          if ( index > 0)
          {
              String str = rs.getString( index );
	      b          = "1".equals( str );
          }
          else
          {
              	  String str = rs.getString( col );
                  b          = "1".equals( str );
          }

      }
      catch( Exception ex )
      {
          logWriter.write(ex);
	  System.out.println( "DBTools.getBoolean( " + col + " ) " + ex );
      }
      return b;
   }

   public static long getLong( ResultSet rs , String col )
   {
      long i = -1;
      int index = getIndexInResultSet( col );

      try
      {
          if ( index > 0)
              i = rs.getLong( index );
          else
              i = rs.getLong( col );
      }
      catch( Exception ex )
      {
          logWriter.write(ex);
	  System.out.println( "DBTools.getLong( " + col + " ) " + ex );
      }
      return i;
   }

   public static byte[] getBytes( ResultSet rs , String col )
   {
      byte[] bytes = null;
      int index = getIndexInResultSet( col );
      try
      {
          if ( index > 0)
              bytes = rs.getBytes( index );
          else
              bytes = rs.getBytes( col );
      }
      catch( Exception ex )
      {
          logWriter.write(ex);
	  System.out.println( "DBTools.getBytes( " + col + " ) " + ex );
      }
      return bytes;
   }

}
