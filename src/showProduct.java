import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class showProduct extends Frame{
	
	private Product product;
	private ArrayList<String> symptomArray;
	private SQLCommand query = new SQLCommand();
	private IngredientS ingredient = new IngredientS("","",0);
	private ResultSet rs;
	
	Button home;
	Button hospital;
	
	public showProduct(String productName) throws SQLException, IOException{
		super();
		this.product = query.equalProduct(productName);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		
		Panel p = new Panel();
		p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
		
		Panel p1 = new Panel();
		Label title = new Label(product.getName(), Label.CENTER);
		p1.add(title);
		
		try {
			Panel p2 = new Panel();
			
			Image image;
			URL url = new URL(product.getImgURL());
			image = ImageIO.read(url);
			
			JLabel img = new JLabel(new ImageIcon(image));
			
			p2.add(img);
			p.add(p2);
		}catch(MalformedURLException ex) {
			ex.printStackTrace();
		}
		
		Panel p3 = new Panel();
		TextArea contents = new TextArea("재료:\n", 50, 50);
		rs = query.productDetail(product.getId());
		
		while(rs.next()) {
			if(rs.getString("Iname") == ingredient.getIname()) {
				ingredient.setSname(rs.getString("Sname"));
			}else {
				if(!ingredient.getIname().equals("")) {
					
					contents.append(ingredient.getIname());
					
					if(!ingredient.getExplain().equals("")) {
						contents.append("-설명: "+ingredient.getExplain()+"\n");
						symptomArray = ingredient.getSname();
						contents.append("-증상: ");
					
						for(String S:symptomArray) {
							contents.append(S);
						}
						contents.append("\n");
						
						if(ingredient.getEat() == 1) {
							contents.append("-위험도: 하\n");
						}else if(ingredient.getEat() == 2){
							contents.append("-위험도: 중\\n");
						}else {
							contents.append("-위험도: 상\n");
						}
					}
				}
				ingredient = new IngredientS(rs.getString("Iname"), rs.getString("I.explain"), rs.getInt("I.eat"));
			}
		}
		p3.add(contents);
		p.add(p3);
		
		Panel p4 = new Panel(new GridLayout(1, 2, 10, 10));
		home = new Button("홈");
		hospital = new Button("병원찾기");
		p4.add(home);
		p4.add(hospital);
		p.add(p4);
		
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
				// 동물병원 찾기
			}
		}
	}
}
