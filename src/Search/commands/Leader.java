package Search.commands;

import java.util.List;

import Search.global.record.SaveSystem;
import Search.global.record.Settings;
import Search.util.tracking.PointableItem;
import Search.global.record.Data;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.Lib;

public class Leader extends CommandGenerics implements Command {

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		int start=1;
		if(args.length>0){
			start = Lib.extractNumber(args[0]);
		}
		if(start<1){
			start=1;
		}
		String s="```Perl\n";
		List<PointableItem> leaders=Settings.momLeaders.getRange((start-1)*10, start*10);
		for(int i=0;i<leaders.size();i++){
			s+=generateLBString(leaders.get(i),i+1+(10*(start-1)),getName(event,leaders.get(i).getIdentifier()))+"\n";
		}
		s+="-----------------------------\n";
		Data user = SaveSystem.getUser(event.getAuthor().getId());
		s+="Your rank :"+Settings.momLeaders.getRank(user)+"\t\tTotal Mom Points: "+user.getPoints();
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
	private String generateLBString(PointableItem user, int rank,String name){
		String LB="["+rank+"]     >#"+name+"\n"
				+ "          Total Mom Points:"+user.getPointValue();
		return LB;
	}

	@Override
	public void help(MessageReceivedEvent event) {
		String s=".top [page]\n"
				+ "Displays mom point leaderboards\n"
				+ "[page] which page of the leaderboards you want to see, default page 1 showing top 10";
		Lib.sendMessage(event, s);

	}

}
