package net.minecraft.src.lambdaMod.generation;

import java.util.Random;

import net.minecraft.src.World;
import net.minecraft.src.lambdaMod.blocks.EntangleCraftBlocks;

public class WorldGenLambdaOre {
	private int[] ores = {EntangleCraftBlocks.BlockRedShard.blockID,EntangleCraftBlocks.BlockYelShard.blockID,EntangleCraftBlocks.BlockBluShard.blockID};
    public WorldGenLambdaOre()
    {
    }

    public boolean generate(World world, Random random, int i, int j, int k)
    {
    	int blockToGen = WorldGenFunctions.chooseRandomItem(random, ores);
    	if (true)
    	{
    		int i1 = i;
    		int j1 = j;
    		int k1 = k;
    		//boolean isBlock = false;
    		//while (!isBlock){
		        i1 = i + random.nextInt(8) - random.nextInt(8);
		        j1 = j + random.nextInt(8) - random.nextInt(8);
		        k1 = k + random.nextInt(8) - random.nextInt(8);
		       // isBlock = (world.blockExists(i1, j1, k1));
    		//}
    		world.setBlockWithNotify(i1, j1, k1,blockToGen);
    		System.out.println("Ore generated at " + i1 + " " + j1 + " " + k1);
    	} 
    	return true;
    }
    
}
