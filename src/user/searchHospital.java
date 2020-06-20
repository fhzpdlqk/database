package user;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.*;
import java.util.ArrayList;

import main.SQLCommand;
import model.Hospital;

import java.awt.Button;

public class searchHospital extends Frame{
	TextField hospitalText;
	Button search;
	Panel p2;
	public searchHospital() {
		super();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
	            System.exit(0);
	         }
	    });
		Panel p = new Panel();
		Panel p1 = new Panel();
		
		hospitalText = new TextField(20);
		search = new Button("°Ë»ö");
		search.addActionListener(new EventHandler());
		
		p1.add(hospitalText);
		p1.add(search);
		
		p2 = new Panel();
		
		p.add(p1);
		p.add(p2);
		
		add(p);
		setSize(500,600);
		setVisible(true);
	}
	class EventHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			SQLCommand query = new SQLCommand();
			ArrayList<Hospital> result = query.searchHospital(hospitalText.getText());
			p2.removeAll();
			for(int i =0; i < result.size(); i++) {
				Panel temp = new Panel();
				Label name = new Label(result.get(i).getName());
				Label address = new Label(result.get(i).getAddress());
				temp.add(name);
				temp.add(address);
				p2.add(temp);
			}
		}
	}
}
