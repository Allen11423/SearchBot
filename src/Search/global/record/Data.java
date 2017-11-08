package Search.global.record;

import java.util.HashMap;

import XML.Attribute;
import XML.Elements;
import Search.global.record.Settings;
import Search.util.tracking.PointableItem;
import util.Lib;


public class Data implements PointableItem{
	
	public static HashMap<String,Data> users=new HashMap<String,Data>();
	
	public String id;//userID
	private int momCombo=1;
	private int momPoints=100;
	private int momPointsDaily=0;
	private int pointStreak=0;
	private long lastDaily=0;
	private long lastDataCheck=0;//when combos/dailypoints have last been checked
	public Data(String id){
		this.id=id;
	}
	public Data(Elements root){
		this.id=root.getAttribute("id").getValue();
		this.momPoints=Lib.getNumber(root, "points")==-1?100:Lib.getNumber(root, "points");//basic point parsing
		try{
			Elements dailies = root.getChilds("Daily").get(0);
			this.momPointsDaily=Lib.getNumber(dailies, "Dailypoint")==-1?0:Lib.getNumber(dailies, "Dailypoint");
			this.lastDaily=Lib.getLong(dailies, "Lastdaily")==-1?0:Lib.getLong(dailies, "Lastdaily");
			this.lastDataCheck=Lib.getLong(dailies, "DailyCheck")==-1?0:Lib.getLong(dailies, "DailyCheck");
			this.momCombo=Lib.getNumber(dailies, "DailyCombo")==-1?0:Lib.getNumber(dailies, "DailyCombo");
			this.pointStreak=Lib.getNumber(dailies, "DailyStreak")==-1?0:Lib.getNumber(dailies, "DailyStreak");
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
	//resets everything
	public Data resetPoints(){
		momPoints=100;
		momPointsDaily=0;
		momCombo=1;
		lastDaily=0;
		lastDataCheck=0;
		pointStreak=0;
		return this;
	}
	public void daily(){
		lastDaily=System.currentTimeMillis();
		addPoints(10);//more secure way of adding points
	}
	private void dataCheck(){
		if(lastDataCheck<Settings.dailyTime){
			momCombo=1;
			momPointsDaily=0;
			pointStreak=0;
		}
		lastDataCheck=System.currentTimeMillis();
	}
	public int getCombo(){
		dataCheck();
		return momCombo;
	}
	/**
	 * 
	 * @return if combo was incremented
	 */
	public boolean comboAdded(){
		dataCheck();
		if(momCombo==5)return false;
		momCombo++;
		return true;
	}
	public void setCombo(int combo){
		this.momCombo=combo;
		if(momCombo>5){
			momCombo=5;
		}
	}
	public int getStreak(){
		return pointStreak;
	}
	public void setStreak(int streak){
		pointStreak=streak;
	}
	/**
	 * 
	 * @param points
	 * @return if combo has increased
	 */
	public void addPoints(int points){
		dataCheck();
		momPoints+=points*momCombo;
		momPointsDaily+=points*momCombo;
	}
	public int getDailyPoints(){
		dataCheck();
		return momPointsDaily;
	}
	//overrides for leaderborads
	public String getIdentifier(){
		return id;
	}
	public int getPointValue(){
		return getPoints();
	}
	public int getPoints(){
		dataCheck();
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
		Elements dailyCombo=new Elements("DailyCombo",""+momCombo);
		Elements dailyStreak=new Elements("DailyStreak",""+pointStreak);
		dailies.add(dailypoint);
		dailies.add(dailycheck);
		dailies.add(dailydcheck);
		dailies.add(dailyCombo);
		dailies.add(dailyStreak);
		
		
		root.add(points);
		return root;
	}
}
