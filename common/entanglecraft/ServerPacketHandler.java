package entanglecraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import entanglecraft.SoundHandling.LambdaSoundHandler;
import entanglecraft.blocks.EntangleCraftBlocks;
import entanglecraft.blocks.TileEntityGenericDestination;
import entanglecraft.blocks.TileEntityLambdaMiner;
import entanglecraft.gui.EnumGui;
import entanglecraft.items.EntangleCraftItems;
import entanglecraft.items.IChanneled;
import entanglecraft.items.ItemChanneled;
import entanglecraft.items.ItemDevice;
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
					if (spell == 1) this.ignite(server.worldServerForDimension(0), thePlayer, x, y, z, side);
					else if (spell == 2) this.placeTorch(server.worldServerForDimension(0), thePlayer, x, y, z, side);
				}
			}
		}

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
			}
			return true;
			// }
		}
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
			/*
			 * if (!thePlayer.canPlayerEdit(x, y, z)) { return false; }
			 * 
			 * else {
			 */
			if (theWorld.getBlockId(x, y, z) == Block.waterStill.blockID) {
				theWorld.setBlockWithNotify(x, y, z, EntangleCraftBlocks.BlockLitWater.blockID);
			} else if (theWorld.getBlockId(x, y, z) != EntangleCraftBlocks.BlockLitWater.blockID) {
				theWorld.setBlockWithNotify(x, y, z, EntangleCraftBlocks.BlockGlowTorch.blockID);
			}

			/*
			 * }
			 */
		}

		return true;
	}
	
	private boolean freeze(World world, EntityPlayer thePlayer, int x, int y, int z, int side)
	{
		
		if (world.getBlockId(x, y, z) == Block.waterStill.blockID) {
			world.setBlockWithNotify(x, y, z, Block.ice.blockID);
			LambdaSoundHandler.playSound(world, new double[] {(double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D}, "icePoof", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
		}
		return true;
	}
}
