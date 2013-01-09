package entanglecraft.blocks;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.ISidedInventory;
import entanglecraft.DistanceHandler;
import entanglecraft.EntangleCraft;
import entanglecraft.InventoryController;
import entanglecraft.NBTSaver;
import entanglecraft.ServerPacketHandler;
import entanglecraft.SoundHandling.LambdaSoundHandler;
import entanglecraft.items.EntangleCraftItems;

public class TileEntityLambdaMiner extends TileEntity implements IInventory, ISidedInventory {
	public int channel;
	public int processTime;
	public int layerToMine = -1;
	public int[] blockCoords;
	public int blockCost = 16;
	private ArrayList<int[]> layerStructure;
	private ArrayList<Integer> filteredIds;
	private ItemStack[] lMItemStacks = new ItemStack[11];
	private int speedMultiplier = 1;
	private String minerSound = "minerFail";
	
	private final int FILTER_SLOT = 10;
	private final int DEFAULT_COST = 16;
	
	public InventoryController invController;
	
	private int[] lastStructStackSizes = new int[] { 0, 0, 0 };
	private boolean filterInclusive = false;
	public boolean isMining = false;

	public TileEntityLambdaMiner(int channel) {
		this.channel = channel;
	}

	public TileEntityLambdaMiner() {
	}

	public void setLayerStructure(ArrayList newStructure) {
		this.layerStructure = new ArrayList<int[]>();
		this.layerStructure = newStructure;
	}

	public void setBlockCoords(int[] coords) {
		this.blockCoords = new int[] { coords[0], coords[1], coords[2] };
		this.invController = new InventoryController(this,blockCoords);
		
		System.out.printf("Layer to mine is equal to " + getLayerToMine() + "\n");
		if (this.getLayerToMine() == -1)
		{
			this.setLayerToMine(coords[1]);
		}
		
			
	}
	
	public void setLayerToMine(int layer) {

		layerToMine = layer;
		//ServerPacketHandler.sendTEFieldUpdate(this, "TileEntityLambdaMiner", "layerToMine");
		//System.out.println("layerToMine now equal to " + layerToMine);
	}

	public int getLayerToMine() {
		return this.layerToMine;
	}
	
	public ArrayList generateLine(int size, int[] start) {
		ArrayList struct = new ArrayList();
		int direction = this.getBlockMetadata();
		direction = ((direction -2)%4 + 2);
		for (int i = 0; i < size; i++) 
		{
			int x = start[0];
			int y = start[1];
			int z = start[2];
			
			if (direction == 5)
				x += i;
			else if (direction == 4)
				x -= i;
			else if (direction == 3)
				z += i;
			else if (direction == 2)
				z -= i;
			
			struct.add(new int[] { x, y, z });
		}
		return struct;
	}

	public void generateLayerStructure() {
		if (!this.worldObj.isRemote) 
		{
			//System.out.println("Generate layer structure was called");
			
			int direction = this.getBlockMetadata();
			direction = ((direction -2)%4 + 2);
			boolean xPlus = direction == 5;
			boolean xMinus = direction == 4;
			boolean zPlus = direction == 3;
			boolean zMinus = direction == 2;
			
			ArrayList theStruct = new ArrayList();
			ArrayList structLeft = new ArrayList();
			ArrayList struct = new ArrayList();
			ArrayList structRight = new ArrayList();
			int scanLeft = lMItemStacks[0] != null ? (lMItemStacks[0].itemID == new ItemStack(EntangleCraftItems.ItemTransmitter, 1).itemID) ? lMItemStacks[0].stackSize
					: 0
					: 0;
			int scanForward = lMItemStacks[1] != null ? (lMItemStacks[1].itemID == new ItemStack(EntangleCraftItems.ItemTransmitter, 1).itemID) ? lMItemStacks[1].stackSize + 1
					: 1
					: 1;
			int scanRight = lMItemStacks[2] != null ? (lMItemStacks[2].itemID == new ItemStack(EntangleCraftItems.ItemTransmitter, 1).itemID) ? lMItemStacks[2].stackSize
					: 0
					: 0;
			this.lastStructStackSizes = new int[] { scanLeft, scanForward, scanRight };
			
			int[] coords = new int[] { blockCoords[0] + (xPlus? 1 : xMinus? -1 : 0), blockCoords[1], blockCoords[2] + (zPlus? 1 : zMinus? -1 : 0)};
			int[][] lr = getLeftAndRightOf(direction);
			
			for (int i = 1; i <= scanLeft; i++) 
			{
				ArrayList temp;
				temp = generateLine(scanForward,new int[] {coords[0] + i*(lr[0][0]), coords[1], coords[2] + i*(lr[0][2])});
				for (Object block : temp) 
				{
					structLeft.add((int[]) block);
				}
			}
			
			struct = generateLine(scanForward, coords);

			for (int i = 1; i <= scanRight; i++) 
			{
				ArrayList temp;
				temp = generateLine(scanForward, new int[] { coords[0] + i*(lr[1][0]), coords[1], coords[2] + i*(lr[1][2])});
				for (Object block : temp) 
				{
					structRight.add((int[]) block);
				}
			}
			
			for (Object block : structLeft) 
			{
				theStruct.add((int[]) block);
			}
			
			for (Object block : struct) 
			{
				theStruct.add((int[]) block);
			}
			
			for (Object block : structRight) 
			{
				theStruct.add((int[]) block);
			}
			setLayerStructure(theStruct);
		}
	}

