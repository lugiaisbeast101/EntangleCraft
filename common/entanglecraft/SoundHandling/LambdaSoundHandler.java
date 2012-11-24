package entanglecraft.SoundHandling;

import net.minecraft.src.Entity;
import net.minecraft.src.SoundManager;
import net.minecraft.src.SoundPoolEntry;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class LambdaSoundHandler{
	
	@ForgeSubscribe
	public void onLoadSoundSettings(SoundLoadEvent event) {
		System.out.println("'onLoadSoundSettings' was called");
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
            soundManager.soundPoolSounds.addSound(soundFiles[i], this.getClass().getClassLoader().getResource("/" + soundFiles[i]));
            System.out.printf("Added %s to the sound pool!\n", soundFiles[i]);
		}
	}
	



}
