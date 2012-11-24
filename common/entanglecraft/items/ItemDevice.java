package entanglecraft.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;

import org.lwjgl.input.Keyboard;
import net.minecraft.src.World;
import entanglecraft.EntangleCraft;

public class ItemDevice extends Item{
	
	private int channel = 0;
	public ItemDevice(int par1) {
		super(par1);
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	public String getTextureFile(){
		return "/lambdaTextures.png";
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {	
		EntangleCraft.teleport(par3EntityPlayer,channel);
		return par1ItemStack;
    }

	public void setChannel(int i){
		channel = i;
	}
	
	public int getChannel(){
		return channel;
	}
		
	public Item incrementChannel(){
		Item is = null;
		int x = channel + 1;
		if (x == 4) x = 0;
		
		if (x == 0) is = EntangleCraftItems.ItemDev;
		if (x == 1) is = EntangleCraftItems.ItemDeviceRed;
		if (x == 2) is = EntangleCraftItems.ItemDeviceYellow;
		if (x == 3) is = EntangleCraftItems.ItemDeviceBlue;
		return is;
	}
	
	
}
