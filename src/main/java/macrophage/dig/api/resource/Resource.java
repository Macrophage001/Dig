package macrophage.dig.api.resource;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Resource implements IResource {
    @Override
    public String getDropModName() { return null; }

    @Override
    public String getParentBlockModName() { return null; }

    @Override
    public Item getDrop() {
        return null;
    }

    @Override
    public Block getParentBlock() {
        return null;
    }

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
}
