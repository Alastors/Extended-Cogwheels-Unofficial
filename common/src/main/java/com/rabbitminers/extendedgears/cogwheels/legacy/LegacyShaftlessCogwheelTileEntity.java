package com.rabbitminers.extendedgears.cogwheels.legacy;

import com.rabbitminers.extendedgears.mixin_interface.IDynamicMaterialBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LegacyShaftlessCogwheelTileEntity extends CustomCogwheelBlockEntity implements IDynamicMaterialBlockEntity {
    // Don't delete
    public LegacyShaftlessCogwheelTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // Don't delete
    @Override
    public void onDataPacket(@NotNull Connection connection, @NotNull ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(connection, packet);
    }

    // Don't delete
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        // Disable Shaft Shite, don't delete
    }

    // DO NOT DELETE
    @Override
    public ResourceLocation getMaterial() {
        /*Block block = getBlockState().getBlock();
        if (block instanceof BasicCogwheelBlock basicBlock) {
            ICogwheelMaterial material = basicBlock.getMaterial();
            return material.id();
        }*/
        return com.rabbitminers.extendedgears.ExtendedCogwheels.asResource("oak");
    }

    // DO NOT DELETE
    @Override
    public InteractionResult applyMaterialIfValid(ItemStack stack) {
        return null;
    }

    // DO NOT DELETE
    @Override
    public void applyMaterial(ResourceLocation material) {}

    public Direction.Axis getRotationAxis() {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof RotatedPillarKineticBlock rotated)
            return rotated.getRotationAxis(state);
        return Direction.Axis.Y;
    }

    public float getRenderedRotation(float partialTicks) {
        return KineticBlockEntityRenderer.getAngleForTe(this, getBlockPos(), getRotationAxis());
    }

}