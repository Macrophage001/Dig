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
        @Config.Comment(ModInfo.CONFIG_SCRIPTS_FOLDER_COMMENT)
        public static String SCRIPTS_FOLDER = ModInfo.SCRIPTS_FOLDER;
        @Config.Comment(ModInfo.CONFIG_TOOL_BLACKLIST_COMMENT)
        public static String[] TOOL_BLACKLIST = {};
        @Config.Comment(ModInfo.CONFIG_ALLOW_HAND_DAMAGE_COMMENT)
        public static boolean ALLOW_HAND_DAMAGE = true;
        public static float HAND_DAMAGE_AMMOUNT = 1;
        @Config.Comment(ModInfo.CONFIG_USE_HUNGER_COMMENT)
        public static boolean USE_HUNGER = true;
        public static float HUNGER_AMOUNT = 1;

    }
}
