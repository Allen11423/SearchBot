package Search.commands.override;

import java.util.HashMap;

import Search.global.record.Data;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Lib;

public class ResetPoint extends OverrideGenerics implements OverrideCommand {

	@Override
	public void action(HashMap<String, String[]> args, MessageReceivedEvent event) {
		for(String k:Data.users.keySet()){
			Data.users.put(k, Data.users.get(k).resetPoints());
		}
		Lib.sendMessage(event, "Points reset");
	}

	@Override
	public void help(MessageReceivedEvent event) {

	}

}
