package global;

import java.util.ArrayList;

import global.record.Settings;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class CommandParser {
	public CommandContainer parse(String rw,MessageReceivedEvent e){
		ArrayList<String> split=new ArrayList<String>();
		String raw= rw;
		String beheaded;
		
		
			beheaded=raw.replaceFirst(replaceMeta(Settings.prefix), "");
		String[] splitbeheaded=beheaded.split(" ");
		for(String s:splitbeheaded){
			split.add(s);
		}
		String invoke=split.get(0);
		String[] args=new String[split.size()-1];
		split.subList(1, split.size()).toArray(args);
		
		return new CommandContainer(raw,beheaded,splitbeheaded, invoke.toLowerCase(), args, e,false);
	}
	private static String replaceMeta(String s){
		s="\\Q"+s+"\\E";//in case some meta characters are in the prefix string
		return s;
	}
	public class CommandContainer{
		public final String raw;
		public final String beheaded;
		public final String[] splitbeheaded;
		public final String invoke;
		public final String[] args;
		public final boolean isModCmd;
		public final MessageReceivedEvent e;
		
		public CommandContainer(String rw,String beheaded,String[] splitbeheaded, String invoke,String[] args,MessageReceivedEvent e,boolean isModCmd){
			this.raw=rw;
			this.beheaded=beheaded;
			this.splitbeheaded=splitbeheaded;
			this.invoke=invoke;
			this.args=args;
			this.e=e;
			this.isModCmd=isModCmd;
		}
	}
}
