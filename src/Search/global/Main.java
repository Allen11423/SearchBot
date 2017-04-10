package Search.global;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import Search.global.ArgumentParser.ArgContainer;
import Search.global.record.Log;
import Search.global.record.Settings;
import Search.commands.*;
import Search.commands.override.*;
import Search.global.BotListener;
import Search.global.CommandParser;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.impl.GameImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class Main {
	public static JDA jda;
	public static CommandParser parser=new CommandParser();
	public static HashMap<String,Command> commands=new HashMap<String,Command>();

	public static final HashMap<String,OverrideCommand> overrides=new HashMap<String,OverrideCommand>();
	public static void main(String[] args){
		try{
			Main.startup();
			Main.setup();
			//global.Main.main(null);
			
		}catch(Exception e){
			Log.logError(e);
			Log.save();
		}
	}
	public static void startup() throws LoginException, IllegalArgumentException, InterruptedException{
		try{
		jda = new JDABuilder(AccountType.BOT).addListener(new BotListener()).setToken(Settings.token).buildBlocking();
		}catch(LoginException e){
			TimeUnit.MINUTES.sleep(5);
			Log.log("System", "error on login, retrying in 5 minutes");
			startup();
		} catch (RateLimitedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jda.setAutoReconnect(true);
		jda.getPresence().setGame(new GameImpl(".serach|.image","null",GameType.DEFAULT));
	}
	public static void shutdown(){
		jda.shutdown(false);
		Log.log("status", "bot shutdown");
	}
	public static void quit(){
		jda.shutdown(true);
		Log.log("status", "Bot Quit");
		Log.save();
		System.exit(1);
	}
	/**
	 * everything that needs to be done when the JVM starts up
	 */
	public static void setup(){
		jda.getPresence().setGame(new GameImpl("the Loading Game...","null",GameType.DEFAULT));
		//put commands in map
		commands.put("search", new Search());
		commands.put("image", new ImgSearch());
		commands.put("kill", new kill());
		commands.put("nsfw", new IsNSFW());
		commands.put("announce", new announce());
		commands.put("announcement", new announcement());
		overrides.put("log", new ViewLog());
		//setup/build various things
		Log.setup();
		jda.getPresence().setGame(new GameImpl(".serach|.image","null",GameType.DEFAULT));
	}
	public static void handleCommand(CommandParser.CommandContainer cmd){
		System.out.println(cmd.invoke);
		if(commands.containsKey(cmd.invoke)&&!cmd.isModCmd){
			boolean safe=commands.get(cmd.invoke).called(cmd.args, cmd.e);
			if(safe){
				commands.get(cmd.invoke).action(cmd.args, cmd.e);
				commands.get(cmd.invoke).executed(safe, cmd.e);
			}
			else{
				commands.get(cmd.invoke).executed(safe, cmd.e);
			}
		}
		
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
