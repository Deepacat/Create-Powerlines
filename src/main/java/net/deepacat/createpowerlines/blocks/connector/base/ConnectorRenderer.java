package net.deepacat.createpowerlines.blocks.connector.base;

import net.deepacat.createpowerlines.rendering.WireNodeRenderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class ConnectorRenderer extends WireNodeRenderer<AbstractConnectorBlockEntity> {

	public ConnectorRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}
}

