package macrophage.dig.helper;

import macrophage.dig.api.ResourceRegistry;
import macrophage.dig.api.resource.IResource;
import macrophage.dig.api.resource.Resource;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ResourceHelper {
    public static String serialize(IResource resource) {
        String[] splitString  = StringHelper.split(String.valueOf(resource.getDrop().getRegistryName()), ':');
        String   itemRegistryName = splitString[1];

        String[] splitString1 = StringHelper.split(String.valueOf(resource.getParentBlock().getRegistryName()), ':');
        String   blockRegistryName = splitString1[1];

        return "[" + resource.getProperTool() + "]" + "(" + resource.getParentBlockModId() + ")" + blockRegistryName + ":" + resource.getBlockMetadata() + "=" + resource.getMaxBlockDegradation() + "|(" + resource.getDropModId() + ")" + itemRegistryName + ":" + resource.getItemMetadata() + "=" + resource.getDropChance();
    }

    public static IResource deserialize(String serializedString) {
        final boolean toolBreakOnUse   = serializedString.contains("!");
        final boolean toolDamageOnUse  = serializedString.contains("~");
        final String  properTool       = toolBreakOnUse || toolDamageOnUse ? StringHelper.split(StringHelper.split(serializedString, '[')[1], ']')[0].substring(1) : StringHelper.split(serializedString, ']')[0].substring(1);
        final String  parentBlockModId = StringHelper.split(StringHelper.split(StringHelper.split(serializedString, '|')[0], ')')[0], '(')[1];
        final String  dropModId        = StringHelper.split(StringHelper.split(serializedString, '|')[1], ')')[0].substring(1);

        final String  blockName        = StringHelper.split(StringHelper.split(StringHelper.split(serializedString, '|')[0], ':')[0], ')')[1];
        final String  dropName         = StringHelper.split(StringHelper.split(StringHelper.split(serializedString, '|')[1], ':')[0], ')')[1];
        final Integer blockMeta        = Integer.parseInt(StringHelper.split(StringHelper.split(StringHelper.split(serializedString, '|')[0], '=')[0], ':')[1]);
        final Integer itemMeta         = Integer.parseInt(StringHelper.split(StringHelper.split(StringHelper.split(serializedString, '|')[1], ':')[1], '=')[0]);
        final Integer dropChance       = Integer.parseInt(StringHelper.split(serializedString, '=')[1]);
        final Integer blockDegradation = Integer.parseInt(StringHelper.split(StringHelper.split(serializedString, '|')[0], '=')[1]);

        return new Resource(){
            @Override
            public String getDropModId() {
                return dropModId;
            }

            @Override
            public String getParentBlockModId() {
                return parentBlockModId;
            }

            @Override
            public String getParentBlockName() { return blockName; }

            @Override
            public String getDropName() { return dropName; }

            @Override
            public String getProperTool() { return properTool; }

            @Override
            public Integer getDropChance() {
                return dropChance;
            }

            @Override
            public Integer getBlockMetadata() {
                return blockMeta;
            }

            @Override
            public Integer getItemMetadata() {
                return itemMeta;
            }

            @Override
            public Integer getMaxBlockDegradation() { return blockDegradation; }

            @Override
            public Item getDrop() {
                return Item.getByNameOrId(dropModId + ":" + dropName);
            }

            @Override
            public Block getParentBlock() {
                return Block.getBlockFromName(parentBlockModId + ":" + blockName);
            }

            @Override
            public boolean getToolBreakOnUse() {
                return toolBreakOnUse;
            }

            @Override
            public boolean getToolDamageOnUse() { return toolDamageOnUse; }
        };
    }

    public static boolean hasResource(String serRes) throws IndexOutOfBoundsException, NullPointerException {
        IResource res = ResourceHelper.deserialize(serRes);
        if (ResourceRegistry.getResources().size() > 0) {
            for (IResource ress : ResourceRegistry.getResources()) {
                if (ress != null && ress.getParentBlock().getUnlocalizedName().compareTo(res.getParentBlock().getUnlocalizedName()) == 0 &&
                        ress.getDrop().equals(res.getDrop())) return true;
            }
        }
        return false;
    }

    public static String checkSyntax(String serializedString) {
        /*
         * 1. Check the 'Proper Tool' and 'Tool Modifier' syntax
         *      - Needs to check and see if the 'proper tool' is 'HAND', 'TOOL', 'PICKAXE', 'AXE', 'SHOVEL', or a custom item.
         *      - Then check whether there is a modifier to be placed on the tool. Available modifiers are:
         *          '~' = damages the item if it has a durability, otherwise it is ignored and treated as if the modifier doesn't exist.
         *          '!' = destroys the item in hand after use.
         */

        return "WIP";
    }

    public static Integer checkResource(String serializedString) {
        IResource testRes = ResourceHelper.deserialize(serializedString);
        if (testRes.getParentBlock() == null)
            return -1;
        if (testRes.getDrop() == null)
            return -2;
        if (testRes.getParentBlock() == null && testRes.getDrop() == null)
            return -3;
        return 0;
    }
}
