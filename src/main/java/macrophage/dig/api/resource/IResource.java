package macrophage.dig.api.resource;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface IResource {
    public String  getDropModName();
    public String  getParentBlockModName();
    public Item    getDrop();
    public Block   getParentBlock();
    public Integer getMaxBlockDegradation();
    public Integer getDropChance();
    public Integer getBlockMetadata();
    public Integer getItemMetadata();
}
