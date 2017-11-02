package Search.commands;

import java.util.concurrent.TimeUnit;

import Search.global.record.Data;
import Search.global.record.SaveSystem;
import Search.global.record.Settings;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Lib;

public class Dailies extends CommandGenerics implements Command {

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		Data user= SaveSystem.getUser(event.getAuthor().getId());
		if(user.dailyReady()){
			user.addPoints(10);
			user.dailyUsed();
			SaveSystem.setUser(user);
			Lib.sendMessageFormated(event, "**%userName%** has gotten their daily 10 mom points x combo of "+user.getCombo());
		}
		else{
			long diff=Settings.dailyTime+86400000-System.currentTimeMillis();
			int hours=(int) TimeUnit.MILLISECONDS.toHours(diff)%24;
			int minutes=(int) TimeUnit.MILLISECONDS.toMinutes(diff)%60;
			int seconds=(int) TimeUnit.MILLISECONDS.toSeconds(diff)%60;
			Lib.sendMessageFormated(event, "**%userName%** your daily mom points refreshses in "+(hours==0?"":hours+" hours ")+(minutes==0?"":minutes+" minutes ")+(seconds==0?"":seconds+" seconds"));
		}
	}

	@Override
	public void help(MessageReceivedEvent event) {
		String s=SaveSystem.getPrefix(event)+"dailies\n"
				+ "Gives daily mom points";
		Lib.sendMessage(event, s);

	}

}
