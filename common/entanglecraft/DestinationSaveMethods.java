package entanglecraft;

import net.minecraft.src.NBTTagCompound;

public class DestinationSaveMethods {

	public static void writeDestToNBT(NBTTagCompound nbt, Destination dest){
		if (dest != null){
		int[] blockCoords = dest.blockCoords;
		double[] destinationCoords = dest.destinationCoords;
		int channel = dest.channel;
		
		int[] intDestinationCoords = doubleToIntArray(destinationCoords);
		
		nbt.setIntArray("blockCoords",blockCoords);
		nbt.setIntArray("destinationCoords",intDestinationCoords);
		nbt.setInteger("channel",channel);
		}
	}
	
	public static Destination readDestFromNBT(NBTTagCompound nbt){
		double[] destCoords;
		int[] blockCoords;
		int channel;
		
		int[] intDestCoords = (nbt.getIntArray("destinationCoords"));
		destCoords = intToDoubleArray(intDestCoords);
		blockCoords = (nbt.getIntArray("blockCoords"));
		channel = (nbt.getInteger("channel"));
		return new Destination(destCoords,blockCoords,channel);
	}
		
	public static double[] intToDoubleArray(int[] array){
		int counter = 0;
		double[] pointsToAdd = new double[3];
		int[] intArray = (int[])array;
		for (int point : intArray){
			double thePoint = point;
			thePoint = thePoint/100;
			pointsToAdd[counter] = thePoint;
			counter += 1;
		}
		return pointsToAdd;
	}
	
	public static int[] doubleToIntArray(double[] array){
		// Values are multiplied by 100 and are not equal to original values!
		// Use intToDouble() to get them back.
		int counter = 0;
		int[] intPoints = new int[array.length];
		for (double point : array){
			point = point*100;
			intPoints[counter] = (int)point;
			counter += 1;
		}
		return intPoints;
	}
}
