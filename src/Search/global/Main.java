package Search.global;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import Search.global.ArgumentParser.ArgContainer;
import Search.global.record.Log;
import Search.global.record.Settings;
import Search.global.record.SaveSystem;
import Search.commands.*;
import Search.commands.mod.*;
import Search.commands.override.*;
import Search.global.BotListener;
import Search.global.CommandParser;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.impl.GameImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import util.Lib;

public class Main {
	public static JDA jda;
	public static CommandParser parser=new CommandParser();
	public static final HashMap<String,Command> commands=new HashMap<String,Command>();
	public static final HashMap<String,Command> modCommands=new HashMap<String,Command>();
	public static final HashMap<String,OverrideCommand> overrides=new HashMap<String,OverrideCommand>();
	public static void main(String[] args){
		try{
			Main.startup();
			Main.setup();
			global.Main.main(null);
			
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
			Log.logError(e);
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
		//put mod commands in map
		modCommands.put("prefix", new Prefix());
		modCommands.put("modprefix", new ModPrefix());
		//put in override commands
		overrides.put("log", new ViewLog());
		//setup/build various things
		Log.setup();
		SaveSystem.setup();
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
		else if(modCommands.containsKey(cmd.invoke)&&cmd.isModCmd){
			if(!isMod(cmd.e)){
				Lib.sendMessage(cmd.e, ":no_entry_sign: You are not authorized to use mod commands here");
				return;
			}
			boolean safe=modCommands.get(cmd.invoke).called(cmd.args, cmd.e);
			if(safe){
				modCommands.get(cmd.invoke).action(cmd.args, cmd.e);
				modCommands.get(cmd.invoke).executed(safe, cmd.e);
			}
			else{
				modCommands.get(cmd.invoke).executed(safe, cmd.e);
			}
		}
		else if(cmd.invoke.equals("help")){
			if(cmd.isModCmd){
				if(cmd.args.length>0&&modCommands.containsKey(cmd.args[0])){
					modCommands.get(cmd.args[0]).help(cmd.e);
				}
				else{
					Lib.printModHelp(cmd.e);
				}
			}
			else{
				if(cmd.args.length>0&&commands.containsKey(cmd.args[0])){
					commands.get(cmd.args[0]).help(cmd.e);
				}
				else{
					Lib.printHelp(cmd.e);
				}
			}
		}
	}
	/**
	 * checks if user is mod and has the admin privilege, or has been set to bot mod through overrides
	 * @param e message received from user
	 * @return whether or not user is a mod or not
	 */
	private static boolean isMod(MessageReceivedEvent e){
		if(e.getAuthor().getId().equals(Settings.ownerID)){
			return true;
		}
		List<Role> roles=e.getMember().getRoles();
		for(Role r:roles){
			if(r.hasPermission(Permission.ADMINISTRATOR)){
				return true;
			}
			
		}
		return false;
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
