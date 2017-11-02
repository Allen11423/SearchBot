package Search.global.record;

import java.util.HashMap;

import XML.Attribute;
import XML.Elements;
import Search.global.record.Settings;
import util.Lib;


public class Data {
	
	public static HashMap<String,Data> users=new HashMap<String,Data>();
	
	public String id;//userID
	private int momCombo=1;
	private int momPoints=0;
	private int momPointsDaily=0;
	private long lastDaily=0;
	private long lastDataCheck=0;//when combos/dailypoints have last been checked
	public Data(String id){
		this.id=id;
	}
	public Data(Elements root){
		this.id=root.getAttribute("id").getValue();
		this.momPoints=Lib.getNumber(root, "points")==-1?0:Lib.getNumber(root, "points");//basic point parsing
		try{
			Elements dailies = root.getChilds("Daily").get(0);
			this.momPointsDaily=Lib.getNumber(dailies, "Dailypoint")==-1?0:Lib.getNumber(dailies, "Dailypoint");
			this.lastDaily=Lib.getNumber(dailies, "Lastdaily")==-1?0:Lib.getNumber(dailies, "Lastdaily");
			this.lastDataCheck=Lib.getNumber(dailies, "DailyCheck")==-1?0:Lib.getNumber(dailies, "DailyCheck");
			this.momCombo=Lib.getNumber(dailies, "DailyCombo")==-1?0:Lib.getNumber(dailies, "DailyCombo");
		}catch(IndexOutOfBoundsException e){}
	}
	public boolean dailyReady(){
		if(lastDaily>Settings.dailyTime){
			return false;
		}
		else{
			return true;
		}
	}
	public void dailyUsed(){
		lastDaily=System.currentTimeMillis();
	}
	private void dataCheck(){
		if(lastDataCheck<Settings.dailyTime){
			momCombo=0;
			momPointsDaily=0;
		}
		else{
			lastDataCheck=System.currentTimeMillis();
		}
	}
	public int getCombo(){
		dataCheck();
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
	public void penalizePoints(int points){
		momPoints-=points;
		momPointsDaily-=points;
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
		
		Elements dailies=new Elements("Daily");
		root.add(dailies);
		
		Elements dailypoint=new Elements("Dailypoint",""+momPointsDaily);
		Elements dailycheck=new Elements("Lastdaily",""+lastDaily);
		Elements dailydcheck=new Elements("DailyCheck",""+lastDataCheck);
		dailies.add(dailypoint);
		dailies.add(dailycheck);
		dailies.add(dailydcheck);
		
		
		root.add(points);
		return root;
	}
}
