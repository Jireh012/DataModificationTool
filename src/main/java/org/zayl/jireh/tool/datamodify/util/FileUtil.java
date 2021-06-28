package org.zayl.jireh.tool.datamodify.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author last_
 */
public class FileUtil {

    private static String path = Const.saveFilePath;

    public static void isChartPathExist(String dirPath) {
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

    public static boolean deleteDir(String path){
        File file = new File(path);
        if(!file.exists()){//判断是否待删除目录是否存在
            System.err.println("The dir are not exists!");
            return false;
        }

        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for(String name : content){
            File temp = new File(path, name);
            if(temp.isDirectory()){//判断是否是目录
                deleteDir(temp.getAbsolutePath());//递归调用，删除目录里的内容
                temp.delete();//删除空目录
            }else{
                if(!temp.delete()){//直接删除文件
                    System.err.println("Failed to delete " + name);
                }
            }
        }
        return true;
    }

    public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }

    static void saveDataToFileNoD(String fileName, String data) {
        isChartPathExist(path);
        BufferedWriter writer = null;
        File file = new File(path + fileName + ".json");
        //如果文件不存在，则新建一个
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void saveDataToFile(String fileName, String data) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        String nextTime = time.format(new Date());
        String pathDate = path + nextTime + File.separator;
        isChartPathExist(pathDate);
        BufferedWriter writer = null;
        File file = new File(pathDate + fileName + ".json");
        //如果文件不存在，则新建一个
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param pathname
     * @return
     * @throws IOException
     */
    public static boolean deleteFile(String pathname){
        boolean result = false;
        File file = new File(pathname);
        if (file.exists()) {
            file.delete();
            result = true;
            System.out.println("文件已经被成功删除");
        }
        return result;
    }

    private List<String> resultLsit = new ArrayList<String>();

    private String fileName = null;

    public FileUtil(){
    }

    public FileUtil(String basePaht,String regex,String suffix){
        this.SearchFlieList(basePaht, regex, suffix);
    }

    public void SearchFlieList(String basePaht,String matchStr,String suffix){
        File root = new File(basePaht);
        if(root!=null&&root.listFiles()!=null) {
            for(File file:root.listFiles()){
                if(file.isDirectory()) {
                    SearchFlieList(file.getAbsolutePath(),matchStr,suffix);
                } else if(file.isFile()&&file.getName().endsWith(suffix)){
                    fileName = file.getName().contains(".")?file.getName().substring(0,file.getName().lastIndexOf(".")):file.getName();
                    if(matching(matchStr,fileName)) {
                        resultLsit.add(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    private boolean matching(String matchStr,String targerStr){
        matchStr = matchStr.replaceAll("\\*", ".*").replaceAll("\\?", "\\.");
        try {
            if(Pattern.compile(matchStr).matcher(targerStr).matches()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    public List<String> getResultLsit() {
        return resultLsit;
    }

    public void setResultLsit(List<String> resultLsit) {
        this.resultLsit = resultLsit;
    }

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        FileUtil searchFile = new FileUtil("d:/", "*ant*", ".jar");
        for (String string : searchFile.getResultLsit()) {
            System.out.println(string);
        }
        System.out.println("耗时："+(System.currentTimeMillis()-start));
    }
}
