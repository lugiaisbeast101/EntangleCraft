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

public class ItemDevice extends ItemChanneled{
	
	public ItemDevice(int par1) {
		super(par1);
	}
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {	
		EntangleCraft.teleport(par3EntityPlayer,channel);
		return par1ItemStack;
    }
		
}
