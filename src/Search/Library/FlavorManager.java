package Search.Library;

import java.util.Collection;
import java.util.HashMap;

import net.dv8tion.jda.core.entities.Guild;

//class to manage the various flavor text used, to enable others to add whatever to the bot's flavor text
public class FlavorManager {
	private static HashMap<String,Flavor> flavors=new HashMap<String,Flavor>();
	//setup of flavor text basically loading from the static maps, from the data file etc. Call before SaveSystem  
	public static void setup(){
		for(FlavorDic d:FlavorDic.values()){
			Flavor f= new Flavor(d.nameID);
			f.addAlltoGlobal(d.values);
			flavors.put(d.nameID, f);
		}
	}
	public static String addFlavorString(String nameID, String id, String flavor){
		return flavors.get(nameID).addPrivate(flavor, id);
	}
	public static void addFlavor(String nameID, Flavor flavor){
		flavors.put(nameID, flavor);//for loading from file
	}
	//get a random one for the specific one
	public static String getRand(String Flavor,Guild guild){
		if(flavors.containsKey(Flavor)){
			return flavors.get(Flavor).aValue(guild.getId());
		}
		else{//if flavor doesn't exist, should've been set up in the setup portion
			return "error";
		}
	}
	public static Collection<Flavor> getFlavors(){
		return flavors.values();
	}
}
