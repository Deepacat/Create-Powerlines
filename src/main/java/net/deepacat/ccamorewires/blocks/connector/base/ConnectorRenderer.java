package net.deepacat.ccamorewires.blocks.connector.base;

import net.deepacat.ccamorewires.rendering.WireNodeRenderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class ConnectorRenderer extends WireNodeRenderer<AbstractConnectorBlockEntity> {

	public ConnectorRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}
}

