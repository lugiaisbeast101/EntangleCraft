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
		System.out.println("onItemRightClick itemShard was called \n");
		
			if (type == 3) 
			{
				ChunkCoordinates coords = thePlayer.getBedLocation();
				if (coords != null)
				{
					ClientPacketHandler.sendShardSpell(-1, -1, -1, -1, type);
					return itemStack;
				}
			}
			
			else if (!world.isRemote) 
			{
				if (type == 4) 
				{
					this.imbuedShardRespond(world, thePlayer, (int)thePlayer.posX, (int)thePlayer.posY, (int)thePlayer.posZ);
				}
			
			}
		
		else if (type == 3 && thePlayer.getBedLocation() != null)
		{
			itemStack.damageItem(2, thePlayer);
			return itemStack;
		}
		
		return itemStack;
	}

	@Override
	public boolean onItemUseFirst(ItemStack par1ItemStack, EntityPlayer thePlayer, World world, int x, int y, int z, int side, float i, float j, float k) {
		
		boolean usedItem = false;
		System.out.println("onItemUseFirst itemShard was called \n");
		if (!world.blockHasTileEntity(x, y, z))
		{
			usedItem = true;
			int posX = x;
			int posY = y;
			int posZ = z;
			
			if (type == 0) 
			{
				MovingObjectPosition movingObject = getMovingObjectPositionFromPlayer(world, thePlayer, true);
				posX = movingObject.blockX;
				posY = movingObject.blockY;
				posZ = movingObject.blockZ;
			} 
	
			else if (type == 3) {
				ChunkCoordinates coords = thePlayer.getBedLocation();
				
				if (coords != null)
				{
					ClientPacketHandler.sendShardSpell(posX, posY, posZ, side, type);
					par1ItemStack.damageItem(2,thePlayer);
				}
			}
			
			else if (type == 4) 
			{
				this.imbuedShardRespond(world, thePlayer, x, y, z);
			}
			
			else
			{
				ClientPacketHandler.sendShardSpell(posX, posY, posZ, side, type);
			}
		}
		
		return usedItem;
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
