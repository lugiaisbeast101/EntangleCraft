package entanglecraft.blocks;

import java.util.Random;

import entanglecraft.SoundHandling.LambdaSoundHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockGlowTorch extends BlockTorch{

	public BlockGlowTorch(int par1, int par2) {
		super(par1, par2);
	
	}
	
	@Override
	public void onBlockAdded(World par1World, int x,int y,int z){
		if (!par1World.isRemote)
		{
			LambdaSoundHandler.playSound(par1World, new double[] {(double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D} , "poof", 0.6F + par1World.rand.nextFloat()*0.2F, par1World.rand.nextFloat() * 0.3F + 0.8F);
		}
	}
	
	
    /**
     * Tests if the block can remain at its current location and will drop as an item if it is unable to stay. Returns
     * True if it can stay and False if it drops. Args: world, x, y, z
     */
    private boolean dropTorchIfCantStay(World world, int x, int y, int z)
    {
        if (!this.canPlaceBlockAt(world, x, y, z))
        {
            if (world.getBlockId(x, y, z) == this.blockID)
            {
                this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockWithNotify(x, y, z, 0);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

	
    /**
     * Gets if we can place a glow torch on a block.
     */
    private boolean canPlaceTorchOn(World world, int x, int y, int z)
    {
        if (world.doesBlockHaveSolidTopSurface(x, y, z))
        {
            return true;
        }
        else
        {
            int idOfBlock = world.getBlockId(x, y, z);
            return (Block.blocksList[idOfBlock] != null && Block.blocksList[idOfBlock].canPlaceTorchOnTop(world, x, y, z));
        }
    }
	
	public String getTextureFile(){
		return "/lambdaTextures.png";
	}
	
	@Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return true;
    }
		
	@Override
    public void onNeighborBlockChange(World world, int par2, int par3, int par4, int par5)
    {

    }
	   
   /**
    * Returns the quantity of items to drop on block destruction.
    */
   @Override
   public int quantityDropped(Random par1Random)
   {
       return 0;
   }

   /**
    * Returns the ID of the items to drop on destruction.
    */
   @Override
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
