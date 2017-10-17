package Search.commands;

import java.util.List;

import Search.global.record.Data;
import Search.global.record.SaveSystem;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Lib;

public class Combo extends CommandGenerics implements Command {

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		Data user=null;
		if(args.length<=0){//if no arguments send combo value for user
			sendComboMsg(event.getAuthor().getAsMention(),event,SaveSystem.getUser(event.getAuthor().getId()).getCombo());
			return;
		}
		//stuff to get user/send message/add combo
		if(event.getMessage().getMentionedUsers().size()>0){
			if(event.getMessage().getMentionedUsers().get(0).getId().equals(event.getAuthor().getId())){//mentions self
				return;
			}
			user=SaveSystem.getUser(event.getMessage().getMentionedUsers().get(0).getId());
			
		}
		else if(args.length>0&&Lib.isNumber(args[0])){
			if(args[0].equals(event.getAuthor().getId())){//mentions self return
				return;
			}
			user=SaveSystem.getUser(args[0]);
		}
		if(!(user==null)){
			user.comboAdded();
			SaveSystem.setUser(user);
		}
		if(event.getMessage().getMentionedUsers().size()>0){
			sendComboMsg(event.getMessage().getMentionedUsers().get(0).getAsMention(),event,user.getCombo());
		}
		else if(args.length>0&&Lib.isNumber(args[0])){
			sendComboMsg(event.getGuild().getMemberById(args[0]).getAsMention(),event,user.getCombo());
		}

	}
	public static void sendComboMsg(String userMention,MessageReceivedEvent event, int combo){
		String s="*"+userMention+"* you have a mom combo of "+combo;
		Lib.sendMessage(event, s);
	}
	public static void comboed(MessageReceivedEvent event){
		String id=lastNonAuthor(event.getAuthor().getId(),event.getChannel()).getId();
		Data user=SaveSystem.getUser(id);
		user.addPoints(ExtractFirstNum(event.getMessage().getContent()));
		String s=lastNonAuthor(event.getAuthor().getId(),event.getChannel()).getAsMention()+" got "+user.getPoints()+" mom points for reasons!";
		Lib.sendMessage(event, s);
		SaveSystem.setUser(user);
	}
	public static User lastNonAuthor(String authID, MessageChannel channel){
		List<Message> past=channel.getHistory().retrievePast(50).complete();
		for(Message m: past){
			if(!m.getAuthor().getId().equals(authID)){
				return m.getAuthor();
			}
		}
		return null;
	}
	private static int ExtractFirstNum(String message){//gets first number from slash
		String i="";
		for(char c:message.trim().toCharArray()){
			if(Character.isDigit(c)){
				i+=c;
			}
			else if(i!=""&&c=='/'){//to get firstnumer
				break;
			}
			else{
				i="";
			}
		}
		if(i.equals("")){
			i="0";
		}
		return Integer.parseInt(i);
	}
	@Override
	public void help(MessageReceivedEvent event) {
		String s =".mombo [userID/userMention] [combo]\n"
				+ "adds to current mombo\n"
				+ "[userID/userMention] either mention user or the numberical ID for the user\n"
				+ "[combo] amount to add to mombo otherwise +1";
		Lib.sendMessage(event, s);
	}

}
