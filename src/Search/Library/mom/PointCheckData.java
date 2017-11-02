package Search.Library.mom;

import Search.util.rng.RandomLibs;

public enum PointCheckData {
	M1("%userMention% got %dpoint% today for an all time total of %point% mom points!");
	
	private String msg;
	PointCheckData(String msg){
		this.msg=msg;
	}
	public String getMsg(){
		return msg;
	}
	public static PointCheckData getDefaults(){
		return RandomLibs.SelectRandom(PointCheckData.values());
	}
	public String toString(){
		return msg;
	}
}
