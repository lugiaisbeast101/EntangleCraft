package entanglecraft.items;

import net.minecraft.src.Block;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemPickaxe;
import net.minecraft.src.ItemTool;

public class ItemShardTool extends ItemTool implements IChanneled{
	
	public int channel = 0;
	private Item[] availableChannels;
	
	public ItemShardTool(int itemID, int baseDamage, EnumToolMaterial par3EnumToolMaterial, Block[] blocksEffectiveAgainst)
	{
		super(itemID, baseDamage, par3EnumToolMaterial, blocksEffectiveAgainst);	
	}

	@Override
	public void setChannel(int i){
		channel = i;
	}
	
	@Override
	public int getChannel(){
		return channel;
	}
	
	@Override
	public void setAvailableChannels(Item[] availableChannels)
	{
		this.availableChannels = availableChannels;
	}
	
	@Override
	public Item incrementChannel(){
		Item is = null;
		
		int x = channel + 1;
		if (x == availableChannels.length) x = 0;
		is = availableChannels[x];

		return is;
	}
	
	public String getTextureFile(){
		return "/lambdaTextures.png";
	}
	
}
