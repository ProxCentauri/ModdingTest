package js25.advancedStuff.util;

import cpw.mods.fml.common.Loader;

public class ModManager {

    public static enum mods {
        IC2("IC2"),
        NEI("NotEnoughItems"),
        TE(null),
        BC("BuildCraft|Core");

        private String id;

        mods(String id) {
            this.id = id;
        }

        public boolean isLoaded() {
            return Loader.isModLoaded(id);
        }
    }


}
