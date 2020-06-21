package user;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.event.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BoxLayout;

import main.SQLCommand;

public class userFoodSearch extends Frame{
   Choice kinds;
   TextField name;
   Button nameSearch;
   List nameList;
   
   Button next;
   
   public userFoodSearch() {
      super();
      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent we) {
            System.exit(0);
         }
      });
      Panel p = new Panel();
      
      Panel p1 = new Panel();
      kinds = new Choice();
      name = new TextField(20);
      nameSearch = new Button("�˻�");
      nameList = new List(20, false);
      
      kinds.add("��ǰ");
      kinds.add("����");
      nameSearch.addActionListener(new EventHandler());
      
      p1.add(kinds);
      p1.add(name);
      p1.add(nameSearch);
      
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      p.add(p1);
      p.add(nameList);
      
      Panel p2 = new Panel();
      next = new Button("����");
      next.addActionListener(new EventHandler());
      p2.add(next);
      p.add(p2);
      
      add(p);
      
      setSize(500, 600);
      setVisible(true);
   }
   
   class EventHandler implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent arg0) {
         SQLCommand query = new SQLCommand();
         if(arg0.getSource() == nameSearch) {
            nameList.removeAll();
            if(kinds.getSelectedItem().equals("��ǰ")) {
               ResultSet rs = query.productList(name.getText());
               
               try {
                  while(rs.next()) {
                     if(rs.getInt(3) == 3) {
                        nameList.add(rs.getString(2)+" ���赵:��");
                     }else if(rs.getInt(3) == 2) {
                        nameList.add(rs.getString(2)+" ���赵:��");
                     }else if(rs.getInt(3) == 1) {
                        nameList.add(rs.getString(2)+" ���赵:��");
                     }else {
                        nameList.add(rs.getString(2)+"      ");
                     }
                  }
               } catch (SQLException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            }
            else if(kinds.getSelectedItem().equals("����")){
               nameList.removeAll();
               ResultSet rs = query.ingredientList(name.getText());

               try {
                  while(rs.next()) {
                     if(rs.getInt(3) == 3) {
                        nameList.add(rs.getString(2)+" ���赵:��");
                     }else if(rs.getInt(3) == 2) {
                        nameList.add(rs.getString(2)+" ���赵:��");
                     }else if(rs.getInt(3) == 1) {
                        nameList.add(rs.getString(2)+" ���赵:��");
                     }else {
                        nameList.add(rs.getString(2)+"      ");
                     }
                  }
               } catch (SQLException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            }
         }
         else if(arg0.getSource() == next) {
            if(nameList.getSelectedItem() != null) {
               if(kinds.getSelectedItem().equals("��ǰ")) {
                  setVisible(false);
                  try {
                     new showProduct(nameList.getSelectedItem().substring(0, nameList.getSelectedItem().length()-6));
                  } catch (SQLException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
               }
               else if(kinds.getSelectedItem().equals("����")) {
                  setVisible(false);
                  try {
                     new showIngredient(nameList.getSelectedItem().substring(0, nameList.getSelectedItem().length()-6));
                  } catch (SQLException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
               }
            }
         }
      }
   }
}