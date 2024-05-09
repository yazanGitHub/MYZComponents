/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.reportComponent;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author yazan
 */
public class ManualReportRow
{
    public ManualReportRow()
    {
    }

  public Vector getRow( ResultSet rs ) throws Exception
  {
//    int    m_pnrObject = rs.getInt( 1 );
    Vector vCols       = new Vector();
    for( int i = 0; i < m_vReportCols.size(); i++ )
    {
      ReportCol rc = m_vReportCols.elementAt( i );
      vCols.addElement( rc.get( rs, this ) );
    }
    return vCols;
  }




  public Vector<ReportCol> m_vReportCols = new Vector();
  public int m_pnrObject;

}
