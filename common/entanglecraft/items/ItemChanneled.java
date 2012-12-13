package entanglecraft.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemChanneled extends Item implements IChanneled{
	
	public int channel = 0;
	private Item[] availableChannels;
	
	public ItemChanneled(int par1) {
		super(par1);
		setCreativeTab(CreativeTabs.tabMisc);
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
