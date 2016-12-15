package macrophage.dig.command;

import macrophage.dig.api.ResourceRegistry;
import macrophage.dig.api.resource.Resource;
import macrophage.dig.handler.ConfigHandler;
import macrophage.dig.helper.ResourceHelper;
import macrophage.dig.util.ModInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;

public class CommandAddResource extends CommandBase {
    @Override
    public String getCommandName() {
        return ModInfo.COMMANDS.ADD_RESOURCE;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return ModInfo.COMMANDS.USAGE.ADD_RESOURCE;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException(getCommandUsage(sender));
        String  syntaxCheck   = ResourceHelper.checkSyntax(args[0]);
        boolean resourceCheck = ResourceHelper.checkResource(args[0]);

        if (syntaxCheck.compareTo("correct") == 0 && resourceCheck) {
            sender.addChatMessage(new TextComponentString(args[0]));
            Resource res = (Resource) ResourceHelper.deserialize(args[0]);
            ResourceRegistry.register(res, null);
            sender.addChatMessage(new TextComponentString("New entry added successfully!"));
        } else {
            throw new CommandException("Invalid entry!");
        }
    }
}
