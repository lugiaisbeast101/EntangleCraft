package entanglecraft.blocks;

import java.util.Random;

import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockLitWaterStill extends BlockStationary{

	public BlockLitWaterStill(int par1) {
		super(par1, Material.water);
	}
	

   @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
	   super.randomDisplayTick(par1World, par2, par3, par4, par5Random);
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
