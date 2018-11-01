package Main;

import Graphics.Texture;
import Utils.File;

import java.util.HashMap;

public class TextureBank {
    public TextureBank() {
        bank = new HashMap<>();
        //addDefaultTextures();
    }

    private void addDefaultTextures() {
        TextureWrap wrap = new TextureWrap();
        wrap.state = 2;
        wrap.texture = Texture.monoColor(255, 255, 255, 0);
        bank.put("none", wrap);
    }

    public void addTexturesFromList(String[] textures) {
        for (String str :
                textures) {

            String name = str.substring(0, str.lastIndexOf('|')),
                    path = str.substring(str.lastIndexOf('|') + 1);

            try {
                addFromDisk(name, path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addFromDisk(String name, String path) throws ThisNameIsEngagedException {
        if (!bank.containsKey(name)) {
            bank.put(name, new TextureWrap(path));
        } else {
            throw new ThisNameIsEngagedException("Texture called '" + name + "' is already exist.");
        }
    }

    public TextureWrap Get(String name) throws NonExistentTextureException {
        if (bank.containsKey(name)) {
            System.out.println(bank.get(name).path);
            return bank.get(name);
        } else
            throw new NonExistentTextureException("Texture called '" + name + "' is not exist.");
    }

    public static class TextureWrap {
        public TextureWrap(String path) {
            state = 0;
            this.path = path;
        }

        public TextureWrap()//error
        {
            state = 2;
            path = null;
            texture = Texture.monoColor(255, 255, 255, 255);//RED ALERT
        }

        public final String path;
        private byte state; // 0 - on a disk, 1 - in RAM, 2 - ready to use
        private File.Image image;
        private Texture texture;

        public TextureWrap downloadToRAM() {
            image = new File.Image(path);
            state = 1;
            return this;
        }

        public TextureWrap prepareToUse() {
            if (state == 0)
                downloadToRAM();
            if (state == 1)
                texture = new Texture(image);
            state = 2;
            return this;
        }

        public TextureWrap deleteFromVideocard() {
            if (state == 2) {
                texture.delete();
                texture = null;
                state = 1;
            }
            return this;
        }

        public void deleteFromRAM() {
            deleteFromVideocard();
            image = null;
            state = 0;
        }

        public Texture getTexture() {
            if (state != 2)
                prepareToUse();
            return texture;
        }
    }

    public static class ThisNameIsEngagedException extends Exception {
        public ThisNameIsEngagedException(String message) {
            super(message);
        }
    }

    public static class NonExistentTextureException extends Exception {
        public NonExistentTextureException(String message) {
            super(message);
        }
    }

    public void freeMemory() {
        for (TextureWrap wrap :
                bank.values()) {
            wrap.deleteFromRAM();
        }
    }

    private HashMap<String, TextureWrap> bank;

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        for (String name :
                bank.keySet()) {
            TextureWrap wrap = bank.get(name);
            info.append(name).append(":\n*path = ").append('"').append(wrap.path).append('"').append("\n*state = ");
            switch (wrap.state) {
                case 0:
                    info.append("on a disk\n");
                    break;
                case 1:
                    info.append("in RAM\n");
                    break;
                case 2:
                    info.append("ready to use\n");
                    break;
            }
        }
        return info.toString();
    }
}
