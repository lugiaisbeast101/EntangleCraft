package entanglecraft.gui;

public enum EnumGui {
	GenericDestination(0), LambdaMiner(1), PowerHub(2);
	
	
	private int index;
	private EnumGui(int i){
		index = i;
	}
	
	public int getIndex(){
		return index;
	}
}
