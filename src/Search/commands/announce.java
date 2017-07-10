package Search.commands;

import java.util.concurrent.TimeUnit;

import Search.global.record.Settings;
import Search.util.Lib;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class announce extends CommandGenerics implements commands.Command{

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		final MessageReceivedEvent eEvent=event;
		int delay=Lib.extractTime(event.getMessage().getContent());
		Settings.executor.schedule(new Runnable(){
			public void run(){
				Lib.sendMessage(eEvent, "@here "+eEvent.getMessage().getContent().substring(".announce".length(),eEvent.getMessage().getContent().lastIndexOf(" in ")));
			}
		}, delay, TimeUnit.SECONDS);
		int hours=delay/3600;
		int min=delay/60%60;
		int sec=delay%60;
		Lib.sendTempMessage(eEvent, "sending message to those here in "+(hours>0?hours+" hours ":"")+(min>0?min+" minutes ":"")+(sec>0?sec+" secounds":""),60);
		event.getMessage().deleteMessage().complete();
	}

	@Override
	public void help(MessageReceivedEvent event) {
		
	}

}
