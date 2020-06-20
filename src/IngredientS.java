import java.util.ArrayList;

public class IngredientS {

	private String Iname;
	private String explain;
	private int eat;
	private ArrayList<String> Sname;
	
	public IngredientS(String Iname, String explain, int eat) {
		this.Iname = Iname;
		this.explain = explain;
		this.eat = eat;
	}
	
	public String getIname() {
		return Iname;
	}
	
	public void setIname(String Iname) {
		this.Iname = Iname;
	}
	
	public String getExplain() {
		return explain;
	}
	
	public void setExplain(String explain) {
		this.explain = explain;
	}
	
	public int getEat() {
		return eat;
	}
	
	public void setEat(int eat) {
		this.eat = eat;
	}
	
	public ArrayList<String> getSname() {
		return Sname;
	}
	
	public void setSname(String Sname) {
		this.Sname.add(Sname);
	}
}
