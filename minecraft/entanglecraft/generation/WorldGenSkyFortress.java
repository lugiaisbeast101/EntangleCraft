package entanglecraft.generation;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import entanglecraft.EntangleCraft;
import entanglecraft.blocks.EntangleCraftBlocks;
import entanglecraft.blocks.TileEntityGenericDestination;
import entanglecraft.items.EntangleCraftItems;

public class WorldGenSkyFortress extends WorldGenForerunnerDungeon{
	
	int fObsidian = EntangleCraftBlocks.BlockFObsidian.blockID;
    int destination = EntangleCraftBlocks.BlockGLD.blockID;
    int bluDestination = EntangleCraftBlocks.BlockBLD.blockID;
    int redDestination = EntangleCraftBlocks.BlockRLD.blockID;
    int yelDestination = EntangleCraftBlocks.BlockYLD.blockID;
    int glowBlock = EntangleCraftBlocks.BlockGlowTorch.blockID;
    int obsidian = Block.obsidian.blockID;
    int chest = Block.chest.blockID;
    int[] destinations = {destination, bluDestination, redDestination, yelDestination};
    int size;
	boolean shouldGenerate = true;
	        public void WorldGenSkyForerunner()
	        {
	        }

	        public boolean generate(World world, Random random, int x, int y, int z)
	        {		
	        	if (shouldGenerate){
	        		this.shouldGenerate = false;
		            createSkyFortress(world, random, x,y,z);
			        	
		        	world.setBlockWithNotify(x, y, z, destination);
		            TileEntityGenericDestination te = (TileEntityGenericDestination)world.getBlockTileEntity(x, y, z);
		            te.setInventorySlotContents(0, new ItemStack(EntangleCraftItems.ItemLambdaCore,16));
		            te.setDest(new int[] {x,y,z},te.channel);
		            te.teleportsEarned = 3;
		            EntangleCraft.addDestination(te.destination);
	        	}

                return true;
	        }
	        
	        private void createSkyFortress(World world, Random random, int x, int y, int z){
	        	this.size = 8;
	            int topDestinationBlock = WorldGenFunctions.chooseRandomItem(random,destinations);
	            int botDestinationBlock = WorldGenFunctions.chooseRandomItem(random,destinations);
	            
	            if (botDestinationBlock == topDestinationBlock){
	            boolean notUnique = true;
	            while (notUnique){
	            	int temp = WorldGenFunctions.chooseRandomItem(random, destinations);
	            	if (temp!=topDestinationBlock){
	            		notUnique = false;
	            		botDestinationBlock = temp;
	            	}
	            }
	            }
	            WorldGenFunctions.generateSphere(world, x, y+size + size/2, z, fObsidian, size*2);	          	
	            WorldGenFunctions.generateSphere(world,x,y,z,glowBlock,size);
	          	WorldGenFunctions.generateSphere(world,x,y,z,0,size - 1);
	            world.setBlockWithNotify(x, y, z, topDestinationBlock);
	            TileEntityGenericDestination te = (TileEntityGenericDestination)world.getBlockTileEntity(x, y, z);
	            te.setInventorySlotContents(0, new ItemStack(EntangleCraftItems.ItemLambdaCore,16));
	            te.setDest(new int[] {x,y,z},te.channel);
	            te.teleportsEarned = 1;
	            EntangleCraft.addDestination(te.destination);
	            WorldGenFunctions.generateHalfSphere(world, x, 62, z,0,size);
	            world.setBlockWithNotify(x, 62 - (size/2), z,glowBlock);
	            world.setBlockWithNotify(x, 62-size, z, Block.chest.blockID);
	            TileEntityChest teChest = (TileEntityChest)world.getBlockTileEntity(x,62-size,z);
	            teChest = WorldGenFunctions.skyFortressLoot(0,teChest);
	            WorldGenFunctions.placeWall(world,x,62,z,0,size);
	            world.setBlockWithNotify(x, 62, z, botDestinationBlock);
	            placeStructurePortion(world,x,y,z,new int[] {1,0,0});
	            placeStructurePortion(world,x,y,z,new int[] {-1,0,0});
	            placeStructurePortion(world,x,y,z,new int[] {0,0,1});
	            placeStructurePortion(world,x,y,z,new int[] {0,0,-1});
	            
	         
	            System.out.println("SkyFortress created at " + x + " , " + y + " , " + z);
	        }
	        
	   
	        
	        private void placeStructurePortion(World world, int x, int y, int z, int[] direction){
	        	Random rand = world.rand;
	        	int[] baseCoords = {x,y,z};
	        	int[] leftOf = {0,0,0};
	        	int[] rightOf = {0,0,0};
	        	leftOf[WorldGenFunctions.getLeftAndRight(direction)] = 1;
	        	rightOf[WorldGenFunctions.getLeftAndRight(direction)] = -1;      	
	        	
	        	
	        	 for (int i = 1; i <= size; i++){
                
	                 WorldGenFunctions.placeWall(world,x+(direction[0]*i),62,z+(direction[2]*i),0,(size));
	                 world.setBlockWithNotify(x+(direction[0]*i),62, z+(direction[2]*i), fObsidian);
	                 int numOfHoles = size - i;
	                 
	                 int n = 1;
	                 int d;
	                 while (n <= numOfHoles){
	                	 d = n;
	                	 int holeSize = 64;
	                        		 
	                	 if (n==1) {
	                		 WorldGenFunctions.placeWall(world,x+(direction[0]*i)+(rightOf[0]*d),63+(rightOf[1]*d),z+(direction[2]*i)+(rightOf[2]*d),0,holeSize);
	                		 WorldGenFunctions.placeWall(world,x+(direction[0]*i)+(rightOf[0]*d),63+(rightOf[1]*d),z+(direction[2]*i)+(rightOf[2]*d),0,holeSize);
	                		
	                	 }
	                	 d = n+1;
	                	 if (d < (size/2)+1){
	                		 WorldGenFunctions.placeWall(world,x+(direction[0]*i)+(rightOf[0]*d),63+(rightOf[1]*d),z+(direction[2]*i)+(rightOf[2]*d),0,holeSize);
	                		 WorldGenFunctions.placeWall(world,x+(direction[0]*i)+(rightOf[0]*d),63+(rightOf[1]*d),z+(direction[2]*i)+(rightOf[2]*d),0,holeSize);
	                	 }
	                     n+=1;
	                 };
	              	 
	              	 if (numOfHoles == 1) {
	              		 world.setBlockWithNotify(x+(direction[0]*i),62, z+(direction[2]*i), fObsidian);
	              	 }
	         
	                 }
	        }

	}
