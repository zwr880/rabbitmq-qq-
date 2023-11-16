package com.ss.tools.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReserveFile {
    public String fileName;
    public String content;
    public String pathName;
    public String msg;
    public ReserveFile(String fileName,String content){
        this.content=content;
        this.fileName=fileName;
        pathName=System.getProperty("user.dir");//默认保存在当前目录下
        File newFile=new File(pathName,fileName);
        try{
            if(newFile.createNewFile()){
                FileWriter writer=new FileWriter(newFile.getAbsoluteFile());
                System.out.println("创建路径"+newFile.getAbsoluteFile());
                writer.write(content);
                writer.close();
                msg="文件内容已保存成功，可在本地目录查看";
                System.out.println("保存文件"+content);
            }else{
                msg="文件已存在或创建失败";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
