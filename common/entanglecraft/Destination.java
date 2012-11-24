package entanglecraft;

public class Destination {
	public double[] destinationCoords;
	public int[] blockCoords;
	public int channel;
	
	public Destination(double[] destPos, int[] blockPos, int channel){
		this.destinationCoords = destPos;
		this.blockCoords = blockPos;
		this.channel = channel;
	}
	
	public Destination(int[] blockPos, int channel){
		double[] d = {blockPos[0] + 0.5, blockPos[1] + 2.65, blockPos[2] + 0.5};
		this.destinationCoords = d;
		this.blockCoords = blockPos;
		this.channel = channel;
	}
	
	public boolean equals(Object o){
		boolean equality = false;
		boolean destEquality = false;
		boolean blockEquality = false;
		boolean channelEquality = false;
		if (o == null) equality = false;
		else{
			
		if (o instanceof Destination){
			Destination dest = (Destination)o;
			destEquality = ((this.destinationCoords[0] == dest.destinationCoords[0])
						&& (this.destinationCoords[1] == dest.destinationCoords[1]))
								&& (this.destinationCoords[2] == dest.destinationCoords[2]);
				
			blockEquality = this.blockCoords[0] == dest.blockCoords[0]
					&& this.blockCoords[1] == dest.blockCoords[1]
							&& this.blockCoords[2] == dest.blockCoords[2];
			channelEquality = (this.channel == dest.channel);
			
			equality = destEquality && blockEquality && channelEquality;
		}
		}
		return equality;
	}
	
	public String toString(){
		String s;
		s = "Destination at Block coordinates " + this.blockCoords[0] + " " + this.blockCoords[1] + " " + this.blockCoords[2];
		return s;
	}
}
