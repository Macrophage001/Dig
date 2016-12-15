package macrophage.dig.command;

import macrophage.dig.api.ResourceRegistry;
import macrophage.dig.api.resource.Resource;
import macrophage.dig.helper.ResourceHelper;
import macrophage.dig.util.ModInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedPlayerList;
import net.minecraft.util.text.TextComponentString;

public class CommandRemResource extends CommandBase {
    @Override
    public String getCommandName() {
        return ModInfo.COMMANDS.REM_RESOURCE;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return ModInfo.COMMANDS.USAGE.REM_RESOURCE;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new CommandException(getCommandUsage(sender));
        } else {
            if (args[0].compareTo("all") == 0) {
                Integer registrySize = ResourceRegistry.getResources().size();
                ResourceRegistry.removeAll();
                sender.addChatMessage(new TextComponentString("Removed " + registrySize + " entry(s)."));
            } else {
                for (String arg : args) {
                    Integer index = Integer.parseInt(arg);
                    if (index > ResourceRegistry.getResources().size() || index < 0) throw new CommandException("Index " + index + " has gone out of range!");
                    else {
                        ResourceRegistry.remove(index);
                        sender.addChatMessage(new TextComponentString("Entry at index " + index + " removed."));
                    }
                }
            }
        }
    }
}
