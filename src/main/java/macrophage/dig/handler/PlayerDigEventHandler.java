package macrophage.dig.handler;

import macrophage.dig.Dig;
import macrophage.dig.api.resource.IResource;
import macrophage.dig.api.ResourceRegistry;
import macrophage.dig.helper.OreDictHelper;
import macrophage.dig.util.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber
public class PlayerDigEventHandler {
    public static Random random = new Random();

    @SubscribeEvent(
            priority = EventPriority.LOWEST,
            receiveCanceled = true
    )
    public static void onPlayerDigBlock(PlayerInteractEvent.RightClickBlock event) throws IndexOutOfBoundsException{
        if ( checkBlacklist(event.getEntityPlayer().getHeldItemMainhand()) ) {

            event.setUseItem( Event.Result.DENY );

        } else {
            if (event.getWorld().isRemote) {

                event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);

            } else {
                List<IResource> registry_resources = ResourceRegistry.getResources();

                ItemStack stack_in_hand = event.getEntityPlayer().getHeldItemMainhand();

                if ( event.getHand() != EnumHand.MAIN_HAND || stack_in_hand.getItem() instanceof ItemBlock ) return;

                if (registry_resources.size() > 0) {
                    ItemStack block_stack = new ItemStack(event.getWorld().getBlockState(event.getPos()).getBlock(), 1);
                    Block block = Block.getBlockFromItem(block_stack.getItem());
                    List<IResource> droppable_resources = new ArrayList<>();

                    if ( !event.getEntityPlayer().isSneaking() ) {

                        if ( ConfigHandler.Features.DISPLAY_ALERTS )
                            event.getEntityPlayer().sendMessage( new TextComponentString( "I need to get a closer look! Perhaps if I duck down..." ) );
                        return;
                    }

                    if ( checkForTileEntity( event.getWorld(), event.getPos() ) ) {
                        event.getEntityPlayer().sendMessage( new TextComponentString( "This block is not as simple as it seems..." ) );
                        return;
                    }

                    for (IResource resource : registry_resources) {
                        String block_reg_name = String.valueOf(block.getRegistryName());
                        String res_block_reg_name = "";
                        res_block_reg_name = resource.getParentBlockModId() + ":" + resource.getParentBlockName();

                        if ( checkTool(event.getEntityPlayer(), resource) ) {
                            if (res_block_reg_name.compareTo(block_reg_name) == 0) {
                                IBlockState block_state = event.getWorld().getBlockState(event.getPos());

                                if (block.getMetaFromState(block_state) == resource.getBlockMetadata()) {
                                    droppable_resources.add(resource);
                                }
                            }
                        }
                    }

                    boolean right_tool = false;
                    boolean break_tool = false;
                    boolean damage_tool = false;
                    boolean has_degradation = DegradationHandler.hasDegradation(event.getPos());

                    if ( droppable_resources.size() == 0 ) {
                        if (ConfigHandler.Features.DISPLAY_ALERTS)
                            event.getEntityPlayer().sendMessage(new TextComponentString("Hmm, nothing here..."));
                    }

                    if ( !has_degradation ) {
                        DegradationHandler.addBlock(event.getPos(), droppable_resources.get(0).getMaxBlockDegradation(), droppable_resources);
                    }

                    if ( droppable_resources.size() > 0 ) {
                        for (IResource resource : droppable_resources ) {
                            right_tool = checkTool( event.getEntityPlayer(), resource );
                            if (right_tool) break;
                        }
                    }

                    if ( ConfigHandler.Features.DISPLAY_ALERTS )
                        event.getEntityPlayer().sendMessage(new TextComponentString("Current Degradation Value: " + DegradationHandler.getDegradationValue(event.getPos()) + "/" + DegradationHandler.getDegradationProgress(event.getPos())));

                    for (IResource resource : droppable_resources) {
                        if (right_tool) {
                            if ( checkDropChance( resource ) ) {
                                event.getEntityPlayer().dropItem(new ItemStack(resource.getDrop(), 1, resource.getItemMetadata()), false);

                                break_tool = resource.getToolBreakOnUse();
                                damage_tool = resource.getToolDamageOnUse();
                            }
                        }
                    }

                    if ( DegradationHandler.getDegradationValue(event.getPos()) < 2 ) {
                        ItemStack held_item = event.getEntityPlayer().getHeldItemMainhand();

                        DegradationHandler.delBlock(event.getPos());
                        event.getWorld().destroyBlock(event.getPos(), false);

                        if (break_tool) {
                            if (held_item.getCount() > 1) {
                                held_item.shrink(1);
                            } else {
                                held_item.setCount(0);
                            }
                        }

                        if (damage_tool) {
                            if ( held_item.isEmpty() && ConfigHandler.Features.ALLOW_HAND_DAMAGE )
                                event.getEntityPlayer().attackEntityFrom(DamageSource.GENERIC, ConfigHandler.Features.HAND_DAMAGE_AMMOUNT);
                            else
                                held_item.attemptDamageItem(1, new Random(), null);
                        }

                        event.getEntityPlayer().setHeldItem(EnumHand.MAIN_HAND, held_item);

                        if ( ConfigHandler.Features.USE_HUNGER ) {
                            event.getEntityPlayer().addExhaustion( ConfigHandler.Features.HUNGER_AMOUNT );
                        }
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
                    event.getWorld().playSound(null, event.getPos(), block.getSoundType(block.getDefaultState(), event.getWorld(), event.getPos(), null).getBreakSound(), SoundCategory.BLOCKS, block.getSoundType(block.getDefaultState(), event.getWorld(), event.getPos(), null).getVolume() * 0.4F, block.getSoundType(block.getDefaultState(), event.getWorld(), event.getPos(), null).getPitch() + (float) (Math.random() * 0.2 - 0.1));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.HarvestDropsEvent event) {
        if (DegradationHandler.hasDegradation(event.getPos())) {
            DegradationHandler.delBlock(event.getPos());
            event.setDropChance(0.F);
            event.getWorld().destroyBlock(event.getPos(), false);
        }
    }

    private static boolean checkDropChance(IResource resource) {
        Integer chance = random.nextInt(100);
        return chance > -1 && chance < resource.getDropChance();
    }

    private static boolean checkTool(EntityPlayer player, IResource res) {

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

    private static boolean checkBlacklist(ItemStack itemStack) {
        String item_name = String.valueOf(itemStack.getItem().getRegistryName());

        for ( String tool_name : ConfigHandler.Features.TOOL_BLACKLIST ) {

            if ( item_name.compareTo(tool_name) == 0) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkForTileEntity(World world, BlockPos block_pos) {
        return world.getTileEntity( block_pos ) != null;
    }
}
