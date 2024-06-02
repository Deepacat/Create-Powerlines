package net.deepacat.ccamorewires.blocks.connector;

import net.deepacat.ccamorewires.blocks.connector.base.AbstractConnectorBlock;
import net.deepacat.ccamorewires.index.CABlockEntities;
import net.deepacat.ccamorewires.shapes.CAShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MassiveConnectorBlock extends AbstractConnectorBlock<MassiveConnectorBlockEntity> {
    public static final VoxelShaper CONNECTOR_SHAPE = CAShapes.shape(5, 0, 5, 11, 7, 11).forDirectional();
    public MassiveConnectorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<MassiveConnectorBlockEntity> getBlockEntityClass() {
        return MassiveConnectorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MassiveConnectorBlockEntity> getBlockEntityType() {
        return CABlockEntities.MASSIVE_CONNECTOR.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return CABlockEntities.MASSIVE_CONNECTOR.create(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return CONNECTOR_SHAPE.get(state.getValue(FACING).getOpposite());
    }
}