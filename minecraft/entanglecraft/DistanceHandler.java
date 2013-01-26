package entanglecraft;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import universalelectricity.core.electricity.Electricity;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class DistanceHandler {
	private static double[] distances = { 0.0, 0.0, 0.0, 0.0 };
	
	public static int[] dungeonCoords;
	public static int[] skyFortressA;
	public static int[] skyFortressB;
	public static int[] skyFortressC;
	
	// The amount of UE joules to EC blockz
	public static double EC_RATIO = 20;
	public static double EC_TO_UE = 1/EC_RATIO;
	
	public int oreGenCount = 0;
	
	public static Double getDistance(int index) {
		return distances[index];
	}

	public static void addToDistance(int index, double amount) {
		distances[index] = distances[index] + amount;
		onDistanceChanged(index);
	}

	public static void setDistance(int index, double amount, boolean shouldNotify) {
		distances[index] = amount;
		if (shouldNotify) onDistanceChanged(index);
	}

	public static String getStringDistance(int index) {
		double distance = distances[index];
		String postFix = "";
		if (distance > 255.0) {
			distance = (distance / 1024.0);
			postFix = " Kbz";
		}
		if (distance > 1048576) {
			distance = (distance / 1048576.0);
			postFix = " Mbz";
		}
		DecimalFormat dec = new DecimalFormat("###.#");
		return dec.format(distance) + postFix;
	}

	public static void subtractDistance(int index, double amount) {
		if (distances[index] >= amount) {
			distances[index] -= amount;
			onDistanceChanged(index);
		}
	}
	
	/* Below are a few methods to help with the Universal Electricity API */
	
	public ElectricityPack getElectricityPack(ElectricityNetwork network) {
		ElectricityPack electricityPack = new ElectricityPack(83.3333333, 120);
		return electricityPack;
	}
	
	public static boolean canDeduct(int channel, double watts) {
		System.out.println(getDistance(channel) > wattsToBlockz(watts) ? wattsToBlockz(watts) + " is ezpz" : "CAN'T DO IT");
		return getDistance(channel) > wattsToBlockz(watts);
	}
 	
	public static boolean canDeduct(int channel, ElectricityPack electricity) {
		return canDeduct(channel, electricity.getWatts());
	}
	
	public static void deductElectricity(ElectricityPack electricity, int channel) {
		double watts = electricity.getWatts();
		subtractDistance(channel, wattsToBlockz(watts));
		System.out.println("Deducted " + wattsToBlockz(watts) + " blockz");
	}
	
	public static void rewardElectricity(ElectricityPack electricity, int channel) {
		double watts = electricity.getWatts();
		addToDistance(channel, wattsToBlockz(watts));
		System.out.println("Rewarded " + wattsToBlockz(watts) + " blockz");
	}
	
	public static double wattsToBlockz(double watts) {
		return (EC_RATIO * (watts/1000D));
	}
	
	public static double blockzToWatts(double blockz) {
		return blockz * 1/EC_RATIO * 1000.0;
	}
	
	public static boolean shouldProduce(ElectricityNetwork network, TileEntity tileEntity) {
		boolean shouldProduce = false;
		
		List<TileEntity> receivers = network.getReceivers();
		shouldProduce = receivers != null;
		if (shouldProduce)
		{
			boolean otherReceiver = false;
			for (TileEntity receiver : receivers)
			{
				otherReceiver = otherReceiver || (receiver != tileEntity);
			}
			shouldProduce = otherReceiver;
		}
		return shouldProduce;
	}
	
	public static boolean shouldReceive(ElectricityNetwork network, TileEntity tileEntity) {
		boolean shouldReceive = false;
		
		HashMap<TileEntity, ElectricityPack> producers = network.getProducers();
		Set<TileEntity> set = producers.keySet();
		
		shouldReceive = set != null;
		if (shouldReceive)
		{
			boolean otherProducer = false;
			for (TileEntity producer : set)
			{
				otherProducer = otherProducer || (producer != tileEntity);
			}
			shouldReceive = otherProducer;
		}
		
		return shouldReceive;
	}
	
	public static double calculate3dDistance(double[] a, double[] b) {
		double x = a[0];
		double y = a[1];
		double z = a[2];
		double x0 = b[0];
		double y0 = b[1];
		double z0 = b[2];
		return Math.sqrt((x - x0) * (x - x0) + (y - y0) * (y - y0) + (z - z0)
				* (z - z0));
	}
	
	public static double calculateXZDistance(double[] a, double[] b)
	{
		double x = a[0];
		double z = a[1];
		
		double x0 = b[0];
		double z0 = b[1];
		return Math.sqrt((x - x0) * (x - x0) + (z - z0) * (z - z0));
	}

	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event) {
		if (!event.world.isRemote)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			NBTSaver ds = new NBTSaver(event.world, "LambdaMod");
			nbt = ds.loadData();
	
			try {
				Integer counter = 0;
				for (Object channel : distances) {
					double distance = nbt.getDouble("channel" + counter);
					setDistance(counter,distance, true);
					counter++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void onDistanceChanged(int index) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream DOS = new DataOutputStream(bytes);
		try {
			DOS.writeInt(3); // 3 for distance update packet
			DOS.writeInt(index);
			DOS.writeDouble(distances[index]);

		} catch (IOException e) {
			e.printStackTrace();
		}

		ServerPacketHandler.sendAPacket(new Packet250CustomPayload(), bytes);
	}

	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event) {
		
		if (!event.world.isRemote)
		{
			NBTSaver ds = new NBTSaver(event.world, "LambdaMod");
			NBTTagCompound nbt = new NBTTagCompound();
			Integer counter = 0;
			for (Object channelElectricity : distances) {
				nbt.setDouble("channel" + counter, (Double) channelElectricity);
				//System.out.println("Saved " + (Integer)channel + " to nbt");
				counter++;
			}
			ds.saveData(nbt);
		}
	}
}
