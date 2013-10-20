package nekto.controller.client;

import java.util.EnumSet;

import nekto.controller.core.Controller;
import nekto.controller.item.ItemBase;
import nekto.controller.item.ItemRemote;
import nekto.controller.network.PacketHandler;
import nekto.controller.ref.GeneralRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class RemoteKeyHandler extends KeyHandler {
	private static KeyBinding keyBind = new KeyBinding("Remote Control Key", Keyboard.KEY_R);

	public RemoteKeyHandler() {
		super(new KeyBinding[] { keyBind }, new boolean[] { false });
	}

	@Override
	public String getLabel() {
		return this.toString();
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (kb.keyDescription.equals(keyBind.keyDescription) && tickEnd) {
			if (Minecraft.getMinecraft().currentScreen == null) {
				EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
				if (player != null) {
					ItemStack stack = player.inventory.getCurrentItem();
					if (stack != null && stack.getItem() instanceof ItemRemote && stack.hasTagCompound() && stack.getTagCompound().hasKey(ItemBase.KEYTAG)) {
						int[] data = stack.getTagCompound().getIntArray(ItemBase.KEYTAG);
						Packet packet = PacketHandler.getGuiPacket(true, -1, data[0], data[1], data[2]);
						player.sendQueue.addToSendQueue(packet);
						player.openGui(Controller.instance, GeneralRef.REMOTE_GUI_ID, Minecraft.getMinecraft().theWorld, (int) player.posX, (int) player.posY, (int) player.posZ);
					}
				}
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}
}
