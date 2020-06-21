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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BoxLayout;

import main.SQLCommand;
import main.FirstPage;
import model.Ingredient;
import model.Symptom;

public class showIngredient extends Frame{

	private Symptom symptom;
	private ArrayList<String> symptomArray;
	private Ingredient ingredient;
	private IngredientS ingredientS = new IngredientS("","",0);;
	private ResultSet rs;
	private SQLCommand query = new SQLCommand();

	Button home;
	Button hospital;

	public showIngredient(String ingredientName) throws SQLException {
		super();
		this.ingredient = query.equalIngredient(ingredientName);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		Panel p = new Panel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		Panel p1 = new Panel();
		Label title = new Label(ingredient.getName(), Label.CENTER);

		p1.add(title);
		p.add(p1);

		Panel p2 = new Panel();
		TextArea contents = new TextArea(50, 50);
		rs = query.ingredientDetail(ingredient.getId());

		while(rs.next()) {

			if(rs.getString(1).equals(ingredientS.getIname())) {
				ingredientS.setSname(rs.getString(4));
			}else {
				if(!ingredientS.getIname().equals("")) {

					if(ingredientS.getExplain() != null && !ingredientS.getExplain().equals("")) {
						contents.append("-설명: "+ingredientS.getExplain()+"\n");
						symptomArray = ingredientS.getSname();
						contents.append("-증상: \n");

						for(String S:symptomArray) {
							contents.append(" " + S+"\n");
						}
						contents.append("\n");

						if(ingredientS.getEat() == 1) {
							contents.append("-위험도: 하\n");
						}else if(ingredientS.getEat() == 2){
							contents.append("-위험도: 중\n");
						}else {
							contents.append("-위험도: 상\n");
						}
					}
				}
				ingredientS = new IngredientS(rs.getString(1), rs.getString(2), rs.getInt(3));
			}
		}
		
		if(ingredientS.getExplain() != null && !ingredientS.getExplain().equals("")) {
			contents.append("-설명: "+ingredientS.getExplain()+"\n");
			symptomArray = ingredientS.getSname();
			contents.append("-증상: \n");

			for(String S:symptomArray) {
				contents.append(" " + S+"\n");
			}
			contents.append("\n");

			if(ingredientS.getEat() == 1) {
				contents.append("-위험도: 하\n");
			}else if(ingredientS.getEat() == 2){
				contents.append("-위험도: 중\n");
			}else {
				contents.append("-위험도: 상\n");
			}
		}
		p2.add(contents);
		p.add(p2);

		Panel p3 = new Panel(new GridLayout(1, 2, 10, 10));
		home = new Button("홈");
		hospital = new Button("병원찾기");
		hospital.addActionListener(new EventHandler());
		home.addActionListener(new EventHandler());
		p3.add(home);
		p3.add(hospital);
		p.add(p3);

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
				new searchHospital();
			}
		}
	}
}
