package net.minecraft.src.lambdaMod.blocks;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.SlotFurnace;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.lambdaMod.EntangleCraft;
import net.minecraft.src.lambdaMod.items.EntangleCraftItems;

public class ContainerGenericDestination extends Container{
	private TileEntityGenericDestination genericDestination;
    private int lastChargeTime = 0;
    private int lastBurnTime = 0;
    private int lastItemBurnTime = 0;
    public int lastTeleportsEarned = 0;
    public String lastKbzDistance = "0";
    
	public ContainerGenericDestination(InventoryPlayer inv, TileEntityGenericDestination tileEntityGD)
    {
        this.genericDestination = tileEntityGD;
    
    	addSlotToContainer(new Slot(tileEntityGD, 0, 56, 34));//FUEL
    	addSlotToContainer(new Slot(tileEntityGD, 1, 20, 34)); //Transformer
    	
		for (int i = 0; i < 3; i++) {
			for (int k = 0; k < 9; k++) {
				addSlotToContainer(new Slot(inv, k + i * 9 + 9,
						8 + k * 18, 84 + i * 18));
			}
		}

		for (int j = 0; j < 9; j++) {
			addSlotToContainer(new Slot(inv, j, 8 + j * 18, 142));
		}
		
		this.lastTeleportsEarned = tileEntityGD.teleportsEarned;
		this.lastKbzDistance = EntangleCraft.dhInstance.getStringDistance(tileEntityGD.channel);
    }
	
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	    {
	        return this.genericDestination.isUseableByPlayer(par1EntityPlayer);
	    }

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift
	 * clicking.
	 */
	public ItemStack transferStackInSlot(int slotNumber) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (slotNumber == 1 || slotNumber == 0) {
				if (!mergeItemStack(itemstack1, 2, 38, true)) {
					return null;
				}
			} else if (slotNumber >= 2 && slotNumber < 38){
				if (itemstack1.itemID == new ItemStack(EntangleCraftItems.ItemTransformer).itemID || itemstack1.itemID == new ItemStack(EntangleCraftItems.ItemReverseTransformer).itemID){
					if (!mergeItemStack(itemstack1, 1,2, false)){
						if (!mergeItemStack(itemstack1,0,2,false)){
						return null;
						}
					}
				}
				else if (!mergeItemStack(itemstack1, 0,2, false)){
					return null;
				}
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize != itemstack.stackSize) {
				slot.onPickupFromSlot(itemstack1);
			} else {
				return null;
			}
		}

		return itemstack;
	}   
	
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.genericDestination.gdProcessTime = par2;
        }

        if (par1 == 1)
        {
        	this.genericDestination.gdProcessTime = par2;
        }

        if (par1 == 2)
        {
        	this.genericDestination.gdProcessTime = par2;
        }
    }
}
