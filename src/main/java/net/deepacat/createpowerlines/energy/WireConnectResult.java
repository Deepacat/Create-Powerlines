package net.deepacat.createpowerlines.energy;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum WireConnectResult {

	LINKED(Component.translatable("statusbar.createpowerlines.wire.linked")),
	LINKED_IN(Component.translatable("statusbar.createpowerlines.wire.linked_in")),
	LINKED_OUT(Component.translatable("statusbar.createpowerlines.wire.linked_out")),

	CONNECT(Component.translatable("statusbar.createpowerlines.wire.connect")),
	CONNECT_IN(Component.translatable("statusbar.createpowerlines.wire.connect_in")),
	CONNECT_OUT(Component.translatable("statusbar.createpowerlines.wire.connect_out")),

	LONG(Component.translatable("statusbar.createpowerlines.wire.long").withStyle(ChatFormatting.RED)),
	OBSTRUCTED(Component.translatable("statusbar.createpowerlines.wire.obstructed").withStyle(ChatFormatting.RED)),
	COUNT(Component.translatable("statusbar.createpowerlines.wire.count").withStyle(ChatFormatting.RED)),
	REMOVED(Component.translatable("statusbar.createpowerlines.wire.removed")),
	EXISTS(Component.translatable("statusbar.createpowerlines.wire.exists").withStyle(ChatFormatting.RED)),
	NO_CONNECTION(Component.translatable("statusbar.createpowerlines.wire.no_connection").withStyle(ChatFormatting.RED)),
	INVALID(Component.translatable("statusbar.createpowerlines.wire.invalid").withStyle(ChatFormatting.RED)),
	MISMATCHED_WIRE(Component.translatable("statusbar.createpowerlines.wire.mismatched_wire").withStyle(ChatFormatting.RED));

	private final Component message;

	WireConnectResult(Component message) {
		this.message = message;
	}

	public Component getMessage() {
		return message;
	}

	public boolean isLinked() {
		return this == LINKED || this == LINKED_IN || this == LINKED_OUT;
	}

	public boolean isConnect() {
		return this == CONNECT || this == CONNECT_IN || this == CONNECT_OUT;
	}

	public static WireConnectResult getLink(boolean in, boolean out) {
		if(in && !out)
			return LINKED_IN;
		if(!in && out)
			return LINKED_OUT;
		return LINKED;
	}

	public static WireConnectResult getConnect(boolean in, boolean out) {
		if(in && !out)
			return CONNECT_IN;
		if(!in && out)
			return CONNECT_OUT;
		return CONNECT;
	}
}
