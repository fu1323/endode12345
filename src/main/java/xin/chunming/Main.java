package xin.chunming;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {
    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<>();
        File file = new File("/Volumes/av1/noav1");
        //File file1 = new File("/Volumes/.Jadine/未命名文件夹 3");
        listFiles(strings, file);
        //listFiles(strings, file1);
        strings.forEach((string) -> {
            try {
                convater(string);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
    public static void listFiles(ArrayList<String> files,File file) {
        File[] files1 = file.listFiles();
        for (File file1 : files1) {
            if (file1.isDirectory()&&!file1.getName().startsWith(".")) {
                listFiles(files, file1);
            } else if (file1.isFile()&&(file1.getName().endsWith(".MP4")||file1.getName().endsWith(".mov")||file1.getName().endsWith(".mkv"))&&!file1.getName().contains("_av1")) {
                files.add(file1.getAbsolutePath());
                System.out.println(file1.getAbsolutePath());


            }
        }

    }
    public static void convater(String path) throws IOException {
        int i = new Random().nextInt();
        ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", path, "-c:a", "copy", "-c:v", "libsvtav1","-crf","28", path+ i + "_av1.mp4");
        processBuilder.redirectErrorStream(true);
        Process start = processBuilder.start();
        InputStream inputStream = start.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line=bufferedReader.readLine())!=null){
            System.out.println(line);
        }
    }
}
