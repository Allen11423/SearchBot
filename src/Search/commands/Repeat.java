package Search.commands;

import java.util.concurrent.TimeUnit;

import Search.global.record.Settings;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import Search.util.Lib;

public class Repeat extends CommandGenerics implements commands.Command {

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		Lib.sendTempMessage(event, "repeating message every 4 hours", 20);
		Settings.executor.scheduleAtFixedRate(new Runnable(){
			public void run(){
				Lib.sendMessage(event, Lib.extract(args));
			}
		}, 4, 4, TimeUnit.HOURS);
	}

	@Override
	public void help(MessageReceivedEvent event) {
		// TODO Auto-generated method stub

	}

}
