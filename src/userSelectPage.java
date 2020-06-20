import java.awt.event.*;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Button;

public class userSelectPage extends Frame{
	Button btn1;
	Button btn2;
	public userSelectPage() {
		super();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
	            System.exit(0);
	         }
	    });
		
		Panel p = new Panel();
		btn1 = new Button("상품 검색");
		btn2 = new Button("동물병원 찾기");
		
		p.setLayout(null);
		btn1.setBounds(200,185,100,30);
		btn2.setBounds(200,385,100,30);
		
		p.add(btn1);
		p.add(btn2);
		
		add(p);
		setSize(500,600);
		setVisible(true);
	}
	class EventHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == btn2) {
				
			}
		}
	}
}
