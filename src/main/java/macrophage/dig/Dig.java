package macrophage.dig;

import macrophage.dig.command.*;
import macrophage.dig.handler.ConfigHandler;
import macrophage.dig.handler.ScriptHandler;
import macrophage.dig.proxy.CommonProxy;
import macrophage.dig.util.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import sun.font.Script;

import java.io.File;
import java.io.IOException;

@Mod(
        modid   = ModInfo.MODID,
        name    = ModInfo.NAME,
        version = ModInfo.VERSION
)
public class Dig {
    @SidedProxy(
            clientSide = ModInfo.PROXY.CLIENT,
            serverSide = ModInfo.PROXY.COMMON
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {}

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {}

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandListResources());
        event.registerServerCommand(new CommandReload());
    }
}
