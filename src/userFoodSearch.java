import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.event.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BoxLayout;

public class userFoodSearch extends Frame{
	Choice kinds;
	TextField name;
	Button nameSearch;
	List nameList;
	
	Button next;
	
	public userFoodSearch() {
		super();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		Panel p = new Panel();
		
		Panel p1 = new Panel();
		kinds = new Choice();
		name = new TextField(20);
		nameSearch = new Button("검색");
		nameList = new List(20, false);
		
		kinds.add("제품");
		kinds.add("증상");
		nameSearch.addActionListener(new EventHandler());
		
		p1.add(kinds);
		p1.add(name);
		p1.add(nameSearch);
		
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(p1);
		p.add(nameList);
		
		Panel p2 = new Panel();
		next = new Button("다음");
		p2.add(next);
		p.add(p2);
		
		add(p);
		
		setSize(500, 600);
		setVisible(true);
	}
	
	class EventHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			SQLCommand query = new SQLCommand();
			if(arg0.getSource() == nameSearch) {
				nameList.removeAll();
				if(kinds.getSelectedItem().equals("제품")) {
					ResultSet rs = query.productList(name.getText());
					
					try {
						while(rs.next()) {
							if(rs.getInt("I.eat") == 3) {
								nameList.add(rs.getString("P.name")+" 위험도:상");
							}else if(rs.getInt("I.eat") == 2) {
								nameList.add(rs.getString("P.name")+" 위험도:중");
							}else if(rs.getInt("I.eat") == 1) {
								nameList.add(rs.getString("P.name")+" 위험도:하");
							}else {
								nameList.add(rs.getString("P.name      "));
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(kinds.getSelectedItem().equals("증상")){
					nameList.removeAll();
					ResultSet rs = query.ingredientList(name.getText());

					try {
						while(rs.next()) {
							if(rs.getInt("I.eat") == 3) {
								nameList.add(rs.getString("P.name")+" 위험도:상");
							}else if(rs.getInt("I.eat") == 2) {
								nameList.add(rs.getString("P.name")+" 위험도:중");
							}else if(rs.getInt("I.eat") == 1) {
								nameList.add(rs.getString("P.name")+" 위험도:하");
							}else {
								nameList.add(rs.getString("P.name      "));
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else if(arg0.getSource() == next) {
				if(nameList.getSelectedItem() != null) {
					if(kinds.getSelectedItem().equals("제품")) {
						setVisible(false);
						try {
							new showProduct(nameList.getSelectedItem().substring(0, nameList.getSelectedItem().length()-7));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(kinds.getSelectedItem().equals("증상")) {
						setVisible(false);
						try {
							new showIngredient(nameList.getSelectedItem().substring(0, nameList.getSelectedItem().length()-7));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
