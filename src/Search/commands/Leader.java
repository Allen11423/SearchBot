package Search.commands;

import Search.global.record.Data;
import Search.global.record.Settings;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Lib;

public class Leader extends CommandGenerics implements Command {

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		String s="```Perl\n";
		for(int i=0;i<Settings.momLeaders.size();i++){
			s+=generateLBString(Settings.momLeaders.get(i),i+1,getName(event,Settings.momLeaders.get(i).id))+"\n";
		}
		s+="```";
		Lib.sendMessage(event, s);

	}
	private String getName(MessageReceivedEvent e,String id){
		Member m=e.getGuild().getMemberById(id);
		if(m==null){
			return id;
		}
		else{
			return m.getEffectiveName();
		}
		
	}
	private String generateLBString(Data user, int rank,String name){
		String LB="["+rank+"]     >#"+name+"\n"
				+ "          Total Mom Points:"+user.getPoints();
		return LB;
	}

	@Override
	public void help(MessageReceivedEvent event) {
		String s=".top\n"
				+ "Displays mom point leaderboards";
		Lib.sendMessage(event, s);

	}

}
