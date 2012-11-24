package entanglecraft.blocks;

import java.util.Random;

import net.minecraft.src.BlockStone;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.World;
import entanglecraft.EntangleCraft;

public class BlockFObsidian extends BlockStone{

	public BlockFObsidian(int par1,int par2) {
		super(par1,par2);
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
