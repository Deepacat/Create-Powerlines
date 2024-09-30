package net.deepacat.createpowerlines.item;

import java.util.List;

import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.energy.IWireNode;
import net.deepacat.createpowerlines.energy.WireConnectResult;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class WireSpool extends Item {

    public final WireMaterial material;

    public WireSpool(Properties props, WireMaterial material) {
        super(props);
        this.material = material;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext c) {
        CompoundTag nbt = c.getItemInHand().getTag();
        if (nbt == null)
            nbt = new CompoundTag();
        BlockPos clickedPos = c.getClickedPos();
        BlockEntity te = c.getLevel().getBlockEntity(clickedPos);
        if (!(te instanceof IWireNode node))
            return InteractionResult.PASS;
        if (hasPos(nbt)) {
            WireMaterial existingMaterial = IWireNode.getWireMaterialOfConnection(c.getLevel(), clickedPos, getPos(nbt));
            WireConnectResult result;
            if (material == null) {
                result = IWireNode.disconnect(c.getLevel(), clickedPos, getPos(nbt));
            } else {
                result = IWireNode.connect(c.getLevel(), getPos(nbt), getNode(nbt), clickedPos,
                        node.getAvailableNode(c.getClickLocation()), material);
            }
            // Play sound
            if (result.isLinked()) {
                c.getLevel().playLocalSound(clickedPos.getX(), clickedPos.getY(), clickedPos.getZ(), SoundEvents.NOTE_BLOCK_XYLOPHONE.get(), SoundSource.BLOCKS, .7f, 1f, false);
            } else if (result.isConnect()) {
                c.getLevel().playLocalSound(clickedPos.getX(), clickedPos.getY(), clickedPos.getZ(), SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1f, 1f, false);
            } else if (result == WireConnectResult.REMOVED) {
                c.getLevel().playLocalSound(clickedPos.getX(), clickedPos.getY(), clickedPos.getZ(), SoundEvents.NOTE_BLOCK_XYLOPHONE.get(), SoundSource.BLOCKS, .7f, .5f, false);
            } else {
                c.getLevel().playLocalSound(clickedPos.getX(), clickedPos.getY(), clickedPos.getZ(), SoundEvents.NOTE_BLOCK_DIDGERIDOO.get(), SoundSource.BLOCKS, .7f, 1f, false);
            }
            te.setChanged();
            if (c.getPlayer() != null && !c.getPlayer().isCreative()) {
                if (result == WireConnectResult.REMOVED) {
                    c.getItemInHand().shrink(1);
                    ItemStack stack = existingMaterial.spool.asStack();
                    boolean shouldDrop = !c.getPlayer().addItem(stack);
                    if (shouldDrop)
                        c.getPlayer().drop(stack, false);
                } else if (result.isLinked()) {
                    c.getItemInHand().shrink(1);
                    ItemStack stack = new ItemStack(WireMaterials.EMPTY_SPOOL.get(), 1);
                    boolean shouldDrop = !c.getPlayer().addItem(stack);
                    if (shouldDrop)
                        c.getPlayer().drop(stack, false);
                }
            }
            c.getItemInHand().setTag(null);
            c.getPlayer().displayClientMessage(result.getMessage(), true);
        } else {
            if (c.getPlayer() == null) return InteractionResult.PASS;
            int index = -1;
            if (material == null) {
                if (!node.hasAnyConnection()) {
                    c.getPlayer().displayClientMessage(WireConnectResult.NO_CONNECTION.getMessage(), true);
                    c.getLevel().playLocalSound(clickedPos.getX(), clickedPos.getY(), clickedPos.getZ(),
                            SoundEvents.NOTE_BLOCK_DIDGERIDOO.get(), SoundSource.BLOCKS, .7f, 1f, false);
                    return InteractionResult.CONSUME;
                }
            } else {
                index = node.getAvailableNode(c.getClickLocation());
                if (index < 0)
                    return InteractionResult.PASS;
                c.getPlayer().displayClientMessage(WireConnectResult.getConnect(node.isNodeInput(index), node.isNodeOutput(index)).getMessage(), true);
            }
            c.getItemInHand().setTag(null);
            c.getItemInHand().setTag(setContent(nbt, node.getPos(), index));
        }
        return InteractionResult.CONSUME;
    }

    public static boolean hasPos(CompoundTag nbt) {
        if (nbt == null)
            return false;
        return nbt.contains("x") && nbt.contains("y") && nbt.contains("z") && nbt.contains("node");
    }

    public static BlockPos getPos(CompoundTag nbt) {
        if (nbt == null)
            return null;
        return new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
    }

    public static int getNode(CompoundTag nbt) {
        if (nbt == null)
            return -1;
        return nbt.getInt("node");
    }

    public static CompoundTag setContent(CompoundTag nbt, BlockPos pos, int node) {
        if (nbt == null)
            return new CompoundTag();
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putInt("node", node);
        return nbt;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        CompoundTag nbt = stack.getTag();
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (hasPos(nbt))
            tooltip.add(Component.translatable("item." + CreatePowerlines.MODID + ".spool.nbt"));
    }
}
