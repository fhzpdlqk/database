import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLCommand {
	private String stmt;
	private PreparedStatement p;
	private Connection connection;
	
	public SQLCommand() {
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres","postgres","qwer1234");		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Product equalProduct(String productName) throws SQLException{
		try {
			stmt = "select P.id, P.name, P.nutrition, P.imgURL from Product P where P.name = ?";
			p = connection.prepareStatement(stmt);			
			p.setString(1, productName);
			
			ResultSet rs = p.executeQuery();
			rs.next();
			
			Product product = new Product(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4));
			
			return product;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Ingredient equalIngredient(String ingredientName) throws SQLException{
		try {
			stmt = "select I.id, I.name, I.eat, I.explain from Ingredient I where I.name = ?";
			p = connection.prepareStatement(stmt);			
			p.setString(1, ingredientName);
			
			ResultSet rs = p.executeQuery();
			rs.next();
			
			Ingredient ingredient = new Ingredient(rs.getInt(1),rs.getString(2), rs.getInt(3), rs.getString(4));
			
			return ingredient;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
	}
	
	public ArrayList<Product> searchProduct(String productName){
		try {
			ArrayList<Product> result = new ArrayList<Product>();
			stmt = "select P.id, P.name, P.nutrition, P.imgURL from Product P where P.name LIKE '%?%'";
			p = connection.prepareStatement(stmt);
			p.setString(1, productName);
			
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				Product product = new Product(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4));
				result.add(product);
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Hospital> searchHospital(String hospitalName){
		try {
			ArrayList<Hospital> result = new ArrayList<Hospital>();		
			stmt = "select H.id, H.name, H.address, H.phone from Hospital H where H.name LIKE '%?%'";
			p = connection.prepareStatement(stmt);
			p.setString(1, hospitalName);
			
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				Hospital hospital = new Hospital(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
				result.add(hospital);
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Ingredient> searchIngredient(String ingredientName){
		try {
			ArrayList<Ingredient> result = new ArrayList<Ingredient>();
			stmt = "select I.id, I.name, I.eat, I.explain from Ingredient I where I.name LIKE '%?%'";
			p = connection.prepareStatement(stmt);
			p.setString(1, ingredientName);
			
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				Ingredient ingredient = new Ingredient(rs.getInt(1),rs.getString(2), rs.getInt(3), rs.getString(4));
				result.add(ingredient);
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Symptom> searchSymptom(String symptomName){
		try {
			ArrayList<Symptom> result = new ArrayList<Symptom>();
			stmt = "select S.id, S.name from Symptom S where S.name LIKE '%?%'";
			p = connection.prepareStatement(stmt);
			p.setString(1, symptomName);
			
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				Symptom symptom = new Symptom(rs.getInt(1), rs.getString(2));
				result.add(symptom);
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet productList(String productName){
		try {
			stmt = "select P.id, P.name, max(I.eat)\n" + 
					"from Product P inner join P_I PI on (P.id = PI.productID)\n" + 
					"inner join Ingredient I on (PI.ingredientID = I.id)\n" + 
					"where P.name LIKE '%?%'\n" +
					"group by P.id, P.name";
			p = connection.prepareStatement(stmt);
			p.setString(1, productName);
			
			ResultSet rs = p.executeQuery();
			
			return rs;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet productDetail(int pId){
		try {
			stmt = "select I.name Iname, I.explain, I.eat, S.name Sname\n" + 
					"from Product P inner join P_I PI on (P.id = PI.productID)\n" + 
					"inner join Ingredient I on (PI.ingredientID = I.id)\n" + 
					"inner join I_S on (I.id = I_S.ingredientID)\n" + 
					"inner join Symptom S on (I_S.symptomID = S.id)\n" + 
					"where P.id = ? order by Iname;";
			p = connection.prepareStatement(stmt);
			p.setInt(1, pId);
			
			ResultSet rs = p.executeQuery();
			
			return rs;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	public ResultSet iIngredientList(String ingredientName){
		try {
			stmt = "select I.id, I.name, I.eat\n" + 
					"from Ingredient I\n" + 
					"where I.name LIKE '%?%';";
			p = connection.prepareStatement(stmt);
			p.setString(1, ingredientName);
			
			ResultSet rs = p.executeQuery();
			
			return rs;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	public ResultSet ingredientList(String symptomName) {
		try {
			stmt = "select I.id, I.name, I.eat\n" + 
					"from Symptom S\n" + 
					"inner join I_S on (S.id = I_S.symptomID)\n" + 
					"inner join Ingredient I on (I_S.ingredientID = I.id)\n" + 
					"where S.name LIKE '%?%';";
			p = connection.prepareStatement(stmt);
			p.setString(1, symptomName);
			
			ResultSet rs = p.executeQuery();
			
			return rs;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet ingredientDetail(int iId) {
		try {
			stmt = "select I.name, I.explain, I.eat, S.name\n" + 
					"from Ingredient I\n" + 
					"inner join I_S on (I.id = I_S.ingredientID)\n" + 
					"inner join Symptom S on (I_S.symptomID = S.id)\n" + 
					"where I.id = ?;";
			p = connection.prepareStatement(stmt);
			p.setInt(1, iId);
			
			ResultSet rs = p.executeQuery();
			
			return rs;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void insertI_S(int ingredientId, int symptomId, int hospitalId, int eat, String explain) {
		try {
			stmt = "insert into I_S values (?, ?, ?);";
			p = connection.prepareStatement(stmt);
			p.setInt(1, ingredientId);
			p.setInt(2, symptomId);
			p.setInt(3, hospitalId);
			
			p.executeUpdate();
			
			stmt = "Update Ingredient SET eat = ? where id = ?;";
			p = connection.prepareStatement(stmt);
			p.setInt(1, eat+1);
			p.setInt(2, ingredientId);
			
			p.executeUpdate();
			
			stmt = "Update Ingredient SET explain = '?' where id = ?;";
			p = connection.prepareStatement(stmt);
			p.setString(1, explain);
			p.setInt(2, ingredientId);
			
			p.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