	private int[][] getLeftAndRightOf(int direction)
	{
		boolean xPlus = direction == 5;
		boolean xMinus = direction == 4;
		boolean zPlus = direction == 3;
		boolean zMinus = direction == 2;
		int[] l = new int[] {0,0,0};
		int[] r = new int[] {0,0,0};
		
		r[0] = r[0] + (zMinus ? 1 : 0);
		l[0] = l[0] - (zMinus ? 1 : 0);
		r[2] = r[2] + (xPlus ? 1 : 0);
		l[2] = l[2] - (xPlus ? 1 : 0);
		
		r[0] = r[0] + (zPlus ? -1 : 0);
		l[0] = l[0] - (zPlus ? -1 : 0);
		r[2] = r[2] + (xMinus ? -1 : 0);
		l[2] = l[2] - (xMinus ? -1 : 0);
		
		return new int[][] {l,r};
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		int shouldLoad = (int) nbt.getShort("shouldLoad");

		if (shouldLoad == -1) 
		{
			this.channel = (int) nbt.getShort("channel");
			setLayerToMine(nbt.getShort("layerToMine"));
			
			this.processTime = (int) nbt.getShort("processTime");
			this.speedMultiplier = (int) nbt.getShort("speedMultiplier");
			this.blockCost = (int) nbt.getShort("blockCost");
			setBlockCoords(nbt.getIntArray("blockCoords"));
			this.invController.setChest(nbt.getIntArray("chest"));
			this.readFiltersFromNBT(nbt);
			int size = (int) nbt.getShort("layerStructureSize");
			this.filterInclusive = nbt.getBoolean("filterInclusive");
			this.isMining = nbt.getBoolean("isMining");
			readLayerStructFromNBT(nbt, size);

			NBTTagList tagList = nbt.getTagList("Items");
			this.lMItemStacks = new ItemStack[this.getSizeInventory()];

			for (int i = 0; i < tagList.tagCount(); ++i) 
			{
				NBTTagCompound itemStack = (NBTTagCompound) tagList.tagAt(i);
				byte thisByte = itemStack.getByte("Slot");
				
				if (thisByte >= 0 && thisByte < this.lMItemStacks.length) 
				{
					this.lMItemStacks[thisByte] = ItemStack.loadItemStackFromNBT(itemStack);
				}
			}
		}
	}



	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("shouldLoad", (short) -1);
		nbt.setShort("channel", (short) this.channel);
		nbt.setShort("layerToMine", (short) this.getLayerToMine());
		nbt.setShort("processTime", (short) this.processTime);
		nbt.setShort("speedMultiplier", (short) this.speedMultiplier);
		nbt.setShort("blockCost", (short) this.blockCost);
		nbt.setIntArray("blockCoords", this.blockCoords);
		NBTSaver.writeFieldToNBT(nbt, "int[]", this.invController.getChest(), "chest");
		this.writeFiltersToNBT(nbt);
		nbt.setShort("layerStructureSize", (short) (this.layerStructure.size()));
		nbt.setBoolean("filterInclusive", this.filterInclusive);
		nbt.setBoolean("isMining", this.isMining);

