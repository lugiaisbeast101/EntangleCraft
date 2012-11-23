package net.minecraft.src.lambdaMod.blocks;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockStairs;
import net.minecraft.src.BlockTorch;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class BlockGlowTorch extends BlockTorch{

	public BlockGlowTorch(int par1, int par2) {
		super(par1, par2);
	
	}
	
	@Override
	public void onBlockAdded(World par1World, int x,int y,int z){
		par1World.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "poof", 0.6F + par1World.rand.nextFloat()*0.2F, par1World.rand.nextFloat() * 0.3F + 0.8F);
	}
	
	public String getTextureFile(){
		return "/lambdaTextures.png";
	}
	
	@Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return true;
    }
	
	private boolean canPlaceTorchOn(World par1World, int par2, int par3, int par4)
    {
        return true;
    }
	
	
	
	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {

    }

   private boolean dropTorchIfCantStay(World par1World, int par2, int par3, int par4)
   {
     return false;
   }
   
   /**
    * Returns the quantity of items to drop on block destruction.
    */
   public int quantityDropped(Random par1Random)
   {
       return 0;
   }

   /**
    * Returns the ID of the items to drop on destruction.
    */
   public int idDropped(int par1, Random par2Random, int par3)
   {
       return 0;
   }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
   @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int var6 = par1World.getBlockMetadata(par2, par3, par4);
        double var7 = (double)((float)par2 + 0.5F);
        double var9 = (double)((float)par3 + 0.5F);
        double var11 = (double)((float)par4 + 0.5F);
        double var13 = 0.2199999988079071D;
        double var15 = 0.27000001072883606D;
        
        String particle1 = "instantSpell";
        String particle = "magicCrit";

        if (var6 == 1)
        {
            par1World.spawnParticle(particle1, var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle(particle, var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
        }
        else if (var6 == 2)
        {
            par1World.spawnParticle(particle1, var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle(particle, var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
        }
        else if (var6 == 3)
        {
            par1World.spawnParticle(particle1, var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle(particle, var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
        }
        else if (var6 == 4)
        {
            par1World.spawnParticle(particle1, var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle(particle, var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
        }
        else
        {
            par1World.spawnParticle(particle1, var7, var9, var11, 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle(particle, var7, var9, var11, 0.0D, 0.0D, 0.0D);
        }
    }
}
