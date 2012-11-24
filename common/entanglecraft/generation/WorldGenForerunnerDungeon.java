package entanglecraft.generation;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;
import entanglecraft.EntangleCraft;
import entanglecraft.blocks.EntangleCraftBlocks;

public class WorldGenForerunnerDungeon extends WorldGenerator
{
	int fObsidian = EntangleCraftBlocks.BlockFObsidian.blockID;
    int destination = EntangleCraftBlocks.BlockGenericDestination.blockID;
    int bluDestination = EntangleCraftBlocks.BlockBLD.blockID;
    int redDestination = EntangleCraftBlocks.BlockRLD.blockID;
    int yelDestination = EntangleCraftBlocks.BlockYLD.blockID;
    int obsidian = Block.obsidian.blockID;
    int chest = Block.chest.blockID;
    int[] destinations = {destination, bluDestination, redDestination, yelDestination};
    int size;
    boolean haveGeneratedForerunnerDungeon = true;
    
        public WorldGenForerunnerDungeon()
        {
        }

        public boolean generate(World world, Random random, int x, int y, int z)
        {		
                createForerunnerDungeon(world, random, x,y,z);
                
                return true;
        }
        
        private void createForerunnerDungeon(World world, Random random, int x, int y, int z){
        	this.size = 16;
            this.haveGeneratedForerunnerDungeon = true;
            WorldGenFunctions.placeFloorBlock(world,x, y, z, WorldGenFunctions.chooseRandomItem(random,destinations),obsidian,size);
            WorldGenFunctions.placeHole(world,x,y,z,16);
            placeStructurePortion(world,x,y,z,new int[] {1,0,0});
            placeStructurePortion(world,x,y,z,new int[] {-1,0,0});
            placeStructurePortion(world,x,y,z,new int[] {0,0,1});
            placeStructurePortion(world,x,y,z,new int[] {0,0,-1});
         
            System.out.println("Forerunner dungeon created at " + x + " , " + y + " , " + z);
        }
        
        private void placeStructurePortion(World world, int x, int y, int z, int[] direction){
        	int[] baseCoords = {x,y,z};
        	int[] leftOf = {0,0,0};
        	int[] rightOf = {0,0,0};
        	leftOf[WorldGenFunctions.getLeftAndRight(direction)] = 1;
        	rightOf[WorldGenFunctions.getLeftAndRight(direction)] = -1;      	
        	
        	
        	 for (int i = 1; i <= size; i++){
        		 WorldGenFunctions.placeFloorBlock(world,x+(direction[0]*i), y+(direction[1]*i), z+(direction[2]*i), fObsidian, obsidian, size);
        		 WorldGenFunctions.placeHole(world,x+(direction[0]*i),y+(direction[1]*i),z+(direction[2]*i),size);
                 int numOfHoles = size - i;
                 
                 int n = 1;
                 int d;
                 while (n <= numOfHoles){
                	 d = n;
                	 if (n==1) {
                		 WorldGenFunctions.placeHole(world,x+(direction[0]*i)+(leftOf[0]*d),y+(direction[1]*i)+(leftOf[1]*d),z+(direction[2]*i)+(leftOf[2]*d),y);
                		 WorldGenFunctions.placeHole(world,x+(direction[0]*i)+(rightOf[0]*d),y+(direction[1]*i)+(rightOf[1]*d),z+(direction[2]*i)+(rightOf[2]*d),y);
                		 WorldGenFunctions.placeFloorBlock(world,x+(direction[0]*i)+(leftOf[0]*d),y+(direction[1]*i)+(leftOf[1]*d),z+(direction[2]*i)+(leftOf[2]*d),0,obsidian,size);
                		 WorldGenFunctions.placeFloorBlock(world,x+(direction[0]*i)+(rightOf[0]*d),y+(direction[1]*i)+(rightOf[1]*d),z+(direction[2]*i)+(rightOf[2]*d),0,obsidian,size);
                	 }
                	 d = n+1;
                	 if (d < (size/2)+1){
                		 WorldGenFunctions.placeHole(world,x+(direction[0]*i)+(leftOf[0]*d),y+(direction[1]*i)+(leftOf[1]*d),z+(direction[2]*i)+(leftOf[2]*d),y);
                		 WorldGenFunctions.placeHole(world,x+(direction[0]*i)+(rightOf[0]*d),y+(direction[1]*i)+(rightOf[1]*d),z+(direction[2]*i)+(rightOf[2]*d),y);
                		 WorldGenFunctions.placeFloorBlock(world,x+(direction[0]*i)+(leftOf[0]*d),y+(direction[1]*i)+(leftOf[1]*d),z+(direction[2]*i)+(leftOf[2]*d),0,obsidian,size);
                		 WorldGenFunctions.placeFloorBlock(world,x+(direction[0]*i)+(rightOf[0]*d),y+(direction[1]*i)+(rightOf[1]*d),z+(direction[2]*i)+(rightOf[2]*d),0,obsidian,size);
                	 }
                     n+=1;
                 }
                 d = n+1;
                 WorldGenFunctions.placeWall(world,x+(direction[0]*i)+(leftOf[0]*d),y+(direction[1]*i)+(leftOf[1]*d),z+(direction[2]*i)+(leftOf[2]*d),fObsidian,size);
                 WorldGenFunctions.placeWall(world,x+(direction[0]*i)+(rightOf[0]*d),y+(direction[1]*i)+(rightOf[1]*d),z+(direction[2]*i)+(rightOf[2]*d),fObsidian,size);
              	 
              	 if (numOfHoles == 0) {
              		WorldGenFunctions.placeWall(world,x+(direction[0]*i), y+(direction[1]*i), z+(direction[2]*i), fObsidian, size); 
              	 }
                 	
                 }
        }
        


}