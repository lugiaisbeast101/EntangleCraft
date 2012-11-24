package net.minecraft.entanglecraft;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import net.minecraft.src.Chunk;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class DistanceHandler{
	private static Integer[] distances = {0,0,0,0};

	public static Integer getDistance(int index){
		return distances[index];
	}
	
	public static void addToDistance(int index, double amount){
		distances[index] += (int)amount;
		onDistanceChanged(index);
	}
	
	public static void setDistance(int index, double amount){
		distances[index] = (int)amount;
		onDistanceChanged(index);
	}
	
	public static String getStringDistance(int index){
		Integer distance = distances[index];
		Float distanceKbz = (float)distance;
		String postFix = "";
		if (distance > 100){
			distanceKbz = (distance/1024F);
			postFix = " Kbz";
		}
		if (distance > 1048576){
			distanceKbz = (distance/1048576F);
			postFix = " Mbz";
		}
		DecimalFormat dec = new DecimalFormat("###.#");
		return dec.format(distanceKbz) + postFix;
	}
	
	public static void subtractDistance(int index, double amount){
		if (distances[index] >= (int)amount){
			distances[index] -= (int)amount;
			onDistanceChanged(index);
		}
	}

	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event) {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTSaver ds = new NBTSaver(event.world, "LambdaMod");
		nbt = ds.loadData();
		
		try {
		Integer counter = 0;
		for (Object channel : distances){
			distances[counter] = nbt.getInteger(counter.toString());
			counter ++;
		}
		}
		catch (Exception e){
		}
		
	}
	
	private static void onDistanceChanged(int index){
		if (EntangleCraft.proxy.isServer){
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	        DataOutputStream DOS = new DataOutputStream(bytes);
	        try {
	        	DOS.writeInt(3); // 3 for distance update packet
				DOS.writeInt(index);
				DOS.writeInt(distances[index]);
	
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	        ServerPacketHandler.sendAPacket(new Packet250CustomPayload(), bytes);
		}
	}

	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event) {
			NBTSaver ds = new NBTSaver(event.world, "LambdaMod");
			NBTTagCompound nbt = new NBTTagCompound();
			Integer counter = 0;
			for (Object channel : distances){
				nbt.setInteger(counter.toString(), (Integer)channel);
				counter ++;
			}
			ds.saveData(nbt);
	}
}
	
