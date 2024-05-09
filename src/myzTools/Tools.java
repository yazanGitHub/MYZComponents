/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myzTools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.myz.log.logWriter;

/**
 * @author yazan
 */
public class Tools
{
    public static String getDate(LocalDate date , boolean singleField)
    {
        String strDate = "";
        if ( singleField)
            strDate = date.toString();
        else
        {
            strDate = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(date);
        }
        return strDate;
    }

    public static String getDate(String stringDate , boolean singleField)
    {
        if (stringDate == null || stringDate.length() < 1)
            return "";
        LocalDate local = null;
        try
        {
           local = LocalDate.parse(stringDate);

        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
        if(local == null)
            return "";

        return getDate(local , singleField);
    }

    public static File zip(List<File> files, File zipFile)
    {
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        try
        {
            // create the ZIP file
            ZipOutputStream out  = new ZipOutputStream(new FileOutputStream(zipFile));
            File            temp = null ;
            // compress the files
            for (File file : files) {
                temp = file.getCanonicalFile() ;
                if(!temp.exists())
                {
                    logWriter.write( temp.getPath() , "missing");
                    continue;
                }
                FileInputStream in = new FileInputStream(temp);
                // add ZIP entry to output stream
                out.putNextEntry(new ZipEntry(file.getName()));
                // transfer bytes from the file to the ZIP file
                int len;
                while((len = in.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                    out.flush();
                }
                // complete the entry
                out.closeEntry();
                in.close();
            }
            // complete the ZIP file
            out.close();
            return zipFile;
        }
        catch (IOException ex)
        {
            logWriter.write(ex);
        }
        return null;
    }
}
