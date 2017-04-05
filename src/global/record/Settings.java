package global.record;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/**
 * mixed bag of static settings for runtime and also used as the object representing a guild(not sure why I'm doing this) 
 * @author Allen
 *
 */
public class Settings {
	public static final String token=Secrets.token;
	public static final String prefix=".";
	public static final String modPrefix="~!";
	public static final String UA="Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
	public static final String saveSource="SearchBotLog";//for the log
	public static final String dataSource="SearchBotData";//for the guild based data
	public static final String overrideSource="override";
	public static final String join="User: %userMention% has joined %guildName%";
	public static final String overridePrefix="!";
	public static final String overrideArg="-";
	public static final String ownerID="206193542693912578";
	public static final long ID=System.currentTimeMillis();//ID for the bot based on when it was started
	public static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);//used for various threaded activities
	public static HashMap<String,Settings> guilds=new HashMap<String,Settings>();//map of guilds and settings stored locally for easy access
	
	public String guildPrefix="";
	public String guildModPrefix="";
	public String defaultSite="";
	public String joinMsg="";
	public String joinPM="";
	public String id="";
	public String[] modded;
	public boolean tJoinMsg=true;
	public boolean tJoinPM=false;
	//in preparation for custom messages for each server
	public Settings(String id){
		this.id=id;
	}
}
