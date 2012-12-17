package entanglecraft.items;

import java.lang.Math;

import net.minecraft.src.Block;
import net.minecraft.src.BlockTorch;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
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
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer thePlayer) {
		System.out.println("onItemRightClick got called");
		if (!world.isRemote) {
			if (type == 3) {
				ChunkCoordinates coords = thePlayer.getBedLocation();
				if (coords != null) {
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
						world.createExplosion(thePlayer, expX, expY, expZ, (float) distance, true);
					else
						ServerPacketHandler.spawnParticleToClients(new double[] { expX, expY, expZ }, "largeexplode");
					LambdaSoundHandler.playSound(world, new double[] { expX, expY, expZ },"tpScroll",  world.rand.nextFloat() * 0.2F + 0.5F,
							world.rand.nextFloat() * 0.2F + 0.8F);
					par1ItemStack.stackSize--;
				}
			}
			
			else if (type == 4) 
			{
				this.imbuedShardRespond(world, thePlayer, (int)thePlayer.posX, (int)thePlayer.posY, (int)thePlayer.posZ);
			}
			
		}
		return par1ItemStack;
	}

	@Override
	public boolean onItemUseFirst(ItemStack par1ItemStack, EntityPlayer thePlayer, World world, int x, int y, int z, int side) {
		
		boolean shouldDamage = true;
		if (type == 0) 
		{
			MovingObjectPosition movingObject = getMovingObjectPositionFromPlayer(world, thePlayer, true);
			ClientPacketHandler.sendShardSpell(movingObject.blockX, movingObject.blockY, movingObject.blockZ, side, 0);
		} 
		
		else if (type == 1) 
		{
			ClientPacketHandler.sendShardSpell(x, y, z, side, 1);
		}

		else if (type == 2) 
		{
			ClientPacketHandler.sendShardSpell(x, y, z, side, 2);

		}

		else if (type == 3) {
			if (!world.isRemote)
			{
				ChunkCoordinates coords = thePlayer.getBedLocation();
				if (coords != null) {
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
						world.createExplosion(thePlayer, expX, expY, expZ, (float) distance, true);
					else
						ServerPacketHandler.spawnParticleToClients(new double[] { expX, expY, expZ }, "largeexplode");
					LambdaSoundHandler.playSound(world, new double[] { expX, expY, expZ },"tpScroll",  world.rand.nextFloat() * 0.2F + 0.5F,
							world.rand.nextFloat() * 0.2F + 0.8F);
					par1ItemStack.stackSize--;
				} 
				
				else
				{
					shouldDamage = false;
				}
			}
		}
		
		else if (type == 4) 
		{
			this.imbuedShardRespond(world, thePlayer, x, y, z);
		}
		
		int damageAmount = type == 3 ? 2 : 1;
		if (shouldDamage)
			thePlayer.getCurrentEquippedItem().damageItem(damageAmount, thePlayer);

		return true;
	}

	private void placePlayer(int posX, int posY, int posZ, World theWorld, EntityPlayer thePlayer) {
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
