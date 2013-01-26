package entanglecraft;

import java.io.File;

import cpw.mods.fml.client.registry.KeyBindingRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import entanglecraft.SoundHandling.LambdaSoundHandler;
import entanglecraft.blocks.container.ContainerPowerHub;
import entanglecraft.blocks.tileEntity.TileEntityGenericDestination;
import entanglecraft.blocks.tileEntity.TileEntityLambdaMiner;
import entanglecraft.blocks.tileEntity.TileEntityPowerHub;
import entanglecraft.gui.EnumGui;
import entanglecraft.gui.GuiGenericDestination;
import entanglecraft.gui.GuiLambdaMiner;
import entanglecraft.gui.GuiPowerHub;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerPreLoad() {
		MinecraftForge.EVENT_BUS.register(new LambdaSoundHandler());
	}
	
	@Override
	public void registerOnLoad() {
		KeyBindingRegistry.registerKeyBinding(new KeyHandling());
		MinecraftForgeClient.preloadTexture("/lambdaTextures.png");
		MinecraftForge.EVENT_BUS.register(new LambdaSoundHandler());
		
		super.registerOnLoad();
	}

	@Override
	public File getWorldSaveDir(World world) {
		File workingDir = new File(".\\\\saves\\" + world.getSaveHandler().getSaveDirectoryName() + "\\");
		return workingDir;
	}
	
	public static void sendTEFieldUpdate(TileEntity te, String tileEntityName, String field) {
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
		} 
		
		else if (ID == EnumGui.PowerHub.getIndex())
		{
			TileEntityPowerHub powerHub = (TileEntityPowerHub) world.getBlockTileEntity(x, y, z);
			return new GuiPowerHub(player.inventory, powerHub);
		}
		else
			return null;
	}
	

}
