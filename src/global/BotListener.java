package global;

import global.record.Log;
import global.record.Settings;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class BotListener extends ListenerAdapter{
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try{
			System.out.println(event.getMessage().getRawContent());
			//test for commands
			if(event.getMessage().getContent().startsWith(Settings.prefix)&&event.getMessage().getAuthor().getId()!=event.getJDA().getSelfUser().getId()){
				Main.handleCommand(Main.parser.parse(event.getMessage().getContent(), event));
			}
		}catch(Exception e){
			Log.logError(e);
		}
	}
	@Override
	public void onReady(ReadyEvent event){
		//Main.log("status","logged in as: "+event.getJDA().getShardInfo().getShardString());
	}
}
