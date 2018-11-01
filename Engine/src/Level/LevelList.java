package Level;

import java.util.HashMap;

public class LevelList {
    private HashMap<String, LevelWrap> list;

    public static class LevelWrap {
        private String path;
        private byte state;//0 -- on a disk, 1 -- in RAM
        private Level level;

        public LevelWrap(String path) {
            this.path = path;
            state = 0;
        }


    }
}
