package Search.global;

import java.util.ArrayList;

import Search.global.record.SaveSystem;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class CommandParser {
	public CommandParser(){
	}
	public CommandContainer parse(String rw,MessageReceivedEvent e){
		ArrayList<String> split=new ArrayList<String>();
		String raw= rw;
		boolean isModCmd=raw.startsWith(SaveSystem.getModPrefix(e));
		String beheaded;
		if(isModCmd){
			beheaded=raw.replaceFirst(replaceMeta(SaveSystem.getModPrefix(e)), "");
		}
		else{
			beheaded=raw.replaceFirst(replaceMeta(SaveSystem.getPrefix(e)), "");
		}
		String[] splitbeheaded=beheaded.split(" ");
		for(String s:splitbeheaded){
			split.add(s);
		}
		String invoke=split.get(0);
		String[] args=new String[split.size()-1];
		split.subList(1, split.size()).toArray(args);
		
		return new CommandContainer(raw,beheaded,splitbeheaded, invoke.toLowerCase(), args, e,isModCmd);
	}
	private static String replaceMeta(String s){
		s="\\Q"+s+"\\E";//in case some meta characters are in the prefix string
		return s;
	}
	
}
