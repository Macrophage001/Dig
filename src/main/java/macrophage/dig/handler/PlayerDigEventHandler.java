package macrophage.dig.handler;

import macrophage.dig.api.resource.IResource;
import macrophage.dig.api.ResourceRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerDigEventHandler {
    public static Random random = new Random();

    @SubscribeEvent
    public void onPlayerDigBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) {

            event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
        } else {

            List<IResource> resources = ResourceRegistry.getResources();
            if (resources.size() > 0) {

                ItemStack equippedStack = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
                if (equippedStack == null && event.getEntityPlayer().isSneaking()) {

                    Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
                    if (block.hasTileEntity(block.getBlockState().getBaseState())) return;
                    List<IResource> resources2 = new ArrayList<IResource>();
                    for (IResource resource : resources) {
                        if (block.equals(Block.getBlockFromItem(new ItemStack(resource.getParentBlock(), 1, resource.getBlockMetadata()).getItem()))) {
                            resources2.add(resource);
                        }
                    }

                    if (event.getHand() == EnumHand.MAIN_HAND) {
                        event.getWorld().playSound(null, event.getPos(), block.getSoundType().getBreakSound(), SoundCategory.BLOCKS, block.getSoundType().getVolume() * 0.4F, block.getSoundType().getPitch() + (float) (Math.random() * 0.2 - 0.1));

                        if (resources2.size() == 0) event.getEntityPlayer().addChatComponentMessage(new TextComponentString("Hmm, nothing here..."));

                        if (!DegradationHandler.hasDegradation(event.getPos())) DegradationHandler.addBlock(event.getPos(), resources2.get(0).getMaxBlockDegradation(), resources2);

                        for (IResource res : resources2) {
                            if (res != null && checkCanDrop(res)) {
                                event.getEntityPlayer().dropItem(new ItemStack(res.getDrop(), 1, res.getItemMetadata()), false);
                            }
                        }

                        if (DegradationHandler.getDegradationValue(event.getPos()) < 2) {
                            DegradationHandler.delBlock(event.getPos());
                            event.getWorld().destroyBlock(event.getPos(), false);
                        } else {
                            DegradationHandler.decrDegradationValue(event.getPos());
                        }

                        if ((DegradationHandler.getDegradationValue(event.getPos()) > 2 && DegradationHandler.getDegradationProgress(event.getPos()) <= 25.D) && !DegradationHandler.hasAlmostFinished(event.getPos())) {
                            event.getEntityPlayer().addChatComponentMessage(new TextComponentString("Almost there..."));
                            DegradationHandler.setAlmostFinished(event.getPos(), true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockBroken(BlockEvent.BreakEvent event) {
        if (DegradationHandler.getDegradationValue(event.getPos()) > 2) {
            DegradationHandler.delBlock(event.getPos());
            event.getWorld().destroyBlock(event.getPos(), false);
        }
    }

    public static boolean checkCanDrop(IResource resource) {
        Integer chance = random.nextInt(100);
        return chance > -1 && chance < resource.getDropChance();
    }


}
