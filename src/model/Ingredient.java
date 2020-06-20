package model;

public class Ingredient {
	private int id;
	private String name;
	private int eat;
	private String explain;
	
	public Ingredient(int id, String name, int eat, String explain) {
		this.setId(id);
		this.setName(name);
		this.setEat(eat);
		this.setExplain(explain);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEat() {
		return eat;
	}

	public void setEat(int eat) {
		this.eat = eat;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}
	
	
}
