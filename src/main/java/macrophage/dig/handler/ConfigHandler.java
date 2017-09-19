package macrophage.dig.handler;

import macrophage.dig.util.ModInfo;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Config(
        modid = ModInfo.MODID,
        name  = ModInfo.MODID + "/" + ModInfo.MODID,
        category = ""
)
@Config.LangKey("dig.config.title")
public final class ConfigHandler {
    public static ConfigHandler.Features features;

    public ConfigHandler() {}

    @Mod.EventBusSubscriber
    private static class EventHandler {
        private EventHandler() {
        }

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(ModInfo.MODID)) {
                ConfigManager.sync(ModInfo.MODID, Config.Type.INSTANCE);
            }

        }
    }

    public static class Features {
        @Config.Comment(ModInfo.CONFIG_ALERTS_COMMENT)
        public static boolean DISPLAY_ALERTS = false;
        @Config.Comment(ModInfo.CONFIG_SNEAK_COMMENT)
        public static boolean SNEAK_REQUIRED = true;
        @Config.Comment(ModInfo.CONFIG_SCRIPTS_FOLDER_COMMENT)
        public static String SCRIPTS_FOLDER = ModInfo.SCRIPTS_FOLDER;
    }
}
