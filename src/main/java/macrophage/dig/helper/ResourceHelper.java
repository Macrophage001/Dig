package macrophage.dig.helper;

import macrophage.dig.api.ResourceRegistry;
import macrophage.dig.api.resource.IResource;
import macrophage.dig.api.resource.Resource;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ResourceHelper {
    public static String serialize(IResource resource) {
        String[] splitString  = StringHelper.split(resource.getDrop().getRegistryName().toString(), ':');
        String   itemRegistryName = splitString[1];

        String[] splitString1 = StringHelper.split(resource.getParentBlock().getRegistryName().toString(), ':');
        String   blockRegistryName = splitString1[1];

        return "(" + resource.getParentBlockModName() + ")" + blockRegistryName + ":" + resource.getBlockMetadata() + "=" + resource.getMaxBlockDegradation() + "|(" + resource.getDropModName() + ")" + itemRegistryName + ":" + resource.getItemMetadata() + "=" + resource.getDropChance();
    }

    public static IResource deserialize(String serializedString) {
        final String  parentBlockModName = StringHelper.split(StringHelper.split(serializedString, '|')[0], ')')[0].substring(1);
        final String  dropModName        = StringHelper.split(StringHelper.split(serializedString, '|')[1], ')')[0].substring(1);

        final String  blockName        = parentBlockModName + ":" + StringHelper.split(StringHelper.split(StringHelper.split(serializedString, '|')[0], ':')[0], ')')[1];
        final String  dropName         = dropModName + ":" + StringHelper.split(StringHelper.split(StringHelper.split(serializedString, '|')[1], ':')[0], ')')[1];
        final Integer blockMeta        = Integer.parseInt(StringHelper.split(StringHelper.split(StringHelper.split(serializedString, '|')[0], '=')[0], ':')[1]);
        final Integer itemMeta         = Integer.parseInt(StringHelper.split(StringHelper.split(StringHelper.split(serializedString, '|')[1], ':')[1], '=')[0]);
        final Integer dropChance       = Integer.parseInt(StringHelper.split(serializedString, '=')[1]);
        final Integer blockDegradation = Integer.parseInt(StringHelper.split(StringHelper.split(serializedString, '|')[0], '=')[1]);

        return new Resource(){
            @Override
            public String getDropModName() {
                return dropModName;
            }

            @Override
            public String getParentBlockModName() {
                return parentBlockModName;
            }

            @Override
            public Item getDrop() {
                Item item = Item.getByNameOrId(dropName);
                if (item == null) {
                    List<ItemStack> dropStacks = OreDictionary.getOres(StringHelper.split(dropName, ':')[1]);
                    if (dropStacks.size() > 0) item = dropStacks.get(0).getItem();
                }
                return item;
            }

            @Override
            public Block getParentBlock() {
                Block block = Block.getBlockFromName(blockName);
                if (block == null) {
                    List<ItemStack> blockStacks = OreDictionary.getOres(StringHelper.split(blockName, ':')[1]);
                    if (blockStacks.size() > 0) block = Block.getBlockFromItem(blockStacks.get(0).getItem());
                }
                return block;
            }

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
            public Integer getMaxBlockDegradation() { return blockDegradation; };
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
        if (serializedString.contains("|")) {
            String[] splitString = StringHelper.split(serializedString, '|');
            if (!splitString[0].contains("(") || !splitString[0].contains(")")) return "Parent block modid missing '(' or ')'";
            if (!splitString[0].contains(":")) return "Parent block meta missing ':'";
            if (!splitString[0].contains("=")) return "Parent block durability missing '=";

            if (!splitString[1].contains("(") || !splitString[1].contains(")")) return "Drop modid missing '(' or ')'";
            if (!splitString[1].contains(":")) return "Drop meta missing ':'";
            if (!splitString[1].contains("=")) return "Drop durability missing '=";
        } else {
            return "Missing '|' seperator!";
        }
        return "correct";
    }

    public static boolean checkResource(String serializedString) {
        IResource testRes = ResourceHelper.deserialize(serializedString);
        return !testRes.getParentBlock().equals(null) && !testRes.getDrop().equals(null);
    }
}
