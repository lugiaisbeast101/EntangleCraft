package entanglecraft.gui;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.tileentity.TileEntityFurnace;
import entanglecraft.DistanceHandler;
import entanglecraft.EntangleCraft;
import entanglecraft.blocks.EntangleCraftBlocks;
import entanglecraft.blocks.block.BlockGenericDestination;
import entanglecraft.blocks.container.ContainerGenericDestination;
import entanglecraft.blocks.container.ContainerPowerHub;
import entanglecraft.blocks.tileEntity.TileEntityGenericDestination;
import entanglecraft.blocks.tileEntity.TileEntityPowerHub;

public class GuiPowerHub extends GuiContainer{

	private String name = "Power Hub";
	private TileEntityPowerHub powerHub;
	public GuiPowerHub(InventoryPlayer inv, TileEntityPowerHub tileEntity) {
		super(new ContainerPowerHub(inv, tileEntity));
		this.powerHub = tileEntity;
	}

	private String getChannelString() {
		int x = powerHub.channel;
		if (x == 0) return "Green Channel";
		if (x == 1) return "Red Channel";
		if (x == 2) return "Yellow Channel";
		if (x == 3) return "Blue Channel";
		
		else return null;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	

		this.fontRenderer.drawString(StatCollector.translateToLocal(getChannelString()), 28, 10, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal(EntangleCraft.dhInstance.getStringDistance(powerHub.channel)), 105, 46, 4210752);
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int background = this.mc.renderEngine.getTexture("/gui/ph.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(background);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		int var7;

}

}
