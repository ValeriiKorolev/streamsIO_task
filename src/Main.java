import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main {

    public static StringBuilder sb = new StringBuilder();
    public static Date dateNow = new Date();
    public static SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd' 'H:mm:ss:SS:");

    public static void main(String[] args) {

        makeDir("/Games/src");
        makeDir("/Games/res");
        makeDir("/Games/savegames");
        makeDir("/Games/temp");
        makeDir("/Games/src/main");
        makeDir("/Games/src/test");
        makeFile("/Games/src/main", "Main.java");
        makeFile("/Games/src/main", "Utils.java");
        makeDir("/Games/res/drawables");
        makeDir("/Games/res/vectors");
        makeDir("/Games/res/icons");
        makeFile("/Games/temp", "temp.txt");

        try (FileWriter writer = new FileWriter("/Games/temp/temp.txt", false)) {
            writer.write(sb.toString());
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


    }

    public static void makeDir(String path) {
        File dir = new File(path);
        if (dir.mkdir()) {
            sb.append("Каталог " + path + " создан " + formatForDateNow.format(dateNow) + "\n");
        }
    }

    public static void makeFile(String path, String fileName) {
        File file = new File(path, fileName);
        try {
            if (file.createNewFile()) {
                sb.append("Файл " + fileName + " создан " + formatForDateNow.format(dateNow) + "\n");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            sb.append(ex.getMessage() + " " + formatForDateNow.format(dateNow) + "\n");
        }
    }
}
