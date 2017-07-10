package Search.commands;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Search.global.record.Log;
import Search.util.Lib;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Search extends CommandGenerics implements commands.Command {

	public boolean called(String[] args, MessageReceivedEvent event) {
		return true;
	}

	public void action(String[] args, MessageReceivedEvent event) {
		try{
		String concat="";
		for(String s:args){
			concat+=s+" ";
		}
		concat.substring(0, concat.length()-1);
		String google = "http://www.google.com/search?q=";
		String search = concat;
		String charset = "UTF-8";
		String param ="";
		boolean safe=true;
		if(event.getChannel().getName().toLowerCase().contains("nsfw")){
			safe=false;
		}
		if(safe){
			param="&safe=strict"+param;
		}
		String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!

		Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)+param).userAgent(userAgent).get().select(".g>.r>a");

		for (Element link : links) {
		    String title = link.text();
		    String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
		    url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

		    if (!url.startsWith("http")) {
		        continue; // Ads/news/etc.
		    }
		    Lib.sendMessage(event, title+"\n"+url);
		    break;
		}
		}catch(Exception e){
			Log.logError(e);
			Lib.sendMessage(event, "error"+e);
		}

	}

	public void help(MessageReceivedEvent event) {
		// TODO Auto-generated method stub

	}

}
