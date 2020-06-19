import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.BoxLayout;

public class doctorSelect extends Frame{
	TextField hospiter;
	Button hospiterSearch;
	List hospiterList;
	
	TextField Ingredient;
	Button IngredientSearch;
	List IngredientList;
	
	TextField Symptom;
	Button SymptomSearch;
	List SymptomList;
	
	Button next;
	
	public doctorSelect() {
		super();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
	            System.exit(0);
	         }
	    });
		
		Panel p = new Panel();
		
		Panel p1 = new Panel();
		Label label = new Label("병원", Label.CENTER);
		hospiter = new TextField(20);
		hospiterSearch = new Button("검색");
		hospiterList = new List(3,false);
		
		hospiterSearch.addActionListener(new EventHandler());
		
		p1.add(label);
		p1.add(hospiter);
		p1.add(hospiterSearch);
		
		Panel p2 = new Panel();
		Label label2 = new Label("재료", Label.CENTER);
		Ingredient = new TextField(20);
		IngredientSearch = new Button("검색");
		IngredientList = new List(3,false);
		
		IngredientSearch.addActionListener(new EventHandler());
		
		p2.add(label2);
		p2.add(Ingredient);
		p2.add(IngredientSearch);
		
		Panel p3 = new Panel();
		Label label3 = new Label("증상", Label.CENTER);
		Symptom = new TextField(20);
		SymptomSearch = new Button("검색");
		SymptomList = new List(3,false);
		
		SymptomSearch.addActionListener(new EventHandler());
		
		p3.add(label3);
		p3.add(Symptom);
		p3.add(SymptomSearch);
		
		next = new Button("다음");
		next.setSize(100,100);
		
		next.addActionListener(new EventHandler());
		
		Panel p4 = new Panel();
		p4.add(next);
		
		p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
		p.add(p1);
		p.add(hospiterList);
		p.add(p2);
		p.add(IngredientList);
		p.add(p3);
		p.add(SymptomList);
		p.add(p4);
		
		add(p);
		setSize(500,600);
		setVisible(true);
	}
	class EventHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			SQLCommand query = new SQLCommand();
			if(arg0.getSource() == hospiterSearch) {
				hospiterList.removeAll();
				ArrayList<Hospital> hospitalArray = query.searchHospital(hospiter.getText());
				for(int i =0; i<hospitalArray.size(); i++) {
					hospiterList.add(hospitalArray.get(i).getName());
				}
			}
			else if(arg0.getSource() == IngredientSearch) {
				IngredientList.removeAll();
				ArrayList<Ingredient> IngredientArray = query.searchIngredient(Ingredient.getText());
				for(int i =0; i<IngredientArray.size(); i++) {
					IngredientList.add(IngredientArray.get(i).getName());
				}
			}
			else if(arg0.getSource() == SymptomSearch) {
				SymptomList.removeAll();
				ArrayList<Symptom> SymptomArray = query.searchSymptom(Symptom.getText());
				for(int i =0; i<SymptomArray.size(); i++) {
					SymptomList.add(SymptomArray.get(i).getName());
				}
			}
			else if(arg0.getSource() == next) {
				if(hospiterList.getSelectedItem() != null && IngredientList.getSelectedItem() != null && SymptomList.getSelectedItem() != null) {
					setVisible(false);
					new doctorInput(hospiterList.getSelectedItem(), IngredientList.getSelectedItem(), SymptomList.getSelectedItem());
				}
			}
		}
	}
}
