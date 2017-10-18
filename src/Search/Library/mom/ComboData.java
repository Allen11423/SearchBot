package Search.Library.mom;

import Search.util.rng.RandomLibs;

//flavor text strings for combos Cx = flavor text for that combo number
public enum ComboData {
	C1(new String[]{"Your current mombo is only 1.0, pump up that score like I pump your mom!",
			"Your current mombo is only 1.0, git gud before I git gud with your mom",
			"Your current mombo is 1.0, try harder!  As hard as my penis when I'm with your mom!",
			"Your current mombo is 1.0, get that score up like how I get it up for your mom.",
			"Your current mombo is 1.0, weak sauce ..like how I gave your mom a week's worth of my sauce.",
			"Your current mombo is 1.0, try harder, faster, longer, deeper, stiffer ...how your mom likes it.",
			}),
	C2(new String[]{"Your current mombo is 2.0, the amount of fingers your mom needs",
			"Your current mombo is 2.0, the same number of holes I used on your mom",
			"Your current mombo is 2.0, like the number of black guys needed to please your mom",
			"Your current mombo is 2.0, that's almost the number of dicks that will satisfy your mom!",
			"Your current mombo is 2.0, the number of escort services your mom works for!",
			"Your current mombo is 2.0, you still need to push farther, like I pushed into your mom!"}),
	C3(new String[]{"Your current mombo is 3.0, your mom's current dick per minute ratio!",
			"Your current mombo is 3.0, that's the max inches your mom can take!",
			"Your current mombo is 3.0, your mom's used holes also increased to 3.0",
			"Your current mombo is 3.0, the number of dudes in your mom's porno!",
			"Your current mombo is 3.0, that's how many hookers Abby found on craigslist.  They were all your mom!",
			"Your current mombo is 3.0, the same number ob bleachings your mom's bunghole needs!"}),
	C4(new String[]{"Your current mombo is 4.0, the number of dudes your mom had in her last night!",
			"Your current mombo is 4.0, the same number of fingers your mom likes!",
			"Your current mombo is 4.0, the number of times I satisfied your mom last night!",
			"Your current mombo is 4.0, you're almost beating your mom's deepthroats per minute!",
			"Your current mombo is 4.0, that's your mom's vaginal GPA!",
			"Your current mombo is 4.0, the amount of times your mom's been around the block today!"}),
	C5(new String[]{"Your current mombo is 5.0, the minimum penis length your mom accepts!",
			"Your current mombo is 5.0, the number of times your mom tried anal!",
			"Your current mombo is 5.0.  Your mom's mombo is also 5!",
			"Your current mombo is 5.0, that's the inches of accessible space in your mom's anal canal!",
			"Your current mombo is 5.0, this is also your mom's maximum dick to hole capacity. :rainbow:The More You Know:rainbow:",
			"Your current mombo is 5.0, rack up those mom points like how I racked up your mom with my point!"}),
	C6(new String[]{"you have a mom combo of %combo%"});
	
	private String[] options;
	private ComboData(String[] options){
		this.options=options;
	}
	public String getRand(){
		return RandomLibs.SelectRandom(options);
	}
	public static ComboData get(int combo){
		if(combo<=5){
			return ComboData.values()[combo-1];
		}
		else{
			return C6;
		}
	}

}
