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
    private final static float OFFSET_HEIGHT = 3f;
    public final static Vec3 OFFSET_DOWN = new Vec3(0f, -OFFSET_HEIGHT/16f, 0f);
    public final static Vec3 OFFSET_UP = new Vec3(0f, OFFSET_HEIGHT/16f, 0f);
    public final static Vec3 OFFSET_NORTH = new Vec3(0f, 0f, -OFFSET_HEIGHT/16f);
    public final static Vec3 OFFSET_WEST = new Vec3(-OFFSET_HEIGHT/16f, 0f, 0f);
    public final static Vec3 OFFSET_SOUTH = new Vec3(0f, 0f, OFFSET_HEIGHT/16f);
    public final static Vec3 OFFSET_EAST = new Vec3(OFFSET_HEIGHT/16f, 0f, 0f);

    public final ConnectorType type;

    public ConnectorBlockEntity(BlockEntityType<?> beType, BlockPos pos, BlockState state, ConnectorType type) {
        super(beType, pos, state, type);
        this.type = type;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {}

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
        return switch (getBlockState().getValue(AbstractConnectorBlock.FACING)) {
            case DOWN -> OFFSET_DOWN;
            case UP -> OFFSET_UP;
            case NORTH -> OFFSET_NORTH;
            case WEST -> OFFSET_WEST;
            case SOUTH -> OFFSET_SOUTH;
            case EAST -> OFFSET_EAST;
        };
    }

    @Override
    public SpoolType getSpoolType() {
        return type.spoolType;
    }

    public int getMaxWireLength() {
        return type.wireLength;
    }
}
