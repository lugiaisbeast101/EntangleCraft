package entanglecraft.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import entanglecraft.EntangleCraft;
import entanglecraft.ServerPacketHandler;

public class BlockLambdaMiner extends BlockContainer{
	public int channel;
	private boolean isActive;
	
	public BlockLambdaMiner(int blockID, int channel) {
		super(blockID, Material.rock);
		
		// Initializing fields
		this.channel = channel;
		this.isActive = false;
		
		String[] channelToColor = {"G", "R", "Y", "B"};
		
		// Calling methods
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setHardness(10.0F);
		this.setResistance(20.0F);
		this.setBlockName("Block" + channelToColor[channel] + "LambdaMiner");
	}
	
	@Override
	public boolean isOpaqueCube(){
		return true;
	}
	
	public void onBlockAdded(World theWorld, int x, int y, int z){
		super.onBlockAdded(theWorld, x, y, z);
		System.out.println("adding a miner at y = " + y);
		
	     TileEntityLambdaMiner te = (TileEntityLambdaMiner)theWorld.getBlockTileEntity(x, y, z);
	     te.setBlockCoords(new int[] {x,y,z});
	}
	
	public static void updateBlockState(boolean isActive, World world, int x, int y, int z) {
		
		System.out.println("Updating block state to " + (isActive ? "active" : "not active\n"));
		
		
		int currentMetadata = world.getBlockMetadata(x, y, z);
		int newMetadata = (((currentMetadata-2)%4)+2);
		newMetadata = isActive ? newMetadata + 4 : newMetadata;
		
		TileEntityLambdaMiner teLM = (TileEntityLambdaMiner)world.getBlockTileEntity(x, y, z);
		
	      if (teLM != null)
	        {
	    	  if (!world.isRemote)
	    	  {
	            world.setBlockMetadataWithNotify(x, y, z, newMetadata);
	            teLM.validate();
	            world.setBlockTileEntity(x, y, z, teLM);
	            world.markBlockForUpdate(x, y, z); 
	            
	    	  }
	        }
	      else
	      {
	    	  world.setBlockAndMetadataWithNotify(x, y, z, world.getBlockId(x, y, z), newMetadata);
	      }	     
	}
	
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving)
    {
        int facing = MathHelper.floor_double((double)(entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (facing == 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, 2);
            System.out.println("metadata of 2");
        }

        if (facing == 1)
        {
            world.setBlockMetadataWithNotify(x, y, z, 5);
            System.out.println("metadata of 5");
        }

        if (facing == 2)
        {
            world.setBlockMetadataWithNotify(x, y, z, 3);
            System.out.println("metadata of 3");
        }

        if (facing == 3)
        {
            world.setBlockMetadataWithNotify(x, y, z, 4);
            System.out.println("metadata of 4");
        }
        
        TileEntityLambdaMiner te = (TileEntityLambdaMiner)world.getBlockTileEntity(x, y, z);
        te.setBlockCoords(new int[] {x,y,z});
        te.generateLayerStructure();
    }
    
	
	@Override
	public void breakBlock(World theWorld, int x,int y, int z, int par5, int par6){
        TileEntityLambdaMiner te = (TileEntityLambdaMiner)theWorld.getBlockTileEntity(x, y, z);
        if (te != null)
        {
            for (int i = 0; i < te.getSizeInventory(); ++i)
            {
                ItemStack itemStack = te.getStackInSlot(i);

                if (itemStack != null)
                {
                    float randX = theWorld.rand.nextFloat() * 0.8F + 0.1F;
                    float randY = theWorld.rand.nextFloat() * 0.8F + 0.1F;
                    float randZ = theWorld.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemStack.stackSize > 0)
                    {
                        int anInt = theWorld.rand.nextInt(21) + 10;

                        if (anInt > itemStack.stackSize)
                        {
                            anInt = itemStack.stackSize;
                        }

                        itemStack.stackSize -= anInt;
                        EntityItem itemToDrop = new EntityItem(theWorld, (double)((float)x + randX), (double)((float)y + randY), (double)((float)z + randZ), new ItemStack(itemStack.itemID, anInt, itemStack.getItemDamage()));

                        if (itemStack.hasTagCompound())
                        {
                        	// itemToDrop.getItemStack().setTagCompound..... (weird name at the moment)
                            itemToDrop.func_92014_d().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
                        }

                        float var13 = 0.05F;
                        itemToDrop.motionX = (double)((float)theWorld.rand.nextGaussian() * var13);
                        itemToDrop.motionY = (double)((float)theWorld.rand.nextGaussian() * var13 + 0.2F);
                        itemToDrop.motionZ = (double)((float)theWorld.rand.nextGaussian() * var13);
                        theWorld.spawnEntityInWorld(itemToDrop);
                    }
                }
            }

        }
        super.breakBlock(theWorld, x, y, z,par5,par6);
    }
	
    public boolean onBlockActivated(World par1World, int i, int j, int k, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
    	TileEntityLambdaMiner tileEntitylM = (TileEntityLambdaMiner) par1World
				.getBlockTileEntity(i, j, k);

    	par5EntityPlayer.openGui(EntangleCraft.instance, 1, par1World, i, j, k);
    	return true;
    }
    	
	public int getBlockTextureFromSideAndMetadata(int i, int metaData){
		
		boolean isActive = metaData > 5;
		int relativeMetadata = ((metaData-2)%4)+2;
		
		int baseIndex = 17;
		baseIndex = isActive ? baseIndex + 3 : baseIndex;
		if(i == 0)
		{
			return 16;
		}
		
		if (i == 1)
		{
			return (baseIndex)+(6*channel);
		}
		
		if(i == relativeMetadata)
		{
			return (baseIndex+1)+(6*channel);
		}
		
		else return (baseIndex + 2)+(6*channel);
		
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id){
		super.onNeighborBlockChange(world,x,y,z,id);
		System.out.println("Lambda Miner: neighbour block changed");
		TileEntity te = world.getBlockTileEntity(x, y, z);
		TileEntityLambdaMiner teLM = (TileEntityLambdaMiner)te;
		
		if(teLM.invController.getChest() == null)
		{
			if(id == Block.chest.blockID)
			{
				teLM.invController.checkForChest();
	
			}
		}
			

	}
	
	public String getTextureFile(){
		return "/lambdaTextures.png";
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityLambdaMiner(channel);
	}
	
}
