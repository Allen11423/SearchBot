package Search.commands;

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
				sendMsg(event.getMessage().getMentionedUsers().get(0).getAsMention(),event,users.getPoints());
				return;
			}
			else if(Lib.isNumber(args[0])){
				users=SaveSystem.getUser(args[0]);
				sendMsg(event.getGuild().getMemberById(args[0]).getAsMention(),event,users.getPoints());
				return;
			}
		}
		users=SaveSystem.getUser(event.getAuthor().getId());
		sendMsg(event.getAuthor().getAsMention(),event,users.getPoints());
	}
	private static void sendMsg(String authMention, MessageReceivedEvent event, int points){
		Lib.sendMessage(event, authMention+" has "+points+" mom points cause reasons!");
	}
	@Override
	public void help(MessageReceivedEvent event) {
		String s="mom points you've got for reasons";
		Lib.sendMessage(event, s);
	}

}
