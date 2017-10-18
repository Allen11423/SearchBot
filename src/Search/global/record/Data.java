package Search.global.record;

import java.util.HashMap;

import XML.Attribute;
import XML.Elements;
import util.Lib;


public class Data {
	
	public static HashMap<String,Data> users=new HashMap<String,Data>();
	
	public String id;//userID
	private int momCombo=1;
	private int momPoints=0;
	private int momPointsDaily=0;
	public Data(String id){
		this.id=id;
	}
	public Data(Elements root){
		this.id=root.getAttribute("id").getValue();
		this.momPoints=Lib.getNumber(root, "points")==-1?0:Lib.getNumber(root, "points");//basic point parsing
	}
	public int getCombo(){
		return momCombo;
	}
	public void comboAdded(){
		if(momCombo==5)return;
		momCombo++;
	}
	public void addPoints(int points){
		momPoints+=points*momCombo;
		momPointsDaily+=points*momCombo;
	}
	public int getDailyPoints(){
		return momPointsDaily;
	}
	public int getPoints(){
		return momPoints;
	}
	public Elements parseToElements() {
		Elements root=new Elements("user");
		root.getAttributes().add(new Attribute("id",id));
		Elements points=new Elements("points");
		points.setText(""+momPoints);
		root.add(points);
		return root;
	}
}
