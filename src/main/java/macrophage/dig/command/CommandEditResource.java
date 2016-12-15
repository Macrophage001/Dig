package macrophage.dig.command;

import macrophage.dig.api.ResourceRegistry;
import macrophage.dig.api.resource.IResource;
import macrophage.dig.helper.ResourceHelper;
import macrophage.dig.util.ModInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandEditResource extends CommandBase {
    @Override
    public String getCommandName() {
        return ModInfo.COMMANDS.EDIT_RESOURCE;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return ModInfo.COMMANDS.USAGE.EDIT_RESOURCE;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) throw new CommandException(getCommandUsage(sender));
        else {
            Integer   index = Integer.parseInt(args[0]);
            String    serializedString = args[1];
            String    syntaxCheck   = ResourceHelper.checkSyntax(serializedString);
            boolean   resourceCheck = ResourceHelper.checkResource(serializedString);

            if (index < 0 || index > ResourceRegistry.getResources().size()) throw new CommandException("Index out of bounds");
            if (syntaxCheck.compareTo("correct") != 0) throw new CommandException(syntaxCheck);
            if (!resourceCheck) throw new CommandException("Either the parent block, or the drop, is invalid!");

            IResource newResource = ResourceHelper.deserialize(serializedString);

            ResourceRegistry.remove(index);
            ResourceRegistry.register(newResource, index);

            sender.addChatMessage(new TextComponentString("Entry at index '" + index + "' has been editted."));
        }
    }
}
