package entanglecraft.generation;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;

import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import entanglecraft.EntangleCraft;
import entanglecraft.blocks.EntangleCraftBlocks;
import entanglecraft.items.EntangleCraftItems;

public class WorldGenFunctions implements IWorldGenerator{
	private WorldGenSkyFortress skyFortressMaker = new WorldGenSkyFortress();
	private WorldGenForerunnerDungeon dungeonMaker = new WorldGenForerunnerDungeon();
	private static boolean shouldGenerateSkyFortress = true;
	
	public static boolean worldGenerationEnabled = true;
	public static boolean oreGenerationEnabled = true;
	public static boolean structureGenerationEnabled = false;
	
    public static int getLeftAndRight(int[] direction){
    	if (direction[0] == 0){
    		return 0;
    	}
    	else return 2;
    }
    
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		
		int x = chunkX + rand.nextInt(16); int y = 192; int z = chunkZ + rand.nextInt(16); 
		
		if (worldGenerationEnabled)
		{
			
			if (structureGenerationEnabled)
			{
				//skyFortressMaker.generate(world, rand, x, y, z); 
				//dungeonMaker.generate(world, rand, x, 32, z);
			}
				
			if (oreGenerationEnabled)
			{
				new WorldGenLambdaOre().generate(world, rand, chunkX, 32, chunkZ); 
			}
		
		}
	}
    
    public static void placeFloorBlock(World world, int x, int y, int z, int block,int roofBlock,int roofHeight){
    	world.setBlockWithNotify(x,y,z,block);
    	placeWall(world, x, y, z, 0, roofHeight+1);
    	world.setBlockWithNotify(x, y+roofHeight+1, z, roofBlock);
    }
    
    public static void placeWall(World world, int x, int y, int z, int block, int wallHeight){
    	for (int i = 1 ; i <= wallHeight ; i++){
    		world.setBlockWithNotify(x, y + i, z, block);
    	}
    }
    
    public static void placeHole(World world, int x, int y, int z, int holeDepth){
    	placeWall(world,x,y-holeDepth-1,z,0,holeDepth);
    }
    
    public static void generateSphere(World world, int posX,int posY,int posZ,int blockID,int r){
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posY); 
        int k = MathHelper.floor_double(posZ);
        for(int x = -r; x < r; x++){
                for(int y = -r; y < r; y++){ 
                        for(int z = -r; z < r; z++){                                    
                                double dist = MathHelper.sqrt_double((x*x + y*y + z*z)); //Calculates the distance
                                if(dist >= r)
                                        continue;
                                world.setBlockWithNotify(i+x, j+y, k+z, blockID);
                        }
                }
        }
        }
        
        public static void generateHalfSphere(World world, int posX,int posY,int posZ,int blockID,int r){
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posY); 
        int k = MathHelper.floor_double(posZ);
        for(int x = -r; x < r; x++){
                for(int y = -r; y <= 0; y++){ 
                        for(int z = -r; z < r; z++){                                    
                                double dist = MathHelper.sqrt_double((x*x + y*y + z*z)); //Calculates the distance
                                if(dist >= r)
                                        continue;
                                world.setBlockWithNotify(i+x, j+y, k+z, blockID);
                        }
                }
        }
        }
    
    public static int chooseRandomItem(Random random,int[] items){
    	int selector = random.nextInt();
    	if (selector < 0) selector = selector *-1;
    	return items[selector%items.length];
    }
    
    public static TileEntityChest skyFortressLoot(int version, TileEntityChest teChest){
    	if (version == 0){
            teChest.setInventorySlotContents(0+teChest.worldObj.rand.nextInt(4), new ItemStack(EntangleCraftBlocks.BlockFObsidian,teChest.worldObj.rand.nextInt(8)+2 * (teChest.worldObj.rand.nextBoolean() ? 1 : -1)));
            teChest.setInventorySlotContents(9+teChest.worldObj.rand.nextInt(4), new ItemStack(EntangleCraftItems.ItemFrShard,1));
            teChest.setInventorySlotContents(14+teChest.worldObj.rand.nextInt(4), new ItemStack(EntangleCraftItems.ItemNethermonicDiamond,teChest.worldObj.rand.nextInt(6)+4 * (teChest.worldObj.rand.nextBoolean() ? 1 : -1)));
            teChest.setInventorySlotContents(18+teChest.worldObj.rand.nextInt(4), new ItemStack(EntangleCraftItems.ItemYelShard,teChest.worldObj.rand.nextInt(2)+2 * (teChest.worldObj.rand.nextBoolean() ? 1 : -1)));
            teChest.setInventorySlotContents(22+teChest.worldObj.rand.nextInt(4), new ItemStack(EntangleCraftItems.ItemRedShard,teChest.worldObj.rand.nextInt(2)+2 * (teChest.worldObj.rand.nextBoolean() ? 1 : -1)));
            teChest.setInventorySlotContents(26+teChest.worldObj.rand.nextInt(4), new ItemStack(EntangleCraftItems.ItemBlueShard,teChest.worldObj.rand.nextInt(2)+2 * (teChest.worldObj.rand.nextBoolean() ? 1 : -1)));
    	}
    	
    	return teChest;
    }
}
