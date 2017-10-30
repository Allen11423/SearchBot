package Search.Library.mom;

import Search.util.rng.RandomLibs;

//flavor text strings for combos Cx = flavor text for that combo number
public enum ComboData {
	C1(new String[]{"%userMention% Your current mombo is only 1.0, pump up that score like I pump Your mom!",
			"%userMention% Your current mombo is only 1.0, git gud before I git gud with Your mom",
			"%userMention% Your current mombo is 1.0, try harder!  As hard as my penis when I'm with Your mom!",
			"%userMention% Your current mombo is 1.0, get that score up like how I get it up for Your mom.",
			"%userMention% Your current mombo is 1.0, weak sauce ..like how I gave Your mom a week's worth of my sauce.",
			"%userMention% Your current mombo is 1.0, try harder, faster, longer, deeper, stiffer ...how Your mom likes it.",
			}),
	C2(new String[]{"%userMention% Your current mombo is 2.0, the amount of fingers Your mom needs",
			"%userMention% Your current mombo is 2.0, the same number of holes I used on Your mom",
			"%userMention% Your current mombo is 2.0, like the number of black guys needed to please Your mom",
			"%userMention% Your current mombo is 2.0, that's almost the number of dicks that will satisfy Your mom!",
			"%userMention% Your current mombo is 2.0, the number of escort services Your mom works for!",
			"%userMention% Your current mombo is 2.0, you still need to push farther, like I pushed into Your mom!"}),
	C3(new String[]{"%userMention% Your current mombo is 3.0, Your mom's current dick per minute ratio!",
			"%userMention% Your current mombo is 3.0, that's the max inches Your mom can take!",
			"%userMention% Your current mombo is 3.0, Your mom's used holes also increased to 3.0",
			"%userMention% Your current mombo is 3.0, the number of dudes in Your mom's porno!",
			"%userMention% Your current mombo is 3.0, that's how many hookers Abby found on craigslist.  They were all Your mom!",
			"%userMention% Your current mombo is 3.0, the same number ob bleachings Your mom's bunghole needs!"}),
	C4(new String[]{"%userMention% Your current mombo is 4.0, the number of dudes Your mom had in her last night!",
			"%userMention% Your current mombo is 4.0, the same number of fingers Your mom likes!",
			"%userMention% Your current mombo is 4.0, the number of times I satisfied Your mom last night!",
			"%userMention% Your current mombo is 4.0, you're almost beating Your mom's deepthroats per minute!",
			"%userMention% Your current mombo is 4.0, that's Your mom's vaginal GPA!",
			"%userMention% Your current mombo is 4.0, the amount of times Your mom's been around the block today!"}),
	C5(new String[]{"%userMention% Your current mombo is 5.0, the minimum penis length Your mom accepts!",
			"%userMention% Your current mombo is 5.0, the number of times Your mom tried anal!",
			"%userMention% Your current mombo is 5.0.  Your mom's mombo is also 5!",
			"%userMention% Your current mombo is 5.0, that's the inches of accessible space in Your mom's anal canal!",
			"%userMention% Your current mombo is 5.0, this is also Your mom's maximum dick to hole capacity. :rainbow:The More You Know:rainbow:",
			"%userMention% Your current mombo is 5.0, rack up those mom points like how I racked up Your mom with my point!"}),
	C6(new String[]{"you have a mom combo of %combo%"});
	
	public String[] options;
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
