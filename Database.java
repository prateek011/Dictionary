package dictionary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Database {

	private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/entries";
	private final String USER = "root";
	private final String PASS = "";
	public Connection conn = null;
	public Statement stmt = null;
	
	public String prevword = null;
	public String nextword = null;
	public String currword = null;
	
	public String word_print = null;
	public String wordtype_print = null;
	public String worddefine_print = null;
	
	public int wordcount = 0;
	
	public boolean wordfound = false;
	
	//public int dynamic[] = new int[26];
	
	// constructor mai connection initialise karna hai
	public Database() {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			// basically at this point we have made the connection and now
			// we need to execute all the queries that we want to without closing the connection
			// so be careful of the try catch block which has conn.close();			
			
			//doDynamic();
		}catch(SQLException se){
			se.printStackTrace();
			}
			catch(Exception e){
			e.printStackTrace();
			}
			finally{
				try{
				if(stmt!=null)
				stmt.close();
				}catch(SQLException se2){
				}
				//try{
				//if(conn!=null)
				//conn.close();
				//}catch(SQLException se){
				//se.printStackTrace();
				//}
			}
	}
/*
	public void doDynamic() {
		Statement stmt = null;
		try{
			for(int i=0;i<26;i++) {
				dynamic[i]=0;
			}
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT word, wordtype, definition FROM entries";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				String word = rs.getString("word");
				// capital A has 65 ascii
				// so we can use that ascii code to increment the required integer
				//int ascii = ((int) word.charAt(0))-65;
				String ash=word.substring(0,1);
				int ascii =Integer.parseInt(ash);
				ascii-=65;
				dynamic[ascii]++;
				
			}
			System.out.print(dynamic[0]);
			rs.close();
			stmt.close();
			}
		catch(SQLException se){
			se.printStackTrace();
			}
			catch(Exception e){
			e.printStackTrace();
			}
			finally{
				try{
				if(stmt!=null)
				stmt.close();
				}catch(SQLException se2){
				// nothing we can do
				}
				//try{
				//if(conn!=null)
				//	conn.close();
				//}catch(SQLException se){
				//se.printStackTrace();
				//}
			}
	}
	*/
	// ye function hai vo search bar ke liye
	public void searchWord(String str) {
		Statement stmt = null;
		try{
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT word, wordtype, definition FROM entries";
			ResultSet rs = stmt.executeQuery(sql);
			Character c1 = new Character(str.charAt(0));
			char c2 = c1.charValue();
			if(Character.isLowerCase(c2))
			{
				c2 = Character.toUpperCase(c2);
				str = Character.toString(c2) + str.substring(1); 
				//System.out.println(str);
			}
			/*
			//char ch = str.charAt(0);
			int counter=0;
			// ch has the character at the first position of the string
			// what we can do is that we can check for the character using the ascii code
			// ch - 65 gives us the index in the dynamic[] array
			// so we can loop till we our counter is less than ch-65
			int ascii =Integer.parseInt(str,0);
			ascii-=65;
			for(int i=0;i<ascii;i++) {
				counter+=dynamic[i];
			}
			*/
			currword = str;
			boolean found = false;
			int f=0;
			while(rs.next()){
				// add condition for the loop to skip iterations
				/*if(counter!=0){
					counter--;
					continue;
				}*/
				String word = rs.getString("word");
				String wordtype = rs.getString("wordtype");
				String definition = rs.getString("definition");
				if(currword.equals(word)&&found==false)
				{
					//System .out.println("word: " + word);
					//System .out.println("wordtype: " + wordtype);
					//System .out.println("definition: " + definition);
					word_print = "Word: " + word;
					wordtype_print = "Wordtype: " + wordtype;
					worddefine_print = "Definition: " + definition;
					found = true;
					wordfound = true;
				}
				if(found == false)
				{
					prevword = word;
				}
				else if(found == true && f==1) {
					nextword = word;
					break;
				}
				else if(found == true && f!=1) {
					f = 1;
				}
			}
			if(found == false)
				wordfound = false;
			rs.close();
			stmt.close();
			}
		catch(SQLException se){
			se.printStackTrace();
			}
			catch(Exception e){
			e.printStackTrace();
			}
			finally{
				try{
				if(stmt!=null)
				stmt.close();
				}catch(SQLException se2){
				// nothing we can do
				}
				//try{
				//if(conn!=null)
				//	conn.close();
				//}catch(SQLException se){
				//se.printStackTrace();
				//}
			}
	} // end of newWord declaration
	
	// for a random word
	public void randomWord() {
		Statement stmt = null;
		try{
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT word, wordtype, definition FROM entries";
			ResultSet rs = stmt.executeQuery(sql);
			Random rand = new Random();
			int counter=0,random;
			random = rand.nextInt(170000);
			while(rs.next()){
				if(counter==random)
				{
					String word = rs.getString("word");
					String wordtype = rs.getString("wordtype");
					String definition = rs.getString("definition");
					System .out.println("serial no.: " + counter);
					System .out.println("word: " + word);
					System .out.println("wordtype: " + wordtype);
					System .out.println("definition: " + definition);
					break;
				}
				counter++;
			}
			rs.close();
			stmt.close();
			}
		catch(SQLException se){
			se.printStackTrace();
			}
			catch(Exception e){
			e.printStackTrace();
			}
			finally{
				try{
				if(stmt!=null)
				stmt.close();
				}catch(SQLException se2){
				// nothing we can do
				}
				//try{
				//if(conn!=null)
				//	conn.close();
				//}catch(SQLException se){
				//se.printStackTrace();
				//}
			}
	} // end of randomWord() function declaration
	
	// next word function
	public int nextWord() {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT word, wordtype, definition FROM entries";
			ResultSet rs = stmt.executeQuery(sql);
			
			if(currword.equals(nextword)) {
				wordcount ++;
			}
			else {
				wordcount = 0;
			}
			int currwordcount = 0;
			boolean found = false;
			int f=0;
			currword = nextword;
			while(rs.next()){
				String word = rs.getString("word");
				String wordtype = rs.getString("wordtype");
				String definition = rs.getString("definition");
				if(currword.equals(word)&&found==false)
				{
					//System .out.println("word: " + word);
					//System .out.println("wordtype: " + wordtype);
					//System .out.println("definition: " + definition);
					if(currwordcount == wordcount) {
					word_print = "Word: " + word;
					wordtype_print = "Wordtype: " + wordtype;
					worddefine_print = "Definition: " + definition;
					found = true;
					}
					else {
					currwordcount++;
					}
				}
				if(found == false)
				{
					prevword = word;
					
				}
				else if(found == true && f==1) {
					nextword = word;
					//System.out.println("Prev word is " + prevword);
					//System.out.println("Next word is " + nextword);
					break;
				}
				else if(found == true && f!=1) {
					f = 1;
				}
			}
			rs.close();
			stmt.close();
		}catch(SQLException se){
		se.printStackTrace();
		}
		catch(Exception e){
		e.printStackTrace();
		}
		finally{
			try{
			if(stmt!=null)
			stmt.close();
			}catch(SQLException se2){
			}
			//try{
			//if(conn!=null)
			//conn.close();
			//}catch(SQLException se){
			//se.printStackTrace();
			//}
		}
		return 0;
	}

	// previous word function
	public int prevWord() {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT word, wordtype, definition FROM entries";
			ResultSet rs = stmt.executeQuery(sql);
			int currwordcount = 0;
			if(currword.equals(prevword) && wordcount > 0) {
				wordcount--;
			}
			else {
				wordcount = 0;
			}
			currword = prevword;
			boolean found = false;
			int f=0;
			while(rs.next()){
				String word = rs.getString("word");
				String wordtype = rs.getString("wordtype");
				String definition = rs.getString("definition");
				if(currword.equals(word)&&found==false)
				{
					//System .out.println("word: " + word);
					//System .out.println("wordtype: " + wordtype);
					//System .out.println("definition: " + definition);
					if(currwordcount == wordcount) {
					word_print = "Word: " + word;
					wordtype_print = "Wordtype: " + wordtype;
					worddefine_print = "Definition: " + definition;
					found = true;
					}
					else {
					currwordcount++;
					}
				}
				if(found == false)
				{
					prevword = word;
				}
				else if(found == true && f==1) {
					nextword = word;
					//System.out.println("Prev word is " + prevword);
					//System.out.println("Next word is " + nextword);
					break;
				}
				else if(found == true && f!=1) {
					f = 1;
				}
			}
			rs.close();
			stmt.close();
		}catch(SQLException se){
		se.printStackTrace();
		}
		catch(Exception e){
		e.printStackTrace();
		}
		finally{
			try{
			if(stmt!=null)
			stmt.close();
			}catch(SQLException se2){
			}
			//try{
			//if(conn!=null)
			//conn.close();
			//}catch(SQLException se){
			//se.printStackTrace();
			//}
		}
		return 0;
	} // end of prevWord function declaration

	// similar word function
	public String[] similarWord() {
		String str[] = new String[200];
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql;
			int i=0;
			sql = "SELECT word, wordtype, definition FROM entries";
			ResultSet rs = stmt.executeQuery(sql);
			String similar = currword;
			Character c1 = new Character(similar.charAt(0));
			char c2 = c1.charValue();
			c2 = Character.toLowerCase(c2);
			similar = Character.toString(c2) + similar.substring(1); 
			//System.out.println(str);
			while(rs.next()) {
				String word = rs.getString("word");
				//String wordtype =+
				rs.getString("wordtype");
				//String definition = rs.getString("definition");
				if(word.contains(currword) && currword.equals(word)==false) {
				str[i++] = word;	
				//System .out.println("word: " + word);
				//System .out.println("wordtype: " + wordtype);
				//System .out.println("definition: " + definition);
				}
			}
			stmt.close();
		}
		catch(SQLException se){
			se.printStackTrace();
			}
			catch(Exception e){
			e.printStackTrace();
			}
			finally{
				try{
				if(stmt!=null)
				stmt.close();
				}catch(SQLException se2){
				// nothing we can do
				}
				//try{
				//if(conn!=null)
				//	conn.close();
				//}catch(SQLException se){
				//se.printStackTrace();
				//}
			}
		return str;
	}

	// all words function
	public String[] allWords() {
		String str[] = new String[180000];
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql;
			int i=0;
			sql = "SELECT word, wordtype, definition FROM entries";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String word = rs.getString("word");
				//System.out.println(word);
				str[i++] = word;
			}
			//System.out.println(i);
			stmt.close();
		}
		catch(SQLException se){
			se.printStackTrace();
			}
			catch(Exception e){
			e.printStackTrace();
			}
			finally{
				try{
				if(stmt!=null)
				stmt.close();
				}catch(SQLException se2){
				// nothing we can do
				}
				//try{
				//if(conn!=null)
				//	conn.close();
				//}catch(SQLException se){
				//se.printStackTrace();
				//}
			}
		return str;
	}
} // end of class definition