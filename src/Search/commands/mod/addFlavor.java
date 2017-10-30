package Search.commands.mod;

import java.util.Arrays;

import Search.Library.FlavorDic;
import Search.Library.FlavorManager;
import Search.commands.Command;
import Search.global.record.SaveSystem;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import Search.util.Lib;

public class addFlavor extends ModGenerics implements Command {

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		if(args.length<2){
			help(event);
			return;
		}
		if(args[0].equalsIgnoreCase(FlavorDic.pointadd.toString())){
			String s= Lib.extract(Arrays.copyOfRange(args, 1, args.length));
			if((s.contains("%userMention%")||s.contains("%userName%"))&&(s.contains("%point%"))){
				FlavorManager.addFlavorString(FlavorDic.pointadd.toString(), event.getGuild().getId(), s);
				defaultSuccess(event);
			}
			else{
				Lib.sendMessage(event, "You must include %userMention% or %userName% in the text and %point% to display the number of points the user got");
			}
		}
		else if(args[0].equalsIgnoreCase(FlavorDic.C1.toString())){
			addCombo(FlavorDic.C1.toString(),args,event);
		}
		else if(args[0].equalsIgnoreCase(FlavorDic.C2.toString())){
			addCombo(FlavorDic.C2.toString(),args,event);
		}
		else if(args[0].equalsIgnoreCase(FlavorDic.C3.toString())){
			addCombo(FlavorDic.C3.toString(),args,event);
		}
		else if(args[0].equalsIgnoreCase(FlavorDic.C4.toString())){
			addCombo(FlavorDic.C4.toString(),args,event);
		}
		else if(args[0].equalsIgnoreCase(FlavorDic.C5.toString())){
			addCombo(FlavorDic.C5.toString(),args,event);
		}
		else if(args[0].equalsIgnoreCase(FlavorDic.pointcheck.toString())){
			String s= Lib.extract(Arrays.copyOfRange(args, 1, args.length));
			if((s.contains("%userMention%")||s.contains("%userName%"))&&(s.contains("%point%")&&s.contains("%dpoint%"))){
				FlavorManager.addFlavorString(FlavorDic.pointcheck.toString(), event.getGuild().getId(), s);
				defaultSuccess(event);
			}
			else{
				Lib.sendMessage(event, "You must include %userMention% or %userName% in the text and %point% and %dpoint% to display the number of points the user got");
			}
		}
		else{
			Lib.sendMessage(event, "invalid name of flavor text to add to available ones are\n"+Lib.extract(Lib.convert(FlavorDic.values()),"\n"));
		}
	}
	private void addCombo(String nameID,String[] args,MessageReceivedEvent event){
		String s= Lib.extract(Arrays.copyOfRange(args, 1, args.length));
		if((s.contains("%userMention%")||s.contains("%userName%"))){
			FlavorManager.addFlavorString(nameID, event.getGuild().getId(), s);
			defaultSuccess(event);
		}
		else{
			Lib.sendMessage(event, "You must include %userMention% or %userName% in the text");
		}
	}
	private void defaultSuccess(MessageReceivedEvent event){
		Lib.sendMessage(event, "You have successfully added the flavor text");
	}
	@Override
	public void help(MessageReceivedEvent event) {
		String s=SaveSystem.getModPrefix(event)+"addflavor [type] [text]\n"
				+ "Adds flavor text for the bot\n"
				+ "[type] what command the text will be used for\n"
				+ "[text] text to add, must include proper formatting, see error messages";
		Lib.sendMessage(event, s);
	}

}
