package net.minecraft.entanglecraft.gui;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Container;
import net.minecraft.src.ContainerFurnace;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.entanglecraft.DistanceHandler;
import net.minecraft.entanglecraft.EntangleCraft;
import net.minecraft.entanglecraft.blocks.BlockGenericDestination;
import net.minecraft.entanglecraft.blocks.ContainerGenericDestination;
import net.minecraft.entanglecraft.blocks.EntangleCraftBlocks;
import net.minecraft.entanglecraft.blocks.TileEntityGenericDestination;

public class GuiGenericDestination extends GuiContainer{

	private TileEntityGenericDestination gdInventory;
	private String name = "Lambda Destination";
	private Integer teleportsEarnedString;
	
	public GuiGenericDestination(InventoryPlayer inv, TileEntityGenericDestination tileEntityGD) {
		super(new ContainerGenericDestination(inv, tileEntityGD));
        this.gdInventory = tileEntityGD;
        this.teleportsEarnedString = tileEntityGD.teleportsEarned;
	}
	
	private void getThisName(){
        int x = gdInventory.channel;
        int GLDChannel = ((BlockGenericDestination)EntangleCraftBlocks.BlockGenericDestination).channel;
        int RLDChannel = ((BlockGenericDestination)EntangleCraftBlocks.BlockRLD).channel;
        int YLDChannel = ((BlockGenericDestination)EntangleCraftBlocks.BlockYLD).channel;
        int BLDChannel = ((BlockGenericDestination)EntangleCraftBlocks.BlockBLD).channel;
        if (x == GLDChannel) this.name = "Green Lambda Destination";
        if (x == RLDChannel) this.name = "Redstone Lambda Destination";
        if (x == YLDChannel) this.name = "Yellow Lambda Destination";
        if (x == BLDChannel) this.name = "Blue Lambda Destination";
	}
	
	protected void drawGuiContainerForegroundLayer()
	    {
		try{
		this.getThisName();
		}
		catch (Exception e){};
		
	this.fontRenderer.drawString(StatCollector.translateToLocal("" + this.gdInventory.teleportsEarned),133,40, 4210752);
	this.fontRenderer.drawString(StatCollector.translateToLocal(this.name), 25, 6, 4210752);
	this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	this.fontRenderer.drawString(StatCollector.translateToLocal(EntangleCraft.dhInstance.getStringDistance(gdInventory.channel)) , 133, 59,4210752);   
	    }	
	
	public void setTeleportsEarnedString(int s){
		this.teleportsEarnedString = s;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3)     {
        int background = this.mc.renderEngine.getTexture("/gui/gd.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(background);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int var7;
        
        var7 = this.gdInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(x + 79, y + 34, 176, 14, var7 + 1, 16);
       
    }

}
