package entanglecraft.blocks.block;

import entanglecraft.EntangleCraft;
import entanglecraft.blocks.tileEntity.TileEntityLambdaMiner;
import entanglecraft.blocks.tileEntity.TileEntityPowerHub;
import entanglecraft.gui.EnumGui;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.Electricity;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.implement.IConductor;
import universalelectricity.prefab.BlockMachine;

public class BlockPowerHub extends BlockMachine {
	private int channel;
	
	public BlockPowerHub(String name, int id, int channel, CreativeTabs creativeTab) {
		super(name, id, UniversalElectricity.machine, creativeTab);
		this.channel = channel;
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
	}
	
	@Override
	public boolean onMachineActivated(World world, int i, int j, int k, EntityPlayer thePlayer, int side, float hitX, float hitY, float hitZ)
	{
		thePlayer.openGui(EntangleCraft.instance, EnumGui.PowerHub.getIndex(), world, i, j, k);
        return true;
	}
	
	public int getBlockTextureFromSide(int i){
		if (i == 1)
		{
			return 41+(channel);
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
	
	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed
	 * (coordinates passed are their own) Args: x, y, z, neighbor blockID
	 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
	}
	
	public String getTextureFile(){
		return "/lambdaTextures.png";
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityPowerHub(channel);
	}

	
}
