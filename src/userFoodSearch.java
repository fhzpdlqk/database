import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.event.*;
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
					ArrayList<Product> productArray = query.searchProduct(name.getText());
					for(int i = 0; i < productArray.size(); i++) {
						nameList.add(productArray.get(i).getName());
					}
				}
				else if(kinds.getSelectedItem().equals("증상")){
					nameList.removeAll();
					ArrayList<Symptom> symptomArray = query.searchSymptom(name.getText());
					for(int i =0; i<symptomArray.size(); i++) {
						nameList.add(symptomArray.get(i).getName());
					}
				}
			}
			else if(arg0.getSource() == next) {
				if(nameList.getSelectedItem() != null) {
					
					if(kinds.getSelectedItem().equals("제품")) {
						setVisible(false);
						new showProduct(query.searchProduct(nameList.getSelectedItem()).get(0));
					}
					else if(kinds.getSelectedItem().equals("증상")) {
						setVisible(false);
						new showSymptom(query.searchSymptom(nameList.getSelectedItem()).get(0));
					}
				}
			}
		}
	}
}
