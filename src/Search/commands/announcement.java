package Search.commands;

import java.util.concurrent.TimeUnit;

import Search.global.record.SaveSystem;
import Search.global.record.Settings;
import Search.util.Lib;
import Search.util.Reminder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class announcement extends CommandGenerics implements commands.Command{

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		final MessageReceivedEvent eEvent=event;
		int delay=Lib.extractTime(event.getMessage().getContent());
		long ID=System.currentTimeMillis();
		Settings.executor.schedule(new Runnable(){
			public void run(){
				Lib.sendMessage(eEvent, "@everyone "+eEvent.getMessage().getContent().substring(".announcement".length(),eEvent.getMessage().getContent().lastIndexOf(" in ")));
				SaveSystem.getGuild(event.getGuild().getId()).reminders.remove(ID);
			}
		}, delay, TimeUnit.SECONDS);
		int hours=delay/3600;
		int min=delay/60%60;
		int sec=delay%60;
		
		Reminder r=new Reminder(ID);
		r.setMsg("@everyone "+eEvent.getMessage().getContent().substring(".announcement".length(),eEvent.getMessage().getContent().lastIndexOf(" in ")));
		r.setTime(System.currentTimeMillis()+(delay*1000));
		r.setChannelID(event.getChannel().getId());
		Settings s=SaveSystem.getGuild(event.getGuild().getId());
		s.reminders.put(ID, r);
		
		Lib.sendTempMessage(eEvent, "sending message to everyone in "+(hours>0?hours+" hours ":"")+(min>0?min+" minutes ":"")+(sec>0?sec+" secounds":""),60);
		event.getMessage().delete().complete();
	}

	@Override
	public void help(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
