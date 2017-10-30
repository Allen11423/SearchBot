package Search.Library;

import java.util.ArrayList;
import java.util.HashMap;

import Search.util.Lib;
import Search.util.rng.RandomLibs;
import XML.Attribute;
import XML.Elements;

public class Flavor {
	protected String nameID;
	//stores global ones that are universal
	protected String[] global=new String[]{};
	//master map to store flavors for each guild
	protected HashMap<String, String[]> values =new HashMap<String,String[]>();
	public Flavor(String nameID){
		this.nameID=nameID;
	}
	public Flavor(Elements root){// default constructor for root cases
		nameID=root.getAttribute("name").getValue();
		
		Elements global=root.getChilds("global").get(0);
		this.global=global.getText().split(",");
		ArrayList<Elements> ids=root.getChilds("subFlavor");
		for(int i=0;i<ids.size();i++){
			if(ids.get(i).getAttribute("id")==null){

			}
		}
	}
	/**
	 * ID for identifying the specific flavor component
	 * @return
	 */
	public String nameID(){
		return nameID;
	}
	/**
	 * Retrieves a potential value for the flavor text
	 * @param id Id of guild to get the flavor text for or whatever
	 * @return
	 */
	public String aValue(String id){
		if(values.containsKey(id)){
			return RandomLibs.SelectRandom(Lib.concat(global, values.get(id)));
		}
		else{
			return RandomLibs.SelectRandom(global);
		}
	}
	//use by enums to add all values to array
	public void addAlltoGlobal(String[] values){
		for(String s:values){
			addGlobal(s);
		}
	}
	public String addGlobal(String flavor){
		global=Lib.concat(global, new String[]{flavor});
		return "FlavorText successfully added to global list";
	}
	public String addPrivate(String flavor, String id){
		if(values.containsKey(id)){
			values.put(id, Lib.concat(values.get(id), new String[]{flavor}));
		}
		else{
			values.put(id, new String[]{flavor});
		}
		return "FlavorText successfully added to guild list";
	}
	public Elements parseToElements(){
		Elements root = new Elements("flavor").addAttribute(new Attribute("name",nameID));

		Elements global=new Elements("global");
		global.setText(Lib.extract(this.global, ","));
		root.add(global);


		//add the custom lists for each guild
		for(String d:values.keySet()){
			Elements guildF= new Elements("guildF").addAttribute(new Attribute("id",d));
			guildF.setText(Lib.extract(values.get(d), ","));
			root.add(guildF);
		}
		return root;
	}

}
