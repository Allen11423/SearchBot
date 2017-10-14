package Search.commands;

import Search.global.record.Data;
import Search.global.record.SaveSystem;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Lib;

public class Combo extends CommandGenerics implements Command {

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		Data user=null;
		int combo =1;
		if(args.length>1){
			if(Lib.isNumber(args[1])){
				combo=Integer.parseInt(args[1]);
			}
		}
		if(args.length<=0){
			Lib.sendMessage(event, "*"+event.getAuthor().getName()+"* has a mom combo of "+SaveSystem.getUser(event.getAuthor().getId()).getCombo());
			return;
		}
		if(event.getMessage().getMentionedUsers().size()>0){
			user=SaveSystem.getUser(event.getMessage().getMentionedUsers().get(0).getId());
		}
		else if(args.length>0&&Lib.isNumber(args[0])){
			user=SaveSystem.getUser(args[0]);
		}
		if(!(user==null)){
			for(int i=0;i<combo;i++){
				user.comboAdded();
			}
			SaveSystem.setUser(user);
		}
		if(event.getMessage().getMentionedUsers().size()>0){
			String s="*"+event.getMessage().getMentionedUsers().get(0).getName()+"* now has a mom combo of "+user.getCombo();
			Lib.sendMessage(event, s);
		}
		else if(args.length>0&&Lib.isNumber(args[0])){
			String s="*"+event.getGuild().getMemberById(args[0])+"* now has a mom combo of "+user.getCombo();
			Lib.sendMessage(event, s);
		}

		

	}
	public static void comboed(MessageReceivedEvent event){
		String id=event.getChannel().getHistory().retrievePast(1).complete().get(0).getAuthor().getId();
		Data user=SaveSystem.getUser(id);
		user.addPoints(ExtractFirstNum(event.getMessage().getContent()));
		String s=event.getChannel().getHistory().retrievePast(1).complete().get(0).getAuthor().getName()+" now has "+user.getPoints()+" mom points cause stuff";
		Lib.sendMessage(event, s);
		SaveSystem.setUser(user);
	}
	private static int ExtractFirstNum(String message){
		String i="";
		for(char c:message.trim().toCharArray()){
			if(Character.isDigit(c)){
				i+=c;
			}
			else if(i!=""){//to get firstnumer
				break;
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
