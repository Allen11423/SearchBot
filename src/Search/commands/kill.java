package Search.commands;

import Search.global.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class kill extends CommandGenerics implements commands.Command{

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		Main.quit();
	}

	@Override
	public void help(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
