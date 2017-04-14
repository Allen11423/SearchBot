package Search.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Library.ElementFilter;
import Search.global.record.Log;
import Search.global.record.SaveSystem;
import Search.global.record.Settings;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
/**
 * Library of various useful methods that are used all over the place
 * @author Allen
 *
 */
public class Lib {
	//methods sending data
	/**
	 * message data for help menu
	 * @param event
	 */
	public static void printHelp(MessageReceivedEvent event){
		String msg="__***Help List***__\n"
				+ "Use "+SaveSystem.getPrefix(event)+"help [command] to get more info on a specific command, i.e.: "+SaveSystem.getPrefix(event)+"help ping\n\n"
				+ "__**Modules**__\n"
				+ "**Core** - `ping` `invite`\n"
				+ "Core commands for bot\n\n"
				+ "**Exvius** - `awaken` `equipment` `lore` `skill` `unitart` `unit`\n"
				+ "Commands to extract info from Exvicus wiki(best for GL players)\n\n"
				+ "**Reddit** - `rawaken` `requipment` `rskill` `runit`\n"
				+ "Commands to extract info from Reddit wiki(best for JP players or GL players looking for future info)\n\n"
				+ "**Salt** - `summon` `salty` `waifu`\n"
				+ "Random commands based on chance\n\n"
				+ "**WIP** - `whale` `banner` `11pull` `pull` `unitinventory`\n"
				+ "Commands that are work in progress currently unimplemented\n\n"
				+ "Don't include the example brackets when using commands!\n"
				+ "To view mod commands, use "+SaveSystem.getModPrefix(event)+"help";
		Lib.sendMessage(event, msg);
	}
	
