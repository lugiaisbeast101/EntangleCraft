package entanglecraft.gui;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Container;
import net.minecraft.src.ContainerFurnace;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;
import net.minecraft.src.TileEntityFurnace;
import entanglecraft.DistanceHandler;
import entanglecraft.EntangleCraft;
import entanglecraft.blocks.BlockLambdaMiner;
import entanglecraft.blocks.ContainerLambdaMiner;
import entanglecraft.blocks.TileEntityLambdaMiner;

public class GuiLambdaMiner extends GuiContainer {

	private TileEntityLambdaMiner lMInventory;
	private String name = "Entanglement Miner";

	public GuiLambdaMiner(InventoryPlayer inv, TileEntityLambdaMiner tileEntitylM) {
		super(new ContainerLambdaMiner(inv, tileEntitylM));
		this.lMInventory = tileEntitylM;

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRenderer.drawString(StatCollector.translateToLocal(this.name), 50, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("Energy Level = " + EntangleCraft.dhInstance.getStringDistance(lMInventory.channel)), 8,
				59, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("-" + this.lMInventory.blockCost), 30, 44, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("Y = " + this.lMInventory.layerToMine), 5, 6, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int background = this.mc.renderEngine.getTexture("/gui/lm.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(background);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		int var7;

		var7 = this.lMInventory.getCookProgressScaled(24);
		this.drawTexturedModalRect(x + 79, y + 34, 176, 14, var7 + 1, 16);

	}

}
