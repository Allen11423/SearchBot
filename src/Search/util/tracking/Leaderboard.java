package Search.util.tracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Used to track a pointable item aka anything with a point value associated with it
 * @author Allen
 *
 */
public class Leaderboard {
	private ArrayList<PointableItem> leaderboard= new ArrayList<PointableItem>();
	
	public Leaderboard(){
	}
	
	//initial sort using insertion sort as it won't be too long and it's super easy to implement
	public Leaderboard(Collection<PointableItem> initializeItems){
		addItems(initializeItems);
	}
	/**
	 * Safe get range of leaderbaord array
	 * @param start
	 * @param end
	 */
	public List<PointableItem> getRange(int start, int end){
		resort();
		if(start>leaderboard.size()){
			return new ArrayList<PointableItem>();
		}
		else{
			return leaderboard.subList(start, end<=leaderboard.size()?end:leaderboard.size());
		}
	}
	public int getRank(PointableItem rank){
		resort();
		for(int i=0;i<leaderboard.size();i++){
			if(leaderboard.get(i).equals(rank)){
				return i+1;
			}
		}
		return -1;
	}
	//insertion sorting
	public void addItem(PointableItem item){
		if(leaderboard.size()==0){
			leaderboard.add(item);
		}
		else if(leaderboard.contains(item)){//in case
			return;
		}
		else{
			if(item.getPointValue()<leaderboard.get(leaderboard.size()-1).getPointValue()){
				leaderboard.add(item);//add to end check
			}
			else{
				for(int i =0;i<leaderboard.size();i++){
					if(leaderboard.get(i).getPointValue()<=item.getPointValue()){
						leaderboard.add(i, item);
						break;//break from array, insert done
					}
				}
			}
		}
	}
	public void addItems(Collection<PointableItem> initializeItems){
		for(PointableItem p:initializeItems){
			addItem(p);
		}
	}
	//modified bubble sort for subsequent calls as order would likely not have changed much, only need to swap a few things
	private void resort(){
		int jump=0;//jumps to resume point, slight efficiency improvement about 25% faster on a large random data set
		for(int i=0;i<leaderboard.size()-1;i++){
			if(leaderboard.get(i).getPointValue()<leaderboard.get(i+1).getPointValue()){
				if(i>jump)jump=i;
				//standard swap of bubble sort
				PointableItem p=leaderboard.get(i);
				leaderboard.set(i, leaderboard.get(i+1));
				leaderboard.set(i+1, p);
				//to continue moving item up
				i--;
				i--;
				i=i<-1?-1:i;
			}
			else if(i<jump){
				i=jump;
			}
		}
	}
	
	
}