	/**
	 * message data for mod help menu
	 * @param event
	 */
	public static void printModHelp(MessageReceivedEvent event){
		String msg="Mod Help List\n"
				+ SaveSystem.getModPrefix(event)+"join [MSG]"
				+ "\t`sets a join message for the server, leave blank for no message`\n"
				+ SaveSystem.getModPrefix(event)+"prefix [prefix]"
				+ "\t`changes the prefix used for the bot's commands`\n"
				+ SaveSystem.getModPrefix(event)+"sleep [timeout]"
				+ "\t`temporariy disable bot`\n"
				+ SaveSystem.getModPrefix(event)+"toggle"
				+ "\t`toggles various toggleable server options`";
		Lib.sendMessage(event, msg);
	}
	public static Message editMessage(Message message,String msg){
		return message.editMessage(msg).complete();
	}
	public static Message sendFile(MessageReceivedEvent event, String msg, File file){
		try {
			if(msg.equals("null")){
				msg="";
			}
			return event.getChannel().sendFile(file, new MessageBuilder().append(msg).build()).complete();
		} catch (IOException e) {
			Log.logError(e);
		}
		return sendFile(event,msg,file);
	}
	/**
	 * Sends a message which will be deleted after a period of time
	 * @param event message recieved
	 * @param msg message to send in response
	 * @param timeout time in seconds after which the message will be deleted
	 */
	public static void sendTempMessage(MessageReceivedEvent event, String msg,long timeout){
		final MessageReceivedEvent FEvent=event;
		final String FMsg=msg;
		final long FTimeout=timeout;
		Settings.executor.execute(new Runnable(){
			public void run(){
				try {
					String id=sendMessageFormated(FEvent, FMsg).getId();
					TimeUnit.SECONDS.sleep(FTimeout);
					FEvent.getChannel().deleteMessageById(id).complete();
				} catch (Exception e) {
					Log.log("ERROR", "error sending delayed message");
					Log.logShortError(e, 5);
				}
				
			}
		});
	}
	/**
	 * 
	 * Formats the message <br/>
	 * Special formatting <br/> 
	 * %userMention% mentions the user that sent the message <br/>
	 * %userName% prints name of user that sent the message <br/>
	 * %selfMention% mentions the bot<br/>
	 * %mentionMention% mentions the first mentioned user in message<br/>
	 * %mentionName% name of the first mentioned user in the message<br/>
	 * @param event message received
	 * @param msg message to send in response
	 * @return message that was sent
	 */
	public static Message sendMessageFormated(MessageReceivedEvent event,String msg){
		return Lib.sendMessage(event,Lib.FormatMessage(event,msg));
	}
	/**
	 * generic send message, will have wrappers to fix some issues and errors in relation to sending messages
	 * @param event message received
	 * @param msg message to send someone
	 * @return message that was sent
	 */
	public static Message sendMessage(MessageReceivedEvent event,String msg){
			if(msg.length()>2000){
				Vector<String> toSend=splitMessage(msg);
				for(String s:toSend){
					sendPrivate(event,s);
				}
				if(!event.isFromType(ChannelType.PRIVATE)){
					sendMessage(event,"Message was too long. Check your DMs for response");
				}
				return null;
			}
			//if(!SpamControl.isSpam(event, "global")&&!event.isFromType(ChannelType.PRIVATE)&&!msg.contains("too many messages, please wait")) return null;//disabled to avoid a bunch of pain
			Message message=event.getChannel().sendMessage(msg).complete();
			return message;
	}
	public static Message sendPrivate(MessageReceivedEvent event, String msg){
			Message message=event.getAuthor().getPrivateChannel().sendMessage(msg).complete();
			return message;
	}
	private static Vector<String> splitMessage(String msg){
		Vector<String> splitMsg=new Vector<String>();
		String[] lines=msg.split("\n");
		int length=0;
		String prep="";
		for(String s:lines){
			if(s.length()>2000){
				if(!prep.equals("")){
					splitMsg.add(prep);
					prep="";
					length=0;
				}
				splitMsg.add(s.substring(0, 2000));
				splitMsg.addAll(splitMessage(s.substring(2000)));
			}
			else{
				if(s.length()+length<2000){
					prep+="\n"+s;
					length+=s.length()+1;
				}
				else{
					splitMsg.add(prep);
					prep=s;
					length=s.length();
				}
			}
		}
		splitMsg.add(prep);
		System.out.println(splitMsg);
		for(String s:splitMsg){
			System.out.println(s.length()+" "+s);
		}
		return splitMsg;
	}
	/**
	 * Formats the message <br/>
	 * Special formatting <br/> 
	 * %userMention% mentions the user that sent the message <br/>
	 * %userName% prints name of user that sent the message <br/>
	 * %selfMention% mentions the bot<br/>
	 * %mentionMention% mentions the first mentioned user in message<br/>
	 * %mentionName% name of the first mentioned user in the message<br/>
	 * @param event message received
	 * @param msg message to send in response
	 * @return string of formatted message to send
	 */
	public static String FormatMessage(MessageReceivedEvent event,String msg){
		return msg.replace("%userMention%", event.getAuthor().getAsMention()).
				replace("%userName%", event.getAuthor().getName()).
				replace("%selfMention%", event.getJDA().getSelfUser().getAsMention()).
				replace("%mentionMention%", event.getMessage().getMentionedUsers().size()>0?event.getMessage().getMentionedUsers().get(0).getAsMention():event.getAuthor().getAsMention()).
				replace("%mentionName%",event.getMessage().getMentionedUsers().size()>0?event.getMessage().getMentionedUsers().get(0).getName():event.getAuthor().getName());
	}
	/**
	 * Formats and send message for guild member joining <br/>
	 * Special formatting<br/>
	 * %userMention% mentions the user that joined<br/>
	 * %userName% prints name of the user<br/>
	 * %guildName% prints name of the server
	 * @param event user join event
	 * @param msg message to send in response
	 * @return message sent
	 */
	public static Message sendMessageFormated(GuildMemberJoinEvent event,String msg){
		Message message=event.getGuild().getPublicChannel().sendMessage(Lib.FormatMessage(event,msg)).complete();
		return message; 
	}
	/**
	 * Formats and send message for guild member joining <br/>
	 * Special formatting<br/>
	 * %userMention% mentions the user that joined<br/>
	 * %userName% prints name of the user<br/>
	 * %guildName% prints name of the server
	 * event user join event
	 * @param msg message to send in response
	 * @return string of formatted message to send
	 */
	public static String FormatMessage(GuildMemberJoinEvent event,String msg){
		return msg.replace("%userMention%", event.getMember().getAsMention()).
		replace("%userName%", event.getMember().getNickname()).
		replace("%guildName%",event.getGuild().getName());
	}
	//stuff for handling elements in a web page
	/**
	 * Gets elements within an element based on tag name
	 * @param element elements to get nestled element
	 * @param tags list of tags to get the nestled element for, going right to left, i.e. ["first","second"] gets &lt;second/&gt; in &lt;first&gt;&lt;second/&gt;&lt;/first&gt; 
	 * @return list of elements that match the parameters
	 */
	public static Elements getNestedDeep(Elements element,String[] tags){
		Elements ele=element;
		for(String s:tags){
			ele=Lib.getNested(ele, s);
		}
		return ele;
	}
	/**
	 * Gets elements within an element based on tagname
	 * @param element elements to get nestled element
	 * @param tag tag to get within the main element
	 * @return list of elements that match the parameters
	 */
	public static Elements getNested(Elements element, String tag){
		Elements ele=new Elements();
		if(element==null){
			return new Elements();
		}
		for(Element eles:element){
			ele.addAll(eles.getElementsByTag(tag));
		}
		return ele;
	}
	/**
	 * 
	 * Gets elements within an element based on tagname
	 * @param element elements to get nestled element
	 * @param index index of the element in the list of elements that match parameter for each specific element in given list
	 * @param tag tag to get within the main element
	 * @return list of elements that match the parameters
	 */
	public static Elements getNestedItem(Elements element,int index,String tag){
		Elements ele=new Elements();
		for(Element eles:element){
			try{
				ele.add(eles.getElementsByTag(tag).get(index));
			}catch(Exception e){};
		}
		return ele;
	}
	/**
	 * Gets elements after what matches element filter
	 * @param elements element list for which you want get the element after for
	 * @param after filter containing parameters for the element
	 * @return elements after any element matching element filter
	 */
	public static Elements getElesAfter(Elements elements, ElementFilter after){
		Elements afters=new Elements();
		boolean found=false;
		for(Element e:elements){
			if(found){
				afters.add(e);
				found=false;
			}
			else{
				if(after.filtered(e)){
					found=true;
				}
			}
		}
		return afters;
	}
	/**
	 * Gets elements after what matches element filter
	 * @param elements element list for which you want get the element after for
	 * @param after filter containing parameters for the element
	 * @return elements after any element matching element filter
	 */
	public static Element getEleAfter(Elements elements,ElementFilter after){
		Elements e=getElesAfter(elements,after);
		if(e.size()>0){
			return e.first();
		}
		return null;
		
	}
	/**
	 * gets table rows in a table
	 * @param table table element &lt;tbody&gt;
	 * @return number of rows, does not include &lt;thead&gt;
	 */
	public static int getTRCount(Element table){
		Elements tr=table.getElementsByTag("tr");
		return tr.size();
	}
	/**
	 * Gets columns in the row
	 * @param table table element &lt;tbody&gt;
	 * @param row row #
	 * @return number of columns
	 */
	public static int getTDCount(Element table,int row){
		Elements td=table.getElementsByTag("tr").get(row).getElementsByTag("td");
		return td.size();
	}
	/**
	 * gets a specific cell in a table
	 * @param row row #
	 * @param col column #
	 * @param table table element &lt;tbody&gt;
	 * @return element representing the specific cell
	 */
	public static Element getCell(int row, int col,Element table){
		return table.getElementsByTag("tr").get(row).getElementsByTag("td").size()>0? 
				table.getElementsByTag("tr").get(row).getElementsByTag("td").get(col):null;
	}
	/**
	 * get specific row
	 * @param row row #
	 * @param table table element &lt;tbody&gt;
	 * @return element representing the specific header row
	 */
	public static Element getHeader(int row, Element table){
		return table.getElementsByTag("tr").get(row).getElementsByTag("th").get(0);
	}
	/**
	 * gets link in element
	 * @param ele element to get link from
	 * @return String
	 */
	public static String getLink(Element ele){
		Elements links = ele.getElementsByTag("a");
		for (Element link : links) {
			return link.attr("abs:href");
		}
		return "";
	}
	//reading stuff for custom XML class
	/**
	 * A wrapper for getting an array for an element easily
	 * @param ele element to search for array
	 * @param tagname name of the element whose text is the array
	 * @return
	 */
	public static String[] textArray(XML.Elements ele,String tagname){
		String[] out=new String[ele.getChilds(tagname).size()];
		int i=0;
		for(XML.Elements e:ele.getChilds(tagname)){
			out[i]=e.getText();
			i++;
		}
		return out;
	}
	public static XML.Elements[] elementArray(XML.Elements ele,String tagname){
		XML.Elements[] out=new XML.Elements[ele.getChilds(tagname).size()];
		int i=0;
		for(XML.Elements e:ele.getChilds(tagname)){
			out[i]=e;
			i++;
		}
		return out;
	}
	/**
	 * A wrapper to get String value for element without crashing process if it doesn't exist
	 * @param root Element you want to search for the element
	 * @param tagname name of element you want to get
	 * @return
	 */
	public static String getString(XML.Elements root,String tagname){
		try{
			return root.getChilds(tagname).get(0).getText();
		}
		catch(IndexOutOfBoundsException e){
			Log.logError(e);
			return "";
		}
	}
	/**
	 * A wrapper to get the boolean value for an element without crashing process if it doesn't exist
	 * @param normal default value if boolean value is not found
	 * @param ele Element you want to search for the element
	 * @param tagname name of element you want to get
	 * @return
	 */
	public static boolean getBooleanSetting(boolean normal,XML.Elements ele,String tagname){
		if(ele.getChilds(tagname).size()<=0){
			return normal;
		}
		if(normal){
			return ele.getChilds(tagname).get(0).getText().trim().equals("false")?false:true;
		}
		else{
			return ele.getChilds(tagname).get(0).getText().trim().equals("true")?true:false;
		}
	}
	/**
	 * A wrapper to get int value for element without crashing process if it doesn't exist
	 * @param ele element within which you want to search for
	 * @param tagname name of element
	 * @return number -1 if invalid
	 */
	public static int getNumber(XML.Elements ele, String tagname){
		try{
			return Integer.parseInt(ele.getChilds(tagname).get(0).getText());
		}catch(Exception e){
			Log.log("ERROR", "Invalid for element"+tagname);
			Log.logShortError(e, 5);
		}
		return -1;
	}
	/**
	 * A wrapper to get long value for element without crashing process if it doesn't exist
	 * @param ele element within which you want to search for
	 * @param tagname name of element
	 * @return number -1 if invalid
	 */
	public static long getLong(XML.Elements ele, String tagname){
		try{
			return Long.parseLong(ele.getChilds(tagname).get(0).getText());
		}catch(Exception e){
			Log.log("ERROR", "Invalid for element"+tagname);
			Log.logShortError(e, 5);
		}
		return -1L;
	}
	//random utilities
	/**
	 * makes sure that string is at least x length, padding it w/ spaces
	 * @param s string to pad
	 * @param pad minimum length of string
	 * @return padded string
	 */
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
		boolean negativeStartFlag=true;
		boolean dotFlag=true;
		for(char c:s.trim().toCharArray()){
			if(!Character.isDigit(c)){
				if(!(c=='-'&&negativeStartFlag||c=='.'&&dotFlag)){
					return false;
				}
				if(c=='.'&&dotFlag){
					dotFlag=false;
				}
			}
			negativeStartFlag=false;
		}
		if(s.length()>0){
		return true;
		}
		else return false;
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
			i="-1";
		}
		return Integer.parseInt(i);
	}
	/**
	 * gets numbers(integers) from string
	 * @param s string to get numbers from
	 * @return array of all the numbers in the string
	 */
	public static int[] extractNumbers(String s){
		String i="";
		boolean number=false;//if currently indexing a number
		boolean dot=false;//decimal place
		for(char c:s.trim().toCharArray()){
			if(number){
				if(Character.isDigit(c)||dot?false:c=='.'){
					if(c=='.'){
						dot=true;
					}
					i+=c;
				}
				else{
					i+=",";
					number=false;
					dot=false;
				}
			}
			else{
				if(Character.isDigit(c)){
					i+=c;
					number=true;
				}
			}
		}
		if(i.endsWith(",")){
			i=i.substring(0, i.length()-1);
		}
		String[] nums=i.split(",");
		int[] ints=new int[nums.length];
		for(int d=0;d<nums.length;d++){
			ints[d]=Integer.parseInt(nums[d]);
		}
		return ints;
	}
	/**
	 * if x array contains obj
	 * @param obj object if it contains
	 * @param os array of objects
	 * @return if os contains obj
	 */
	public static boolean contains(Object obj,Object[] os){
		for(Object o:os){
			if(o.equals(obj)){
				return true;
			}
		}
		return false;
	}
	/**
	 * Adds up all the numbers in array
	 * @param numbers int array to add all the numbers for
	 * @return
	 */
	public static int Summation(int[] numbers){
		int sum=0;
		for(int i:numbers){
			sum+=i;
		}
		return sum;
	}
	/**
	 * converts a string array to 
	 * @param args
	 * @return
	 */
	public static String extract(String[] args){
		String out="";
		for(String s:args){
			out+=" "+s;
		}
		return out.substring(1);
	}
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	/**
	 * 
	 * @param s
	 * @return time represented in seconds
	 */
	public static int extractTime(String s){
		int totalTime=0;
		String time;
		String[] pieces;
		if(s.contains(" in ")){
			time=s.substring(s.lastIndexOf(" in "));
			pieces=time.split(" ");
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
		}
		if(s.contains(" at ")&&s.contains(":")){
			time=s.substring(s.lastIndexOf(" at "));
			int hour=Lib.extractNumber(time.substring(0,time.indexOf(':')));
			int minute=Lib.extractNumber(time.substring(time.indexOf(':')));
			if(s.toLowerCase().contains("pm"))hour+=12;
			Calendar future=Calendar.getInstance();
			future.setTime(new Date(System.currentTimeMillis()+(totalTime*1000)));
			future.set(Calendar.SECOND, 0);
			int diff=minute-future.get(Calendar.MINUTE);
			if(diff<0)future.add(Calendar.HOUR_OF_DAY, 1);
			future.add(Calendar.MINUTE, diff);
			diff=hour-future.get(Calendar.HOUR_OF_DAY);
			if(diff<0)future.add(Calendar.DAY_OF_YEAR, 1);
			future.add(Calendar.HOUR_OF_DAY, diff);
			totalTime=(int) ((future.getTimeInMillis()-System.currentTimeMillis())/1000);
		}
		return totalTime;
	}
}
