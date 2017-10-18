package Search.commands;

import java.util.List;

import Search.Library.mom.ComboData;
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
			sendComboMsg(event.getAuthor().getAsMention(),event,SaveSystem.getUser(event.getAuthor().getId()).getCombo(),false);
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
		boolean rankup=true;
		if(!(user==null)){
			int combo=user.getCombo();
			user.comboAdded();
			if(combo==user.getCombo()){
				rankup=false;
			}
			SaveSystem.setUser(user);
		}
		if(event.getMessage().getMentionedUsers().size()>0){
			sendComboMsg(event.getMessage().getMentionedUsers().get(0).getAsMention(),event,user.getCombo(),rankup);
		}
		else if(args.length>0&&Lib.isNumber(args[0])){
			sendComboMsg(event.getGuild().getMemberById(args[0]).getAsMention(),event,user.getCombo(),rankup);
		}

	}
	public static void sendComboMsg(String userMention,MessageReceivedEvent event, int combo,boolean rankup){
		String s;
		if(rankup){
			s=""+userMention+"'s mombo went up to "+combo+" "+ComboData.get(combo).getRand();
		}
		else{
			s=""+userMention+" "+ComboData.get(combo).getRand();
		}
		s=s.replace("%combo%", ""+combo);
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

		String s;
		if(event.getMessage().getMentionedUsers().size()>0){
			s=event.getMessage().getMentionedUsers().get(0).getAsMention()+" got "+points+" mom points, better deposit it like how I deposit it in yourmom";
		}
		else{
			s=lastNonAuthor(event.getAuthor().getId(),event.getChannel()).getAsMention()+" got "+points+" mom points, better deposit it like how I deposit it in yourmom";
		}

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
		String s =".mombo [userID/userMention] [combo]\n"
				+ "adds to current mombo\n"
				+ "[userID/userMention] either mention user or the numberical ID for the user\n"
				+ "[combo] amount to add to mombo otherwise +1";
		Lib.sendMessage(event, s);
	}

}
