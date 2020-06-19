import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLCommand {
	Connection connect;
	Statement state;
	public SQLCommand() {
		try {
			connect = DriverManager.getConnection("jdbc:postgresql://localhost/postgres","postgres","wjdtjd864");
			state = connect.createStatement();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public ArrayList<Hospital> searchHospital(String hospitalName){
		try {
			ArrayList<Hospital> result = new ArrayList<Hospital>();
			
			ResultSet rs = state.executeQuery("select H.id, H.name, H.address, H.phone from Hospital H where H.name LIKE '%" + hospitalName+ "%'");
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
	
	public ArrayList<Ingredient> searchIngredient(String IngredientName){
		try {
			ArrayList<Ingredient> result = new ArrayList<Ingredient>();
			
			ResultSet rs = state.executeQuery("select I.id, I.name, I.eat, I.explain from Ingredient I where I.name LIKE '%" + IngredientName + "%'");
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
	
	public ArrayList<Symptom> searchSymptom(String SymptomName){
		try {
			ArrayList<Symptom> result = new ArrayList<Symptom>();
			
			ResultSet rs = state.executeQuery("select S.id, S.name from Symptom S where S.name LIKE '%" + SymptomName +"%'");
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
}
