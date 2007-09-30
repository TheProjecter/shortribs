/*
 * FileOutput.java
 * 
 * Created on Sep 29, 2007, 2:19:02 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shortribs;
import java.io.*;

/**
 *
 * @author Kaiser
 */
public class FileOutput {
    
    void WriteXML(String Path,String XMLFileName, String XML)
    {
        File PathFile = new File(Path);
        String FileString = "";
        
        //First we need to make sure the path exists
        if (!PathFile.exists())
        {
            //If it doesn't exists then create it
            PathFile.mkdir();
        }
        
        //Then we need to create the full file path as a string
        FileString = PathFile.toString() + "\\"+ XMLFileName;
        
        //Print our XML to a file
        try
        {
            FileWriter Out = new FileWriter(FileString);
            PrintWriter OutPrintWriter = new PrintWriter(Out);
            OutPrintWriter.print(XML);
            OutPrintWriter.close();
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
        }
    }
}
