
public class Product {
	private int id;
	private String name;
	private String nutrition;
	private String imgURL;
	
	public Product(int id, String name, String nutrition, String imgURL) {
		this.setId(id);
		this.setName(name);
		this.setNutrition(nutrition);
		this.setImgURL(imgURL);
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
	
	public String getNutrition() {
		return nutrition;
	}
	
	public void setNutrition(String nutrition) {
		this.nutrition = nutrition;
	}
	
	public String getImgURL() {
		return imgURL;
	}
	
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
}
