package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Lib;

public class IsNSFW extends CommandGenerics implements Command {

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		if(event.getChannel().getName().toLowerCase().contains("nsfw")){
			Lib.sendMessage(event, "NSFW searches will be allowed on this channel");
		}
		else{
			Lib.sendMessage(event, "NSFW searches will not be allowed on this channel");
		}

	}

	@Override
	public void help(MessageReceivedEvent event) {
		// TODO Auto-generated method stub

	}

}
