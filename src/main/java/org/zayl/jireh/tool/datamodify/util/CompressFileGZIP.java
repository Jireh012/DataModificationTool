package org.zayl.jireh.tool.datamodify.util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;


/**
 * @author last_
 */
public class CompressFileGZIP {

    private Logger logger = Logger.getLogger(CompressFileGZIP.class);

    public static void doCompressFile(String inFileName) {
        try {
            String outFileName = inFileName + ".gz";
            GZIPOutputStream out = null;
            try {
                out = new GZIPOutputStream(new FileOutputStream(outFileName));
            } catch(FileNotFoundException e) {

                System.err.println("Could not create file: " + outFileName);
            }

            FileInputStream in = null;
            try {
                in = new FileInputStream(inFileName);
            } catch (FileNotFoundException e) {
                System.err.println("File not found. " + inFileName);
            }

            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();

            out.finish();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sole entry point to the class and application.  
     * @param args Array of String arguments.  
     */
    public static void main(String[] args) {
        String str="C:\\Users\\last_\\Desktop\\不均衡优化-外挂需求\\test\\temp.csv";

        doCompressFile(str);

    }
}  