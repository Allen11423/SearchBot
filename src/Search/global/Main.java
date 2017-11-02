package Search.global;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import Search.global.ArgumentParser.ArgContainer;
import Search.global.record.Log;
import Search.global.record.Settings;
import Search.global.record.SaveSystem;
import Search.global.record.Secrets;
import Search.Library.FlavorManager;
import Search.commands.*;
import Search.commands.mod.*;
import Search.commands.override.*;
import Search.global.BotListener;
import Search.global.CommandParser;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import Search.util.CmdControl;
import Search.googleutil.drive.DataEnum;
import Search.googleutil.drive.DriveFile;
import Search.googleutil.drive.DriveManager;

public class Main {
	public static JDA jda;
	public static CommandParser parser=new CommandParser();
	public static final HashMap<String,OverrideCommand> overrides=new HashMap<String,OverrideCommand>();
	public static void main(String[] args){
		try{
			//disabled for now until I get everything set up
			Runtime.getRuntime().addShutdownHook(new Thread() {//bot shutdown, push and upload data
				@Override
				public void run() {
					System.out.println("shutting down");
					if(Settings.token.contentEquals(Secrets.token)){
						SaveSystem.pushUserData();
						Log.save();//last as this may take a long time as is not as high of a priority to complete
						DriveManager.update(new DriveFile(Log.LogSource,DataEnum.LogSource.id));
					}
				}   
			}); 
			Main.startup();
			Main.setup();
			if(Settings.token.contentEquals(Secrets.token)){
				global.Main.main(null);
			}
			
		}catch(Exception e){
			Log.logError(e);
			Log.save();
		}
	}
	public static void startup() throws LoginException, IllegalArgumentException, InterruptedException{
		try{
		jda = new JDABuilder(AccountType.BOT).addEventListener(new BotListener()).setToken(Settings.token).buildBlocking();
		}catch(LoginException e){
			TimeUnit.MINUTES.sleep(5);
			Log.log("System", "error on login, retrying in 5 minutes");
			startup();
		} catch (RateLimitedException e) {
			Log.logError(e);
		}
		jda.setAutoReconnect(true);
		jda.getPresence().setGame(Game.of(".serach|.image"));
	}
	public static void shutdown(){
		//jda.shutdown(false);
		//Log.log("status", "bot shutdown");
	}
	public static void quit(){
		jda.shutdown();
		Log.log("status", "Bot Quit");
		Log.save();
		System.exit(1);
	}
	/**
	 * everything that needs to be done when the JVM starts up
	 */
	public static void setup(){
		jda.getPresence().setGame(Game.of("the Loading Game..."));
		
		String Module="Core";
		CmdControl.addCommand("info", new Info(), Module);
		CmdControl.addCommand("kill", new kill(), Module);
		
		Module="Search";
		CmdControl.addCommand("search", new Search(), Module);
		CmdControl.addCommand("image", new ImgSearch(), Module);
		CmdControl.addCommand("nsfw", new IsNSFW(), Module);
		
		Module="Util";
		CmdControl.addCommand("announce", new announce(), Module);
		CmdControl.addCommand("announcement", new announcement(), Module);
		CmdControl.addCommand("repeat", new Repeat(), Module);
		
		Module="RandomGarbage";
		CmdControl.addCommand("mombo", new Combo(), Module);
		CmdControl.addCommand("momcombo", new Combo(), Module);
		CmdControl.addCommand("mompoints", new Point(), Module);
		CmdControl.addCommand("point", new Point(), Module);
		CmdControl.addCommand("points", new Point(), Module);
		CmdControl.addCommand("top", new Leader(), Module);
		CmdControl.addCommand("leaderboard", new Leader(), Module);
		CmdControl.addCommand("dailies", new Dailies(), Module);
		CmdControl.addCommand("daily", new Dailies(), Module);
		
		
		CmdControl.addModCommand("prefix", new Prefix());
		CmdControl.addModCommand("modprefix", new ModPrefix());
		CmdControl.addModCommand("addflavor", new addFlavor());

		//put in override commands
		overrides.put("log", new ViewLog());
		overrides.put("push", new DrivePush());
		overrides.put("preset", new ResetPoint());
		//setup/build various things
		FlavorManager.setup();
		DriveManager.setup();
		Log.setup();
		SaveSystem.setup();
		jda.getPresence().setGame(Game.of(".serach|.image"));
	}
	public static void log(String type,String msg){
		Log.log(type, msg);
	}
	public static boolean handleOverride(ArgContainer args,MessageReceivedEvent event){
		if(overrides.containsKey(args.command)){
			if(args.args.containsKey("help")){
				overrides.get(args.command).help(event);
				return true;
			}
			boolean safe=overrides.get(args.command).called(args.args, event);
			if(safe){
				overrides.get(args.command).action(args.args, event);
			}
			overrides.get(args.command).executed(safe, event);
			return true;
		}
		return false;
	}
}
