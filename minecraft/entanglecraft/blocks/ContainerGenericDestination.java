package entanglecraft.blocks;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.tileentity.TileEntityFurnace;
import entanglecraft.EntangleCraft;
import entanglecraft.items.EntangleCraftItems;

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
	@Override
	public ItemStack transferStackInSlot(EntityPlayer thePlayer, int slotNumber) {
		
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(slotNumber);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotNumber == 1 || slotNumber == 0) 
			{
				if (!mergeItemStack(itemstack1, 2, 38, true)) 
				{
					return null;
				}
			} 
			
			else if (slotNumber >= 2 && slotNumber < 38)
			{
				ItemStack powerSlot = ((Slot)inventorySlots.get(0)).getStack();
				ItemStack transformerSlot = ((Slot)inventorySlots.get(1)).getStack();
				ItemStack[] destinationSlots = new ItemStack[] {powerSlot,transformerSlot};
				
				boolean itemIsTransformer = itemstack1.itemID == new ItemStack(EntangleCraftItems.ItemTransformer).itemID 
						|| itemstack1.itemID == new ItemStack(EntangleCraftItems.ItemReverseTransformer).itemID;
				
				int i = itemIsTransformer ? 1 : 0;
				int difference;
				
				ItemStack workingCopy = itemstack1.copy();
				ItemStack workingCopy1 = workingCopy.copy();
				
				if (destinationSlots[i] == null || destinationSlots[i].stackSize < 16)
				{
					difference = destinationSlots[i] == null ? 16 : 16 - destinationSlots[i].stackSize;
					
					if (workingCopy.stackSize > difference)
					{
						workingCopy = workingCopy1.splitStack(difference);
					}
					
					else workingCopy1.stackSize = 0;
					
					if (!mergeItemStack(workingCopy,i,2,false))
					{
						return null;
					}
					
					else itemstack1.stackSize = workingCopy1.stackSize;
			
				}
				
				else 
				{
					return null;
				}
			}
			

			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize != itemstack.stackSize) {
				slot.onPickupFromSlot(thePlayer, itemstack1);
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
