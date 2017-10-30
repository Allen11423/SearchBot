package Search.Library.mom;

import Search.util.rng.RandomLibs;

//flavor text Strings for getting points
public enum PointGetData {
	M1("%userName% got %point% points, the same amount of thrusts it takes to finish your mom!"),
	M2("%userName% got %point% points, better bank them like how I banked your mom!"),
	M3("%userName% got %point% points, hope you're as satisfied as when I satisfied your mom!"),
	M4("%userName% got %point% points, the number of times I banged your mom last night!"),
	M5("%userName% got %point% points, the same number of dicks your mom got last night!"),
	M6("%userName% got %point% points, hang onto those like your mom hangs onto this dick!"),
	M7("%userName% got %point% points, the number of escort services your mom works for!"),
	M8("%userName% got %point% points, the only penis length that can satisfy your mom!"),
	M9("%userName% got %point% points, the number of dudes at your mom's gang bang!"),
	M10("%userName% got %point% points, hey isn't that the same street number your mom turns tricks on?"),
	M11("%userName% got %point% points, better store them like I store my point in your mom."),
	M12("%userName% got %point% points, better deposit it like how I deposit it in your mom!");
	
	
	
	private String msg;
	PointGetData(String msg){
		this.msg=msg;
	}
	public String getMsg(){
		return msg;
	}
	public static PointGetData getDefaults(){
		return RandomLibs.SelectRandom(PointGetData.values());
	}
	public String toString(){
		return msg;
	}
	
}
