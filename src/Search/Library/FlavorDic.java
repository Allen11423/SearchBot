package Search.Library;

import Search.Library.mom.ComboData;
import Search.Library.mom.PointCheckData;
import Search.Library.mom.PointGetData;

public enum FlavorDic {
	pointadd("addPoint",PointGetData.values()),
	pointcheck("checkPoint", PointCheckData.values()),
	pointdelete("deletePoint",PointGetData.values()),
	comboBreak("comboBreak",new String[]{"m-m-m-MOMBO BREAKER!!!"}),
	C1("Combo1",ComboData.C1.options),
	C2("Combo2",ComboData.C2.options),
	C3("Combo3",ComboData.C3.options),
	C4("Combo4",ComboData.C4.options),
	C5("Combo5",ComboData.C5.options);
	
	
	String nameID;
	String[] values;
	FlavorDic(String nameID,Object[] values){
		this.nameID=nameID;
		this.values=new String[values.length];
		for(int i=0;i<values.length;i++){
			this.values[i]=values[i].toString();
		}
	}
	public String toString(){
		return nameID;
	}
	public void addAll(Flavor f){
		f.addAlltoGlobal(this.values);
	}
	

}
