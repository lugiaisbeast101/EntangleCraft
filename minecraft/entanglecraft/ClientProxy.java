package entanglecraft;

import java.io.File;

import cpw.mods.fml.client.registry.KeyBindingRegistry;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.SoundManager;
import net.minecraft.src.World;
import entanglecraft.SoundHandling.LambdaSoundHandler;
import entanglecraft.blocks.TileEntityGenericDestination;
import entanglecraft.blocks.TileEntityLambdaMiner;
import entanglecraft.gui.EnumGui;
import entanglecraft.gui.GuiGenericDestination;
import entanglecraft.gui.GuiLambdaMiner;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerClientSide() {
		KeyBindingRegistry.registerKeyBinding(new KeyHandling());
	}

	public File getWorldSaveDir(World world) {
		File workingDir = new File(".\\\\saves\\" + world.getWorldInfo().getWorldName() + "\\");
		return workingDir;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == EnumGui.GenericDestination.getIndex()) {
			TileEntityGenericDestination tileEntityGD = (TileEntityGenericDestination) world.getBlockTileEntity(x, y, z);
			return new GuiGenericDestination(player.inventory, tileEntityGD);
		}

		else if (ID == EnumGui.LambdaMiner.getIndex()) {
			TileEntityLambdaMiner tileEntitylM = (TileEntityLambdaMiner) world.getBlockTileEntity(x, y, z);
			return new GuiLambdaMiner(player.inventory, tileEntitylM);
		} else
			return null;
	}
	
	@Override
	public void playSound(World theWorld, String soundName, double[] destination, boolean randomFloats)
	{
	}
}
