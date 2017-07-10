package Search.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Search.global.record.Log;
import Search.util.Lib;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import net.dv8tion.jda.core.entities.MessageEmbed.ImageInfo;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ImgSearch extends CommandGenerics implements commands.Command {


	public void action(String[] args, MessageReceivedEvent event) {
		String query="";
		for(String s:args){
			query+=s+" ";
		}

		String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
		String url = "https://www.google.com/search?site=imghp&tbm=isch&source=hp&q="+query+"&gws_rd=cr";
		boolean safe=true;
		if(event.getChannel().getName().toLowerCase().contains("nsfw")){
			safe=false;
		}
		if(safe){
			url+="&safe=strict";
		}
		List<String> resultUrls = new ArrayList<String>();
		try {
			org.jsoup.nodes.Document doc = Jsoup.connect(url).userAgent(userAgent).referrer("https://www.google.com/").get();
			Elements elements = doc.select("div.rg_meta");
			for (Element element : elements) {
				if (element.childNodeSize() > 0) {
					JsonElement jelement = new JsonParser().parse(element.childNode(0).toString());
					JsonObject  jobject = jelement.getAsJsonObject();
					String result= jobject.get("ou").getAsString();
					resultUrls.add(result);
				}
			}
			for (String link : resultUrls) {
				//  String title = link.text();
				//url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
				if (!link.startsWith("http")) {
					continue; // Ads/news/etc.
				}
				//System.out.println("Title: " + title);
				BufferedImage image;
				try{
				image=ImageIO.read(new URL(link).openStream());
				ImageIO.write(image, "PNG", new File("search.png"));
				}catch(IOException|IllegalArgumentException e1){//403 errors and other related ones
					continue;
				}
				if(new File("search.png").length()>8388608){
					MessageEmbedImpl embed=new MessageEmbedImpl();
					List<Field> fields=new Vector<Field>();
					Field field=new Field("Image Result", link, false);
					fields.add(field);
					embed.setFields(fields);
					
					BufferedImage img=ImageIO.read(new File("search.png"));//to get width and height
					embed.setImage(new ImageInfo(link, link, img.getWidth(), img.getHeight()));
					Lib.sendEmbed(event, embed);
				}
				else{
					event.getChannel().sendFile(new File("search.png"),null ).queue();
				}
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
