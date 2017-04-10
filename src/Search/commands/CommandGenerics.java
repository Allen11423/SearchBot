package Search.commands;

import Search.global.record.Log;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
/**
 * Generic class for commands contains some things to make calling commands easier
 * @author Allen
 *
 */
public abstract class CommandGenerics implements Command {

	public boolean called(String[] args, MessageReceivedEvent event) {
		Log.log("status", this.getClass()+" called by "+event.getAuthor().getName()+getGuildName(event));
		return true;
	}
	public static String getGuildName(MessageReceivedEvent event){
		return (event.isFromType(ChannelType.PRIVATE)?"":" on "+event.getGuild());
	}
	public abstract void action(String[] args, MessageReceivedEvent event);

	public abstract void help(MessageReceivedEvent event);


	public void executed(boolean sucess, MessageReceivedEvent event) {
		if(!sucess){
			Log.log("CMDERROR", this.getClass()+" failed when called by "+event.getAuthor().getName()+getGuildName(event));
		}

	}

}
