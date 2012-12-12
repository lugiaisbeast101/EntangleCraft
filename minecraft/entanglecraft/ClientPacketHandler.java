package entanglecraft;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import entanglecraft.blocks.TileEntityGenericDestination;
import entanglecraft.blocks.TileEntityLambdaMiner;
import entanglecraft.gui.EnumGui;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ClientPacketHandler implements IPacketHandler {

	private void spawnParticleFromPacket(INetworkManager network, DataInputStream dataStream) {
		try {
			String particleName = dataStream.readUTF();
			double posX = dataStream.readDouble();
			double posY = dataStream.readDouble();
			double posZ = dataStream.readDouble();
			ModLoader.getMinecraftInstance().theWorld.spawnParticle(particleName, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void distanceHandlerUpdate(INetworkManager network, DataInputStream dataStream) {
		try {
			int channel = dataStream.readInt();
			int value = dataStream.readInt();
			DistanceHandler.setDistance(channel, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateTileEntityField(INetworkManager network, DataInputStream dataStream) {
		int tileEntityID = -1; // Using EnumGui indexes as ids, don't see why
								// not
		String field = "unknown";
		Integer xPos = null;
		Integer yPos = null;
		Integer zPos = null;
		try {
			field = dataStream.readUTF();
			tileEntityID = dataStream.readInt();
			xPos = dataStream.readInt();
			yPos = dataStream.readInt();
			zPos = dataStream.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		TileEntity te = ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(xPos, yPos, zPos);

		if (xPos != null && yPos != null && zPos != null && te != null) {
			if (tileEntityID == EnumGui.LambdaMiner.getIndex()) {
				TileEntityLambdaMiner teLM = (TileEntityLambdaMiner) te;
				try {
					teLM.recieveUpdateFromServer(network, dataStream, field);
				} catch (Exception e) {
				}
			}

			else if (tileEntityID == EnumGui.GenericDestination.getIndex()) {
				TileEntityGenericDestination teGD = (TileEntityGenericDestination) te;
				try {
					teGD.recieveUpdateFromServer(network, dataStream, field);
				} catch (Exception e) {
				}
			}
		}
	}

	public void playSoundFromPacket(INetworkManager network, DataInputStream dataStream) {
		try {
			String soundName = dataStream.readUTF();
			float vol = dataStream.readFloat();
			float pitch = dataStream.readFloat();
			double posX = dataStream.readDouble();
			double posY = dataStream.readDouble();
			double posZ = dataStream.readDouble();
			ModLoader.getMinecraftInstance().theWorld.playSound(posX, posY, posZ, soundName, vol, pitch);
			System.out.println("Sound packet recieved");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playTPSoundFromPacket(INetworkManager network, DataInputStream dataStream) {
		String playerName = "";
		double[] dest = new double[3];
		try {
			playerName = dataStream.readUTF();
		} catch (Exception e) {
		}

		try {
			dest[0] = dataStream.readDouble();
			dest[1] = dataStream.readDouble();
			dest[2] = dataStream.readDouble();
		} catch (Exception e) {
			ModLoader.getMinecraftInstance().theWorld.getPlayerEntityByName(playerName).addChatMessage("Failed to load destination... Let nado know.");
		}

		boolean shouldPlayPlayerSound = true;
		boolean shouldPlayDestSound = true;
		World world = ModLoader.getMinecraftInstance().theWorld;
		EntityPlayer thisPlayer = ModLoader.getMinecraftInstance().thePlayer;
		EntityPlayer player = world.getPlayerEntityByName(playerName);
		double[] thisPlayerPos = { thisPlayer.posX, thisPlayer.posY, thisPlayer.posZ };
		double[] playerPos = new double[3];
		try {
			playerPos[0] = player.posX;
			playerPos[1] = player.posY;
			playerPos[2] = player.posZ;
		} catch (Exception e) {
			shouldPlayPlayerSound = false;
		}

		if (shouldPlayPlayerSound)
			shouldPlayPlayerSound = EntangleCraft.getDistance(thisPlayerPos, playerPos) < 30.0D;
		if (shouldPlayPlayerSound) {
			world.playSound(player.posX, player.posY, player.posZ, "teleport", world.rand.nextFloat() * 0.2F + 0.3F, world.rand.nextFloat() * 0.2F + 0.8F);
		}
		if ((shouldPlayDestSound) && (EntangleCraft.getDistance(playerPos, dest) > 10.0D)) {
			world.playSound(dest[0], dest[1], dest[2], "teleport", world.rand.nextFloat() * 0.2F + 0.3F, world.rand.nextFloat() * 0.2F + 0.8F);
		}
	}

	public static void sendDeviceToggle() {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream DOS = new DataOutputStream(bytes);
		try {
			DOS.writeInt(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "EntangleCraft";
		packet.data = bytes.toByteArray();
		packet.length = packet.data.length;
		ModLoader.sendPacket(packet);
	}

	@Override
	public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player) {
		EntityPlayer thePlayer = (EntityPlayer) player;
		
		//thePlayer.addChatMessage("CLIENT SIDE PACKET");
		DataInputStream dataStream = new DataInputStream(new java.io.ByteArrayInputStream(packet.data));
		int task = -1;
		try {
			task = dataStream.readInt();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//System.out.println("Recieved packet for task " + task);
		
		if (task == 0)
			playTPSoundFromPacket(network, dataStream);
		else if (task == 1)
			playSoundFromPacket(network, dataStream);
		else if (task == 2)
			updateTileEntityField(network, dataStream);
		else if (task == 3)
			distanceHandlerUpdate(network, dataStream);
		else if (task == 4)
			spawnParticleFromPacket(network, dataStream);
	}
}
