package macrophage.dig;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import macrophage.dig.command.*;
import macrophage.dig.handler.ConfigHandler;
import macrophage.dig.handler.PlayerDigEventHandler;
import macrophage.dig.util.ModInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

import java.io.File;

@Mod(
        modid   = ModInfo.MODID,
        name    = ModInfo.NAME,
        version = ModInfo.VERSION
)
public class Dig {
    public static File suggestedConfigFile;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        suggestedConfigFile = event.getSuggestedConfigurationFile();
        ConfigHandler.init(suggestedConfigFile);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerDigEventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {}

    @Mod.EventHandler
    public void serverStarted(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandAddResource());
        event.registerServerCommand(new CommandListResources());
        event.registerServerCommand(new CommandRemResource());
        event.registerServerCommand(new CommandEditResource());
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        ConfigHandler.reInit(suggestedConfigFile);
    }
}
