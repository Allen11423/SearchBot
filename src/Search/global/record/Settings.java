package Search.global.record;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Search.global.record.Secrets;
import Search.global.record.Settings;
import XML.Attribute;
import XML.Elements;
import global.Main;
import Search.util.ModuleController;
import Search.util.Reminder;
import Search.util.Lib;


/**
 * mixed bag of static settings for runtime and also used as the object representing a guild(not sure why I'm doing this) 
 * @author Allen
 *
 */
public class Settings {
	public static final String token=Secrets.token;
	public static final String prefix=".";
	public static final String modPrefix=".!";
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
	public static final HashMap<String,Settings> guilds=new HashMap<String,Settings>();//map of guilds and settings stored locally for easy access
	
	public String guildPrefix="";
	public String guildModPrefix="";
	public String defaultSite="";
	public String joinMsg="";
	public String joinPM="";
	public String id="";
	public String[] modded=new String[]{};
	public boolean tJoinMsg=true;
	public boolean tJoinPM=false;
	public HashMap<Long,Reminder> reminders=new HashMap<Long,Reminder>();
	public HashMap<String,ModuleController> disabled=new HashMap<String,ModuleController>();//hashmap of disabled modules module/module controller
	//in preparation for custom messages for each server
	public Settings(String id){
		this.id=id;
	}
	public Settings(Elements root){
		id=root.getAttribute("id").getValue();
		joinMsg=Lib.getString(root,"join");
		guildPrefix=Lib.getString(root,"prefix");
		guildModPrefix=Lib.getString(root,"modPrefix");
		modded=Lib.textArray(root,"modded");
		tJoinMsg=Lib.getBooleanSetting(false,root,"tJoin");
		tJoinPM=Lib.getBooleanSetting(false,root,"tJoinPM");
		for(Elements e:Lib.elementArray(root, "moduleControl")){
			disabled.put(e.getAttribute("name").getValue(), new ModuleController(e));
		}
		for(Elements e:Lib.elementArray(root, "reminder")){
			Reminder r=new Reminder(e);
			reminders.put(Long.parseLong(e.getAttribute("ID").getValue()), r);
			executor.schedule(new Runnable(){//autoexecute
				public void run(){
					Main.jda.getGuildById(id).getTextChannelById(r.getChannelID()).sendMessage(r.getMsg());
					reminders.remove(r);
				}
			}, r.getTime()-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}
	}
	public Elements parseToElements(){
		Elements root=new Elements("guild");
		root.getAttributes().add(new Attribute("id",id));
		
		Elements settings=new Elements("settings");
		root.add(settings);
		
		Elements join=new Elements("join");
		join.setText(joinMsg);
		settings.add(join);
		
		
		Elements mPrefix=new Elements("modPrefix");
		mPrefix.setText(guildModPrefix);
		settings.add(mPrefix);
		
		Elements prefix=new Elements("prefix");
		prefix.setText(guildPrefix);
		settings.add(prefix);
		
		for(String s:modded){
			Elements mod=new Elements("modded").setText(s);
			root.add(mod);
		}
		for(String s:this.disabled.keySet()){
			root.add(this.disabled.get(s).parseToElements());
		}
		for(Long l:this.reminders.keySet()){
			root.add(this.reminders.get(l).toElements());
		}
		Elements toggle=new Elements("toggle");
		root.add(toggle);
		
		Elements tJoin=new Elements("tJoin").setText(""+tJoinMsg);
		toggle.add(tJoin);
		
		Elements tJoinPM=new Elements("tJoinPM").setText(""+this.tJoinPM);
		toggle.add(tJoinPM);
		
		return root;
	}
}