		writeLayerStructToNBT(nbt);

		NBTTagList tagList = new NBTTagList();
		for (int var3 = 0; var3 < this.lMItemStacks.length; ++var3) {
			if (this.lMItemStacks[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.lMItemStacks[var3].writeToNBT(var4);
				tagList.appendTag(var4);
			}
		}

		nbt.setTag("Items", tagList);
	}

	private void writeLayerStructToNBT(NBTTagCompound nbt) {
		Integer counter = 0;
		for (Object block : layerStructure) {
			int[] intBlock = (int[]) block;
			nbt.setIntArray("layerStructure" + counter, intBlock);
			counter++;
		}
	}

	private void writeFiltersToNBT(NBTTagCompound nbt) {
		if (filteredIds != null) {
			Integer counter = 0;
			nbt.setInteger("numOfFilteredIds", this.filteredIds.size());
			for (Object ID : filteredIds) {
				Integer intID = (Integer) ID;
				nbt.setInteger("filteredIds" + counter, intID);
				counter++;
			}
		}
	}

	private void readFiltersFromNBT(NBTTagCompound nbt) {
		Integer counter = 0;
		this.filteredIds = new ArrayList<Integer>();
		int size = nbt.getInteger("numOfFilteredIds");
		while (counter < size) {
			filteredIds.add(nbt.getInteger("filteredIds" + counter));
			counter++;
		}
	}

	private boolean isFiltering() {
		boolean isFiltering = false;
		if (this.lMItemStacks[FILTER_SLOT] != null) {
			isFiltering = this.lMItemStacks[FILTER_SLOT].itemID == new ItemStack(EntangleCraftItems.ItemInclusiveFilter, 1).itemID
					|| this.lMItemStacks[FILTER_SLOT].itemID == new ItemStack(EntangleCraftItems.ItemExclusiveFilter, 1).itemID;
		}
		return isFiltering;
	}
	
	private boolean isDestroyFiltering() {
		boolean isFiltering = false;
		if (this.lMItemStacks[10] != null) {
			isFiltering = this.lMItemStacks[FILTER_SLOT].itemID == new ItemStack(EntangleCraftItems.ItemDestroyFilter, 1).itemID
					|| this.lMItemStacks[FILTER_SLOT].itemID == new ItemStack(EntangleCraftItems.ItemDontDestroyFilter, 1).itemID;
		}
		return isFiltering;
	}

	private void readLayerStructFromNBT(NBTTagCompound nbt, int size) {
		this.layerStructure = new ArrayList<int[]>();
		ArrayList<int[]> temp = new ArrayList<int[]>();
		for (int i = 0; i < size; i++) {
			temp.add(nbt.getIntArray("layerStructure" + i));
		}
		this.layerStructure = temp;
	}

