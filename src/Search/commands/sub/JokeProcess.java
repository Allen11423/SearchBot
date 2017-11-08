package Search.commands.sub;

import Search.Library.FlavorManager;
import Search.global.record.Data;
import Search.global.record.SaveSystem;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class JokeProcess {
	
	public static String processPoint(Data author, Guild guild){
		String message=flavor("checkPoint",guild);
		Member user= guild.getMemberById(author.id);
		message = formatMessage(message,user.getAsMention(),user.getEffectiveName(),""+author.getPoints(),""+author.getDailyPoints(),author.getCombo()+"");
		return message;
	}
	public static String processCombo(Data userdata,Data author,Guild guild){
		String message;
		Member user= guild.getMemberById(userdata.id);
		if(userdata.id.equals(author.id)){
			message=getCombo(author,guild);
		}
		else{
			userdata.comboAdded();
			message=getCombo(userdata,guild);
		}
		message = formatMessage(message,user.getAsMention(),user.getEffectiveName(),"0","0",userdata.getCombo()+"");
		SaveSystem.setUser(userdata);//only thing that gets changed
		return message;
	}
	
	public static String processComboed(Data userdata,int points,Guild guild,Data author){
		Member user= guild.getMemberById(userdata.id);
		String message;
		//checking what point message to use
		if(points >0){
			message=flavor("addPoint",guild);
			//stuff related to setting the streak
			userdata.setStreak(userdata.getStreak()+1);//increment streak
			if(userdata.getStreak()==2){//control this to change the streak needed to increase combo
				userdata.comboAdded();
				userdata.setStreak(0);
				message+="\n"+getCombo(userdata,guild);
			}
			userdata.addPoints(points);
		}
		else{
			message=flavor("deletePoint",guild);
			int combo=userdata.getCombo();
			if(combo>1){
				message+="\n"+flavor("comboBreak",guild);
				userdata.setCombo(1);
				userdata.setStreak(0);
			}
		}
		author.addPoints((-1)*Math.abs(points));//penalize points for the author
		message=formatMessage(message,user.getAsMention(),user.getEffectiveName(),points*userdata.getCombo()+"","0",userdata.getCombo()+"");
		//save the data stuff
		SaveSystem.setUser(author);
		SaveSystem.setUser(userdata);
		return message;
		
	}
	
	//utility classes etc
	//does all formatting to message w/ all different parameters
	public static String formatMessage(String message,String userMention,String userName, String point, String dailyPoint,String combo){
		return message.replace("%userMention%", userMention)
				.replace("%userName%", userName)
				.replace("%dpoint%", dailyPoint)
				.replace("%point%", point)
				.replace("%combo%", combo);
	}
	public static String flavor(String detail,Guild guild){
		return FlavorManager.getRand(detail, guild);
	}
	public static String getCombo(Data user,Guild guild){
		return getCombo(user.getCombo(),guild);
	}
	public static String getCombo(int combo,Guild guild){
		String s="";
		switch(combo){
		case 1:
			s=FlavorManager.getRand("Combo1", guild);
			break;
		case 2:
			s=FlavorManager.getRand("Combo2", guild);
			break;
		case 3:
			s=FlavorManager.getRand("Combo3", guild);
			break;
		case 4:
			s=FlavorManager.getRand("Combo4", guild);
			break;
		case 5:
			s=FlavorManager.getRand("Combo5", guild);
			break;
		default:
			s="Combo out of range";//default case to avoid errors
			break;
		}
		return s;
	}
}
