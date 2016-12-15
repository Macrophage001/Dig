package macrophage.dig.command;

import macrophage.dig.api.ResourceRegistry;
import macrophage.dig.api.resource.IResource;
import macrophage.dig.api.resource.Resource;
import macrophage.dig.util.ModInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandListResources extends CommandBase {
    @Override
    public String getCommandName() {
        return ModInfo.COMMANDS.LIST_RESOURCES;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return ModInfo.COMMANDS.USAGE.LIST_RESOURCES;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        ITextComponent parentBlockName = new TextComponentString("Parent Block Name").setStyle(new Style().setColor(TextFormatting.DARK_GREEN));
        ITextComponent parentBlockMeta = new TextComponentString("Metadata").setStyle(new Style().setColor(TextFormatting.DARK_GREEN));
        ITextComponent parentBlockDura = new TextComponentString("Durability").setStyle(new Style().setColor(TextFormatting.DARK_GREEN));

        ITextComponent dropName = new TextComponentString("Drop").setStyle(new Style().setColor(TextFormatting.DARK_GREEN));
        ITextComponent dropMeta = new TextComponentString("Metadata").setStyle(new Style().setColor(TextFormatting.DARK_GREEN));
        ITextComponent dropChance = new TextComponentString("Drop Chance").setStyle(new Style().setColor(TextFormatting.DARK_GREEN));

        if (args.length < 1|| args[0].compareTo("") == 0) throw new CommandException(getCommandUsage(sender));
        if (args[0].compareTo("all") == 0) {
            if (ResourceRegistry.getResources().size() < 1) throw new CommandException("No entries available!");
            for (Integer index = 0; index < ResourceRegistry.getResources().size(); index++) {
                IResource res = ResourceRegistry.getResources().get(index);
                if (res != null) {
                    sender.addChatMessage(new TextComponentString(new TextComponentString(index.toString()).setStyle(new Style().setColor(TextFormatting.GOLD)).getFormattedText() + ": [" + parentBlockName.getFormattedText() + ": " + res.getParentBlock().getRegistryName() + ", " + parentBlockMeta.getFormattedText() + ": " + res.getBlockMetadata() + ", " + parentBlockDura.getFormattedText() + ": " + res.getMaxBlockDegradation() + " | " + dropName.getFormattedText() + ": " + res.getDrop().getRegistryName() + ", " + dropMeta.getFormattedText() + ": " + res.getItemMetadata() + ", " + dropChance.getFormattedText() + ": " + res.getDropChance() + "%]"));
                }
            }
        } else {
            if (args.length == 2) {
                List<Integer> indexes = new ArrayList<Integer>();
                List<IResource> resources = new ArrayList<IResource>();
                if (args[0].compareTo("drops") == 0) {
                    String dropStr = args[1];
                    for (Integer index = 0; index < ResourceRegistry.getResources().size(); index++) {
                        IResource res = ResourceRegistry.getResources().get(index);
                        if (res.getDrop().getRegistryName().toString().compareTo(dropStr) == 0) {
                            indexes.add(index);
                            resources.add(res);
                        }
                    }
                } else if (args[0].compareTo("blocks") == 0) {
                    String blockStr = args[1];
                    for (Integer index = 0; index < ResourceRegistry.getResources().size(); index++) {
                        IResource res = ResourceRegistry.getResources().get(index);
                        if (res.getParentBlock().getRegistryName().toString().compareTo(blockStr) == 0) {
                            indexes.add(index);
                            resources.add(res);
                        }
                    }
                }

                for (int i = 0; i < indexes.size(); i++) {
                    sender.addChatMessage(new TextComponentString(new TextComponentString(indexes.get(i).toString()).setStyle(new Style().setColor(TextFormatting.GOLD)).getFormattedText() + ": [" + parentBlockName.getFormattedText() + ": " + resources.get(i).getParentBlock().getRegistryName() + ", " + parentBlockMeta.getFormattedText() + ": " + resources.get(i).getBlockMetadata() + ", " + parentBlockDura.getFormattedText() + ": " + resources.get(i).getMaxBlockDegradation() + " | " + dropName.getFormattedText() + ": " + resources.get(i).getDrop().getRegistryName() + ", " + dropMeta.getFormattedText() + ": " + resources.get(i).getItemMetadata() + ", " + dropChance.getFormattedText() + ": " + resources.get(i).getDropChance() + "%]"));
                }
            }
        }
    }
}
