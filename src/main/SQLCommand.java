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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

	private String stmt;
	private PreparedStatement p;
	public SQLCommand() {
		try {
			connect = DriverManager.getConnection("jdbc:postgresql://localhost/postgres","postgres","kangho");
			connect.setAutoCommit(true);
			state = connect.createStatement();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public Product equalProduct(String productName) throws SQLException{
		try {
			stmt = "select P.id, P.name, P.nutrition, P.imageurl from Product P where P.name = ?";
			p = connect.prepareStatement(stmt);			
			p.setString(1, productName);

			ResultSet rs = p.executeQuery();
			if(rs.next()) {
				Product product = new Product(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4));
				return product;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Ingredient equalIngredient(String ingredientName) throws SQLException{
		try {
			stmt = "select I.id, I.name, I.eat, I.explain from Ingredient I where I.name = ?";
			p = connect.prepareStatement(stmt);			
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
	
	public ResultSet productList(String productName){
		try {
		stmt = "select P.id, P.name, max(I.eat)\n" + 
				"from Product P inner join P_I PI on (P.id = PI.productID)\n" + 
				"inner join Ingredient I on (PI.ingredientID = I.id)\n" + 
				"where P.name LIKE '%"+ productName +"%'\n" +
				"group by P.id, P.name";
		ResultSet rs = state.executeQuery(stmt);
		return rs;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet productDetail(int pId){
		try {
			stmt = "select I.name Iname, I.explain, I.eat, S.name\n" + 
					"from Product P inner join P_I PI on (P.id = PI.productID)\n" + 
					"inner join Ingredient I on (PI.ingredientID = I.id)\n" + 
					"left join I_S on (I.id = I_S.ingredientID)\n" + 
					"left join Symptom S on (I_S.symptomID = S.id)\n" + 
					"where P.id = ?\n" + 
					"union\n" + 
					"select I.name Iname, I.explain, I.eat, S.name\n" + 
					"from Product P inner join P_I PI on (P.id = PI.productID)\n" + 
					"inner join Ingredient I on (PI.ingredientID = I.id)\n" + 
					"inner join I_S on (I.id = I_S.ingredientID)\n" + 
					"inner join Symptom S on (I_S.symptomID = S.id)\n" + 
					"where P.id = ? order by Iname;";
			p = connect.prepareStatement(stmt);
			p.clearParameters();
			p.setInt(1, pId);
			p.setInt(2, pId);
			ResultSet rs = p.executeQuery();
			return rs;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public ResultSet ingredientList(String symptomName) {
		try {
			stmt = "select I.id, I.name, I.eat\n" + 
					"from Symptom S\n" + 
					"inner join I_S on (S.id = I_S.symptomID)\n" + 
					"inner join Ingredient I on (I_S.ingredientID = I.id)\n" + 
					"where S.name LIKE '%"+symptomName+"%';";
//			p = connect.prepareStatement(stmt);
//			p.setString(1, symptomName);
			ResultSet rs = state.executeQuery(stmt);
			return rs;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<Hospital> searchHospitalAddress(String address){
	      try {
	         ArrayList<Hospital> result = new ArrayList<Hospital>();
	         
	         ResultSet rs = state.executeQuery("select H.id, H.name, H.address, H.phone from Hospital H where H.address LIKE '%" + address + "%'");
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
	public ResultSet ingredientDetail(int iId) {
		try {
			stmt = "select I.name, I.explain, I.eat, S.name\n" + 
					"from Ingredient I\n" + 
					"inner join I_S on (I.id = I_S.ingredientID)\n" + 
					"inner join Symptom S on (I_S.symptomID = S.id)\n" + 
					"where I.id = ?;";
			p = connect.prepareStatement(stmt);
			p.clearParameters();
			p.setInt(1, iId);
			ResultSet rs = p.executeQuery();

			return rs;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void insertI_S(int IngredientId, int SymptomId, int hospitalId, int eat, String explain) {
		try {
			String q = new String();
			PreparedStatement query;
			q = "insert into I_S values (?,?,?)";
			query = connect.prepareStatement(q);
			query.clearParameters();
			query.setInt(1, IngredientId);
			query.setInt(2, SymptomId);
			query.setInt(3, hospitalId);
			query.executeUpdate();
			state.executeUpdate("Update Ingredient SET eat = " + (eat + 1) + ", explain = '" + explain + "' where id = " + IngredientId + ";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	   static public void createTable(Statement stt) { // create Query
	      ArrayList<String> createlist = new ArrayList<String>();

	      createlist.add("create table Product(id SERIAL NOT NULL, name varchar(20) NOT NULL UNIQUE, "
	            + "nutrition varchar(20), imageURL varchar(100), primary key(id));");

	      createlist.add("create table Ingredient(id SERIAL NOT NULL, name varchar(20) NOT NULL UNIQUE, "
	            + "eat smallint CHECK (eat = 1 or eat = 2 or eat = 3), explain varchar(10000), "
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


	   private void insertDefaultTable(Statement stt, Connection conn) {
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
	         insertDefaultIngredientTable(stt,conn);
	         insertDefaultI_STable(stt,conn);
	      } catch (Exception ex) {

	      }
	      
	      setTriggerI_STable(stt,conn);
	      
	   }

	   private void insertDefaultI_STable(Statement stt, Connection conn) throws SQLException, NumberFormatException, IOException {
		   String[] parsing;
           stmt = "insert into I_S(ingredientId,symptomID) values(?, ?);";
           p = connect.prepareStatement(stmt);
		   
		   File file2 = new File("I_S.txt");
           //입력 스트림 생성
           FileReader filereader2 = new FileReader(file2);
           //입력 버퍼 생성
           BufferedReader bufReader2 = new BufferedReader(filereader2);
           p.clearParameters();
           
           String line2 = "";
           
           while((line2 = bufReader2.readLine()) != null){
               parsing = line2.split(" ");

                 p.setInt(1, Integer.parseInt(parsing[0]));
                 p.setInt(2, Integer.parseInt(parsing[1]));
                 p.executeUpdate();
           }
           //.readLine()은 끝에 개행문자를 읽지 않는다.            
           bufReader2.close();
	   }
	private void insertDefaultIngredientTable(Statement stt, Connection conn) throws SQLException, NumberFormatException, IOException {
		   String[] parsing;
		   stmt = "insert into Ingredient(name, eat, explain) values(?, ?, ?) on conflict (name) do nothing";
		   p = connect.prepareStatement(stmt);
		   
		   File file = new File("ingredient.txt");
           //입력 스트림 생성
           FileReader filereader = new FileReader(file);
           //입력 버퍼 생성
           BufferedReader bufReader = new BufferedReader(filereader);
           String line = "";

           while((line = bufReader.readLine()) != null){
               parsing = line.split("\t");
               p.setString(1, parsing[0]);
               p.setInt(2, Integer.parseInt(parsing[1]));
               p.setString(3, parsing[2]);
               p.executeUpdate();
           }
           //.readLine()은 끝에 개행문자를 읽지 않는다.            
           bufReader.close();
	}
	private static void setTriggerI_STable(Statement stt, Connection conn) {
		   try {
		   String trigger_func = 
				   "create or replace function before_insert_I_S()\n" +
				   "returns trigger as $before_insert_I_S$\n" +
				    "begin\n" +
				     "if exists(select * from I_S " +
				        "where ingredientID=new.ingredientID and symptomID=new.symptomID) then\n" + 
				         "return null;\n" +
				     "else\n" +
				         "return new;\n" +
				     "end if;\n"+
				   "end;\n $before_insert_I_S$ language 'plpgsql';";
		   String create_trigger =
				    "create trigger t1\n" +
					"before insert on I_S\n" +
				    "for each row\n" +
					"execute procedure before_insert_I_S();";
		   
		   
		   stt.executeUpdate(trigger_func);
		   stt.executeUpdate(create_trigger);
		   } catch(Exception e) {
			   
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
	         insertHospital(api_res_hospital,connect);

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
	      syptom_list.add("구토");
	      syptom_list.add("설사");
	      syptom_list.add("혼수상태");
	      syptom_list.add("식욕부진");
	      syptom_list.add("급성 신부전");
	      syptom_list.add("우울증");
	      syptom_list.add("광선혐기증");
	      syptom_list.add("침흘림");
	      syptom_list.add("보행 장애");
	      syptom_list.add("염증");
	      syptom_list.add("마비");
	      syptom_list.add("호흡곤란");
	      syptom_list.add("소화불량");
	      syptom_list.add("중추신경계 이상증세");
	      syptom_list.add("중독");
	      syptom_list.add("빈혈");
	      syptom_list.add("소변 변색");
	      syptom_list.add("위염");
	      syptom_list.add("위궤양");
	      syptom_list.add("혈변");
	      syptom_list.add("경련");
	      syptom_list.add("발작");
	      syptom_list.add("고열");
	      syptom_list.add("빠른 심박동");
	      syptom_list.add("염분과다 증상");
	      syptom_list.add("당과다 증상");
	      syptom_list.add("습진");
	      syptom_list.add("알레르기");
	      syptom_list.add("급성복막염");
	      syptom_list.add("피부염(피부 트러블)");
	      syptom_list.add("췌장염");
	      syptom_list.add("흥분");
	      syptom_list.add("내출혈");
	      syptom_list.add("부정맥");
	      syptom_list.add("신장 기능 장애");
	      syptom_list.add("비정상적인 혈액산도");
	      syptom_list.add("고혈압");
	      syptom_list.add("저혈당증");
	      syptom_list.add("혈액 응고 장애");
	      syptom_list.add("고창증");
	      syptom_list.add("갈증");
	      syptom_list.add("탈수");
	      syptom_list.add("과도한 배뇨");
	      syptom_list.add("나트륨 이온중독");
	      syptom_list.add("복통");
	      syptom_list.add("장파열");
	      syptom_list.add("무기력");
	      
	      
	      return syptom_list;
	   }

	   private static ArrayList<Integer> getIngredientId(String g, Connection conn) {
	      Set<String> igd_txt = deleteBracketText(g);
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
		               + "=981ffd3aea3f459aa28449256aaf20f8&Type=xml&pSize=1000"); /* * Service
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
		         String phn_num = phn.getFirstChild() == null ? "" : phn.getFirstChild().getNodeValue();

		         NodeList address = child.getElementsByTagName("REFINE_ROADNM_ADDR");
		         Element adr = (Element) address.item(0);
		         String adr_txt = adr.getFirstChild().getNodeValue();

		         String q = new String();
		         PreparedStatement query;
		         q = "insert into hospital(name,phone,address) values (?, ?, ?);";
		         query = conn.prepareStatement(q);
		         query.clearParameters();
		         query.setString(1, hsp_name);
		         query.setString(2, phn_num);
		         query.setString(3, adr_txt);

		         query.executeUpdate();
		      }
		      }catch(Exception e) {
		         e.printStackTrace();
		      }
		   }
}
