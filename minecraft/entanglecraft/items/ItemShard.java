package entanglecraft.items;

import java.lang.Math;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import entanglecraft.ClientPacketHandler;
import entanglecraft.DistanceHandler;
import entanglecraft.EntangleCraft;
import entanglecraft.ServerPacketHandler;
import entanglecraft.SoundHandling.LambdaSoundHandler;
import entanglecraft.blocks.EntangleCraftBlocks;

public class ItemShard extends Item {
	
	private double imbuedLastDistance = -1;
	private int closerCount = 0;
	private String[] closerSounds = {"closer", "closerAgainOne", "closerAgainTwo", "closerAgainThree", "closerAgainFour", "closerAgainFive", "closerAgainSix"};
	
	private int type;

	public ItemShard(int par1, int type) {
		super(par1);
		setCreativeTab(CreativeTabs.tabMisc);
		this.type = type;
	}

	public String getTextureFile() {
		return "/lambdaTextures.png";
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer thePlayer) {
		
		if (world.isRemote)
		{

		}
		
		else
		{
			if (type == 3) 
			{
				ChunkCoordinates coords = thePlayer.getBedLocation();
				if (coords != null)
				{
					tpScrollTeleport(world, thePlayer);
					return itemStack;
				}
			}
			
			else if (type == 4) 
			{
				this.imbuedShardRespond(world, thePlayer, (int)thePlayer.posX, (int)thePlayer.posY, (int)thePlayer.posZ);
			}
		
		}
		
		return itemStack;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer thePlayer, World world, int x, int y, int z, int side, float i, float j, float k) {
		
		boolean usedItem = false;

		usedItem = true;
		int posX = x;
		int posY = y;
		int posZ = z;
		
		
		if (world.isRemote)
		{
			if (type == 0) 
			{
				MovingObjectPosition movingObject = getMovingObjectPositionFromPlayer(world, thePlayer, true);
				posX = movingObject.blockX;
				posY = movingObject.blockY;
				posZ = movingObject.blockZ;
				ClientPacketHandler.sendShardSpell(posX, posY, posZ, side, type);
			} 
			
			else if (type != 3)
			{
				ClientPacketHandler.sendShardSpell(posX, posY, posZ, side, type);
			}
		}
		
		else if (type == 3) 
		{
			ChunkCoordinates coords = thePlayer.getBedLocation();
			if (coords != null)
			{
				tpScrollTeleport(world, thePlayer);
				par1ItemStack.damageItem(2,thePlayer);
			}
		}

		else if (type == 4) 
		{
			this.imbuedShardRespond(world, thePlayer, x, y, z);
		}
		
		return usedItem;
	}
	
	public static void tpScrollTeleport(World world, EntityPlayer thePlayer) {
		
		boolean travelledThroughSpaceAndTime = false;
		ChunkCoordinates coords = thePlayer.getBedLocation();
		if (coords != null) 
		{
			if (thePlayer.dimension != 0)
			{
				thePlayer.travelToDimension(0);
				travelledThroughSpaceAndTime = true;
			}
			
			ChunkCoordinates theCoords = thePlayer.verifyRespawnCoordinates(world, coords, true);
			theCoords = theCoords != null ? theCoords : coords;
			double expX = thePlayer.posX;
			double expY = thePlayer.posY;
			double expZ = thePlayer.posZ;
			ServerPacketHandler.placePlayer(theCoords.posX, theCoords.posY, theCoords.posZ, world, thePlayer);
			LambdaSoundHandler.playSound(world, new double[] { theCoords.posX + 0.5, theCoords.posY, theCoords.posZ + 0.5 },
					"tpScroll", world.rand.nextFloat() * 0.2F + 0.5F, world.rand.nextFloat() * 0.2F + 0.8F);
			ServerPacketHandler.spawnParticleToClients(new double[] { theCoords.posX + 0.5, theCoords.posY, theCoords.posZ + 0.5 }, "largeexplode");

			double distance = EntangleCraft.getDistance(new double[] { expX, expY, expZ }, new double[] { thePlayer.posX, thePlayer.posY,
					thePlayer.posZ });
			distance = distance < 30 ? 0 : distance < 96 ? (double) (int) Math.log(distance) * 0.7
					: distance < 256 ? (double) (int) Math.log(distance) * 1.5 : (double) (int) Math.log(distance) * 2.5;
			System.out.println("explosion size = " + distance);
			if (distance != 0)
			{
				//sendExplosionToClients(thePlayer, expX, expY, expZ, (float) distance, true);
				if (!travelledThroughSpaceAndTime)
				{
					world.createExplosion(thePlayer, expX, expY, expZ, (float) distance, true);
				}
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
	
	private void imbuedShardRespond(World theWorld, EntityPlayer thePlayer, int x, int y, int z)
	{
		if (!theWorld.isRemote)
		{
			if (DistanceHandler.dungeonCoords != null)
			{
				double[] coords = new double[] {DistanceHandler.dungeonCoords[0], DistanceHandler.dungeonCoords[2]};
				double thisDistance = DistanceHandler.calculateXZDistance(coords, new double[] {x,z});
				if (thisDistance <= this.imbuedLastDistance || this.imbuedLastDistance == -1)
				{
					this.imbuedLastDistance = thisDistance;
					if (thisDistance <= 8)
					{
						LambdaSoundHandler.playSound(theWorld, new double[] {x,y,z}, "foundIt",  theWorld.rand.nextFloat() * 0.2F + 0.5F, 1F);
						thePlayer.addChatMessage("The Dungeon lies below");
					}
					
					else
					{
						LambdaSoundHandler.playSound(theWorld, new double[] {x,y,z}, closerSounds[closerCount],  theWorld.rand.nextFloat() * 0.2F + 0.5F, 1F);
						closerCount = closerCount == (closerSounds.length-1) ? (closerSounds.length-1) : closerCount + 1;
						thePlayer.addChatMessage("Closer");
					}
				}
				
				else
				{
					this.imbuedLastDistance = thisDistance;
					LambdaSoundHandler.playSound(theWorld, new double[] {x,y,z}, "further", theWorld.rand.nextFloat() * 0.2F + 0.5F, theWorld.rand.nextFloat() * 0.05F + 0.95F);
					thePlayer.addChatMessage("Further");
					closerCount = 0;
				}
			}
			
			else
			{
				System.out.println("No Dungeon Found.");
			}
		}
	}

	



}
