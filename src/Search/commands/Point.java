package Search.commands;

import Search.commands.sub.JokeProcess;
import Search.global.record.Data;
import Search.global.record.SaveSystem;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Lib;

public class Point extends CommandGenerics implements Command {

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		Data users=null;
		if(args.length>0){
			if(event.getMessage().getMentionedUsers().size()>0){
				users=SaveSystem.getUser(event.getMessage().getMentionedUsers().get(0).getId());
			}
			else if(Lib.isNumber(args[0])){
				users=SaveSystem.getUser(args[0]);
			}
		}
		else{
		users=SaveSystem.getUser(event.getAuthor().getId());
		}
		String message=JokeProcess.processPoint(users, event.getGuild());
		Lib.sendMessage(event, message);
	}
	
	@Override
	public void help(MessageReceivedEvent event) {
		String s="mom points you've got for reasons";
		Lib.sendMessage(event, s);
	}

}
