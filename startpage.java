package dictionary;

import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import javax.swing.*;


public class startpage extends JFrame implements ActionListener
{
	Container c;
	CardLayout card;
	
	JButton b1;
	
	
	startpage()
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			SwingUtilities.updateComponentTreeUI(c);
		} catch(Exception e){}
		c = getContentPane();
		
		card = new CardLayout();
		c.setLayout(card);
		ImageIcon im = new ImageIcon("assets/dictionary.png");
		b1 = new JButton(im);
		/*Border bd = BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.red,Color.green);
		b1 = new JButton("Continue...");
		b1.setBorder(bd);*/
		
		
		c.add("Center",b1);
		
		c.add("Home",new home());	//HOME
		
		b1.addActionListener(this);
		
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		card.next(c);
	}
	
	public static void main (String args[])
		{
			startpage f= new startpage();
			f.setTitle("Dictionary");
			f.setSize(800,650);
			f.setVisible(true);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		}
}



class home extends JPanel implements ActionListener, ItemListener
{
	JSplitPane sp;
	JTextField tf;	//search text field
	JButton b2,prev,next;	//search button , previous , next
	JLabel lb1,lb2,lb3,sim,sima;
	String st = null;
	Database db = new Database();
	String str[] = new String[200],def,didmean;
	JList list1;
	String strall[] = new String[180000];
	List lst1,lst2;
	JTextArea tadef;
	
	
	home()
	{
		
		
		
		
		
		JPanel bodyleft = new JPanel();				//current word showing panel
		BoxLayout bo2 = new BoxLayout(this, BoxLayout.X_AXIS);	
		this.setLayout(bo2);
		
		
		
		prev = new JButton("previous word");	//previous button
		prev.setVisible(false);
		next = new JButton("next word");			//next button
		next.setVisible(false);
		Box box21 = Box.createHorizontalBox();
		Box box22 = Box.createHorizontalBox();
		Box box23 = Box.createHorizontalBox();
		Box box3 = Box.createHorizontalBox();
		Box ver2 = Box.createVerticalBox();
		lb1 =  new JLabel("");			//current word
		lb2 =  new JLabel("");			//current wordtype
		lb3 =  new JLabel("");			//current word definition
		lb1.setFont(new Font("Helvetica",Font.BOLD,40));
		lb1.setForeground(Color.blue);
		box21.add(lb1);
		lb2.setFont(new Font("Helvetica",Font.ITALIC,20));
		lb2.setForeground(Color.blue);
		box22.add(lb2);
		
		tadef = new JTextArea(def,3,20);
		tadef.setFont(new Font ("Times New Roman",Font.PLAIN,20));
		
		tadef.setVisible(false);
		
		box23.add(tadef);
		
		/*lb3.setFont(new Font("Helvetica",Font.PLAIN,20));
		lb3.setForeground(Color.black);
		box23.add(lb3);*/
		
		
		
		box3.add(prev);
		box3.add(Box.createHorizontalStrut(300));
		box3.add(next);
		ver2.add(box21);
		box22.add(Box.createVerticalStrut(50));
		ver2.add(box22);
		box23.add(Box.createVerticalStrut(50));
		ver2.add(box23);
		box3.add(Box.createVerticalStrut(50));
		ver2.add(box3);
		bodyleft.add(ver2);
		
		
		/* bodyright = new JPanel();*/
		
		JPanel bodyup = new JPanel();		//did you mean panel
		sim = new JLabel("Did You Mean...");
		sim.setFont(new Font("Times New Roman", Font.BOLD,15));
		sim.setForeground(Color.red);
		bodyup.add(sim);
		lst1 = new List(14);
		lst1.addItemListener(this);
		bodyup.add(lst1);
		
		JPanel bodylow = new JPanel();		//all words panel
		
		
		
		sima = new JLabel("All Words");
		sima.setFont(new Font("Times New Roman", Font.BOLD,15));
		sima.setForeground(Color.red);
		bodylow.add(sima);
		lst2 = new List(14);
		bodylow.add(lst2);
		
		
		JSplitPane bodyl = new JSplitPane(JSplitPane.VERTICAL_SPLIT, bodyup, bodylow);
		bodyl.setDividerLocation(250);
				
		
		JSplitPane spleft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, bodyleft, bodyl);
		spleft.setDividerLocation(600);
		
		
		JPanel top = new JPanel();			// search bar panel
		BoxLayout b = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(b);
		Box box1 = Box.createHorizontalBox();
		Box ver = Box.createVerticalBox();
		tf = new JTextField("Search Here... ",50);	//search text field
		
