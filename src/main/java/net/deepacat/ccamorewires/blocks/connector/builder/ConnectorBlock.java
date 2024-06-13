package net.deepacat.ccamorewires.blocks.connector.builder;

import com.simibubi.create.foundation.utility.VoxelShaper;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.deepacat.ccamorewires.blocks.connector.base.AbstractConnectorBlock;
import net.deepacat.ccamorewires.shapes.CAShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ConnectorBlock extends AbstractConnectorBlock<ConnectorBlockEntity> {
    public static final VoxelShaper CONNECTOR_SHAPE = CAShapes.shape(6, 0, 6, 10, 5, 10).forDirectional();

    public final ConnectorProperties connProps;
    public final BlockEntityEntry<ConnectorBlockEntity>[] beEntry;

    public ConnectorBlock(Properties blockProps, ConnectorProperties connProps, BlockEntityEntry<ConnectorBlockEntity>[] beEntry) {
        super(blockProps);
        this.connProps = connProps;
        this.beEntry = beEntry;
    }

    @Override
    public Class<ConnectorBlockEntity> getBlockEntityClass() {
        return ConnectorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ConnectorBlockEntity> getBlockEntityType() {
        return beEntry[0].get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ConnectorBlockEntity(beEntry[0].get(), pos, state, connProps);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return CONNECTOR_SHAPE.get(state.getValue(FACING).getOpposite());
    }
}
