package net.minecraft.src.lambdaMod.blocks;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.BlockFurnace;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import net.minecraft.src.lambdaMod.Destination;
import net.minecraft.src.lambdaMod.DestinationSaveMethods;
import net.minecraft.src.lambdaMod.EntangleCraft;
import net.minecraft.src.lambdaMod.NBTSaver;
import net.minecraft.src.lambdaMod.ServerPacketHandler;
import net.minecraft.src.lambdaMod.items.EntangleCraftItems;

public class TileEntityGenericDestination extends TileEntity implements IInventory, ISidedInventory{
	private ItemStack[] gdItemStacks = new ItemStack[1];

    /** The number of ticks that the current item has been cooking for */
	public int[] blockCoords = new int[3];
	public double[] destinationCoords = new double[3];

    public int gdProcessTime = 0;
	public int teleportsEarned = 0;
	public Destination destination;
	public int channel = 0;
	public int speedMultiplier = 1;
	private ArrayList validStacks = new ArrayList();
	private String beepSound = "beep";
	private int teleportKbzCost = 32;
	
		
	public TileEntityGenericDestination(){
		gdItemStacks = new ItemStack[2];
		validStacks.add(new ItemStack(EntangleCraftItems.ItemNetherEssence,1).itemID);
		validStacks.add(new ItemStack(EntangleCraftItems.ItemNethermonicDiamond,1).itemID);
		validStacks.add(new ItemStack(EntangleCraftItems.ItemLambdaCore,1).itemID);
	}
	@Override
	public int getStartInventorySide(ForgeDirection side) {
		return 0;
	}
	
	public void setDest(int[] blockCoords,int channel){
		this.blockCoords = blockCoords;
		this.destinationCoords[0] = blockCoords[0] + 0.5;
		this.destinationCoords[1] = blockCoords[1] + 2.65;
		this.destinationCoords[2] = blockCoords[2] + 0.5;
		this.channel = channel;
		createDestination();
	}
		
