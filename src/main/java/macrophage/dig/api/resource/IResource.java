package macrophage.dig.api.resource;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface IResource {
    public String  getDropModId();
    public String  getParentBlockModId();
    public String  getParentBlockName();
    public String  getDropName();
    public String  getProperTool();
    public Integer getMaxBlockDegradation();
    public Integer getDropChance();
    public Integer getBlockMetadata();
    public Integer getItemMetadata();

    public Item    getDrop();
    public Block   getParentBlock();
    public boolean getToolBreakOnUse();
    public boolean getToolDamageOnUse();
}
