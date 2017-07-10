package Search.util;

import XML.Attribute;
import XML.Elements;

/**
 * Generic class to deal with things that are said at a later time
 * @author Allen
 *
 */
public class Reminder {
	private Long time;
	private String msg;
	private Long ID;
	private String channelID;
	public Reminder(Long ID){
		this.ID=ID;
	}
	public Reminder(Elements root){
		this.ID=Long.parseLong(root.getAttribute("ID").getValue());
		time=Lib.getLong(root, "time");
		msg=Lib.getString(root, "msg");
		
	}
	public Long getTime() {
		return time;
	}
	public String getMsg() {
		return msg;
	}
	public Long getID() {
		return ID;
	}
	public String getChannelID() {
		return channelID;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Elements toElements(){
		Elements root=new Elements("reminder");
		root.addAttribute(new Attribute("ID",""+ID));
		
		Elements time=new Elements("time").setText(""+this.time);
		root.add(time);
		
		Elements msg=new Elements("msg").setText(this.msg);
		root.add(msg);
		
		return root;
	}
	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}
}
