package macrophage.dig.api.resource;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Resource implements IResource {
    @Override
    public String getDropModId() { return null; }

    @Override
    public String getParentBlockModId() { return null; }

    @Override
    public String getParentBlockName() { return null; }

    @Override
    public String getDropName() { return null; }

    @Override
    public String getProperTool() { return null; }

    @Override
    public Integer getMaxBlockDegradation() { return null; }

    @Override
    public Integer getDropChance() {
        return null;
    }

    @Override
    public Integer getBlockMetadata() {
        return null;
    }

    @Override
    public Integer getItemMetadata() {
        return null;
    }

    @Override
    public Item getDrop() {
        return null;
    }

    @Override
    public Block getParentBlock() {
        return null;
    }

    @Override
    public boolean getToolBreakOnUse() { return false; }

    @Override
    public boolean getToolDamageOnUse() { return false; }
}
