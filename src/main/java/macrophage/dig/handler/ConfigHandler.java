package macrophage.dig.handler;

import macrophage.dig.api.ResourceRegistry;
import macrophage.dig.api.resource.IResource;
import macrophage.dig.helper.ResourceHelper;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    public static Configuration config;

    public static String CATEGORY_DROPS = "Drops";
    public static String CATEGORY_DROPS_DESC = "What items can be dropped, from which block, and the chance of them dropping";

    public static String FORMAT = "(block_modid)<block_registry/oredict_name>:<metadata>=<max_durability>|(drop_modid)<drop_registry/oredict_name>:<metadata>=<drop_chance>";

    public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    public static void reInit(File file) {
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            System.err.println(e);
        }

        init(file);
    }

    public static void syncConfig() {
        config.addCustomCategoryComment(CATEGORY_DROPS, CATEGORY_DROPS_DESC);
        List<IResource> ress = ResourceRegistry.getResources();
        List<String> serRess = new ArrayList<String>();
        if (ress.size() > 0) {
            for (IResource res : ress) {
                try {
                    serRess.add(ResourceHelper.serialize(res));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] serRessArray = new String[serRess.size()];
        serRess.toArray(serRessArray);

        String[] confSerRess = config.get(CATEGORY_DROPS, "Drops", serRessArray, FORMAT).getStringList();

        for (String serres : confSerRess) {
            if (serres.compareTo("") != 0) {
                System.out.println(serres);
                IResource resource = ResourceHelper.deserialize(serres);
                ResourceRegistry.register(resource, null);
            }
        }

        config.save();
    }
}
