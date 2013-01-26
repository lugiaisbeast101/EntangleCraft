package entanglecraft;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.network.IGuiHandler;
import entanglecraft.blocks.EntangleCraftBlocks;
import entanglecraft.blocks.container.ContainerGenericDestination;
import entanglecraft.blocks.container.ContainerLambdaMiner;
import entanglecraft.blocks.container.ContainerPowerHub;
import entanglecraft.blocks.tileEntity.TileEntityGenericDestination;
import entanglecraft.blocks.tileEntity.TileEntityLambdaMiner;
import entanglecraft.blocks.tileEntity.TileEntityPowerHub;
import entanglecraft.gui.EnumGui;
import entanglecraft.gui.GuiGenericDestination;
import entanglecraft.gui.GuiLambdaMiner;
import entanglecraft.items.EntangleCraftItems;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy implements IGuiHandler {
	
	public void registerPreLoad() {
	}
	
	public void registerOnLoad() {
		EntangleCraftBlocks.addBlocks();
		EntangleCraftItems.addItems();
	}

	public static void registerDistanceSaver(DistanceHandler dh) {
		MinecraftForge.EVENT_BUS.register(dh);
	}

	public File getWorldSaveDir(World world) {
		File workingDir = new File(".\\\\world\\");
		return workingDir;
	}
	
	public static void sendTEFieldUpdate(TileEntity te, String tileEntityName, String field) {
		ServerPacketHandler.sendTEFieldUpdate(te, tileEntityName, field);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == EnumGui.GenericDestination.getIndex()) {
			TileEntityGenericDestination tileEntityGD = (TileEntityGenericDestination) world.getBlockTileEntity(x, y, z);
			return new ContainerGenericDestination(player.inventory, tileEntityGD);

		}

		else if (ID == EnumGui.LambdaMiner.getIndex()) {
			TileEntityLambdaMiner tileEntitylM = (TileEntityLambdaMiner) world.getBlockTileEntity(x, y, z);
			return new ContainerLambdaMiner(player.inventory, tileEntitylM);
		} 
		
		else if (ID == EnumGui.PowerHub.getIndex())
		{
			TileEntityPowerHub powerHub = (TileEntityPowerHub) world.getBlockTileEntity(x, y, z);
			return new ContainerPowerHub(player.inventory, powerHub);
		}
			return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
	


}