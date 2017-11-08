package Search.commands;

import java.util.List;

import Search.commands.sub.JokeProcess;
import Search.global.record.Data;
import Search.global.record.SaveSystem;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import Search.util.Lib;

public class Combo extends CommandGenerics implements Command {

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		Data user=null;
		Data author = SaveSystem.getUser(event.getAuthor().getId());
		if(args.length<=0){//if no arguments send combo value for user
			user=author;
		}
		else if(event.getMessage().getMentionedUsers().size()>0){
			if(event.getMessage().getMentionedUsers().get(0).getId().equals(event.getAuthor().getId())){//mentions self
				user=author;
			}
			user=SaveSystem.getUser(event.getMessage().getMentionedUsers().get(0).getId());
		}
		else if(args.length>0&&Lib.isNumber(args[0])){
			if(args[0].equals(event.getAuthor().getId())){//mentions self return
				user=author;
			}
			user=SaveSystem.getUser(args[0]);
		}
		String message=JokeProcess.processCombo(user, author, event.getGuild());
		Lib.sendMessage(event, message);

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
		Data author = SaveSystem.getUser(event.getAuthor().getId());
		String message=JokeProcess.processComboed(user, points, event.getGuild(), author);
		Lib.sendMessage(event, message);
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
