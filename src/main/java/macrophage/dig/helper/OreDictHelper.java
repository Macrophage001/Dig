package macrophage.dig.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class OreDictHelper {
    public static String[] oreDictPrefixes = { "ore", "block", "log", "plank", "nugget" };

    public static String getOreDictName(ItemStack itemStack) {
        String ore_dict_name = "";
        try {
            ore_dict_name = OreDictionary.getOreName(OreDictionary.getOreIDs(itemStack)[0]);
        } catch ( IllegalArgumentException ignored) {

        }
        return ore_dict_name;
    }
}
