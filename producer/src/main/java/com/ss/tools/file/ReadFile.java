package com.ss.tools.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ReadFile {
    public String url;
    public String content;
    public byte[]body;
    public ReadFile(String url) throws FileNotFoundException {//前提是文件不是目录，已筛选
        this.url=url;
        File f=new File(url);
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }
            content = stringBuilder.toString();
            System.out.println(content);
            body = content.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
