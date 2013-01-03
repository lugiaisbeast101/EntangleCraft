package entanglecraft.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemLambda extends Item{

	public ItemLambda(int par1) {
		super(par1);
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	public String getTextureFile(){
		return "/lambdaTextures.png";
	}
}
