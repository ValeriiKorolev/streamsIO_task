import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class Main {

    public static StringBuilder sb = new StringBuilder();
    public static Date dateNow = new Date();
    public static SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd' 'H:mm:ss:SS:");

    public static void main(String[] args) throws IOException {
        // Task 1
        String[][] makeDirFile = {{"/Games/src", ""}, {"/Games/res", ""}, {"/Games/savegames", ""}, {"/Games/temp", ""}, {"/Games/src/main", ""},
                {"/Games/src/test", ""}, {"/Games/src/main", "Main.java"}, {"/Games/src/main", "Utils.java"}, {"/Games/res/drawables", ""},
                {"/Games/res/vectors", ""}, {"/Games/res/icons", ""}, {"/Games/temp", "temp.txt"}};
        for (int i = 0; i < makeDirFile.length; i++) {
            if (makeDirFile[i][1].equals("")) makeDir(makeDirFile[i][0]);
            else makeFile(makeDirFile[i][0], makeDirFile[i][1]);
        }

        try (FileWriter writer = new FileWriter("/Games/temp/temp.txt", false)) {
            writer.write(sb.toString());
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // Task 2
        GameProgress gameProgress1 = new GameProgress(20, 3, 2, 120.55);
        GameProgress gameProgress2 = new GameProgress(25, 4, 4, 250.88);
        GameProgress gameProgress3 = new GameProgress(40, 6, 5, 300.44);
        saveGame("/Games/savegames/save1.dat", gameProgress1);
        saveGame("/Games/savegames/save2.dat", gameProgress2);
        saveGame("/Games/savegames/save3.dat", gameProgress3);
        zipFiles("/Games/savegames/savedGP.zip", "/Games/savegames/");

        // Task 3
        openZip("/Games/savegames/savedGP.zip", "/Games/savegames/");
        GameProgress gameProgress = null;
        gameProgress = (GameProgress) openProgress(gameProgress, "/Games/savegames/save1.dat");
        System.out.println(gameProgress);

    }

    // Методы для Task 1

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

    // Методы для Task 2
    public static void saveGame(String path, Object object) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(object);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String zipPathName, String zipDir) throws IOException {
        File zippedDir = new File(zipDir);
        String[] fileList = zippedDir.list();
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPathName));
        for (int i = 0; i < fileList.length; i++) {
            File f = new File(fileList[i]);
            try (FileInputStream fis = new FileInputStream(zipDir + f.getPath())) {
                ZipEntry ze = new ZipEntry(f.getPath());
                zos.putNextEntry(ze);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zos.write(buffer);
                zos.closeEntry();

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }
        zos.close();
        for (int i = 0; i < fileList.length; i++) {
            if (new File(zipDir + fileList[i]).delete()) {
                System.out.println("Файл " + fileList[i] + " добавлен в архив " + zipPathName);
                System.out.println("Файл " + fileList[i] + " удален из папки " + zipDir);
            }
        }
    }

    // Методы для Task 3
    public static void openZip(String zipPathName, String zipDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPathName))) {
            ZipEntry ze;
            String fileName;
            while ((ze = zis.getNextEntry()) != null) {
                fileName = ze.getName();
                FileOutputStream fos = new FileOutputStream(zipDir + fileName);
                for (int i = zis.read(); i != -1; i = zis.read()) {
                    fos.write(i);
                }
                fos.flush();
                zis.closeEntry();
                fos.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static Object openProgress(Object object, String filePathName) {
        try (FileInputStream fis = new FileInputStream(filePathName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            object = (Object) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return object;
    }

}
