package entanglecraft;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class InventoryController {
	private int chestRecursion = 0;
	private int[] chest;
	private TileEntity tileEntity = null;
	private int[] blockCoords = null;
	private boolean isChestEnder;
	
	public InventoryController(TileEntity te, int[] blockCoords) {
		this.tileEntity = te;
		this.blockCoords = blockCoords;
	}
	
	public void setChest(int[] coords) {
		if (coords != null && coords.length == 3) {
			this.chest = new int[] { coords[0], coords[1], coords[2] };
		} else
			this.chest = null;
	}
	
	public void checkForChest() {
		int chestId = Block.chest.blockID;
		World world = tileEntity.worldObj;
		int x = this.blockCoords[0];
		int y = this.blockCoords[1];
		int z = this.blockCoords[2];

		if (world.getBlockId(x + 1, y, z) == chestId) 
		{
			setChest(new int[] { x + 1, y, z });
		} 
		else if (world.getBlockId(x - 1, y, z) == chestId) 
		{
			setChest(new int[] { x - 1, y, z });
		} 
		else if (world.getBlockId(x, y, z + 1) == chestId)
		{
			setChest(new int[] { x, y, z + 1 });
		} 
		else if (world.getBlockId(x, y, z - 1) == chestId)
		{
			setChest(new int[] { x, y, z - 1 });
		} else
			setChest(null);
	}

	public int[] getChest() {
		if (this.chest == null) {
			return null;
		} else
			return this.chest;
	}
	
	public TileEntityChest getTileEntityChest() {
		return (TileEntityChest) this.tileEntity.worldObj.getBlockTileEntity(chest[0], chest[1], chest[2]);
	}
	
	public static boolean isShearable(World world, int blockID, int x, int y, int z)
	{
		if (!(Block.blocksList[blockID] instanceof IShearable))
		{
			return false;
		}
		
		ItemStack itemstack = new ItemStack(Item.shears,1);
		IShearable target = (IShearable)Block.blocksList[blockID];
        return target.isShearable(itemstack, world, x, y, z);
	}
	
	public static ItemStack getItemStackFromIDAndMetadata(int blockID, int metadata) {
		ItemStack result = null;
		Random rand = new Random();
		if (blockID == Block.stone.blockID) 
		{
			result = new ItemStack(Block.cobblestone, 1);
		} 
		
		else if (blockID == Block.waterMoving.blockID || blockID == Block.waterStill.blockID 
				|| blockID == Block.lavaStill.blockID
				|| blockID == Block.lavaMoving.blockID || blockID == Block.bedrock.blockID) 
		{
			result = null;
		} 
		
		else if (blockID == Block.oreCoal.blockID) 
		{
			result = new ItemStack(Item.coal, 1);
		} 
		
		else if (blockID == Block.oreRedstone.blockID) 
		{
			result = new ItemStack(Item.redstone, rand.nextInt(1) + 4);
		} 
				
		else if (blockID == Block.oreLapis.blockID) 
		{
			result = new ItemStack(Item.dyePowder, rand.nextInt(1) + 4, 4);
		} 
		
		else if (blockID == Block.oreDiamond.blockID) 
		{
			result = new ItemStack(Item.diamond, 1);
		} 
		
		else if (blockID != 0)
		{
			Block theBlock = Block.blocksList[blockID];
			result = new ItemStack(theBlock.idDropped(theBlock.blockID, new Random(), 0), theBlock.quantityDropped(new Random()), theBlock.damageDropped(metadata));
		}
	
		return result;
	}
			
	public static int getBlockIDFromItem(int id) {
		int result = id;
		if (id == new ItemStack(Item.diamond, 1).itemID) 
		{
			result = Block.oreDiamond.blockID;
		} 
		
		else if (id == new ItemStack(Item.coal).itemID) 
		{
			result = Block.oreCoal.blockID;
		} 
		
		else if (id == new ItemStack(Item.redstone).itemID) 
		{
			result = Block.oreRedstone.blockID;
		} 
		
		else if (id == new ItemStack(Item.dyePowder, 1, 4).itemID) 
		{
			result = Block.blockLapis.blockID;
		} 
		
		else if (id == Block.cobblestone.blockID) 
		{
			result = Block.stone.blockID;
		}

		return result;
	}
	
	public boolean addStackToInventory(ItemStack itemStack) {
		boolean overFlowed = false;
		checkForChest();
		if (getChest() != null) {
			TileEntityChest theChest = getTileEntityChest();
			overFlowed = addStackToInventory(theChest, itemStack);
		}

		else {
			EntityItem e = new EntityItem(this.tileEntity.worldObj, (double) this.blockCoords[0] + 0.5, (double) this.blockCoords[1] + 1.5,
					(double) this.blockCoords[2] + 0.5, itemStack);
			e.dropItem(itemStack.itemID, itemStack.stackSize);
		}
		return overFlowed;
	}
	
	public boolean addStackToInventory(TileEntityChest inv, ItemStack itemStack) {
				
		this.chestRecursion += 1;
		int counter = 0;
		boolean invContainsItem = false;
		boolean slotOccupied = false;
		boolean overFlow = false;
		boolean chestOverFlow = false;
		boolean stackAdded = false;

		if (itemStack != null) {
			ItemStack overFlowStack = itemStack.copy();
			for (counter = 0; counter < inv.getSizeInventory(); counter++) {
				slotOccupied = inv.getStackInSlot(counter) != null;
				if (slotOccupied) {
					invContainsItem = inv.getStackInSlot(counter).itemID == itemStack.itemID;
					invContainsItem = invContainsItem && inv.getStackInSlot(counter).stackSize < 64;
					if (invContainsItem)
						break;
				} else {
					break;
				}
			}

			if (invContainsItem) {
				int sizeOf = inv.getStackInSlot(counter).stackSize;
				itemStack.stackSize = (itemStack.stackSize + sizeOf) % 64;
				if (itemStack.stackSize < sizeOf) {
					overFlow = true;
					overFlowStack.stackSize = 64;
				}
				if (!overFlow) {
					inv.setInventorySlotContents(counter, itemStack);
					stackAdded = true;
				} else {
					inv.setInventorySlotContents(counter, overFlowStack);
					addStackToInventory(inv, itemStack);
					stackAdded = true;
				}
			} else if (!slotOccupied) {
				inv.setInventorySlotContents(counter, itemStack);
				stackAdded = true;
			}

			if (!stackAdded) {
				if (chestRecursion < 3) {

					if (inv.adjacentChestXNeg != null)
						addStackToInventory(inv.adjacentChestXNeg, itemStack);
					else if (inv.adjacentChestXPos != null)
						addStackToInventory(inv.adjacentChestXPos, itemStack);
					else if (inv.adjacentChestZNeg != null)
						addStackToInventory(inv.adjacentChestZNeg, itemStack);
					else if (inv.adjacentChestZPosition != null)
						addStackToInventory(inv.adjacentChestZPosition, itemStack);
					else {
						chestOverFlow = true;
						EntityItem e = new EntityItem(this.tileEntity.worldObj, (double) this.blockCoords[0] + 0.5, (double) this.blockCoords[1] + 1.5,
								(double) this.blockCoords[2] + 0.5, itemStack);
						e.dropItem(itemStack.itemID, itemStack.stackSize);
					}
				} else {
					chestOverFlow = true;
					EntityItem e = new EntityItem(this.tileEntity.worldObj, (double) this.blockCoords[0] + 0.5, (double) this.blockCoords[1] + 1.5,
							(double) this.blockCoords[2] + 0.5, itemStack);
					e.dropItem(itemStack.itemID, itemStack.stackSize);
				}
			}
		}
		this.chestRecursion = 0;
		
		return chestOverFlow;
	}
	
	public boolean addStackToInventory(InventoryBasic inv, ItemStack itemStack) {
		int counter = 0;
		boolean invContainsItem = false;
		boolean slotOccupied = false;
		boolean overFlow = false;
		if (itemStack != null) {
			ItemStack overFlowStack = itemStack.copy();
			for (counter = 0; counter < inv.getSizeInventory(); counter++) {
				slotOccupied = inv.getStackInSlot(counter) != null;
				if (slotOccupied) {
					invContainsItem = inv.getStackInSlot(counter).itemID == itemStack.itemID;
					invContainsItem = invContainsItem && inv.getStackInSlot(counter).stackSize < 64;
					if (invContainsItem)
						break;
				} else {
					break;
				}
			}

			if (invContainsItem) {
				int sizeOf = inv.getStackInSlot(counter).stackSize;
				itemStack.stackSize = (itemStack.stackSize + sizeOf) % 64;
				if (itemStack.stackSize < sizeOf) {
					overFlow = true;
					overFlowStack.stackSize = 64;
				}
				if (!overFlow)
					inv.setInventorySlotContents(counter, itemStack);
				else {
					inv.setInventorySlotContents(counter, overFlowStack);
					addStackToInventory(inv, itemStack);
				}
			} else {
				inv.setInventorySlotContents(counter, itemStack);
			}
		}
		
		return overFlow;
	}

}
