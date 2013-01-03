package entanglecraft.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.src.ModLoader;

import org.lwjgl.input.Keyboard;
import net.minecraft.world.World;
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
