package macrophage.dig;

import macrophage.dig.command.*;
import macrophage.dig.proxy.CommonProxy;
import macrophage.dig.util.ModInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

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

    public static Logger logger;

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
