package commands;

import java.util.concurrent.TimeUnit;

import global.record.Settings;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Lib;

public class announcement extends CommandGenerics{

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		final MessageReceivedEvent eEvent=event;
		int delay=Lib.extractTime(event.getMessage().getContent());
		Settings.executor.schedule(new Runnable(){
			public void run(){
				Lib.sendMessage(eEvent, "@everyone "+eEvent.getMessage().getContent().substring(".announcement".length(),eEvent.getMessage().getContent().lastIndexOf(" in ")));
			}
		}, delay, TimeUnit.SECONDS);
		int hours=delay/3600;
		int min=delay/60%60;
		int sec=delay%60;
		Lib.sendMessage(eEvent, "sending message to everyone in "+(hours>0?hours+" hours ":"")+(min>0?min+" minutes ":"")+(sec>0?sec+" secounds":""));
		
	}

	@Override
	public void help(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
