package Search.util;

import java.util.Arrays;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Library of various useful methods that are used all over the place
 * @author Allen
 *
 */
public class Lib {
	public static void printHelp(MessageReceivedEvent event){
		String msg="Help\n";
		Lib.sendMessage(event, msg);
	}
	
	public static void sendMessageFormated(MessageReceivedEvent event,String msg){
		event.getChannel().sendMessage(Lib.FormatMessage(event,msg));
	}
	public static void sendMessage(MessageReceivedEvent event,Message msg){
		event.getChannel().sendMessage(msg).queue();
	}
	public static void sendMessage(MessageReceivedEvent event,String msg){
		event.getChannel().sendMessage(msg).queue();
	}
	public static String FormatMessage(MessageReceivedEvent event,String msg){
		return msg.replace("%userMention%", event.getAuthor().getAsMention()).
				replace("%userName%", event.getAuthor().getName()).
				replace("%selfMention%", event.getJDA().getSelfUser().getAsMention());
	}
	public static void sendMessageFormated(GuildMemberJoinEvent event,String msg){
		event.getGuild().getPublicChannel().sendMessage(Lib.FormatMessage(event,msg)).queue();;
	}
	public static String FormatMessage(GuildMemberJoinEvent event,String msg){
		return msg.replace("%userMention%", event.getMember().getAsMention()).
		replace("%userName%", event.getMember().getNickname()).
		replace("%guildName%",event.getGuild().getName());
	}
	public static Elements getNestedDeep(Elements element,String[] tags){
		Elements ele=element;
		for(String s:tags){
			ele=Lib.getNested(ele, s);
		}
		return ele;
	}
	public static Elements getNested(Elements element, String tag){
		Elements ele=new Elements();
		for(Element eles:element){
			ele.addAll(eles.getElementsByTag(tag));
		}
		return ele;
	}
	public static Elements getNestedItem(Elements element,int index,String tag){
		Elements ele=new Elements();
		for(Element eles:element){
			try{
				ele.add(eles.getElementsByTag(tag).get(index));
			}catch(Exception e){};
		}
		return ele;
	}
	public static int getTRCount(Element table){
		Elements tr=table.getElementsByTag("tr");
		return tr.size();
	}
	public static int getTDCount(Element table,int row){
		Elements td=table.getElementsByTag("tr").get(row).getElementsByTag("td");
		return td.size();
	}
	public static Element getCell(int row, int col,Element table){
		return table.getElementsByTag("tr").get(row).getElementsByTag("td").size()>0? 
				table.getElementsByTag("tr").get(row).getElementsByTag("td").get(col):null;
	}
	public static Element getHeader(int row, Element table){
		return table.getElementsByTag("tr").get(row).getElementsByTag("th").get(0);
	}
	public static String getLink(Element ele){
		Elements links = ele.getElementsByTag("a");
		for (Element link : links) {
			return link.attr("abs:href");
		}
		return "";
	}
	public static String pad(String s, int pad){
		for(int i=s.length();i<pad;i++){
			s=s+" ";
		}
		return s;
	}
	/**
	 * if s trimmed and split is part of Character.isDigit 
	 * @param s string to check if it's a number
	 * @return if s is all numbers or not
	 */
	public static boolean isNumber(String s){
		for(char c:s.trim().toCharArray()){
			if(!Character.isDigit(c)){
				return false;
			}
		}
		return true;
	}
	/**
	 * gets only number chars from a string
	 * @param s string to extract number from
	 * @return number with non digit characters removed
	 */
	public static int extractNumber(String s){
		String i="";
		for(char c:s.trim().toCharArray()){
			if(Character.isDigit(c)){
				i+=c;
			}
		}
		if(i.equals("")){
			return 0;
		}
		return Integer.parseInt(i);
	}
	/**
	 * 
	 * @param s
	 * @return time represented in seconds
	 */
	public static int extractTime(String s){
		int totalTime=0;
		String time=s.substring(s.lastIndexOf(" in "));
		String[] pieces=time.split(" ");
		int t=0;
		for(String c:pieces){
			c=c.trim();
			int temp=extractNumber(c);	
			if(temp>0){
			t=temp;
			}
			if(c.contains("h")){
				totalTime+=t*3600;
				t=0;
			}
			else if(c.contains("m")){
				totalTime+=t*60;
				t=0;
			}
			else if(c.contains("s")){
				totalTime+=t;
				t=0;
			}
		}
		System.out.println(totalTime+" "+t);
		return totalTime;
	}
	public static boolean contains(Object obj,Object[] os){
		for(Object o:os){
			if(o.equals(obj)){
				return true;
			}
		}
		return false;
	}
	public static <T> T[] concat(T[] first, T[] second) {
		  T[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
		}
}
