package macrophage.dig.handler;

import macrophage.dig.api.resource.IResource;
import macrophage.dig.api.ResourceRegistry;
import macrophage.dig.helper.OreDictHelper;
import macrophage.dig.util.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class PlayerDigEventHandler {
    public static Random random = new Random();

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onPlayerDigBlock(PlayerInteractEvent.RightClickBlock event) throws IndexOutOfBoundsException{
        if ( event.getWorld().isRemote ) {

            event.getEntityPlayer().swingArm( EnumHand.MAIN_HAND );

        } else {
            List<IResource> registry_resources = ResourceRegistry.getResources();
            boolean is_sneak_required = ConfigHandler.Features.SNEAK_REQUIRED;

            if (registry_resources.size() > 0) {
                if ( is_sneak_required && !event.getEntityPlayer().isSneaking() ) return;

                ItemStack block_stack = new ItemStack(event.getWorld().getBlockState(event.getPos()).getBlock(), 1);
                Block block = Block.getBlockFromItem(block_stack.getItem());

                if (block.hasTileEntity(block.getDefaultState())) return;

                List<IResource> droppable_resources = new ArrayList<>();
                for (IResource resource : registry_resources) {
                    String block_reg_name = String.valueOf(block.getRegistryName());
                    String block_ore_dict_name = OreDictHelper.getOreDictName( block_stack );

                    String res_block_reg_name = "";

                    for (String ore_dict_prefix : OreDictHelper.oreDictPrefixes) {
                        if (resource.getParentBlockModId().compareTo(ore_dict_prefix) == 0)
                            res_block_reg_name = resource.getParentBlockModId() + resource.getParentBlockName();
                        else
                            res_block_reg_name = resource.getParentBlockModId() + ":" + resource.getParentBlockName();
                    }

                    if (checkTool(event.getEntityPlayer(), resource)) {
                        if (res_block_reg_name.compareTo(block_reg_name) == 0) {
                            IBlockState block_state = event.getWorld().getBlockState(event.getPos());

                            if (block.getMetaFromState(block_state) == resource.getBlockMetadata()) {
                                droppable_resources.add(resource);
                            }
                        } else if (res_block_reg_name.compareTo(block_ore_dict_name) == 0) {
                            droppable_resources.add(resource);
                        }
                    }

                    if (event.getHand() == EnumHand.MAIN_HAND) {
                        boolean right_tool = false;
                        boolean break_tool = false;
                        boolean damage_tool = false;

                        if (droppable_resources.size() == 0 || !DegradationHandler.hasDegradation(event.getPos())) {
                            if (ConfigHandler.Features.DISPLAY_ALERTS)
                                event.getEntityPlayer().sendMessage(new TextComponentString("Hmm, nothing here..."));
                        }

                        if (!DegradationHandler.hasDegradation(event.getPos())) {
                            DegradationHandler.addBlock(event.getPos(), droppable_resources.get(0).getMaxBlockDegradation(), droppable_resources);
                        }

                        if (droppable_resources.size() > 0) {
                            for (IResource resource2 : droppable_resources) {
                                right_tool = checkTool(event.getEntityPlayer(), resource2);
                                if (right_tool) break;
                            }
                        }

                        for (IResource resource2 : droppable_resources) {
                            if (right_tool) {
                                if (resource2 != null && checkDropChance(resource2)) {
                                    event.getEntityPlayer().dropItem(new ItemStack(resource2.getDrop(), 1, resource2.getItemMetadata()), false);

                                    break_tool = resource2.getToolBreakOnUse();
                                    damage_tool = resource2.getToolDamageOnUse();
                                }
                            }
                        }

                        if (DegradationHandler.getDegradationValue(event.getPos()) < 2) {
                            DegradationHandler.delBlock(event.getPos());
                            event.getWorld().destroyBlock(event.getPos(), false);

                            ItemStack held_item = event.getEntityPlayer().getHeldItemMainhand();

                            if ( break_tool ) {
                                if ( held_item.getCount() > 1 ) {
                                    held_item.setCount( held_item.getCount() - 1 );
                                } else {
                                    held_item.setCount(0);
                                }
                            } else if ( damage_tool ) {
                                held_item.attemptDamageItem(1, new Random(), null);
                            }

                            event.getEntityPlayer().setHeldItem( EnumHand.MAIN_HAND, held_item );
                        } else {
                            if (right_tool) {
                                DegradationHandler.decrDegradationValue(event.getPos());
                            }
                        }

                        if ((DegradationHandler.getDegradationValue(event.getPos()) > 2 && DegradationHandler.getDegradationProgress(event.getPos()) <= 25.D) && !DegradationHandler.hasAlmostFinished(event.getPos())) {
                            if (ConfigHandler.Features.DISPLAY_ALERTS)
                                event.getEntityPlayer().sendMessage(new TextComponentString("Almost there..."));
                            DegradationHandler.setAlmostFinished(event.getPos(), true);
                        }
                    }

                    event.getWorld().playSound(null, event.getPos(), block.getSoundType(block.getDefaultState(), event.getWorld(), event.getPos(), null).getBreakSound(), SoundCategory.BLOCKS, block.getSoundType(block.getDefaultState(), event.getWorld(), event.getPos(), null).getVolume() * 0.4F, block.getSoundType(block.getDefaultState(), event.getWorld(), event.getPos(), null).getPitch() + (float) (Math.random() * 0.2 - 0.1));
                }
            }
        }

        /*
        if (event.getWorld().isRemote) {
            event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
        } else {
            List<IResource> regRess = ResourceRegistry.getResources();
            if (regRess.size() > 0) {
                ItemStack mainHand = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
                if (event.getEntityPlayer().isSneaking()) {
                    ItemStack blockStack = new ItemStack(event.getWorld().getBlockState(event.getPos()).getBlock(), 1);
                    Block block = Block.getBlockFromItem(blockStack.getItem());

                    if (block.hasTileEntity(block.getBlockState().getBaseState())) return;

                    List<IResource> ressToDrop = new ArrayList<IResource>();
                    for (IResource resource : regRess) {
                        String blockRegName = String.valueOf(block.getRegistryName());
                        String blockOreDictName = OreDictionary.getOreName(OreDictionary.getOreIDs(new ItemStack(block))[0]);
                        String resBlockRegName = resource.getParentBlockModId().compareTo("ore") == 0 || resource.getParentBlockModId().compareTo("log") == 0 || resource.getParentBlockModId().compareTo("block") == 0 ? resource.getParentBlockModId() + resource.getParentBlockName() : resource.getParentBlockModId() + ":" + resource.getParentBlockName();

                        if (checkTool(event.getEntityPlayer(), resource)) {
                            if (resBlockRegName.compareTo(blockRegName) == 0) {
                                if (block.getMetaFromState(event.getWorld().getBlockState(event.getPos())) == resource.getBlockMetadata()) {
                                    ressToDrop.add(resource);
                                }
                            } else if (resBlockRegName.compareTo(blockOreDictName) == 0) {
                                ressToDrop.add(resource);
                            }
                        }
                    }

                    if (event.getHand() == EnumHand.MAIN_HAND) {
                        boolean rightTool = false;
                        boolean breakTool = false;

                        if (ressToDrop.size() == 0 || !DegradationHandler.hasDegradation(event.getPos())) {
                            if (ConfigHandler.displayAlerts)
                                event.getEntityPlayer().sendMessage(new TextComponentString("Hmm, nothing here..."));
                        }

                        if (!DegradationHandler.hasDegradation(event.getPos()))
                            DegradationHandler.addBlock(event.getPos(), ressToDrop.get(0).getMaxBlockDegradation(), ressToDrop);

                        if (ressToDrop.size() > 0) {
                            for (IResource res : ressToDrop) {
                                rightTool = checkTool(event.getEntityPlayer(), res);
                                if (rightTool) break;
                            }
                        }

                        for (IResource res : ressToDrop) {
                            if (res != null && checkDropChance(res)) {
                                if (rightTool) {
                                    event.getEntityPlayer().dropItem(new ItemStack(res.getDrop(), 1, res.getItemMetadata()), false);
                                    breakTool = res.getToolBreakOnUse();
                                }
                            }
                        }

                        if (DegradationHandler.getDegradationValue(event.getPos()) < 2) {
                            DegradationHandler.delBlock(event.getPos());
                            event.getWorld().destroyBlock(event.getPos(), false);
                            if (breakTool) {
                                if (mainHand.getCount() > 1) mainHand.setCount(mainHand.getCount() - 1);
                                else mainHand.setCount(0);
                            }
                        } else {
                            if (rightTool)
                                DegradationHandler.decrDegradationValue(event.getPos());
                        }

                        if ((DegradationHandler.getDegradationValue(event.getPos()) > 2 && DegradationHandler.getDegradationProgress(event.getPos()) <= 25.D) && !DegradationHandler.hasAlmostFinished(event.getPos())) {
                            event.getEntityPlayer().sendMessage(new TextComponentString("Almost there..."));
                            DegradationHandler.setAlmostFinished(event.getPos(), true);
                        }
                    }

                    event.getWorld().playSound(null, event.getPos(), block.getSoundType(block.getDefaultState(), event.getWorld(), event.getPos(), null).getBreakSound(), SoundCategory.BLOCKS, block.getSoundType(block.getDefaultState(), event.getWorld(), event.getPos(), null).getVolume() * 0.4F, block.getSoundType(block.getDefaultState(), event.getWorld(), event.getPos(), null).getPitch() + (float) (Math.random() * 0.2 - 0.1));
                }
            }
        }
        */
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.HarvestDropsEvent event) {
        if (DegradationHandler.hasDegradation(event.getPos())) {
            DegradationHandler.delBlock(event.getPos());
            event.setDropChance(0.F);
            event.getWorld().destroyBlock(event.getPos(), false);
        }
    }

    public static boolean checkDropChance(IResource resource) {
        Integer chance = random.nextInt(100);
        return chance > -1 && chance < resource.getDropChance();
    }

    public static boolean checkTool(EntityPlayer player, IResource res) {

        if (res.getProperTool().compareTo(ModInfo.PROPER_TOOL.HAND) == 0) return player.getHeldItem(EnumHand.MAIN_HAND).isEmpty();
        else if (res.getProperTool().compareTo(ModInfo.PROPER_TOOL.PICKAXE) == 0) return player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemPickaxe;
        else if (res.getProperTool().compareTo(ModInfo.PROPER_TOOL.AXE) == 0) return player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemAxe;
        else if (res.getProperTool().compareTo(ModInfo.PROPER_TOOL.SHOVEL) == 0) return player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSpade;
        else if (res.getProperTool().compareTo(ModInfo.PROPER_TOOL.TOOL) == 0) return player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemTool;
        else {
            String resToolName = res.getProperTool();
            String handToolName = String.valueOf(player.getHeldItem(EnumHand.MAIN_HAND).getItem().getRegistryName());

            if (handToolName.compareTo(resToolName) != 0) {
                return false;
            }
        }

        return true;
    }
}
