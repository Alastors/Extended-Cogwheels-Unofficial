package com.rabbitminers.extendedgears.mixin;

import com.rabbitminers.extendedgears.cogwheels.ICustomCogwheel;
import com.rabbitminers.extendedgears.config.ExtendedCogwheelsConfig;
import com.rabbitminers.extendedgears.mixin_interface.IDynamicMaterialBlockEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BracketedKineticBlockEntity.class)
public class MixinBracketedKineticBlockEntity extends SimpleKineticBlockEntity implements IDynamicMaterialBlockEntity {
    public BlockState material = Blocks.SPRUCE_PLANKS.defaultBlockState();

    public MixinBracketedKineticBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public BlockState getMaterial() {
        return material;
    }

    @Override
    public InteractionResult applyMaterialIfValid(ItemStack stack) {
        if (!(stack.getItem()instanceof BlockItem blockItem))
            return InteractionResult.PASS;
        BlockState material = blockItem.getBlock()
                .defaultBlockState();
        if (material == this.material)
            return InteractionResult.PASS;
        if (!material.is(BlockTags.PLANKS))
            return InteractionResult.PASS;
        if (level.isClientSide() && !isVirtual())
            return InteractionResult.SUCCESS;
        this.material = material;
        notifyUpdate();
        level.levelEvent(2001, worldPosition, Block.getId(material));
        return InteractionResult.SUCCESS;
    }

    private int getStressLimit() {
        Integer stressLimit = null;
        if (getBlockState().getBlock() instanceof ICustomCogwheel cogwheel)
            stressLimit = ExtendedCogwheelsConfig.getStressLimitByMaterial(cogwheel.getMaterial());
        return stressLimit == null ? ExtendedCogwheelsConfig.SPRUCE_COGWHEEL_STRESS_LIMITS.get()
                : stressLimit;
    }

    private int getRotationalSpeedLimit() {
        Integer stressLimit = null;
        if (getBlockState().getBlock() instanceof ICustomCogwheel cogwheel)
            stressLimit = ExtendedCogwheelsConfig.getRotationLimitByMaterial(cogwheel.getMaterial());
        return stressLimit == null ? ExtendedCogwheelsConfig.SPRUCE_COGWHEEL_ROTATION_LIMITS.get()
                : stressLimit;
    }

    public void tick() {
        super.tick();
        if (!(this.getBlockState().getBlock() instanceof ICogWheel)
                || level == null || level.isClientSide)
            return;
        boolean shouldBreak = (ExtendedCogwheelsConfig.APPLY_ROTATION_LIMITS.get()
                && Math.abs(speed) > getRotationalSpeedLimit())
                || (ExtendedCogwheelsConfig.APPLY_STRESS_LIMITS.get()
                && Math.abs(capacity) > getStressLimit());
        if (shouldBreak)
            level.destroyBlock(worldPosition, true);
    }

    protected void redraw() {
        if (!isVirtual())
            requestModelDataUpdate();
        if (hasLevel()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            level.getChunkSource()
                    .getLightEngine()
                    .checkBlock(worldPosition);
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        BlockState prevMaterial = material;
        if (!compound.contains("Material"))
            return;

        material = NbtUtils.readBlockState(compound.getCompound("Material"));
        if (material.isAir())
            material = Blocks.SPRUCE_PLANKS.defaultBlockState();

        if (clientPacket && prevMaterial != material)
            redraw();
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Material", NbtUtils.writeBlockState(material));
    }
}
