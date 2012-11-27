package entanglecraft.generation;

import java.util.Random;

import net.minecraft.src.World;
import entanglecraft.blocks.EntangleCraftBlocks;

public class WorldGenLambdaOre {
	private int[] ores = {EntangleCraftBlocks.BlockRedShard.blockID,EntangleCraftBlocks.BlockYelShard.blockID,EntangleCraftBlocks.BlockBluShard.blockID};
	private int oreGenCount = 0;
    public WorldGenLambdaOre()
    {
    }

    public boolean generate(World world, Random random, int i, int j, int k)
    {
    	int blockToGen = WorldGenFunctions.chooseRandomItem(random, ores);
    	
		int i1 = i;
		int j1 = j;
		int k1 = k;

        i1 = i + random.nextInt(256) - random.nextInt(256);
        j1 = j + random.nextInt(16) - random.nextInt(16);
        k1 = k + random.nextInt(256) - random.nextInt(256);
	    
        if (world.blockExists(i1, j1, k1) && world.getBlockId(i1, j1, k1) != EntangleCraftBlocks.BlockFObsidian.blockID){
        	world.setBlockWithNotify(i1, j1, k1,blockToGen);
			oreGenCount += 1;
        }
    		
    	return true;
    }
    
}
