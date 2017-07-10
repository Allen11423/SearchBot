package Search.global;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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