	public void createDestination(){
		destination = new Destination(destinationCoords,blockCoords,channel);
	}
	
	
	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 1;
	}
	
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList tagList = par1NBTTagCompound.getTagList("Items");
        this.gdItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            NBTTagCompound itemStack = (NBTTagCompound)tagList.tagAt(i);
            byte thisByte = itemStack.getByte("Slot");

            if (thisByte >= 0 && thisByte < this.gdItemStacks.length)
            {
                this.gdItemStacks[thisByte] = ItemStack.loadItemStackFromNBT(itemStack);
            }
        }

        this.gdProcessTime = par1NBTTagCompound.getShort("processTime");
        this.teleportsEarned = (int) par1NBTTagCompound.getShort("teleportsEarned");
        this.speedMultiplier = (int) par1NBTTagCompound.getShort("speedMultiplier");
        if (this.speedMultiplier == 0) this.speedMultiplier = 1;
        this.destination = DestinationSaveMethods.readDestFromNBT(par1NBTTagCompound);
        if (teleportsEarned > 0) EntangleCraft.addDestination(this.destination);
    }
	
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("gdProcessTime", (short)this.gdProcessTime);
        par1NBTTagCompound.setShort("teleportsEarned", (short)(int)this.teleportsEarned);
        par1NBTTagCompound.setShort("speedMultiplier", (short)this.speedMultiplier);
        NBTTagList tagList = new NBTTagList();

        for (int var3 = 0; var3 < this.gdItemStacks.length; ++var3)
        {
            if (this.gdItemStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.gdItemStacks[var3].writeToNBT(var4);
                tagList.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", tagList);
        DestinationSaveMethods.writeDestToNBT(par1NBTTagCompound, destination);
    }
    

	@Override
    public int getSizeInventory()
    {
        return this.gdItemStacks.length;
    }

	@Override
	  public ItemStack getStackInSlot(int par1)
    {
        return this.gdItemStacks[par1];
    }
	
    private boolean canSmelt()
    {	boolean canThisSmelt = false;
    	ItemStack itemInSlot = this.gdItemStacks[0];    	
    	if (itemInSlot == null) canThisSmelt =  false;
    	else {
    		int thisItemsID = itemInSlot.itemID;
    		
    		for (Object ID : validStacks){
    			Integer intID = (Integer)ID;
    		canThisSmelt = canThisSmelt | thisItemsID == intID;
    		}
    	}
    	canThisSmelt = canThisSmelt && teleportsEarned < 1024;
    	return canThisSmelt;
    }

	@Override
	public ItemStack decrStackSize(int slot, int decreaseAmount) {
	    if (this.gdItemStacks[slot] != null)
        {
            ItemStack itemStack;

            if (this.gdItemStacks[slot].stackSize <= decreaseAmount)
            {
                itemStack = this.gdItemStacks[slot];
                this.gdItemStacks[slot] = null;
                return itemStack;
            }
            else
            {
                itemStack = this.gdItemStacks[slot].splitStack(decreaseAmount);

                if (this.gdItemStacks[slot].stackSize == 0)
                {
                    this.gdItemStacks[slot] = null;
                }

                return itemStack;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		
	        if (this.gdItemStacks[slot] != null)
	        {
	            ItemStack itemStack = this.gdItemStacks[slot];
	            this.gdItemStacks[slot] = null;
	            return itemStack;
	        }
	        else
	        {
	            return null;
	        }
	    }

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.gdItemStacks[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
        {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
    }

	@Override
	public String getInvName() {
		// TODO Auto-generated method stub
		return "tileEntityGD";
	}

	@Override
	public int getInventoryStackLimit() {
		return 16;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}
	
	   /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    public int getCookProgressScaled(int par1)
    {
    	int denominator = 200/this.speedMultiplier;
        return this.gdProcessTime * par1 / denominator;
    }



    public void updateEntity()
    {
    	super.updateEntity();
        boolean aBoolean = false;
        
        {
        	this.transform();
        	this.reverseTransform();
            if (canSmelt())
            {
                ++this.gdProcessTime;
                int goal = 200/this.speedMultiplier;
                if (this.gdProcessTime == goal || this.gdProcessTime > goal)
                {
                	if (EntangleCraft.proxy.isServer){
                        this.gdProcessTime = 0;
                        ServerPacketHandler.sendTEFieldUpdate(this, "TileEntityGenericDestination", "gdProcessTime");
                        this.smeltItem();
                        ServerPacketHandler.playSoundToClients(new double[] {(double)this.destinationCoords[0], (double)this.destinationCoords[1], (double)this.destinationCoords[2]},this.worldObj.rand.nextFloat()*0.05F + 0.02F,(((float)this.speedMultiplier)/16.0F) * 0.4F + 0.6F,this.beepSound);
                        
                        aBoolean = true;
                	}
                }
            }
            else
            {
                this.gdProcessTime = 0;
            }
        }

        if (aBoolean)
        {
            this.onInventoryChanged();
        }
                
    }
    
    public boolean canTransform(){
    	boolean canTransform = false;
    	if (!this.worldObj.isRemote){
	    	canTransform = this.teleportsEarned > 0;
	    	canTransform = canTransform && this.gdItemStacks[1] != null;
	    	if(canTransform) {
	    		canTransform = canTransform && this.gdItemStacks[1].itemID == new ItemStack(EntangleCraftItems.ItemTransformer,1).itemID;
	    		canTransform = canTransform && this.gdItemStacks[1].stackSize <= this.teleportsEarned;
	    	}
	    	else canTransform = false;
    	}
    	return canTransform;
    }
    
    public boolean canReverseTransform(){
    	boolean canReverseTransform = EntangleCraft.dhInstance.getDistance(this.channel) >= this.teleportKbzCost;
    	canReverseTransform = canReverseTransform && this.teleportsEarned < 1024;
    	canReverseTransform = canReverseTransform && this.gdItemStacks[1] != null;
    	if (canReverseTransform) {
    		canReverseTransform = canReverseTransform && this.gdItemStacks[1].itemID == new ItemStack(EntangleCraftItems.ItemReverseTransformer,1).itemID;
    		canReverseTransform = canReverseTransform && EntangleCraft.dhInstance.getDistance(this.channel) >= this.teleportKbzCost*this.gdItemStacks[1].stackSize;
    	}
    	else canReverseTransform = false;
    	
    	return canReverseTransform;
    }
    
    public void smeltItem(){
    	if (this.canSmelt()){
    		this.beepSound = "beep";
    		if(this.gdItemStacks[0].itemID == (Integer)(validStacks.get(1))){
    			this.changeTeleportsEarned(1);
    		}		
    		else if(this.gdItemStacks[0].itemID == (Integer)validStacks.get(2)){
    			this.changeTeleportsEarned(2);
    			this.beepSound = "superBeep";
    		}
    		else{
    		this.decrStackSize(0,1);
    		this.changeTeleportsEarned(1);
    		}
    	}
    }
    
    public void transform(){
    	
    	if (canTransform()){
    		int transformMultiplier = this.gdItemStacks[1].stackSize;
    		
    	this.changeTeleportsEarned(transformMultiplier*-1);
    	EntangleCraft.dhInstance.addToDistance(this.channel, transformMultiplier*32);
    	}
    }
    
    public void reverseTransform(){
    	if (canReverseTransform()){
    		int transformMultiplier = this.gdItemStacks[1].stackSize;
    	this.changeTeleportsEarned(transformMultiplier*1);
    	
    	EntangleCraft.dhInstance.subtractDistance(this.channel, transformMultiplier*this.teleportKbzCost);  

    	}
    }
    
    public void onInventoryChanged(){
    	super.onInventoryChanged();
    	
    	if (EntangleCraft.proxy.isServer){
	    	int howMany = 0;
	    	boolean isItemValid = false;
	    	boolean isItemLambdaCore = false;
   	
	    	try{
	    	ItemStack gdItem = this.gdItemStacks[0];
	    	
			for (Object ID : validStacks){
				Integer intID = (Integer)ID;
			isItemValid = isItemValid | gdItem.itemID == intID;
			isItemLambdaCore = gdItem.itemID == intID && intID == (new ItemStack(EntangleCraftItems.ItemLambdaCore,1).itemID);
			}
	    	if (isItemValid) {
	    		howMany = gdItem.stackSize; this.speedMultiplier = howMany*2;
	    		if (gdItem.itemID == new ItemStack(EntangleCraftItems.ItemNetherEssence,1).itemID) this.speedMultiplier = howMany;
	    		}
	    	else this.speedMultiplier = 1;
	    	}
	    	catch (Exception e){	}
	    	if (this.speedMultiplier == 0) this.speedMultiplier = 1;  
	    	ServerPacketHandler.sendTEFieldUpdate(this,"TileEntityGenericDestination","speedMultiplier");
    	}
    }
    
    public void changeTeleportsEarned(int amount){
    	if (EntangleCraft.proxy.isServer){
	    	this.teleportsEarned = this.teleportsEarned + amount;
	    	if (this.teleportsEarned == 1 && amount > 0){
	    		this.addOrRemoveThisDest("add");
	    	}
	    	else if (this.teleportsEarned == 2 && amount > 0){
	    		this.addOrRemoveThisDest("remove"); // There is a high chance its already in here
	    		this.addOrRemoveThisDest("add");
	    	}
	    	else if (this.teleportsEarned == 0){
	    		this.addOrRemoveThisDest("remove");
	    	}
	    	
	    	if (this.teleportsEarned >1024) this.teleportsEarned = 1024;
	    	else if (this.teleportsEarned <0) this.teleportsEarned = 0;
	    	ServerPacketHandler.sendTEFieldUpdate(this,"TileEntityGenericDestination","teleportsEarned");
    	}
    }
    
    
	public void addOrRemoveThisDest(String action){
		if (action == "add"){
			EntangleCraft.addDestination(destination);
		}
		else if (action == "remove"){
			EntangleCraft.removeDestination(destination);
			
		}
	}
	
	
	public int getChannel(){
		return destination.channel;
	}
	
	public void setChannel(int i){
		destination.channel = i;
	}
    
	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	public void recieveUpdateFromServer(NetworkManager network, DataInputStream dataStream, String fieldName) throws IllegalArgumentException, IllegalAccessException{
		Class<?> c = this.getClass();
		Field theField = null;
		boolean shouldUpdate = true;
		try {
			theField = c.getDeclaredField(fieldName);
			if (theField != null) theField.setAccessible(true);
		} catch (NoSuchFieldException e1) {
			shouldUpdate = false;
			e1.printStackTrace();
		} catch (SecurityException e1) {
			shouldUpdate = false;
			e1.printStackTrace();
		}
		
		if (shouldUpdate){
			if (theField.get(this) instanceof Integer){
				try{
					int fieldValue = dataStream.readInt();
					theField.setInt(this,fieldValue);
				}
				catch (Exception e){
					e.printStackTrace();
				}
			}
			
			else if (theField.get(this) instanceof int[]){
				try{
					int arrSize = dataStream.readInt();
					int[] fieldValue = new int[arrSize];
					for (int i = 0; i < arrSize; i ++){
						fieldValue[i] = dataStream.readInt();
					}
				}
				catch (Exception e){
					e.printStackTrace();
				};
			}
			else if (theField.get(this) instanceof Boolean){
				boolean fieldValue;
				try {
					fieldValue = dataStream.readBoolean();
				theField.setBoolean(this,fieldValue);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}

}
}
