import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class GameProgress implements Serializable {
    private static final long serialVersionUID = 1L;

    private int health;
    private int weapons;
    private int lvl;
    private double distance;

    public GameProgress(int health, int weapons, int lvl, double distance) {
        this.health = health;
        this.weapons = weapons;
        this.lvl = lvl;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "GameProgress{" +
                "health=" + health +
                ", weapons=" + weapons +
                ", lvl=" + lvl +
                ", distance=" + distance +
                '}';
    }

    public void saveGame(String path, GameProgress gameProgress) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void zipFiles(String pathForZip, List<String> filePaths) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pathForZip))) {
            for (String paths : filePaths) {
                File fileToZip = new File(paths);
                zos.putNextEntry(new ZipEntry(fileToZip.getName()));
                Files.copy(fileToZip.toPath(), zos);
                fileToZip.delete();
            }

            zos.closeEntry();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void openZip(String pathForZip, String pathOpenZip) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(pathForZip))) {
           ZipEntry entry;
           String name;
           while((entry = zis.getNextEntry()) != null) {
               name = entry.getName();
               FileOutputStream fout = new FileOutputStream(pathOpenZip + "//" + name);
               for (int c = zis.read(); c != -1; c = zis.read()) {
                   fout.write(c);
               }
               fout.flush();
               zis.closeEntry();
               fout.close();
           }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return gameProgress;
    }
}
