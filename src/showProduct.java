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
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class showProduct extends Frame{
	
	private Product product;
	private ArrayList<Ingredient> ingredientArray;
	private ArrayList<Symptom> symptomArray;
	private Ingredient ingredient;
	
	Button home;
	Button hospital;
	
	public showProduct(Product product) throws IOException {
		super();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		this.product = new Product(product.getId(), product.getName(), product.getNutrition(), product.getImgURL());
		SQLCommand query = new SQLCommand();
		this.ingredientArray  = query.productToingredient(product.getId());
		
		Panel p = new Panel();
		p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
		
		Panel p1 = new Panel();
		Label productName = new Label(product.getName(), Label.CENTER);
		p1.add(productName);
		
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
		
		for(int i = 0; i < ingredientArray.size(); i++) {
			
			this.ingredient = ingredientArray.get(i);

			if(ingredient.getExplain() != null && !ingredient.getExplain().isEmpty()) {
				symptomArray = query.ingredientTosymptom(ingredient.getId());
				contents.append(ingredient.getName()+"\n");
				contents.append("-설명: "+ingredient.getExplain()+"\n");
				contents.append("-증상: ");
				
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
			contents.append("섭취 여부"+ingredient.getEat()+"\n");
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
			}
		}
	}
}
