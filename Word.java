import java.util.ArrayList;

//I have neither given nor received unauthorized aid on this assignment

public class Word implements Comparable<Word>{
	
	private String text;
	private boolean ignored, replaced;
	private String replacement;
	private ArrayList<Integer> linenum = new ArrayList<Integer>();
	
	/*
	 * Word object used in spellchecker/spellsorter
	 * 
	 * text: actual text of word in string form
	 * ignored: if true, a duplicate word will be skipped. if false, it will prompt as a new word
	 * replaced: if true, it will replace with replacement
	 * replacement: word that acts as replacement when replaced = true
	 * linenum: keeps track of line numbers for words for output file
	 * 
	 * below are the setter/getter methods
	 * 
	 */
	
	
	
	public Word(String w) {
		
		this.text = w;
		
	}
	
	public void setText(String w) {
		
		this.text = w;
		
	}
	
public void setIgnored(boolean b) {
		
		this.ignored = b;
		
	}

public void setReplaced(boolean b) {
	
	this.replaced = b;
	
}

public void setReplacement(String s) {
	
	this.replacement = s;
	
}

public String getText() {
	
	return this.text;
	
}


public boolean getIgnored() {
	
	return this.ignored;
	
}


public boolean getReplaced() {
	
	return this.replaced;
	
}


public String getReplacement() {
	
	return this.replacement;
	
}

public int compareTo(Word other) { 
	
	return this.text.compareTo(other.text);
	
	
}

public void addLineNum(Integer num) {
	
	linenum.add(num);
	
	
}

public ArrayList<Integer> getNums() {
	
	return this.linenum;
}



}
