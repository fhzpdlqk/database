package user;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Button;
import java.awt.event.*;
public class userPage extends Frame{
	Button btn1;
	Button btn2;
	public userPage() {
		super();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		Panel p = new Panel();
		btn1 = new Button("식품 정보 찾기");
		btn2 = new Button("동물 병원 찾기");
		
		p.add(btn1);
		p.add(btn2);
		
		btn1.addActionListener(new EventHandler());
		btn2.addActionListener(new EventHandler());
		
		p.setLayout(null);
		btn1.setBounds(150,185,200,50);
		btn2.setBounds(150,385,200,50);
		
		add(p);
		setSize(500, 600);
		setVisible(true);
	}
	
	class EventHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == btn2) {
				new searchHospital();
				setVisible(false);
			}
			else if(arg0.getSource() == btn1){
				setVisible(false);
				new userFoodSearch();
			}
		}
	}
}
