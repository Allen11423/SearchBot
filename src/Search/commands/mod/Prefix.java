package Search.commands.mod;

import java.util.concurrent.TimeUnit;

import Search.global.record.Log;
import Search.global.record.SaveSystem;
import Search.global.record.Settings;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import Search.util.Lib;

public class Prefix implements commands.Command{

	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		if(event.isFromType(ChannelType.PRIVATE)){
			return false;
		}
		Log.log("status", "Prefix changed to "+(args.length>0?args[0]:"")+" by "+event.getAuthor().getName()+" on "+event.getGuild());
		return true;
	}
	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		try{
		if(args.length>0){
			Settings guild=SaveSystem.getGuild(event.getGuild().getId());
			guild.guildPrefix=args[0];
			SaveSystem.setSetting(guild);
			TimeUnit.SECONDS.sleep(1);
			SaveSystem.loadGuilds();
			Lib.sendMessage(event, "Prefix changed to:"+args[0]);
		}
		else{
			Lib.sendMessage(event, "Must include prefix ~!prefix [prefix]");
		}
		}catch(Exception e){
			Log.logError(e);
		}
	}

	@Override
	public void help(MessageReceivedEvent event) {
		String s=SaveSystem.getModPrefix(event)+"prefix [newPrefix]"
				+ "Sets prefix of normal commands to the prefix specified";
		
		Lib.sendMessage(event, s);
	}

	@Override
	public void executed(boolean sucess, MessageReceivedEvent event) {
		
	}

}
