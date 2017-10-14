package Search.global.record;

import java.util.HashMap;


public class Data {
	
	public static HashMap<String,Data> users=new HashMap<String,Data>();
	
	public String id;//userID
	private int momCombo=1;
	private int momPoints=0;

	public Data(String id){
		this.id=id;
	}
	public int getCombo(){
		return momCombo;
	}
	public void comboAdded(){
		momCombo++;
	}
	public void addPoints(int points){
		momPoints+=points*momCombo;
	}
	public int getPoints(){
		return momPoints;
	}
}
