package Search.commands;

import Search.Library.FlavorManager;
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
				sendMsg(event.getMessage().getMentionedUsers().get(0).getAsMention(),event.getMessage().getMentionedUsers().get(0).getName(),event,users.getPoints(),users.getDailyPoints());
				return;
			}
			else if(Lib.isNumber(args[0])){
				users=SaveSystem.getUser(args[0]);
				sendMsg(event.getGuild().getMemberById(args[0]).getAsMention(),event.getGuild().getMemberById(args[0]).getNickname(),event,users.getPoints(),users.getDailyPoints());
				return;
			}
		}
		users=SaveSystem.getUser(event.getAuthor().getId());
		sendMsg(event.getAuthor().getAsMention(),event.getAuthor().getName(),event,users.getPoints(),users.getDailyPoints());
	}
	private static void sendMsg(String authMention,String authName, MessageReceivedEvent event, int points,int dpoints){
		if(dpoints==69){
			Lib.sendMessage(event, authMention+ " got a total of 69 mom points today for a total of "+points+". No Comment.");
		}
		else{
			String s = FlavorManager.getRand("checkPoint", event.getGuild())
					.replace("%userMention%", authMention)
					.replace("%userName%", authName)
					.replace("%dpoint%", dpoints+"")
					.replace("%point%", ""+points);
			Lib.sendMessage(event, s);
		}
	}
	@Override
	public void help(MessageReceivedEvent event) {
		String s="mom points you've got for reasons";
		Lib.sendMessage(event, s);
	}

}
