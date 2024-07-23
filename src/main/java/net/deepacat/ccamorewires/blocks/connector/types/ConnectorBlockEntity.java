package net.deepacat.ccamorewires.blocks.connector.types;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.deepacat.ccamorewires.blocks.connector.base.AbstractConnectorBlock;
import net.deepacat.ccamorewires.blocks.connector.base.AbstractConnectorBlockEntity;
import net.deepacat.ccamorewires.blocks.connector.base.SpoolType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ConnectorBlockEntity extends AbstractConnectorBlockEntity {
    public final ConnectorType type;

    public ConnectorBlockEntity(BlockEntityType<?> beType, BlockPos pos, BlockState state, ConnectorType type) {
        super(beType, pos, state, type);
        this.type = type;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
    }

    @Override
    public int getMaxIn() {
        return type.energyIn;
    }

    @Override
    public int getMaxOut() {
        return type.energyOut;
    }

    @Override
    public int getNodeCount() {
        return type.connections;
    }

    @Override
    public Vec3 getNodeOffset(int node) {
        return new Vec3(getBlockState().getValue(AbstractConnectorBlock.FACING)
                .step().mul((8 - (type.height + type.style.baseHeight)) / 16F));
    }

    @Override
    public SpoolType getSpoolType() {
        return type.spoolType;
    }

    public int getMaxWireLength() {
        return type.wireLength;
    }
}
