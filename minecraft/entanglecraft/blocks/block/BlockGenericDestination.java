package entanglecraft.blocks.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.block.material.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import entanglecraft.Destination;
import entanglecraft.EntangleCraft;
import entanglecraft.blocks.EntangleCraftBlocks;
import entanglecraft.blocks.tileEntity.TileEntityGenericDestination;
import entanglecraft.gui.GuiGenericDestination;

public class BlockGenericDestination extends BlockContainer{
	
	public int channel = 0;
	public int id;
	
	public BlockGenericDestination(int par1, int channel) {
		super(par1, Material.rock);
		id = par1;
		this.channel = channel;
		
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	public void onBlockAdded(World theWorld, int x, int y, int z){
		super.onBlockAdded(theWorld, x, y, z);

		int dimension = theWorld.provider.dimensionId;

		Destination dest = new Destination(new int[]{x,y,z},this.channel,dimension);
		System.out.println(dest.toString());
		
	}
	
	public void breakBlock(World theWorld, int x,int y, int z, int a, int b){
		EntangleCraft.removeDestination((new Destination(new int[] {x,y,z}, channel, theWorld.provider.dimensionId)));
		
        TileEntityGenericDestination te = (TileEntityGenericDestination)theWorld.getBlockTileEntity(x, y, z);

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
                        	// itemToDrop.getItemStack()...... weird name at the moment
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

        super.breakBlock(theWorld, x, y, z, a, b);
    }


	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer parPlayer, int par6, float par7, float par8, float par9)
    {   	
		TileEntityGenericDestination tileEntityGD = (TileEntityGenericDestination) world
				.getBlockTileEntity(i, j, k);

		
		int dimension = world.provider.dimensionId;
		Destination dest = new Destination(new int[]{i,j,k},this.channel,dimension);
		tileEntityGD.setDest(dest);

    	parPlayer.openGui(EntangleCraft.instance, 0, world, i, j, k);
        return true;
    }
	
    
	public int getBlockTextureFromSide(int i){
		if (i == 1)
		{
			return 9+(channel);
		}
		else if(i == 0)
		{
			return 0;
		}
		else
		{
			return 2+(channel*2);
		}
	}
	
	
	public String getTextureFile(){
		return "/lambdaTextures.png";
	}
	
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return EntangleCraftBlocks.BlockGLD.blockID + channel;
    }
 
   
	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityGenericDestination();
	}
    
    
}


