import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Choice;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;

public class doctorInput extends Frame{
	Choice choice;
	TextArea textarea;
	Button Complete;
	
	int hospitalId;
	int IngredientId;
	int SymptomId;
	
	public doctorInput(int hospitalId, int IngredientId, int SymptomId) {
		super();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
	            System.exit(0);
	         }
	    });
		this.hospitalId = hospitalId;
		this.IngredientId = IngredientId;
		this.SymptomId = SymptomId;
		
		Panel p = new Panel();
		
		Panel p1 = new Panel();
		Label label1 = new Label("반려동물 섭취 가능 여부");
		choice = new Choice();
		choice.add("섭취 가능");
		choice.add("일부 가능");
		choice.add("불가능");
		
		Panel p2 = new Panel();
		Label label2 = new Label("재료 증상 설명");
		textarea = new TextArea(10,30);
		textarea.setSize(100,100);
		
		Panel p3 = new Panel();
		Complete = new Button("완료");
		Complete.setSize(100,30);
		Complete.addActionListener(new EventHandler());
		
		
		p1.add(label1);
		p1.add(choice);
		
		p2.add(label2);
		p2.add(textarea);
		p3.add(Complete);
		
		p.add(p1);
		p.add(p2);
		p.add(p3);
		p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
		
		add(p);
		setSize(500,600);
		setVisible(true);
	}
	class EventHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			SQLCommand sqlcommand = new SQLCommand();
			if(arg0.getSource() == Complete) {
				if(choice.getSelectedItem() != null && textarea.getText() != "") {
					sqlcommand.insertI_S(IngredientId,SymptomId,hospitalId,choice.getSelectedIndex(),textarea.getText());
					new FirstPage();
					setVisible(false);
				}
			}
		}
	}
}
