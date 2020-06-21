package user;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import main.SQLCommand;
import model.Hospital;

import java.awt.Button;

public class searchHospital extends Frame{
	TextField hospitalText;
	Button search;
	JLabel p2;
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
		
		p2 = new JLabel();
		
		p.add(p1);
		p.add(p2);
		p.setLayout(null);
		p1.setBounds(0,0,500,50);
		p2.setBounds(0,0,500,600);
		
		add(p);
		setSize(500,600);
		setVisible(true);
	}
	class EventHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == search) {
				SQLCommand query = new SQLCommand();
				ArrayList<Hospital> result = query.searchHospitalAddress(hospitalText.getText());
				String text = new String();
				text += "<html><br/>";
				int len = result.size() > 6 ? 6 : result.size();
				for(int i =0; i < len; i++) {
					System.out.println(result.get(i).getName());
					text += "<html>" + result.get(i).getName() + "<br/>";
					text += result.get(i).getPhone() + "<br/>";
					text += result.get(i).getAddress() + "<br/>";
					text += "----------------------------------- <br/>";
 				}
				text += "</html>";
				p2.setText(text);
			}
		}
	}
}
