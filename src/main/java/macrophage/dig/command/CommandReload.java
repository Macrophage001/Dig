package macrophage.dig.command;

import macrophage.dig.api.ResourceRegistry;
import macrophage.dig.api.resource.Resource;
import macrophage.dig.handler.ScriptHandler;
import macrophage.dig.helper.ResourceHelper;
import macrophage.dig.util.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CommandReload extends CommandBase {
    @Override
    public String getName() {
        return ModInfo.COMMANDS.RELOAD;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        ResourceRegistry.getResources().clear();

        try {
            if (ScriptHandler.parseScripts()) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Script(s) loaded successfully!"));
            } else {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("An error occurred when loading script(s)!"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
