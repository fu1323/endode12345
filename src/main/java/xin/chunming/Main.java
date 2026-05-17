package xin.chunming;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        File file1 = new File("urls");
        File file2 = new File("fails");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file1, true));

        ArrayList<String> strings = new ArrayList<>();

        ArrayList<String> exists = new ArrayList<>();
        ArrayList<String> fails = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            exists.add(line);
        }
        bufferedReader.close();

        BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file2));
        String line2;
        while ((line2 = bufferedReader2.readLine()) != null) {
           // System.out.println(line2+"000");
            fails.add(line2);
        }
        bufferedReader2.close();


        File file = new File("/Volumes/.Jadine/视频");

        listFiles(strings, file);

        //            System.out.println(string);
        exists.forEach(strings::remove);
        System.out.println(strings.size());

        fails.forEach(str->{
            strings.remove(str.split(".mp4")[0]+".mp4");
           // strings.remove(str.split(".part")[0]+".mp4");
        });
        System.out.println(strings.size());

        Thread.sleep(1500);
        //listFiles(strings, file1);
        strings.forEach(System.out::println);

        strings.forEach((string) -> {
            // System.out.println(string);
            System.out.println(strings.size());
            try {

                convater(string, bufferedWriter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


    }

    public static void listFiles(ArrayList<String> files, File file) {
        File[] files1 = file.listFiles();
        for (File file1 : files1) {
            if (file1.isDirectory() && !file1.getName().startsWith(".")) {
                listFiles(files, file1);
            } else if (file1.isFile() && (file1.getName().endsWith(".MP4")
                    || file1.getName().endsWith(".mp4")
                    || file1.getName().endsWith(".mov")
                    || file1.getName().endsWith(".avi")
                    || file1.getName().endsWith(".wmv")
                    || file1.getName().endsWith(".flv")
                    || file1.getName().endsWith(".rm")
                    || file1.getName().endsWith(".rmvb")
                    || file1.getName().endsWith(".part")
                    || file1.getName().endsWith(".m4v")
                    || file1.getName().endsWith(".asf")
                    || file1.getName().endsWith(".mpg")
                    || file1.getName().endsWith(".webm")
                    || file1.getName().endsWith(".3gp")
                    || file1.getName().endsWith(".mkv")) &&
                    !file1.getName().contains("_av1") && !file1.getName().startsWith(".") && !file1.getName().contains("gjxgfd")) {
//                if (!file1.getName().contains("telegram")
//                        && !file1.getAbsolutePath().contains("output")
//                        && !file1.getAbsolutePath().contains("screen")
//                        && !file1.getAbsolutePath().contains("destroyed")) {
                    files.add(file1.getAbsolutePath());
//                }
                // System.out.println(file1.getAbsolutePath());


            }
        }

    }

    public static void convater(String path, BufferedWriter bw) throws IOException, InterruptedException {

        int i = new Random().nextInt();
        String outPath = path + i + "_av1.mp4";
        ProcessBuilder processBuilder = new ProcessBuilder("taskpolicy", "-c", "utility", "ffmpeg", "-i", path, "-c:a", "aac", "-c:v", "libsvtav1", "-crf", "28", outPath);
        processBuilder.redirectErrorStream(true);
        Process start = processBuilder.start();
        InputStream inputStream = start.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        double biteratesrc = 0;
        double biteratetarget = 0;
        int times = 0;
        int all = 0;
        while ((line = bufferedReader.readLine()) != null) {
           //System.out.println(times);
           //System.out.println(all);
           //System.out.println(biteratesrc);
           //System.out.println(biteratetarget);
            if (times > 10&&all<60) {
                fail(outPath);
                start.destroy();
                break;
            }


            System.out.println(line);
            if (line.contains("bitrate: ")) {
                String strip = line.split("bitrate: ")[1].split("kb/s")[0].strip();
                if (!strip.contains("N/A")) {

                    biteratesrc = Double.parseDouble(strip);
                }
            }
            if (line.contains("bitrate=")) {
                String s = line.split("bitrate=")[1].split("kbits/s")[0].strip();
                if (!s.contains("N/A")) {

                    biteratetarget = Double.parseDouble(s);
                }
            }
            if (biteratesrc < biteratetarget && biteratetarget != 0 && biteratesrc != 0) {
                all++;
                times++;
            } else if (biteratesrc > biteratetarget && biteratetarget != 0 && biteratesrc != 0) {
                times--;
            }
            ;
        }
        start.waitFor();
        if (start.exitValue() == 0) {
            bw.write(path);
            bw.newLine();
            bw.flush();
        }
    }

    public static void fail(String path) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("fails", true));
        bufferedWriter.write(path);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
