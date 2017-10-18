package Search.global.record;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Search.global.Main;
import Search.global.record.Log;
import Search.global.record.Settings;
import XML.Attribute;
import XML.Elements;
import XML.XMLStAXFile;
import Search.googleutil.drive.DataEnum;
import Search.googleutil.drive.DriveFile;
import Search.googleutil.drive.DriveManager;
import Search.global.record.Data;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
/**
 * Class containing all the static methods relating to saving/loading data for the bot
 * @author Allen
 *
 */
public class SaveSystem {
	/**
	 * loads basic data
	 */
	public static void setup(){
		if(!new File(Settings.dataSource).exists()){
			List<Guild> guilds=Main.jda.getGuilds();
			Elements root=new Elements("SearchBot");
			for(Guild g:guilds){
				root.add(new Settings(g.getId()).parseToElements());
			}
			XMLStAXFile file=new XMLStAXFile(new File(Settings.dataSource));
			file.writeXMLFile();
			file.startWriter();
			file.writeElement(root);
			file.endWriter();
		}
		load();
		buildLeaderBoards();
	}
	private static void buildLeaderBoards(){
		for(String s: Data.users.keySet()){
			addToLeaderBoards(Data.users.get(s));//should automatically sort things, first in list is largest
		}
	}
	private static void addToLeaderBoards(Data user){
		for(int i=0;i<(Settings.momLeaders.size()<10?Settings.momLeaders.size():10);i++){
			if(Settings.momLeaders.get(i).getPoints()<=user.getPoints()){
				Settings.momLeaders.add(i, user);
				break;
			}
		}
		//should only go over by 1 at max
		if(Settings.momLeaders.size()>10){
			Settings.momLeaders.remove(10);//remove last
		}
		if(Settings.momLeaders.size()==0){
			Settings.momLeaders.add(user);
		}
	}
	/**
	 * Loads saved data from file
	 */
	public static void load(){
		loadGuilds();
		XMLStAXFile file=new XMLStAXFile(new File(Settings.dataSource));
		file.readXMLFile();
		ArrayList<Elements> users=file.parseToElements("user");
		for(Elements e:users){
			try{
				Data.users.put(e.getAttribute("id").getValue(), new Data(e));
			}catch(Exception e2){
				Log.log("ERROR", "error putting user(likely missing id attribute)"+e);
				Log.logError(e2);
			}
		}
		file.endReader();
	}
	/**
	 * Loads guild info from file
	 */
	public static void loadGuilds(){
		Settings.guilds.clear();
		XMLStAXFile file=new XMLStAXFile(new File(Settings.dataSource));
		file.readXMLFile();
		try{
		ArrayList<Elements> guilds=file.parseToElements("guild");
		for(Elements e:guilds){
			try{
			Settings.guilds.put(e.getAttribute("id").getValue(), new Settings(e));
			}catch(Exception e1){
				Log.log("ERROR", "error putting guild(likely missing id attribute)"+e);
				Log.logError(e1);
			}
		}
		}catch(Exception e){
			Log.log("ERROR", "error loading guilds");
		}
		file.endReader();
	}
	/**
	 * Get the locally saved user data
	 * @param id id of the user
	 * @return Data object representing user data
	 */
	public static Data getUser(String id){
		if(Data.users.containsKey(id)){
			return Data.users.get(id);
		}
		else{
			return new Data(id);
		}
	}
	/**
	 * Gets all users which are recorded by bot(called at least once in any method) i.e. lapis
	 * @return vector of all users the bot is tracking
	 */
	public static Vector<Data> getRegisteredUsers(){
		Vector<Data> users=new Vector<Data>();
		for(String key:Data.users.keySet()){
			users.add(Data.users.get(key));
		}
		return users;
	}
	/**
	 * Set the user data to local save
	 * @param user user data to save locally
	 */
	public static void setUser(Data user){
		Data.users.put(user.id, user);
		//pushUserData();
	}
	public static Settings getGuild(String id){
		try{
		XMLStAXFile file=new XMLStAXFile(new File(Settings.dataSource));
		file.readXMLFile();
		Elements guild=file.parseToElements(new Attribute("id",id)).get(0);
		file.endReader();
		return new Settings(guild);
		}
		catch(Exception e){
			return new Settings(id);
		}
	}
	public static String getSetting(String id,String tag){
		XMLStAXFile file=new XMLStAXFile(new File(Settings.dataSource));
		file.readXMLFile();
		Elements guild=file.parseToElements(new Attribute("id",id)).get(0);
		String s= guild.getChilds(tag).get(0).getText();
		file.endReader();
		return s;
	}
	public static void setSetting(Settings guild){
		XMLStAXFile file=new XMLStAXFile(new File(Settings.dataSource));
		file.readXMLFile();
		Elements doc=file.parseDocToElements();
		file.endReader();
		for(int i=0;i<doc.getChilds().size();i++){
			if(doc.getChilds().get(i).getTagName().equals("guild")){
				if(doc.getChilds().get(i).getAttribute("id").getValue().equals(guild.id)){
					doc.getChilds().remove(i);
					i--;//decrement due to something being removed
				}
			}
		}
		doc.getChilds().add(guild.parseToElements());
		file.writeXMLFile();
		file.startWriter();
		file.writeElement(doc);
		file.endWriter();
	}
	public static String getPrefix(MessageReceivedEvent event){
		if(event.isFromType(ChannelType.PRIVATE)){
			return Settings.prefix;
		}
		else{
			return getPrefix(event.getGuild());
		}
	}
	public static String getPrefix(Guild guild){
		if(Settings.guilds.containsKey(guild.getId())){
			String prefix=Settings.guilds.get(guild.getId()).guildPrefix;
			if(prefix.equals("")){
				return Settings.prefix;
			}
			else{
				return prefix;
			}
		}
		else{
			return Settings.prefix;
		}
		
	}
	public static String getModPrefix(MessageReceivedEvent event){
		if(event.isFromType(ChannelType.PRIVATE)){
			return Settings.modPrefix;
		}
		else{
			return getModPrefix(event.getGuild());
		}
	}
	public static String getModPrefix(Guild guild){
		if(Settings.guilds.containsKey(guild.getId())){
			String modPrefix=Settings.guilds.get(guild.getId()).guildModPrefix;
			if(modPrefix.equals("")){
				return Settings.modPrefix;
			}
			else{
				return modPrefix;
			}
		}
		else{
			return Settings.modPrefix;
		}
	}
	public static String[] getOverrides() {
		String input="";
		try {
			if(new File(Settings.overrideSource).exists()){
				BufferedReader in=new BufferedReader(new FileReader(new File(Settings.overrideSource)));
				while(in.ready()){
					input+=in.readLine()+",";
				}
				in.close();
			}
		} catch (IOException e) {
			Log.logError(e);
		}
		if(input.length()>0)input=input.substring(0, input.length()-1);
		String[] overrides=input.split(",");
		return overrides;
	}
	/**
	 * remove an override string so that it cannot be used again
	 * @param overide override string to remove
	 */
	public static void removeOverride(String overide){
		String input="";
		try {
			BufferedReader in=new BufferedReader(new FileReader(new File(Settings.overrideSource)));
			while(in.ready()){
				String current=in.readLine();
				if(!current.equals(overide)){
					input+=current+"\n";
				}
			}
			in.close();
		} catch (IOException e) {
			Log.logError(e);
		}
		if(input.length()>1){
			input=input.substring(0, input.length()-1);
		}
		try{
			BufferedWriter out=new BufferedWriter(new FileWriter(new File(Settings.overrideSource)));
			out.write(input);
			out.close();
		}catch(Exception e){
			
		}
	}
	public static void pushUserData() {
		XMLStAXFile file=new XMLStAXFile(new File(Settings.dataSource));
		file.readXMLFile();
		Elements doc=file.parseDocToElements();
		file.endReader();
		for(int i=0;i<doc.getChilds().size();i++){
			if(doc.getChilds().get(i).getTagName().equals("user")){
				if(Data.users.containsKey(doc.getChilds().get(i).getAttribute("id").getValue())){
					doc.getChilds().remove(i);
					i--;
				}
			}
		}
		for(String key:Data.users.keySet()){
			doc.getChilds().add(Data.users.get(key).parseToElements());
		}
		file.writeXMLFile();
		file.startWriter();
		file.writeElement(doc);
		file.endWriter();
		DriveManager.update(new DriveFile(Settings.dataSource,DataEnum.SearchData.id));
		
	}
}
