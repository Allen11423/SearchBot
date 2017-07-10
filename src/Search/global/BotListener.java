package Search.global;

import Search.global.record.Log;
import Search.util.Overrider;
import Search.global.Main;
import Search.global.record.SaveSystem;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import Search.util.CmdControl;


public class BotListener extends ListenerAdapter{
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try{
			if(Overrider.parseOverride(event))return;//test for override commands
			if(CmdControl.parseCommands(event))return;//test for commands
			//base commands that are for prefixes
			if(event.getMessage().isMentioned(event.getJDA().getSelfUser())&&!event.getMessage().mentionsEveryone()){
				if(event.getMessage().getContent().toLowerCase().contains("modprefix")){
					Search.util.Lib.sendMessage(event, "mod prefix for server:"+SaveSystem.getModPrefix(event));
				}
				else if(event.getMessage().getContent().toLowerCase().contains("prefix")){
					Search.util.Lib.sendMessage(event, "prefix for server:"+SaveSystem.getPrefix(event));
				}
			}
		}catch(Exception e){
			Log.logError(e);
		}
	}
	@Override
	public void onReady(ReadyEvent event){
		Main.log("status","logged in as: "+event.getJDA().getSelfUser().getName());
	}
}