	public void recieveUpdateFromServer(INetworkManager network, DataInputStream dataStream, String fieldName) throws IllegalArgumentException,
			IllegalAccessException {
		if (worldObj.isRemote) {
			Class<?> c = this.getClass();
			Field theField = null;
			boolean shouldUpdate = true;
			try {
				theField = c.getDeclaredField(fieldName);
				if (theField != null)
					theField.setAccessible(true);
			} catch (NoSuchFieldException e1) {
				shouldUpdate = false;
				e1.printStackTrace();
			} catch (SecurityException e1) {
				shouldUpdate = false;
				e1.printStackTrace();
			}

			if (shouldUpdate) {

				if (theField.get(this) instanceof Integer) {
					try {
						int fieldValue = dataStream.readInt();
						theField.setInt(this, fieldValue);
						//System.out.println(fieldName + " now equals " + fieldValue + " on the client side");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				else if (theField.get(this) instanceof int[]) {
					try {
						int arrSize = dataStream.readInt();
						int[] fieldValue = new int[arrSize];
						for (int i = 0; i < arrSize; i++) {
							fieldValue[i] = dataStream.readInt();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					;
				}

				else if (fieldName == "filterInclusive") {
					try {
						boolean theValue = dataStream.readBoolean();
						theField.setBoolean(this, theValue);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				else if (fieldName.equals("isMining")) {
					try {
						boolean theValue = dataStream.readBoolean();
						theField.setBoolean(this, theValue);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			else {
			}
		}
	}

	public int getStartInventorySide(ForgeDirection side) {
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 1;
	}

	@Override
	public int getSizeInventory() {
		return this.lMItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return this.lMItemStacks[var1];
	}

	@Override
	public ItemStack decrStackSize(int slot, int decreaseAmount) {
		if (this.lMItemStacks[slot] != null) {
			ItemStack itemStack;

			if (this.lMItemStacks[slot].stackSize <= decreaseAmount) {
				itemStack = this.lMItemStacks[slot];
				this.lMItemStacks[slot] = null;
				return itemStack;
			} else {
				itemStack = this.lMItemStacks[slot].splitStack(decreaseAmount);

				if (this.lMItemStacks[slot].stackSize == 0) {
					this.lMItemStacks[slot] = null;
				}

				return itemStack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {

		if (this.lMItemStacks[slot] != null) {
			ItemStack itemStack = this.lMItemStacks[slot];
			this.lMItemStacks[slot] = null;
			return itemStack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.lMItemStacks[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	public void updateEntity() {

		super.updateEntity();
		if (hasStructureChanged())
			generateLayerStructure();
		boolean mined = false;
		boolean isProcessing = processTime > 0;
		boolean isServer = !worldObj.isRemote;
		{
			if (canSmelt()) 
			{
				++this.processTime;
				int goal = 200 / this.speedMultiplier;

				if (this.isMining == false) 
				{
					this.setIsMining(true);
				}

				if (this.processTime == goal || this.processTime > goal) 
				{
					this.processTime = 0;
					ServerPacketHandler.sendTEFieldUpdate(this, "TileEntityLambdaMiner", "processTime");
					this.smeltItem();

					mined = true;
				}
			} 
			
			else 
			{
				if (isMining)
				{
					setIsMining(false);
				}
				
	            if (isProcessing != this.processTime > 0)
	            {
	            }

			}
		}

		if (mined) 
		{
			this.onInventoryChanged();
		}

	}

	/*
	private void setMetaData(int i) {
		int x, y, z;
		if (this.blockCoords != null) 
		{
			x = this.blockCoords[0];
			y = this.blockCoords[1];
			z = this.blockCoords[2];
		} 
		
		else 
		{
			x = this.xCoord;
			y = this.yCoord;
			z = this.zCoord;
		}
		
		int lightValue = i == 0 ? 0 : 1000;
		this.worldObj.setLightValue(EnumSkyBlock.Block, x, y, z, lightValue);
		this.worldObj.setBlockMetadataWithNotify(x, y, z, i);
	}*/

	public void setIsMining(boolean a) {
		if (!this.worldObj.isRemote)
		{
			
			if (this.isMining != a) 
			{
				this.isMining = a;
				BlockLambdaMiner.updateBlockState(a, worldObj, this.blockCoords[0], this.blockCoords[1], this.blockCoords[2]);
			}
		}
	}

	private void smeltItem() {
		if (!worldObj.isRemote) {
			minerSound = "minerFail";
			float minerSoundPitch = 1F - (((float) this.blockCoords[1] - (float) this.getLayerToMine()) / (float) this.blockCoords[1]) / 4F;
			if (minerSoundPitch > 1F)
				minerSoundPitch = 1F;
			if (minerSoundPitch < 0.05F)
				minerSoundPitch = 0.05F;
			int x, y, z;
			x = this.blockCoords[0];
			y = this.getLayerToMine();
			z = this.blockCoords[2];

			InventoryBasic reward = prepareToMine();
			int counter = 0;
			for (counter = 0; counter < reward.getSizeInventory(); counter++) {
				ItemStack itemStack = reward.getStackInSlot(counter);

				if (itemStack != null) {
					minerSound = itemStack.itemID == new ItemStack(Item.diamond, 1).itemID ? "teleport" : "mineProcess";
					minerSoundPitch = itemStack.itemID == new ItemStack(Item.diamond, 1).itemID ? 1F : minerSoundPitch;
					invController.checkForChest();
					if (invController.getChest() != null) {
						TileEntityChest theChest = invController.getTileEntityChest();
						invController.addStackToInventory(theChest, itemStack);
					}

					else {
						EntityItem e = new EntityItem(this.worldObj, (double) this.blockCoords[0] + 0.5, (double) this.blockCoords[1] + 1.5,
								(double) this.blockCoords[2] + 0.5, itemStack);
						e.dropItem(itemStack.itemID, itemStack.stackSize);
					}

				}
			}
			LambdaSoundHandler.playSound(this.worldObj, new double[] { (double) xCoord, (double) yCoord, (double) zCoord },
					minerSound, this.worldObj.rand.nextFloat() * 0.05F + 0.05F, minerSoundPitch);
			
			LambdaSoundHandler.playSound(this.worldObj, new double[] { (double) xCoord, (double) this.getLayerToMine(), (double) zCoord },
					minerSound, this.worldObj.rand.nextFloat() * 0.2F + 0.7F, minerSoundPitch);
		}
	}

	private boolean isShearable(World world, int blockID, int x, int y, int z)
	{
		if (!(Block.blocksList[blockID] instanceof IShearable))
		{
			return false;
		}
		
		ItemStack itemstack = new ItemStack(Item.shears,1);
		IShearable target = (IShearable)Block.blocksList[blockID];
        return target.isShearable(itemstack, world, x, y, z);
	}
	
	private InventoryBasic prepareToMine() {		
		InventoryBasic inv = new InventoryBasic(null, layerStructure.size());
		for (Object block : this.layerStructure) 
		{
			if (DistanceHandler.getDistance(channel) >= this.blockCost) 
			{
				int[] blockToMine = (int[]) block;
				int blockID = processBlock(this.worldObj, blockToMine[0], this.getLayerToMine(), blockToMine[2]);
				if (blockID != 0) 
				{
					int metadata = this.worldObj.getBlockMetadata(blockToMine[0], blockToMine[1], blockToMine[2]);
					ItemStack result = isShearable
							(this.worldObj, blockID, blockToMine[0], this.getLayerToMine(), blockToMine[2]) 
							? null : invController.getItemStackFromIDAndMetadata(blockID, metadata);

					if (result != null) 
					{
						invController.addStackToInventory(inv, invController.getItemStackFromIDAndMetadata(blockID, metadata));
						DistanceHandler.subtractDistance(channel, this.blockCost);
					}
				}
			} 
			else 
			{
				break;
			}
		}
		if (DistanceHandler.getDistance(channel) >= this.blockCost) {
			this.setLayerToMine(this.getLayerToMine() - 1);
			ServerPacketHandler.sendTEFieldUpdate(this, "TileEntityLambdaMiner", "layerToMine");
		}
		return inv;
	}

	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();

		if (!worldObj.isRemote) 
		{
			// Handling the 3 layer Generation slots
			if (hasStructureChanged()) 
			{
			}

			// Handling the SpeedMultiplier slot
			if (this.lMItemStacks[3] != null) 
			{
				if (this.lMItemStacks[3].itemID == new ItemStack(EntangleCraftItems.ItemInductionCircuit, 1).itemID) {
					setSpeedMultiplier(2);
				} else if (this.lMItemStacks[3].itemID == new ItemStack(EntangleCraftItems.ItemSuperInductionCircuit, 1).itemID) {
					setSpeedMultiplier(5);
				}
			} else setSpeedMultiplier(1);

			// Handling the filter slots
			this.filteredIds = new ArrayList();
			if (isFiltering() || isDestroyFiltering()) 
			{
				this.filterInclusive = this.lMItemStacks[10].itemID == new ItemStack(EntangleCraftItems.ItemInclusiveFilter, 1).itemID;
				for (int i = 4; i < 10; i++) 
				{
					if (this.lMItemStacks[i] != null) 
					{
						this.filteredIds.add(invController.getBlockIDFromItem(lMItemStacks[i].itemID));
					}
				}
			} else this.filteredIds = null;
			
			generateBlockCost();
		}
	}

	private boolean hasStructureChanged() {
		boolean hasChanged = false;
		int scanLeft = lMItemStacks[0] != null ? (lMItemStacks[0].itemID == new ItemStack(EntangleCraftItems.ItemTransmitter, 1).itemID) ? lMItemStacks[0].stackSize
				: 0
				: 0;
		int scanForward = lMItemStacks[1] != null ? (lMItemStacks[1].itemID == new ItemStack(EntangleCraftItems.ItemTransmitter, 1).itemID) ? lMItemStacks[1].stackSize + 1
				: 1
				: 1;
		int scanRight = lMItemStacks[2] != null ? (lMItemStacks[2].itemID == new ItemStack(EntangleCraftItems.ItemTransmitter, 1).itemID) ? lMItemStacks[2].stackSize
				: 0
				: 0;
		int[] temp = new int[] { scanLeft, scanForward, scanRight };
		for (int i = 0; i < 3; i++) {
			hasChanged = hasChanged || temp[i] != this.lastStructStackSizes[i];
		}
		return hasChanged || this.lastStructStackSizes[1] == 0;
	}

	public void setSpeedMultiplier(int i) {
		if (!worldObj.isRemote) {
			this.speedMultiplier = i;
			System.out.println("speed multiplier is now " + i + " on the server side");
			ServerPacketHandler.sendTEFieldUpdate(this, "TileEntityLambdaMiner", "speedMultiplier");
		}
	}

	private void generateBlockCost() {
		if (!this.worldObj.isRemote)
		{
			int cost = this.DEFAULT_COST;
			if (this.isFiltering()) 
			{
				if (this.filterInclusive) 
				{
					cost = 0;
					for (Object id : filteredIds) 
					{
						Integer intID = (Integer) id;
						cost += getInclusiveCost(intID);
					}
				} 
				else 
				{
					cost = 16;
					for (Object id : filteredIds) 
					{
						Integer intID = (Integer) id;
						cost += getExclusiveCost(intID);
					}
				}
			}
			
			else if (this.isDestroyFiltering())
			{
				boolean assumeDestroy = lMItemStacks[FILTER_SLOT].itemID == new ItemStack (EntangleCraftItems.ItemDontDestroyFilter).itemID;
				if (assumeDestroy)
				{
					cost = DEFAULT_COST/4;
					for (Integer id : filteredIds)
					{
						cost += (getInclusiveCost(id))/32;
					}
				}
				else
				{
					cost = 0;
					for (Integer id : filteredIds)
					{
						cost += (getExclusiveCost(id))/32;
					}
				}
			}
			this.blockCost = cost;
			ServerPacketHandler.sendTEFieldUpdate(this, "TileEntityLambdaMiner", "blockCost");
		}
		
	}

	private int getInclusiveCost(int itemID) {
		int cost = 4;
		if (itemID == new ItemStack(Item.diamond, 1).itemID || itemID == Block.oreDiamond.blockID) {
			cost = 1024;
		} else if (itemID == Block.oreIron.blockID || itemID == Block.oreCoal.blockID || itemID == Block.oreRedstone.blockID || itemID == Block.oreGold.blockID
				|| itemID == Block.oreLapis.blockID) {
			cost = 256;
		}
		return cost;
	}

	private int getExclusiveCost(int itemID) {
		int cost = 64;
		if (itemID == new ItemStack(Item.diamond, 1).itemID || itemID == Block.oreDiamond.blockID) {
			cost = 0;
		} else if (itemID == Block.oreIron.blockID || itemID == Block.oreCoal.blockID || itemID == Block.oreRedstone.blockID || itemID == Block.oreGold.blockID
				|| itemID == Block.oreLapis.blockID) {
			cost = 16;
		} else if (itemID == Block.dirt.blockID || itemID == Block.sand.blockID) {
			cost = 128;
		}
		return cost;
	}

	private boolean canMine(int blockID) {
		boolean canMine = true;
		if (this.isFiltering()) 
		{
			if (this.filterInclusive && this.filteredIds != null) 
			{
				canMine = false; // Assume you can't mine it for inclusive
									// filtering
				// Inclusive = 'mine only X' filter
				for (Object id : this.filteredIds) 
				{
					Integer intID = (Integer) id;
					if (intID == blockID) 
					{
						canMine = true;
						break;
					}
				}
			}

			// this part deals with exclusive filters
			else 
			{
				ArrayList<Integer> notMineable = new ArrayList<Integer>();
				notMineable.add(Block.bedrock.blockID);
				if (filteredIds != null) 
				{

					for (Object id : filteredIds) 
					{
						Integer intID = (Integer) id;
						notMineable.add(intID);
					}
				}
				
				for (Object id : notMineable) 
				{
					Integer intID = (Integer) id;
					if (intID == blockID) 
					{
						canMine = false;
						break;
					}
				}
			}
		} 


		return canMine;
	}

	private boolean canDestroy(int blockID)
	{
		boolean canDestroy = true;
		if (isDestroyFiltering())
		{
			boolean assumeDestroy = lMItemStacks[FILTER_SLOT].itemID == new ItemStack(EntangleCraftItems.ItemDestroyFilter, 1).itemID;
			
			if (!assumeDestroy)
			{
				if (filteredIds != null)
				{
					ArrayList<Integer> shouldNotDestroy = new ArrayList<Integer>();
					for (Integer id : filteredIds) 
					{
						shouldNotDestroy.add(id);
					}
					
					for (Integer id : shouldNotDestroy) 
					{
						if (id == blockID) 
						{
							canDestroy = false;
							break;
						}
					}
				}
			}
			
			else
			{
				canDestroy = false;
				if (filteredIds != null)
				{
					ArrayList<Integer> shouldDestroy = new ArrayList<Integer>();
					for (Integer id : filteredIds) 
					{
						shouldDestroy.add(id);
					}
					
					for (Integer id : shouldDestroy) 
					{
						if (id == blockID) 
						{
							canDestroy = true;
							break;
						}
					}
				}
			}
		}
		
		return canDestroy;
	}
	
	private int processBlock(World world, int x, int y, int z) {
		int blockID = 0;
		if (world.blockExists(x, y, z)) 
		{
			blockID = world.getBlockId(x, y, z);
			if (blockID != 0)
			{
				Block theBlock = Block.blocksList[blockID];
				blockID = theBlock != null ? theBlock.idDropped(theBlock.blockID, new Random(), 0) : blockID;
				if (this.isDestroyFiltering())
				{
					if (canDestroy(blockID))
					{
						world.setBlockWithNotify(x, y, z, 0);
						DistanceHandler.subtractDistance(channel, this.blockCost);
						this.minerSound = "destroyProcess";
					}
					
					else this.minerSound = "minerFail";
					blockID = 0;
				}
				
				else if (this.canMine(blockID)) 
				{
					world.setBlockWithNotify(x, y, z, 0);
				}
				
				else
				{
					blockID = 0;
				}
			}
		}
		return blockID;
	}

	private boolean canSmelt() {
		boolean canSmelt = true;
		if (!this.worldObj.isRemote) 
		{
			if (DistanceHandler.getDistance(channel) < this.blockCost)
				canSmelt = false;
			if (this.getLayerToMine() == 0)
				canSmelt = false;
			canSmelt = canSmelt && this.worldObj.isBlockIndirectlyGettingPowered(this.blockCoords[0], this.blockCoords[1], this.blockCoords[2]);
			
			if (canSmelt)
			{
	
			}
			
		} 
		
		else 
		{
			canSmelt = this.isMining;
		}
		return canSmelt;
	}

	public int getCookProgressScaled(int par1) {
		int denominator = 200 / this.speedMultiplier;
		return this.processTime * par1 / denominator;
	}
	
	/**
	 * whenever this TE is freshly sent to a client or marked for update in the world this entire packet will be sent to all tracking player
	 */
	@Override
	public Packet getDescriptionPacket()
	  {
	  NBTTagCompound tileTag = new NBTTagCompound();
	  this.writeToNBT(tileTag);
	  return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tileTag);
	  }

	/**
	 * Called when you receive a TileEntityData packet for the location this
	 * TileEntity is currently in. On the client, the NetworkManager will always
	 * be the remote server. On the server, it will be whomever is responsible for 
	 * sending the packet.
	 * 
	 * @param net The NetworkManager the packet originated from 
	 * @param pkt The data packet
	 */
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	  {
	  this.readFromNBT(pkt.customParam1);
	  }

	@Override
	public String getInvName() {
		return "lMInventory";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {

	}

	@Override
	public void closeChest() {

	}

}
