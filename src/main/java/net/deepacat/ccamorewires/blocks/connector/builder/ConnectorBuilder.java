package net.deepacat.ccamorewires.blocks.connector.builder;

import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.deepacat.ccamorewires.CCAMoreWires;
import net.deepacat.ccamorewires.energy.NodeMovementBehaviour;
import net.minecraft.world.level.block.Block;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ConnectorBuilder {
    public static void connectorBuilder(String displayName) {
        // Register Block
        BlockEntry<Block> newBlock =
            CCAMoreWires.REGISTRATE.block(displayName, Block::new)
                .initialProperties(SharedProperties::stone)
                .onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
                .item()
                .transform(customItemModel())
                .register();
    }

    public static void register(){
        connectorBuilder("test_block1");
        connectorBuilder("test_block2");
        connectorBuilder("test_block3");
        connectorBuilder("test_block4");
    }
}
