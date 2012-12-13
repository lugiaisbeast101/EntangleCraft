package entanglecraft;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import entanglecraft.items.EntangleCraftItems;
import entanglecraft.items.IChanneled;
import entanglecraft.items.ItemChanneled;
import entanglecraft.items.ItemDevice;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class KeyHandling extends KeyHandler {

	static KeyBinding increment = new KeyBinding("incrementChannel", Keyboard.KEY_G);

	public KeyHandling() {
		super(new KeyBinding[] { increment }, new boolean[] { false });
	}

	@Override
	public String getLabel() {
		return "keyBindingsEC";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding event, boolean tickEnd, boolean isRepeat) {
		if (tickEnd)
			return;
		System.out.println("Trying to change channel");
		try {
			ItemStack itemInUse = ModLoader.getMinecraftInstance().thePlayer.getCurrentEquippedItem();
			if (((itemInUse.getItem() instanceof IChanneled)) && (event == increment)) {
				InventoryPlayer inv = ModLoader.getMinecraftInstance().thePlayer.inventory;
				int x = itemInUse.stackSize;
				inv.setInventorySlotContents(inv.currentItem, new ItemStack(((IChanneled) itemInUse.getItem()).incrementChannel(), x));
				ClientPacketHandler.sendDeviceToggle();
				System.out.println("Changed Channel");
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		// TODO Auto-generated method stub

	}

	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}