		b2 = new JButton("Search");	//search button
		box1.add(tf);
		box1.add(Box.createHorizontalStrut(20));
		box1.add(b2);
		ver.add(Box.createVerticalStrut(20));
		ver.add(box1);
		top.add(ver);
		
		
		
		b2.addActionListener(this);
		
		
		
		
		prev.addActionListener(this);
		next.addActionListener(this);
		
		/*list1 = new JList();
		list1.setBounds(100, 150, 100, 100);
		bodyup.add(list1);
		//bodyup.add(scroll2);*/
		
		
		sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,top,spleft);
		sp.setDividerLocation(80);
		this.add(sp);
		
	}
	
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==b2){
			
		st = tf.getText();
		//System.out.println(st);
		db.searchWord(st);
		
		if(db.wordfound == false) {
			lb1.setText("Word not found!");
			lb2.setText("Please enter another word!");
			tadef.setVisible(false);
			prev.setVisible(false);
			next.setVisible(false);
		}
		
		else {
		lb1.setText(db.word_print);
		
		lb2.setText(db.wordtype_print);
		def = db.worddefine_print;
		tadef.setVisible(true);
		tadef.setText(def);
		tadef.setEditable(false);
		str =  db.similarWord();
		lst1.removeAll();
		for(int i=0; i<200 && str[i]!=null;i++)
		{	
			lst1.add(str[i]);	}
		//list1.setListData(str);
		strall = db.allWords();
		//lst2.removeAll();
		for(int i=0; i<300 && strall[i]!=null;i++)
		{	
			lst2.add(strall[i]);	}
		/*for(int i=0;strall[i]!=null;i++)
		{	System.out.println(strall[i]);	}
		for(int i=0;str[i]!=null;i++)
		{	System.out.println(str[i]);	}*/
		prev.setVisible(true);
		next.setVisible(true);
		}
		} // end of if condtion for first button
		
		if(ae.getSource()==prev) {
			db.prevWord();
			//System.out.println(db.word_print);
			lb1.setText(db.word_print);
			lb2.setText(db.wordtype_print);
			def = db.worddefine_print;
			tadef.setText(def);
			tadef.setEditable(false);
			//lb3.setText(db.worddefine_print);
		}
		if(ae.getSource()==next){
			db.nextWord();
			//System.out.println(db.word_print);
			lb1.setText(db.word_print);
			lb2.setText(db.wordtype_print);
			def = db.worddefine_print;
			tadef.setText(def);
			tadef.setEditable(false);
			//lb3.setText(db.worddefine_print);
		}
		
	}


	public void itemStateChanged(ItemEvent ie) {
			didmean = lst1.getSelectedItem();
			st = didmean;
			//System.out.println(didmean);
			db.searchWord(st);
			lb1.setText(db.word_print);
			lb2.setText(db.wordtype_print);
			def = db.worddefine_print;
			tadef.setVisible(true);
			tadef.setText(def);
			tadef.setEditable(false);
			str =  db.similarWord();
			lst1.removeAll();
			for(int i=0; i<200 && str[i]!=null;i++)
			{	
				lst1.add(str[i]);	}
			//list1.setListData(str);
			strall = db.allWords();
			//lst2.removeAll();
			for(int i=0; i<300 && strall[i]!=null;i++)
			{	
				lst2.add(strall[i]);	}
			/*for(int i=0;strall[i]!=null;i++)
			{	System.out.println(strall[i]);	}
			for(int i=0;str[i]!=null;i++)
			{	System.out.println(str[i]);	}*/
			prev.setVisible(true);
			
	} // end of item listener
}// end of home class

