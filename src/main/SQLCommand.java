package main;

import model.Symptom;
import model.Hospital;
import model.Ingredient;
import model.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


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
	public ArrayList<Product> searchProduct(String productName){
		try {
			ArrayList<Product> result = new ArrayList<Product>();
			
			ResultSet rs = state.executeQuery("select P.id, P.name, P.nutrition, P.imgURL from Product P where P.name LIKE '%" + productName + "%'");
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
	
	public ArrayList<Ingredient> productToingredient(int pId){
		try {
			ArrayList<Ingredient> result = new ArrayList<Ingredient>();
			
			ResultSet rs = state.executeQuery("select I.id, I.name Iname, I.explain, I.eat\n" + 
					"from Product P inner join P_I PI on (P.id = PI.productID)\n" + 
					"inner join Ingredient I on (PI.ingredientID = I.id)\n" +  
					"where P.id = "+pId+";");
			
			while(rs.next()) {
				Ingredient ingredient = new Ingredient(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4));
				result.add(ingredient);
			}
			
			return result;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Symptom> ingredientTosymptom(int iId){
		try {
			ArrayList<Symptom> result = new ArrayList<Symptom>();
			
			ResultSet rs = state.executeQuery("select S.id, S.name Sname\n" + 
					"from Ingredient I inner join I_S IS on (I.id = IS.ingredientID)\n" + 
					"inner join Symptom S on (IS.symptomID = S.id)\n" +  
					"where I.id = "+iId+";");
			
			while(rs.next()) {
				Symptom symptom = new Symptom(rs.getInt(1), rs.getString(2));
				result.add(symptom);
			}
			
			return result;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Ingredient> symptomToingredient(int sId){
		try {
			ArrayList<Ingredient> result = new ArrayList<Ingredient>();
			
			ResultSet rs = state.executeQuery("select I.id, I.name Iname, I.explain, I.eat\n" + 
					"from Symptom S inner join I_S IS on (S.id = IS.symptomID)\n" + 
					"inner join Ingredient I on (IS.ingredientID = I.id)\n" +  
					"where S.id = "+sId+";");
			
			while(rs.next()) {
				Ingredient ingredient = new Ingredient(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4));
				result.add(ingredient);
			}
			
			return result;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void insertI_S(int IngredientId, int SymptomId, int hospitalId, int eat, String explain) {
		try {
			state.executeUpdate("insert into I_S values (" + IngredientId + ", "+ SymptomId + ", " + hospitalId + ");");
			state.executeUpdate("Update Ingredient SET eat = " + (eat + 1) + " where id = " + IngredientId + ";");
			state.executeUpdate("Update Ingredient SET explain = '" + explain + "' where id = " + IngredientId + ";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	   static public void createTable(Statement stt) { // create Query
	      ArrayList<String> createlist = new ArrayList<String>();

	      createlist.add("create table Product(id SERIAL NOT NULL, name varchar(20) NOT NULL UNIQUE, "
	            + "nutrition varchar(20), imageURL varchar(100), primary key(id));");

	      createlist.add("create table Ingredient(id SERIAL NOT NULL, name varchar(20) NOT NULL UNIQUE, "
	            + "eat smallint CHECK (eat = 1 or eat = 2 or eat = 3), explain varchar(100), "
	            + "primary key(id));");

	      createlist.add("create table Symptom(id SERIAL NOT NULL, name varchar(20) NOT NULL UNIQUE, " + 
	            "primary key (id));");

	      createlist.add("create table Hospital(id SERIAL NOT NULL, name varchar(20) NOT NULL, " + 
	            "address varchar(50), phone varchar(15), primary key(id));");

	      createlist.add("create table I_S(ingredientID int, symptomID int, hospitalID int, " + 
	            "foreign key(ingredientID ) references Ingredient(id), " + 
	            "foreign key(symptomID) references Symptom(id), " + 
	            "foreign key(hospitalID) references Hospital(id));");
	      
	      createlist.add("create table P_I(productID int , ingredientID int, "
	            + "foreign key(productID) references Product(id), "
	            + "foreign key(ingredientID ) references Ingredient(id));");

	      try {
	         for (String q : createlist) {
	            stt.executeUpdate(q);
	         }
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }
	   }


	   private static void insertDefaultTable(Statement stt, Connection conn) {
	      ArrayList<String> symptom_list = initSymptomList();

	      String q = new String();
	      PreparedStatement query;
	      try {
	         for (String symptom : symptom_list) {
	            q = "insert into Symptom(name) values (?);";
	            query = conn.prepareStatement(q);
	            query.clearParameters();
	            query.setString(1, symptom);

	            query.executeUpdate();
	         }
	      } catch (Exception ex) {

	      }
	      
	   }

	   static public StringBuilder sendAPI() {
	      StringBuilder urlBuilder = new StringBuilder(
	            "http://apis.data.go.kr/B553748/CertImgListService/getCertImgListService"); /* URL */
	      StringBuilder sb = new StringBuilder();
	      URL url;
	      HttpURLConnection conn;
	      try {
	         urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8")
	               + "=c0fVGEAE6knQgHRJrWPM22gDUEmEimBNtNXbWnhjt9zpJyAUb3zpy%2B3b%2FMnAH0rOtFMfCwEQu%2BbnZ53vgFq%2FCw%3D%3D"); /*
	                                                                                                 * Service
	                                                                                                 * Key
	                                                                                                 */
	         urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "="
	               + URLEncoder.encode("xml", "UTF-8")); /*  �솁 �뵬  �굨 �뻼 */
	         urlBuilder.append(
	               "&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*  �읂 �뵠�릯   �땾 */
	         urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
	               + URLEncoder.encode("5", "UTF-8")); /*  釉�  �읂 �뵠�릯  �뼣 揶�  �땾 */

	         url = new URL(urlBuilder.toString());
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         conn.setRequestProperty("Content-type", "application/json");
	         System.out.println("Response code: " + conn.getResponseCode());

	         BufferedReader rd;
	         if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
	         } else {
	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	         }

	         String line;
	         while ((line = rd.readLine()) != null) {
	            sb.append(line);
	         }
	         rd.close();
	         conn.disconnect();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return sb;
	   }

	   public void initTable() {
	      // ResultSet result = null;
	      try {
	         System.out.println("Connecting PostgreSQL database");

	         createTable(state);
	         insertDefaultTable(state,connect);
	         

	         StringBuilder api_res = sendAPI();

	         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	         InputSource is = new InputSource(new StringReader(api_res.toString()));
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         Document doc = builder.parse(is);
	         XPathFactory xpathFactory = XPathFactory.newInstance();
	         XPath xpath = xpathFactory.newXPath();
	         // XPathExpression expr = xpath.compile("/response/body/items/item");
	         XPathExpression expr = xpath.compile("//items/item");
	         NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

	         for (int i = 0; i < nodeList.getLength(); i++) {
	            Element child = (Element) nodeList.item(i);

	            NodeList product_name = child.getElementsByTagName("prdlstNm");
	            Element prd = (Element) product_name.item(0);
	            String prd_name = prd.getFirstChild().getNodeValue();

	            NodeList imgurlnode = child.getElementsByTagName("imgurl1");
	            Element imgurl = (Element) imgurlnode.item(0);
	            String img = imgurl.getFirstChild().getNodeValue();

	            int p_id = getProductId(prd_name, img, connect); // get product id

	            NodeList ingredient_name = child.getElementsByTagName("rawmtrl");
	            Element igd = (Element) ingredient_name.item(0);
	            String g = igd.getFirstChild().getNodeValue();

	            ArrayList<Integer> i_id_list = getIngredientId(g, connect); // get ingredient id

	            insertPITable(p_id, i_id_list, connect);

	         }
	         StringBuilder api_res_hospital = sendAPIHospital();
	         System.out.println(api_res_hospital);
	         insertHospital(api_res_hospital,connect);
	         System.out.println(api_res.toString());

	      } catch (Exception ex) {
	         System.out.println(ex);
	      } finally {
	         try {
	            if (state != null)
	               state.close();
	            if (connect != null)
	               connect.close();

	         } catch (SQLException e) {
	            e.printStackTrace();
	         }
	      }

	   }

	   private static void insertPITable(int p_id, ArrayList<Integer> i_id_list, Connection conn) {
	      String q = new String();
	      PreparedStatement query;
	      try {
	         for (Integer i_id : i_id_list) {
	            q = "insert into P_I values (?, ?);";
	            query = conn.prepareStatement(q);
	            query.clearParameters();
	            query.setInt(1, p_id);
	            query.setInt(2, i_id);

	            query.executeUpdate();
	         }
	      } catch (Exception ex) {

	      }

	   }
	   private static ArrayList<String> initSymptomList(){
	      ArrayList<String> syptom_list = new ArrayList<String>();
	      syptom_list.add("�뤃�뗫꽅");
	      syptom_list.add(" 苑� 沅�");
	      syptom_list.add(" �깕 �땾 湲� 源�");
	      syptom_list.add(" �뻼 �뒠�겫 �릯 ");
	      syptom_list.add("疫뀀맩苑�  �뻿�겫  �읈");
	      syptom_list.add(" �뒭 �뱽�닶 ");
	      syptom_list.add("�꽴臾믨퐨 �굢疫꿸퀣弛�");
	      syptom_list.add("燁삘뫂�벥�뵳 ");
	      syptom_list.add("癰귣똾六�  �삢 釉�");
	      syptom_list.add(" 肉쇔닶 ");
	      syptom_list.add("�븰�뜄�돩");
	      syptom_list.add(" �깈 �뵃�ⓦ끇 ");
	      syptom_list.add(" �꺖 �넅�겫�뜄�쎗");
	      syptom_list.add("餓λ쵐�뀤 �뻿野껋럡��  �뵠 湲썲닶�빘苑�");
	      syptom_list.add("餓λ쵎猷�");
	      syptom_list.add("�뜮�뜇�굙");
	      syptom_list.add(" �꺖癰�  癰�  源�");
	      syptom_list.add(" �맄亦끹끉堉�");
	      syptom_list.add(" �굙癰� ");
	      syptom_list.add("獄쏆뮇�삂");
	      syptom_list.add("�⑥쥙肉�");
	      syptom_list.add("�뜮醫듼뀲  �뼎獄쏅베猷�");
	      syptom_list.add(" 肉쇤겫袁㏓궢 �뼄 �닶�빘湲�");
	      syptom_list.add(" �맄 肉�");
	      syptom_list.add("�꽴臾롫궢 �뼄 �닶�빘湲�");
	      syptom_list.add(" �뮸�릯 ");
	      syptom_list.add(" 釉� �쟿�몴�떯由�");
	      syptom_list.add("疫뀀맩苑�癰귣벉彛� 肉�");
	      syptom_list.add("�뿆�슣�삢 肉�");
	      syptom_list.add(" 逾얗겫  肉�");
	      syptom_list.add(" �뵊�겫 ");
	      syptom_list.add("�겫  �젟�븰 ");
	      syptom_list.add("野껋럥�졃");
	      syptom_list.add(" �굙 釉�  �벓��   �삢 釉�");
	      syptom_list.add("�⑥눖猷� 釉� 獄쏄퀡�닁");
	      syptom_list.add(" 援� �뱜�몴   �뵠 �궔餓λ쵎猷�");
	      syptom_list.add(" �삢 �솁 肉�");
	      syptom_list.add("�눧�떯由� �젾");
	      syptom_list.add("�⑥쥙媛밧닶 ");
	      
	      return syptom_list;
	   }

	   private static ArrayList<Integer> getIngredientId(String g, Connection conn) {
	      Set<String> igd_txt = deleteBracketText(g);
	      System.out.println(igd_txt);
	      ArrayList<Integer> i_id_list = new ArrayList();

	      String q = new String();
	      try {
	         for (String igd_name : igd_txt) {
	            q = "select id " + "from Ingredient " + "where name = ?;";
	            PreparedStatement query = conn.prepareStatement(q);
	            query.clearParameters();
	            query.setString(1, igd_name);

	            ResultSet result = query.executeQuery();
	            int i_id;
	            if (result.next()) {
	               i_id = result.getInt(1);
	            } else {
	               i_id = findIngredientID(igd_name, conn);
	            }

	            i_id_list.add(i_id);
	         }
	      } catch (Exception e) {
	      }
	      return i_id_list;
	   }

	   private static int getProductId(String prd_name, String img, Connection conn) {
	      String q = new String();
	      PreparedStatement query;
	      ResultSet result;
	      int p_id = 0;
	      try {
	         query = conn.prepareStatement(q);
	         q = "insert into Product(name,nutrition,imageURL) values (?, null, ?);";
	         query = conn.prepareStatement(q);
	         query.clearParameters();
	         query.setString(1, prd_name);
	         query.setString(2, img);

	         query.executeUpdate();

	         q = "select id " + "from Product " + "where name = ?;";
	         query = conn.prepareStatement(q);
	         query.clearParameters();
	         query.setString(1, prd_name);

	         result = query.executeQuery();
	         if(result.next()) {
	            p_id = result.getInt(1);
	         }
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }

	      return p_id;
	   }

	   private static int findIngredientID(String igd_name, Connection conn) {

	      String q = new String();
	      PreparedStatement query;
	      ResultSet result;
	      int i_id = 0;
	      try {
	         query = conn.prepareStatement(q);
	         q = "insert into Ingredient(name, eat, explain) values (?, null, null);";
	         query = conn.prepareStatement(q);
	         query.clearParameters();
	         query.setString(1, igd_name);

	         query.executeUpdate();

	         q = "select id " + "from Ingredient " + "where name = ?;";
	         query = conn.prepareStatement(q);
	         query.clearParameters();
	         query.setString(1, igd_name);

	         result = query.executeQuery();
	         if(result.next()) {
	            i_id = result.getInt(1);
	         }
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }

	      return i_id;
	   }

	   private static Set<String> deleteBracketText(String txt) {
	      Pattern ptn = Pattern.compile("\\([^\\(\\)]+\\)");
	      Matcher matcher = ptn.matcher(txt);

	      String puretxt = txt;
	      String removeArea = new String();

	      while (matcher.find()) {
	         int startidx = matcher.start();
	         int endidx = matcher.end();

	         removeArea = puretxt.substring(startidx, endidx);
	         puretxt = puretxt.replace(removeArea, "");
	         matcher = ptn.matcher(puretxt);
	      }

	      puretxt = puretxt.replaceAll("[^가-힣]", " ");

	      ptn = Pattern.compile("[가-힣]+");
	      matcher = ptn.matcher(puretxt);

	      Set<String> igdlist = new HashSet();
	      while (matcher.find()) {
	         String tmp = matcher.group();
	         if (tmp != "") {
	            igdlist.add(tmp);
	         }
	      }

	      return igdlist;
	   }

	   static public StringBuilder sendAPIHospital() {
		      StringBuilder urlBuilder = new StringBuilder(
		            "https://openapi.gg.go.kr/OrganicAnimalProtectionFacilit"); /* URL */
		      StringBuilder sb = new StringBuilder();
		      URL url;
		      HttpURLConnection conn;
		      try {
		         urlBuilder.append("?" + URLEncoder.encode("KEY", "MS949")
		               + "=981ffd3aea3f459aa28449256aaf20f8&Type=xml"); /* * Service
		                                                * Key
		                                             */

		         url = new URL(urlBuilder.toString());
		         conn = (HttpURLConnection) url.openConnection();
		         conn.setRequestMethod("GET");
		         conn.setRequestProperty("Content-type", "application/xml");
		         System.out.println("Response code: " + conn.getResponseCode());

		         BufferedReader rd;
		         if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
		            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
		         } else {
		            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		         }

		         String line;
		         while ((line = rd.readLine()) != null) {
		            sb.append(line);
		         }
		         rd.close();
		         conn.disconnect();
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
		      return sb;
		   }
	   
	   private static void insertHospital(StringBuilder api_res_hospital, Connection conn) {
		      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		      InputSource is = new InputSource(new StringReader(api_res_hospital.toString()));
		      try {
		      DocumentBuilder builder = factory.newDocumentBuilder();
		      Document doc = builder.parse(is);
		      XPathFactory xpathFactory = XPathFactory.newInstance();
		      XPath xpath = xpathFactory.newXPath();
		      // XPathExpression expr = xpath.compile("/response/body/items/item");
		      XPathExpression expr = xpath.compile("//row");
		      NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

		      for (int i = 0; i < nodeList.getLength(); i++) {
		         Element child = (Element) nodeList.item(i);

		         NodeList hospital_name = child.getElementsByTagName("ENTRPS_NM");
		         Element hsp = (Element) hospital_name.item(0);
		         String hsp_name = hsp.getFirstChild().getNodeValue(); 

		         NodeList phone = child.getElementsByTagName("ENTRPS_TELNO");
		         Element phn = (Element) phone.item(0);
		         String phn_num = phn.getFirstChild().getNodeValue();

		         NodeList address = child.getElementsByTagName("REFINE_ROADNM_ADDR");
		         Element adr = (Element) address.item(0);
		         String adr_txt = adr.getFirstChild().getNodeValue();

		         String q = new String();
		         PreparedStatement query;
		         q = "insert into hospital(name,address,phone) values (?, ?, ?);";
		         query = conn.prepareStatement(q);
		         query.clearParameters();
		         query.setString(1, hsp_name);
		         query.setString(2, phn_num);
		         query.setString(3, adr_txt);

		         query.executeUpdate();
		      }
		      }catch(Exception e) {
		         
		      }
		   }
}
