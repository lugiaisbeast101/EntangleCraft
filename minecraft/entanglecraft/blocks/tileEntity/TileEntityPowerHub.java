package entanglecraft.blocks.tileEntity;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.Electricity;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IVoltage;
import universalelectricity.prefab.tile.TileEntityConductor;
import universalelectricity.prefab.tile.TileEntityElectricityProducer;
import java.util.EnumSet;

import entanglecraft.DistanceHandler;
import entanglecraft.EntangleCraft;

public class TileEntityPowerHub extends TileEntityElectricityProducer {

	public static final String NAME = "tileEntityPowerHub";
	public int channel;
	
	public static final ForgeDirection INPUT1 = ForgeDirection.EAST;
	public static final ForgeDirection OUTPUT1 = ForgeDirection.WEST;
	public static final EnumSet<ForgeDirection> inputDirections = EnumSet.of(INPUT1);
	public static final EnumSet<ForgeDirection> outputDirections = EnumSet.of(OUTPUT1);

	public TileEntityPowerHub(int channel) {
		this.channel = channel;
		EnumSet<ForgeDirection> directions = EnumSet.copyOf(inputDirections);
		directions.addAll(outputDirections);
		ElectricityConnections.registerConnector(this, directions);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(this, INPUT1);
		ElectricityNetwork outputNetwork = ElectricityNetwork.getNetworkFromTileEntity(this, OUTPUT1);

		// Handling input network
		ElectricityPack inputRequest = EntangleCraft.dhInstance.getElectricityPack(inputNetwork);
		if (inputNetwork != null)
		{
			if (!inputNetwork.isRequesting(this))
			{
				inputNetwork.startRequesting(this, inputRequest);
			}
		
			if (inputNetwork.isRequesting(this))
			{
				EntangleCraft.dhInstance.rewardElectricity(inputNetwork.consumeElectricity(this), channel);
			}
		}
		
		// Handling output network
		if (outputNetwork != null)
		{
			double outputWatts = Math.min(outputNetwork.getRequest().getWatts(), Math.min(DistanceHandler.blockzToWatts(DistanceHandler.getDistance(channel)), 10000));
			ElectricityPack output = new ElectricityPack(outputWatts / this.getVoltage(), this.getVoltage());
	
			if (outputNetwork != inputNetwork) 
			{
				System.out.println("haha");
				if (DistanceHandler.canDeduct(channel, output))
				{
					outputNetwork.startProducing(this, output);
				}
				
				else 
				{
					outputNetwork.stopProducing(this);
				}
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setShort("channel", (short) (this.channel));
	}
	
	@Override 
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		this.channel = nbt.getShort("channel");
	}

	@Override
	public double getVoltage(Object... data) {
		return 120;
	}
	
	
}
