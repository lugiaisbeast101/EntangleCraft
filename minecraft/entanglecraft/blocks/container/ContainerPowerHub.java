package entanglecraft.blocks.container;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import entanglecraft.EntangleCraft;
import entanglecraft.blocks.tileEntity.TileEntityPowerHub;
import entanglecraft.items.EntangleCraftItems;

public class ContainerPowerHub extends Container{
	private TileEntityPowerHub powerHub;
    private int lastChargeTime = 0;
    private int lastBurnTime = 0;
    private int lastItemBurnTime = 0;
    public int lastTeleportsEarned = 0;
    public String lastKbzDistance = "0";
    
	public ContainerPowerHub(InventoryPlayer inv, TileEntityPowerHub tileEntity)
    {
        this.powerHub = tileEntity;
   

        
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
	
	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		
		return true;
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift
	 * clicking.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer thePlayer, int slotNumber) {
		return null;
	}   

}
