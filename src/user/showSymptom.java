package user;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.*;
import java.util.ArrayList;

import main.SQLCommand;
import main.FirstPage;
import model.Ingredient;
import model.Symptom;

public class showSymptom extends Frame{

	private Symptom symptom;
	private ArrayList<Ingredient> ingredientArray;
	private ArrayList<Symptom> symptomArray;
	private Ingredient ingredient;
	
	Button home;
	Button hospital;
	
	public showSymptom(Symptom symptom) {
		super();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		this.symptom = new Symptom(symptom.getId(), symptom.getName());
		SQLCommand query = new SQLCommand();
		this.ingredientArray  = query.symptomToingredient(symptom.getId());
		
		Panel p = new Panel();
		
		Panel p1 = new Panel();
		TextArea contents = new TextArea(symptom.getName()+"\n\n占쏙옙占쏙옙 占쏙옙占�:\n", 50, 50);
		
		for(int i = 0; i < ingredientArray.size(); i++) {
			
			this.ingredient = ingredientArray.get(i);

			if(ingredient.getExplain() != null && !ingredient.getExplain().isEmpty()) {
				symptomArray = query.ingredientTosymptom(ingredient.getId());
				contents.append(ingredient.getName()+"\n");
				contents.append("-占쏙옙占쏙옙: "+ingredient.getExplain()+"\n");
				contents.append("-占쏙옙占쏙옙: ");
				
				for(int j = 0; j < symptomArray.size(); j++) {
					if(j == symptomArray.size()) {
						contents.append(symptomArray.get(j).getName()+"\n");
					}
					else {
					contents.append(symptomArray.get(j).getName()+",");
					}
				}
			}
			else {
				contents.append(ingredient.getName()+"\n");
			}
			contents.append("占쏙옙占쏙옙 占쏙옙占쏙옙"+ingredient.getEat()+"\n");
		}
		
		p1.add(contents);
		p.add(p1);
		
		Panel p2 = new Panel(new GridLayout(1, 2, 10, 10));
		home = new Button("홈");
		hospital = new Button("占쏙옙占쏙옙찾占쏙옙");
		p2.add(home);
		p2.add(hospital);
		p.add(p2);
		
		add(p);
		
		setSize(500, 600);
		setVisible(true);
	}
	
	class EventHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == home) {
				setVisible(false);
				new FirstPage();
			}
			else if(arg0.getSource() == hospital) {
				setVisible(false);
			}
		}
	}
}
