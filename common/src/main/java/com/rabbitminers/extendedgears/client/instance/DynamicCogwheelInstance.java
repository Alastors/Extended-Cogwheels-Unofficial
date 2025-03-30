package com.rabbitminers.extendedgears.client.instance;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.model.BlockModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedgears.cogwheels.CogwheelModelKey;
import com.rabbitminers.extendedgears.cogwheels.DynamicCogwheelRenderer;
import com.rabbitminers.extendedgears.cogwheels.HalfShaftCogwheelBlock;
import com.rabbitminers.extendedgears.mixin_interface.CogwheelTypeProvider;
import com.rabbitminers.extendedgears.mixin_interface.IDynamicMaterialBlockEntity;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.CachedBufferer;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import com.rabbitminers.extendedgears.config.ExtendedCogwheelsConfig;

public class DynamicCogwheelInstance extends SingleRotatingInstance<KineticBlockEntity> {
    protected final boolean large;
    @Nullable protected final CogwheelModelKey key;
    protected RotatingData additionalShaft;

    public DynamicCogwheelInstance(MaterialManager materialManager, KineticBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        BlockState state = blockState;
        this.large = state.getBlock() instanceof com.simibubi.create.content.kinetics.simpleRelays.ICogWheel cog && cog.isLargeCog();

        if (!(blockEntity instanceof IDynamicMaterialBlockEntity provider)) {
            throw new IllegalStateException("Expected IDynamicMaterialBlockEntity on cogwheel block entity.");
        }

        this.key = new CogwheelModelKey(large, state, provider.getMaterial());
    }

    @Override
    public void init() {
        super.init();
        if (!ExtendedCogwheelsConfig.common().renderCasings.get())
            return;

        Axis axis = ((IRotate) blockState.getBlock()).getRotationAxis(blockState);
        float speed = blockEntity.getSpeed();
        float offset = com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos);

        AxisDirection direction = blockState.getBlock() instanceof HalfShaftCogwheelBlock
                ? HalfShaftCogwheelBlock.getAxisDirection(blockState)
                : AxisDirection.POSITIVE;

        Direction facing = Direction.fromAxisAndDirection(axis, direction);
        PartialModel shaftModel = getShaftModel(blockState.getBlock());
        if (shaftModel == null)
            return;

        PoseStack transform = rotateToAxis(axis, direction);

        Instancer<RotatingData> instancer = getRotatingMaterial().getModel(shaftModel, blockState, facing, () -> transform);
        additionalShaft = setup(instancer.createInstance(), speed);
        additionalShaft.setRotationOffset(offset);
    }


    private PoseStack rotateToAxis(Axis axis, AxisDirection direction) {
        PoseStack poseStack = new PoseStack();
        TransformStack.cast(poseStack)
                .centre()
                .rotateToFace(Direction.fromAxisAndDirection(axis, direction).getOpposite())
                .unCentre();
        return poseStack;
    }

    @Nullable
    protected PartialModel getShaftModel(Block block) {
        if (block instanceof CogwheelTypeProvider provider) {
            return switch (provider.getType()) {
                case STANDARD -> AllPartialModels.COGWHEEL_SHAFT;
                case HALF_SHAFT -> AllPartialModels.SHAFT_HALF;
                case SHAFLTESS -> null;
            };
        }
        return AllPartialModels.COGWHEEL_SHAFT;
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        if (key == null)
            return super.getModel();

        return getRotatingMaterial().model(key, () -> {
            BakedModel model = DynamicCogwheelRenderer.generateModel(key);
            Direction dir = Direction.fromAxisAndDirection(
                    ((IRotate) key.state().getBlock()).getRotationAxis(key.state()),
                    key.state().getBlock() instanceof HalfShaftCogwheelBlock
                            ? HalfShaftCogwheelBlock.getAxisDirection(key.state())
                            : AxisDirection.POSITIVE
            );
            PoseStack transform = CachedBufferer.rotateToFaceVertical(dir).get();
            return BlockModel.of(model, Blocks.AIR.defaultBlockState(), transform);
        });
    }


}
