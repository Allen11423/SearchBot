package Search.commands;

import java.util.List;

import Search.Library.FlavorManager;
import Search.global.record.Data;
import Search.global.record.SaveSystem;
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
			sendComboMsg(event.getAuthor().getAsMention(),event.getAuthor().getName(),event,SaveSystem.getUser(event.getAuthor().getId()).getCombo());
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
			sendComboMsg(event.getMessage().getMentionedUsers().get(0).getAsMention(),event.getMessage().getMentionedUsers().get(0).getName(),event,user.getCombo());
		}
		else if(args.length>0&&Lib.isNumber(args[0])){
			sendComboMsg(event.getGuild().getMemberById(args[0]).getAsMention(),event.getGuild().getMemberById(args[0]).getNickname(),event,user.getCombo());
		}

	}
	public static void sendComboMsg(String userMention,String userName,MessageReceivedEvent event, int combo){
		String s="";
		switch(combo){
		case 1:
			s=FlavorManager.getRand("Combo1", event.getGuild());
			break;
		case 2:
			s=FlavorManager.getRand("Combo2", event.getGuild());
			break;
		case 3:
			s=FlavorManager.getRand("Combo3", event.getGuild());
			break;
		case 4:
			s=FlavorManager.getRand("Combo4", event.getGuild());
			break;
		case 5:
			s=FlavorManager.getRand("Combo5", event.getGuild());
			break;
		default:
			break;
		}
		s=s.replace("%userMention%", userMention)
				.replace("%userName%", userName);
		Lib.sendMessage(event, s);
	}
	public static void comboed(MessageReceivedEvent event){
		String id;
		if(event.getMessage().getMentionedUsers().size()>0){
			id=event.getMessage().getMentionedUsers().get(0).getId();
			if(id.equals(event.getAuthor().getId())){
				id=lastNonAuthor(event.getAuthor().getId(),event.getChannel()).getId();
			}
		}
		else{
			id=lastNonAuthor(event.getAuthor().getId(),event.getChannel()).getId();
		}
		Data user=SaveSystem.getUser(id);
		int points=ExtractFirstNum(event.getMessage().getContent());
		user.addPoints(points);
		Data author = SaveSystem.getUser(event.getAuthor().getId());
		author.penalizePoints(Math.abs(points));
		String s;
		if(points>=0){
			s=FlavorManager.getRand("addPoint",event.getGuild());
		}
		else{
			s=FlavorManager.getRand("deletePoint",event.getGuild());
		}
		if(event.getMessage().getMentionedUsers().size()>0){
			s=s.replace("%userMention%", event.getMessage().getMentionedUsers().get(0).getAsMention())
					.replace("%userName%", event.getGuild().getMemberById(event.getMessage().getMentionedUsers().get(0).getId()).getNickname())
					.replace("%point%", ""+points*user.getCombo());
		}
		else{
			s=s.replace("%userMention%", lastNonAuthor(event.getAuthor().getId(),event.getChannel()).getAsMention())
					.replace("%userName%", event.getGuild().getMemberById(lastNonAuthor(event.getAuthor().getId(),event.getChannel()).getId()).getNickname())
					.replace("%point%", ""+points*user.getCombo());
		}

		Lib.sendMessage(event, s);
		SaveSystem.setUser(user);
		SaveSystem.buildLeaderBoards();
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
			if(Character.isDigit(c)||c=='-'){
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
		//limit point increase to 20 max either positive or negative

		int value=0;
		try{
			value=Integer.parseInt(i);
		}catch(NumberFormatException e){
			if(!i.equals("")){
				if(i.startsWith("-")){
					value=-20;
				}
				else{
					value=20;
				}
			}
		}
		if(value<-20){
			value=-20;
		}
		else if(value>20){
			value=20;
		}
		return value;
	}
	@Override
	public void help(MessageReceivedEvent event) {
		String s =".mombo [userID/@user]\n"
				+ "adds to current mombo for the user\n"
				+ "[userID/@user] either mention user or the numberical ID for the user";
		Lib.sendMessage(event, s);
	}

}
