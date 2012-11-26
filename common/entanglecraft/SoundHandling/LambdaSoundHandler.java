package entanglecraft.SoundHandling;

import java.io.File;

import entanglecraft.ServerPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.SoundManager;
import net.minecraft.src.SoundPoolEntry;
import net.minecraft.src.World;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class LambdaSoundHandler{

	@ForgeSubscribe
    public void onSound(SoundLoadEvent event){
		SoundManager soundManager = event.manager;
		String[] soundFiles = {
			"teleport.wav",	
			"beep.wav",	
			"superBeep.wav",
			"mineProcess.wav",
			"minerFail.wav",
			"poof.wav",
			"poof1.wav",
			"poof2.wav",
			"poof3.wav",
			"poof4.wav",
			"poof5.wav",
			"icePoof.wav",
			"tpScroll.wav"
		};
		for (int i = 0; i < soundFiles.length; i++){
			try{
				soundManager.soundPoolSounds.addSound(soundFiles[i], this.getClass().getClassLoader().getResource("/" + soundFiles[i]));
	            System.out.printf("Added %s to the sound pool!\n", soundFiles[i]);
			}
			catch (Exception e)
			{
				System.out.println(soundFiles[i] + " failed to load! :(\n");
			}
		}
		
		
	}
	

	public static void playSound(World theWorld, String soundName, double[] destination, float volume, float pitch)
	{
		if (!theWorld.isRemote){
			ServerPacketHandler.playSoundToClients(destination, volume, pitch, soundName);
			System.out.println("Tried to play sound '" + soundName + "'\n");
		}
	}


}
