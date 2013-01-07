package entanglecraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import net.minecraft.server.MinecraftServer;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import entanglecraft.SoundHandling.LambdaSoundHandler;
import entanglecraft.blocks.EntangleCraftBlocks;
import entanglecraft.blocks.TileEntityGenericDestination;
import entanglecraft.blocks.TileEntityLambdaMiner;
import entanglecraft.gui.EnumGui;
import entanglecraft.items.EntangleCraftItems;
import entanglecraft.items.IChanneled;
import entanglecraft.items.ItemChanneled;
import entanglecraft.items.ItemDevice;
import entanglecraft.items.ItemShardPick;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ServerPacketHandler implements IPacketHandler {

	public static void playTPSoundToClients(EntityPlayer player, double[] dest, String sound) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream DOS = new DataOutputStream(bytes);
		try {
			DOS.writeInt(0); // 0 for TP sound packet
			DOS.writeUTF(player.username);
			DOS.writeUTF(sound);
			DOS.writeDouble(dest[0]);
			DOS.writeDouble(dest[1]);
			DOS.writeDouble(dest[2]);
		} catch (IOException e) {
			e.printStackTrace();
		}

		sendAPacket(new Packet250CustomPayload(), bytes);
	}
	
	public static void playSoundToClients(double[] dest, float volume, float pitch, String sound) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream DOS = new DataOutputStream(bytes);
		try {
			DOS.writeInt(1); // 1 for sound packet
			DOS.writeUTF(sound);
			DOS.writeFloat(volume);
			DOS.writeFloat(pitch);
			DOS.writeDouble(dest[0]);
			DOS.writeDouble(dest[1]);
			DOS.writeDouble(dest[2]);
		} catch (IOException e) {
			e.printStackTrace();
		}

		sendAPacket(new Packet250CustomPayload(), bytes);
	}

	public static void spawnParticleToClients(double[] dest, String particle) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream DOS = new DataOutputStream(bytes);
		try {
			DOS.writeInt(4); // 4 for particle packet
			DOS.writeUTF(particle);
			DOS.writeDouble(dest[0]);
			DOS.writeDouble(dest[1]);
			DOS.writeDouble(dest[2]);
		} catch (IOException e) {
			e.printStackTrace();
		}

		sendAPacket(new Packet250CustomPayload(), bytes);
	}

	public static void sendTEFieldUpdate(TileEntity te, String tileEntityName, String field) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream DOS = new DataOutputStream(bytes);
		Class<?> c;
		try {
			if (tileEntityName == "TileEntityLambdaMiner") {
				TileEntityLambdaMiner teLM = (TileEntityLambdaMiner) te;
				DOS.writeInt(2); // TEFieldUpdate packet id!
				DOS.writeUTF(field);
				DOS.writeInt(EnumGui.LambdaMiner.getIndex());
				DOS.writeInt(teLM.xCoord);
				DOS.writeInt(teLM.yCoord);
				DOS.writeInt(teLM.zCoord);
				c = teLM.getClass();
				Field theField = c.getDeclaredField(field);
				writeFieldToOut(DOS, teLM, theField);
			} else if (tileEntityName == "TileEntityGenericDestination") {
				TileEntityGenericDestination teGD = (TileEntityGenericDestination) te;
				DOS.writeInt(2); // TEFieldUpdate packet id!
				DOS.writeUTF(field);
				DOS.writeInt(EnumGui.GenericDestination.getIndex());
				DOS.writeInt(teGD.xCoord);
				DOS.writeInt(teGD.yCoord);
				DOS.writeInt(teGD.zCoord);
				c = teGD.getClass();
				Field theField = c.getDeclaredField(field);
				writeFieldToOut(DOS, teGD, theField);
			}
			sendAPacket(new Packet250CustomPayload(), bytes);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeFieldToOut(DataOutputStream DOS, Object o, Field theField) {
		theField.setAccessible(true);
		try {
			if (theField.get(o) instanceof Integer) {
				int fieldValue = theField.getInt(o);
				DOS.writeInt(fieldValue);
			} else if (theField.get(o) instanceof Boolean) {
				boolean fieldValue = theField.getBoolean(o);
				DOS.writeBoolean(fieldValue);
			} else if (theField.get(o) instanceof int[]) {
				int[] fieldValue = (int[]) theField.get(o);
				int arrSize = fieldValue.length;
				DOS.writeInt(arrSize);
				for (Object element : fieldValue) {
					Integer theElement = (Integer) element;
					DOS.writeInt(theElement);
				}
			}
			DOS.flush();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendAPacket(Packet250CustomPayload packet, ByteArrayOutputStream bytes) {
		packet.channel = "EntangleCraft";
		packet.data = bytes.toByteArray();
		packet.length = packet.data.length;
		MinecraftServer server = ModLoader.getMinecraftServerInstance();
		server.getConfigurationManager().sendPacketToAllPlayers(packet);
	}

	@Override
	public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player) {
		EntityPlayer thePlayer = (EntityPlayer) player;
		MinecraftServer server = ModLoader.getMinecraftServerInstance();
		
		if (!thePlayer.worldObj.isRemote) {
			//thePlayer.addChatMessage("SERVER PACKET");
			int ID = -1;
			DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			try 
			{
				ID = dataStream.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String task = this.getTaskFromEnum(ID);
			if (task == "increment") 
			{
				// Toggle lambdaDevice channel
				this.toggleChannel(thePlayer);
			}
			
			else if (task == "shardSpell")
			{
				Integer x = null;
				Integer y = null;
				Integer z = null;
				Integer side = null;
				Integer spell = null;
				try
				{
					spell = dataStream.readInt();
					x = dataStream.readInt();
					y = dataStream.readInt();
					z = dataStream.readInt();
					side = dataStream.readInt();
					
				}
				
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (spell != null)
				{
					if (spell == 0) this.freeze(server.worldServerForDimension(0), thePlayer, x, y, z, side);
					else if (spell == 1) this.ignite(server.worldServerForDimension(0), thePlayer, x, y, z, side);
					else if (spell == 2) this.placeTorch(server.worldServerForDimension(0), thePlayer, x, y, z, side);
					else if (spell == 3) this.tpScrollTeleport(server.worldServerForDimension(0), thePlayer);
					else if (spell == 5) this.shardPickMineBlock(server.worldServerForDimension(0), thePlayer, x, y, z, side);
				}
			}
		}

	}
	
	public void sendExplosionToClients(EntityPlayer thePlayer, double x, double y, double z, float size , boolean isSmokey){
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream DOS = new DataOutputStream(bytes);
		
		String id = thePlayer.username;
		try 
		{
			DOS.writeInt(5); // 5 for explosion packet
			DOS.writeUTF(id);
			DOS.writeDouble(x);
			DOS.writeDouble(y);
			DOS.writeDouble(z);
			DOS.writeFloat(size);
			DOS.writeBoolean(isSmokey);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		sendAPacket(new Packet250CustomPayload(), bytes);
	}
	

	private void toggleChannel(EntityPlayer thePlayer) {
		ItemStack itemInUse = thePlayer.getCurrentEquippedItem();
		InventoryPlayer inv = thePlayer.inventory;
		int x = itemInUse.stackSize;
		inv.setInventorySlotContents(inv.currentItem, new ItemStack(((IChanneled) itemInUse.getItem()).incrementChannel(), x));
		System.out.println("Changed Channel");
	}

	private String getTaskFromEnum(int i) {
		if (i == 0)
			return "increment";
		if (i == 1)
			return "shardSpell";
		else
			return "unknown";
	}

	
	private boolean ignite(World theWorld, EntityPlayer thePlayer, Integer x, Integer y, Integer z, Integer side) {
		{
			if (x != null && y != null && z != null && side != null)
			{
				if (side == 0) 
				{
					--y;
				}
				if (side == 1) 
				{
					++y;
				}
				if (side == 2) 
				{
					--z;
				}
				if (side == 3) 
				{
					++z;
				}
				if (side == 4) 
				{
					--x;
				}
				if (side == 5) 
				{
					++x;
				}
	
				/*
				 * if (!thePlayer.canPlayerEdit(x, y, z)) { return false; } else {
				 */
				LambdaSoundHandler.playSound(theWorld, new double[] {(double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D}, "fire.ignite", 1.0F, theWorld.rand.nextFloat() * 0.4F + 0.8F);
				theWorld.setBlockWithNotify(x, y, z, Block.fire.blockID);
				thePlayer.getCurrentEquippedItem().damageItem(1, thePlayer);
			}
			return true;
			// }
		}
	}
	
	private boolean shardPickMineBlock(World world, EntityPlayer thePlayer, int x, int y, int z, int side) {
		
		int blockID = world.getBlockId(x, y, z);

		int metadata = world.getBlockMetadata(x, y, z);
		double[] coords = new double[] {x + 0.5D, y + 0.5D, z + 0.5D};
		ItemStack item = thePlayer.getCurrentEquippedItem();
		float soundPitch = 1F;
		
		Item[] shardPicks = new Item[] {EntangleCraftItems.ItemShardPickG, EntangleCraftItems.ItemShardPickR, EntangleCraftItems.ItemShardPickB, EntangleCraftItems.ItemShardPickY};
		
		boolean acceptable = item.getItem() instanceof ItemShardPick;
		
		if (acceptable)
		{
			int channel = ((ItemShardPick)item.getItem()).channel;
			
			Destination closestDest = EntangleCraft.closestDestToCoord(coords, EntangleCraft.channelDests[channel]);
		
			if (closestDest != null)
			{
				//double[] doubleCoords = new double[] {(double)x,(double)y,(double)z};
				//double cost = DistanceHandler.calculate3dDistance(doubleCoords, closestDest.destinationCoords);
				
				//if (DistanceHandler.getDistance(channel) >= cost)
				//{
					TileEntityGenericDestination teGD = (TileEntityGenericDestination)world.getBlockTileEntity(closestDest.blockCoords[0], closestDest.blockCoords[1], closestDest.blockCoords[2]);
			
					ItemStack itemStack = InventoryController.getItemStackFromIDAndMetadata(blockID, metadata);
					
					if (itemStack != null)
					{
						boolean hasChest = true;
						
						hasChest = hasChest && teGD.invController != null;
						if (hasChest)
						{
							teGD.invController.checkForChest();
							hasChest = hasChest && teGD.invController.getChest() != null;					
						}
						if (hasChest)
						{
							teGD.invController.addStackToInventory(teGD.invController.getTileEntityChest(), itemStack);
						}
						
						else
						{
							EntityItem e = new EntityItem(teGD.worldObj, (double) teGD.blockCoords[0] + 0.5, (double) teGD.blockCoords[1] + 1.5,
									(double) teGD.blockCoords[2] + 0.5, itemStack);
							e.dropItem(itemStack.itemID, itemStack.stackSize);
						}
						
						
						soundPitch = teGD.teleportsEarned < 16 ? 0.8F + (0.2F * (teGD.teleportsEarned/16F)) : soundPitch;
						LambdaSoundHandler.playSound(world, coords, "shardMineProcess", world.rand.nextFloat() * 0.1F + 0.9F, soundPitch);
						//DistanceHandler.subtractDistance(channel, cost);
						teGD.changeTeleportsEarned(-1);
						world.setBlockWithNotify(x, y, z, 0);
					}
				//}
			}
		}
		return true;
	}


	
	private boolean placeTorch(World theWorld, EntityPlayer thePlayer, int x, int y, int z, int side) {
		if (!theWorld.isRemote)
		{
			if (side == 0) {
				--y;
			}

			if (side == 1) {
				++y;
			}

			if (side == 2) {
				--z;
			}

			if (side == 3) {
				++z;
			}

			if (side == 4) {
				--x;
			}

			if (side == 5) {
				++x;
			}

			if (theWorld.getBlockId(x, y, z) == Block.waterStill.blockID) 
			{
				theWorld.setBlockWithNotify(x, y, z, EntangleCraftBlocks.BlockLitWater.blockID);
				thePlayer.getCurrentEquippedItem().damageItem(1, thePlayer);
			} 
			
			else if (theWorld.getBlockId(x, y, z) != EntangleCraftBlocks.BlockLitWater.blockID) 
			{
				theWorld.setBlockWithNotify(x, y, z, EntangleCraftBlocks.BlockGlowTorch.blockID);
				thePlayer.getCurrentEquippedItem().damageItem(1, thePlayer);
				
			}

			
		}

		return true;
	}
	
	private static void placePlayer(int posX, int posY, int posZ, World theWorld, EntityPlayer thePlayer) {
		if (!theWorld.isRemote) {
			while (posY > 0.0D) {
				thePlayer.setPosition(posX, posY, posZ);
				if (thePlayer.worldObj.getCollidingBoundingBoxes(thePlayer, thePlayer.boundingBox).size() == 0) {
					break;
				}

				++posY;
			}
			thePlayer.setPositionAndUpdate(posX + 0.5, posY, posZ + 0.5);
			thePlayer.motionX = thePlayer.motionY = thePlayer.motionZ = 0.0D;
		}
	}
	
	
	public static void tpScrollTeleport(World world, EntityPlayer thePlayer) {
		
		ChunkCoordinates coords = thePlayer.getBedLocation();
		if (coords != null) 
		{
			ChunkCoordinates theCoords = thePlayer.verifyRespawnCoordinates(world, coords, true);
			theCoords = theCoords != null ? theCoords : coords;
			double expX = thePlayer.posX;
			double expY = thePlayer.posY;
			double expZ = thePlayer.posZ;
			placePlayer(theCoords.posX, theCoords.posY, theCoords.posZ, world, thePlayer);
			LambdaSoundHandler.playSound(world, new double[] { theCoords.posX + 0.5, theCoords.posY, theCoords.posZ + 0.5 },
					"tpScroll", world.rand.nextFloat() * 0.2F + 0.5F, world.rand.nextFloat() * 0.2F + 0.8F);
			ServerPacketHandler.spawnParticleToClients(new double[] { theCoords.posX + 0.5, theCoords.posY, theCoords.posZ + 0.5 }, "largeexplode");

			double distance = EntangleCraft.getDistance(new double[] { expX, expY, expZ }, new double[] { thePlayer.posX, thePlayer.posY,
					thePlayer.posZ });
			distance = distance < 30 ? 0 : distance < 96 ? (double) (int) Math.log(distance) * 0.7
					: distance < 256 ? (double) (int) Math.log(distance) * 1.5 : (double) (int) Math.log(distance) * 2.5;

			if (distance != 0)
			{
				//sendExplosionToClients(thePlayer, expX, expY, expZ, (float) distance, true);
				world.createExplosion(thePlayer, expX, expY, expZ, (float) distance, true);
			}
			else
			{
				ServerPacketHandler.spawnParticleToClients(new double[] { expX, expY, expZ }, "largeexplode");
			}
			LambdaSoundHandler.playSound(world, new double[] { expX, expY, expZ },"tpScroll",  world.rand.nextFloat() * 0.2F + 0.5F,
					world.rand.nextFloat() * 0.2F + 0.8F);
			
			
			ItemStack iS = thePlayer.getCurrentEquippedItem();
			if (iS != null) iS.damageItem(2, thePlayer);
		}
		
	}
	
	private boolean freeze(World world, EntityPlayer thePlayer, int x, int y, int z, int side)
	{
		
		if (world.getBlockId(x, y, z) == Block.waterStill.blockID) {
			world.setBlockWithNotify(x, y, z, Block.ice.blockID);
			LambdaSoundHandler.playSound(world, new double[] {(double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D}, "icePoof", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
			thePlayer.getCurrentEquippedItem().damageItem(1, thePlayer);
		}
		return true;
	}
}
