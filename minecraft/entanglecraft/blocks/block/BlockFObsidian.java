package entanglecraft.blocks.block;

import java.util.Random;

import net.minecraft.block.BlockStone;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;
import entanglecraft.EntangleCraft;
import entanglecraft.blocks.EntangleCraftBlocks;

public class BlockFObsidian extends BlockStone{

	public BlockFObsidian(int par1, int textureIndex) {
		super(par1,textureIndex);
		setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	public String getTextureFile()
    {
            return "/lambdaTextures.png";
    }
		
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return EntangleCraftBlocks.BlockFObsidian.blockID;
    }

}
