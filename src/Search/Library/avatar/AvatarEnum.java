package Search.Library.avatar;

public enum AvatarEnum {
	a1("a0.jpg"),
	a2("a1.jpg"),
	a3("a2.png");
	String name;
	AvatarEnum(String name){
		this.name=name;
	}
	public String toString(){
		return name;
	}
}
