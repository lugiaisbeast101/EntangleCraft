package net.minecraft.src.lambdaMod.blocks;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import net.minecraft.src.lambdaMod.items.EntangleCraftItems;

public class BlockLambdaOre extends Block{
	private int type = 0;
	public BlockLambdaOre(int par1, int type) {
		super(par1, Material.rock);
		this.type = type;
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	
	public int getBlockTextureFromSide(int i){
		return 64 + type;
	}
	
	public String getTextureFile(){
		return "/lambdaTextures.png";
	}
	
	@Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return new ItemStack(EntangleCraftItems.ItemYelShard).itemID -type;
    }
	
	public void breakBlock(World theWorld, int x,int y, int z, int par5, int par6){
		int amountOfXP = 0;
		amountOfXP = MathHelper.getRandomIntegerInRange(theWorld.rand, 5, 8);
		this.dropXpOnBlockBreak(theWorld, x, y, z, amountOfXP*3);
	}
	
}
