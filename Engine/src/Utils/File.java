package Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class File {
    private File() {
    }

    public static String loadAsString(String file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer + '\n');
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static class Image {
        public final int WIDTH, HEIGHT;
        public final String path;
        public final int[] data;

        public Image(String path) {
            int width = 1, height = 1;
            this.path = path;
            int[] pixels = null;
            try {
                BufferedImage image = ImageIO.read(new FileInputStream(path));
                width = image.getWidth();
                height = image.getHeight();
                pixels = new int[width * height];
                image.getRGB(0, 0, width, height, pixels, 0, width);
            } catch (IOException e) {
                System.err.println("Texture in '" + path + "' is unreadable.");
                this.HEIGHT = 1;
                this.WIDTH = 1;
                data = new int[1];
                data[0] = Integer.MAX_VALUE;
                return;
            }

            this.WIDTH = width;
            this.HEIGHT = height;

            data = new int[width * height];
            for (int i = 0; i < width * height; i++) {
                int a = (pixels[i] & 0xff000000) >> 24;
                int r = (pixels[i] & 0xff0000) >> 16;
                int g = (pixels[i] & 0xff00) >> 8;
                int b = (pixels[i] & 0xff);

                data[i] = a << 24 | b << 16 | g << 8 | r;
            }

        }
    }

    /* Эти листы я буду использовать для мгновенной загрузки кучи всяких шейдеров.
     * Пишу в одном месте список, потом подгружаю.
     * названия в списке будут и названиями файлов, и путями к ним.
     */
    public static Vector<Textfile> loadTextfilesFromAList(String file) {
        Vector<Textfile> res = new Vector<>();
        String prefix = file.substring(0, file.lastIndexOf('/') + 1);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                res.add(new Textfile(buffer.substring(0, buffer.lastIndexOf('.')),
                        loadAsString(prefix + buffer)));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

}
