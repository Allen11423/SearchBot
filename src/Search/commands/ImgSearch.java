package Search.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Search.global.record.Log;
import Search.util.Lib;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ImgSearch extends CommandGenerics implements Command {

	
	public void action(String[] args, MessageReceivedEvent event) {
		try{
		String concat="";
		for(String s:args){
			concat+=s+" ";
		}
		concat.substring(0, concat.length()-1);
		String google = "http://www.google.com/search?tbm=isch&q=";
		String search = concat;
		String charset = "UTF-8";
		String param = "&source=web&sa=X&ved=0ahUKEwiEr5rZrMLSAhXM8YMKHRVzCJcQ_AUICCgB&biw=1440&bih=799#imgrc=XWXPqrX1RFJiaM:";
		boolean safe=true;
		if(event.getChannel().getName().toLowerCase().contains("nsfw")){
			safe=false;
		}
		if(safe){
			param="&safe=strict"+param;
		}
		String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!
		System.out.println(Jsoup.connect(google + URLEncoder.encode(search, charset)+param).followRedirects(true).userAgent(userAgent).timeout(200000).get());
		Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)+param).followRedirects(true).userAgent(userAgent).timeout(200000).get().select("#ires td a img");
		for (Element link : links) {
		  //  String title = link.text();
		    String url = link.absUrl("src"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
		    //url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
		    System.out.println(url);
		    if (!url.startsWith("http")) {
		        continue; // Ads/news/etc.
		    }
		    //System.out.println("Title: " + title);
		    BufferedImage image=ImageIO.read(new URL(url).openStream());
		    ImageIO.write(image, "PNG", new File("search.png"));
		    event.getChannel().sendFile(new File("search.png"),null ).queue();
		    Files.delete(new File("search.png").toPath());
		    break;
		}
		}catch(Exception e){
			Log.logError(e);
			Lib.sendMessage(event, "error");
		}
	}
	public void help(MessageReceivedEvent event) {
		// TODO Auto-generated method stub

	}

}
