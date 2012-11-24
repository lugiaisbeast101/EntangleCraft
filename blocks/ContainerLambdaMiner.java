package net.minecraft.entanglecraft.blocks;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.SlotFurnace;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.entanglecraft.EntangleCraft;
import net.minecraft.entanglecraft.items.EntangleCraftItems;

public class ContainerLambdaMiner extends Container{
	private TileEntityLambdaMiner minerEntity;
    private int lastChargeTime = 0;
    private int lastBurnTime = 0;
    private int lastItemBurnTime = 0;
    public int lastTeleportsEarned = 0;
    public String lastKbzDistance = "0";
    
	public ContainerLambdaMiner(InventoryPlayer inv, TileEntityLambdaMiner miner)
    {
        this.minerEntity = miner;
   
        addSlotToContainer(new Slot(miner, 0, 65, 18)); //Left
        addSlotToContainer(new Slot(miner, 1, 83, 18)); //Forward
        addSlotToContainer(new Slot(miner, 2, 101, 18)); //Right
        addSlotToContainer(new Slot(miner, 3, 38,18)); //Speed Increaser
        addSlotToContainer(new Slot(miner, 10,11,18)); // Filter
        
        addSlotToContainer(new Slot(miner, 4, 132, 18)); // Filter 1
        addSlotToContainer(new Slot(miner, 5, 150, 18)); // Filter 2
        addSlotToContainer(new Slot(miner, 6, 132, 36)); // Filter 3
        addSlotToContainer(new Slot(miner, 7, 150, 36)); // Filter 4
        addSlotToContainer(new Slot(miner, 8, 132, 54)); // Filter 5
        addSlotToContainer(new Slot(miner, 9, 150, 54)); // Filter 6
        
		for (int i = 0; i < 3; i++) {
			for (int k = 0; k < 9; k++) {
				addSlotToContainer(new Slot(inv, k + i * 9 + 9,
						8 + k * 18, 84 + i * 18));
			}
		}

		for (int j = 0; j < 9; j++) {
			addSlotToContainer(new Slot(inv, j, 8 + j * 18, 142));
		}
		
    }
	
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	    {
	        return this.minerEntity.isUseableByPlayer(par1EntityPlayer);
	    }

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift
	 * clicking.
	 */
	public ItemStack transferStackInSlot(int par1) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(par1);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par1 >= 0 && par1 <= 10) {
				if (!mergeItemStack(itemstack1, 11, 47, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (par1 >= 11 && par1 < 47){
				if (itemstack1.itemID == new ItemStack(EntangleCraftItems.ItemTransmitter).itemID){
					if (!mergeItemStack(itemstack1,0,3,false)) return null;
				}
				else if (itemstack1.itemID == new ItemStack(EntangleCraftItems.ItemInductionCircuit).itemID || itemstack1.itemID == new ItemStack(EntangleCraftItems.ItemSuperInductionCircuit).itemID){
					if (!mergeItemStack(itemstack1,3,4,false)) return null;
				}
				else if (itemstack1.itemID == new ItemStack(EntangleCraftItems.ItemInclusiveFilter).itemID || itemstack1.itemID == new ItemStack(EntangleCraftItems.ItemExclusiveFilter).itemID){
					if (!mergeItemStack(itemstack1,4,5,false)) return null;
				}
				else{
					if (!mergeItemStack(itemstack1, 5, 11, false)) {
						return null;	
				}
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
            this.minerEntity.processTime = par2;
        }

        if (par1 == 1)
        {
        	this.minerEntity.processTime = par2;
        }

        if (par1 == 2)
        {
        	this.minerEntity.processTime = par2;
        }
    }
}
