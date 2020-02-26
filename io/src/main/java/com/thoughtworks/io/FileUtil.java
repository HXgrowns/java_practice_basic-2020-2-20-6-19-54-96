package com.thoughtworks.io;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

public class FileUtil {

    /**
     * 完成复制文件夹方法:
     * 1. 把给定文件夹from下的所有文件(包括子文件夹)复制到to文件夹下
     * 2. 保证to文件夹为空文件夹，如果to文件夹不存在则自动创建
     * <p>
     * 例如把a文件夹(a文件夹下有1.txt和一个空文件夹c)复制到b文件夹，复制完成以后b文件夹下也有一个1.txt和空文件夹c
     */
    public static void copyDirectory(File from, File to) throws IOException {
        if (!to.exists()) {
            to.mkdirs();
        }

        Queue<File> directoryQueue = new LinkedList<>();
        directoryQueue.offer(from);

        String toAbsolutePath = to.getAbsolutePath();
        String fromAbsolutePath = from.getAbsolutePath();

        while (!directoryQueue.isEmpty()) {
            File directory = directoryQueue.poll();
            File[] files = directory.listFiles();
            for (File file : files) {
                String fileAbsolutePath = file.getAbsolutePath();
                File targetFile = Paths.get(toAbsolutePath, fileAbsolutePath.substring(fromAbsolutePath.length())).toFile();

                if (file.isDirectory()) {
                    directoryQueue.offer(file);
                    targetFile.mkdirs();
                } else {
                    FileReader fileReader = new FileReader(fileAbsolutePath);
                    FileWriter fileWriter = new FileWriter(targetFile);
                    char[] cbuf = new char[1024];
                    int len = -1;
                    while ((len = fileReader.read(cbuf)) != -1) {
                        fileWriter.write(cbuf, 0, len);
                    }
                    fileReader.close();
                    fileWriter.close();
                }
            }
        }
    }
}
