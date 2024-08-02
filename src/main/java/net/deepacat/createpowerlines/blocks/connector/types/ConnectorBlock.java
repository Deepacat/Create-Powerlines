package net.deepacat.createpowerlines.blocks.connector.types;

import net.deepacat.createpowerlines.blocks.connector.base.AbstractConnectorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ConnectorBlock extends AbstractConnectorBlock<ConnectorBlockEntity> {
    public final ConnectorType type;

    public ConnectorBlock(Properties blockProps, ConnectorType connProps) {
        super(blockProps);
        this.type = connProps;
    }

    @Override
    public Class<ConnectorBlockEntity> getBlockEntityClass() {
        return ConnectorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ConnectorBlockEntity> getBlockEntityType() {
        return type.beEntry.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ConnectorBlockEntity(type.beEntry.get(), pos, state, type);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return type.shape.get(state.getValue(FACING).getOpposite());
    }
}
