package macrophage.dig.helper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class OreDictHelper {
    public static String getOreDictName(ItemStack itemStack) {
        String[] oreNames = OreDictionary.getOreNames();
        String   oreDictName = "";
        for (String oreName : oreNames) {
            List<ItemStack> oreStacks = OreDictionary.getOres(oreName);
            if (oreStacks != null) {
                ItemStack oreStack = oreStacks.get(0);
                String oreStackRegName = oreStack.getItem().getRegistryName().toString();
                String itemStackRegName = itemStack.getItem().getRegistryName().toString();

                if (oreStackRegName.compareTo(itemStackRegName) == 0) {
                    oreDictName = oreName;
                }
            } else {
                oreDictName = "No entry found";
            }
        }
        return oreDictName;
    }
}
