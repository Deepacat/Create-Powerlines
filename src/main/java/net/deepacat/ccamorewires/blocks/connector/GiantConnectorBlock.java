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

public class GiantConnectorBlock extends AbstractConnectorBlock<GiantConnectorBlockEntity> {
    public static final VoxelShaper CONNECTOR_SHAPE = CAShapes.shape(6, 0, 6, 10, 5, 10).forDirectional();
    public GiantConnectorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<GiantConnectorBlockEntity> getBlockEntityClass() {
        return GiantConnectorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GiantConnectorBlockEntity> getBlockEntityType() {
        return CABlockEntities.GIANT_CONNECTOR.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return CABlockEntities.GIANT_CONNECTOR.create(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return CONNECTOR_SHAPE.get(state.getValue(FACING).getOpposite());
    }
}
