import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellChecker {
	
	//I have neither given nor received unauthorized aid on this assignment
	
	static QuadraticProbingHashTable<String> dictionary = new QuadraticProbingHashTable<String>();
	static ArrayList<Word>[] misTable = new ArrayList[52];
	static char a;
	static File file;
	static String outfile;
	static String outfile2;
	static BufferedWriter order;
	static BufferedWriter corrected;

	public static void main(String[] args) throws IOException {
		
		
		
		for(int i = 0; i < misTable.length; i ++)
			misTable[i] = new ArrayList();
		
		File dict = new File(args[0]);
		System.out.println("Reading in Dictionary. . . ");
		readDict(dict);
		System.out.println("Dictionary read.");
		
		
		do {
			
			
			
			Scanner scan = new Scanner(System.in);
		
			System.out.println("Please enter a file to spell check>>");
		
			String filename = scan.next();
			String[] filename_arr = filename.split("[.]");
			
			outfile = filename_arr[0] + "_order.txt";
			outfile2 = filename_arr[0] + "_corrected.txt";
			
			corrected = new BufferedWriter(new FileWriter(outfile2));
			order = new BufferedWriter(new FileWriter(outfile));
			
			file = new File(filename);
			
			do {
			
				System.out.println("Print words (p), enter new file (f), or quit (q) ?");
				a = scan.next().charAt(0);
			
				if(a == 'p')
					readFile(file);
			
				else if(a == 'q')
					System.out.println("\nGoodbye!");
				
				else if(a == 'f')
					break;
				
				else
					System.out.println("Error: please choose option from list:\n");
				
			} while(a != 'q');

		
		} while(a != 'q');
		
		order.close();
		corrected.close();
		

	}
	
	public static void readDict(File f) throws IOException {
		
		Scanner fr = new Scanner(f);
		
		while(fr.hasNext()) {
				
				String s = fr.next();
				dictionary.insert(s);
				
			}
			
			
			
			
		}
	
	
		
	public static void readFile(File f) throws IOException {
		
		
		order.write("Order:\n\n");
		corrected.write("Corrected:\n\n");
		
		int linenum = 0;
		int index;
		Word w = null;
		boolean ask = true;
		
		Scanner fr = new Scanner(f);
		
		while(fr.hasNextLine()) {
				
				linenum += 1;
				
				String[] line = fr.nextLine().split(" ");
				
				for(int i = 0; i < line.length; i++) {
					
					String s = line[i];
					String punctuation = "";
					
					
					if(Character.isLetter(s.charAt(s.length() - 1)) == false & Character.isDigit(s.charAt(s.length() -1 ))== false) { //if the word ends with special character, omit that character
						
						punctuation = s.substring(s.length() - 1, s.length());
						s = s.substring(0, s.length() - 1);
						
					}
					
					
					if(!dictionary.contains(s)) {
						


						order.write(s + " " + linenum + "\n");
					
					
					
						if(contains(misTable, s) == false) { // if not already in misTable
						
							w = new Word(s);
						
							index = getIndex(s.charAt(0)); //get index where it will be added
						
							misTable[index].add(w);	// add to mistable 

							
							if(ask != false) {
							
								System.out.println("--" + w.getText() + " " + linenum);
								
								Scanner scan = new Scanner(System.in);
								System.out.println("ignore all (i), replace all (r), next(n), or quit(q)?");
								
								char choice = scan.next().charAt(0);
								
									
								while(choice != 'i' & choice != 'r' & choice != 'n' & choice != 'q') {
									
									System.out.println("Error: option not in list. Try again: \n");
									choice = scan.next().charAt(0);
									
								}
								
								if(choice == 'i') {
									
									w.setIgnored(true);

									if(punctuation.length() == 0)
										corrected.write(s + " ");
									
									else
										corrected.write(s + punctuation + " ");

									
								}
							
								else if(choice == 'r') {
									
									ArrayList<String> replace_options = new ArrayList<String>();
									
									String incorrect = w.getText();
									
									
									
									// Delete method:
									
									for(int k = 0; k < incorrect.length(); k++) {
										
										String test = incorrect.substring(0, k) + incorrect.substring(k, incorrect.length());
										
										if(dictionary.contains(test)) 
											replace_options.add(test);
										
									}
									
									
									//Swap method
									
									for(int l = 0; l < incorrect.length()-1; l++) {
										
										char[] testArr = incorrect.toCharArray();
										char temp = testArr[l];
										testArr[l] = testArr[l+1];
										testArr[l+1] = temp;
										
										String tester = new String(testArr);
										
										if(dictionary.contains(tester)) 
											replace_options.add(tester);
										
										
										
									}
									
									//Split
									
									for(int m = 0; m < incorrect.length(); m++) {
										
										String test1 = incorrect.substring(0, m+1);
										String test2 = incorrect.substring(m+1, incorrect.length());
										

										if(dictionary.contains(test1) & dictionary.contains(test2))
											replace_options.add(test1 + " " + test2);

									}
									
									
									if(replace_options.size() == 0) {
										
										System.out.println("There are no suggestions.");
										w.setReplaced(true);
										w.setReplacement(s);
										
										if(punctuation.length() == 0)
											corrected.write(s + " ");
										
										else
											corrected.write(s + punctuation + " ");
										
										
									}
									
									else {
									
										boolean bool2 = false;
										int replace_choice;
									
										do {
										
										
											System.out.print("Replace with ");
									
											for(int n = 0; n < replace_options.size(); n++) {
										
												System.out.print("(" + (n+1) + ")" + replace_options.get(n));
										
											}
											
											System.out.println("\n");
									
											replace_choice = scan.nextInt();
										
											if(replace_choice > 0 & replace_choice <= replace_options.size())
												bool2 = true;
										
											else
												System.out.println("Error: choose valid option: \n");
										
										} while(bool2 == false);
									
								
										w.setReplaced(true);
										w.setReplacement(replace_options.get(replace_choice - 1));
								
										
										if(punctuation.length() == 0)
											corrected.write(w.getReplacement() + " ");
										
										else
											corrected.write(w.getReplacement() + punctuation + " ");
										
									}
								
								
							}
								else if(choice == 'q') {
									
									ask = false;
									if(punctuation.length() == 0)
										corrected.write(s + " ");
									
									else
										corrected.write(s + punctuation + " ");
									
								}
							
							
							}
							
							else {
								
								if(punctuation.length() == 0)
									corrected.write(s + " ");
								
								else
									corrected.write(s + punctuation + " ");
							
								
								
							}
								
								

								
								
							}
								
								

					
					
						else { //already in misTable
							
							Word w2 = getWord(misTable, s);
							
							if(w2.getIgnored() == true) {
								
								if(punctuation.length() == 0)
									corrected.write(w2.getText() + " ");
								
								else
									corrected.write(w2.getText() + punctuation + " ");
								
								
								
							}
							
							else if(w2.getReplaced() == true) {
								
								
								
								if(punctuation.length() == 0)
									corrected.write(w2.getReplacement() + " ");
								
								else
									corrected.write(w2.getReplacement() + punctuation + " ");
							
									
							
								}
						
							else { // user chose next, need to treat as new word
							

								if(ask != false) {
									System.out.println("--" + w2.getText() + " " + linenum);
									Scanner scan = new Scanner(System.in);
									System.out.println("ignore all (i), replace all (r), next(n), or quit(q)?");
									
									char choice = scan.next().charAt(0);
									
										
									while(choice != 'i' & choice != 'r' & choice != 'n' & choice != 'q') {
										
										System.out.println("Error: option not in list. Try again: \n");
										choice = scan.next().charAt(0);
										
									}
									
									if(choice == 'i') {
										
										w2.setIgnored(true);
										
										if(punctuation.length() == 0)
											corrected.write(s + " ");
										
										else
											corrected.write(s + punctuation + " ");
										

									
									}
									
									
								
									else if(choice == 'r') {
										
										
										ArrayList<String> replace_options = new ArrayList<String>();
										
										String incorrect = w2.getText();
										
										
										
										// Delete method:
										
										for(int k = 0; k < incorrect.length(); k++) {
											
											String test = incorrect.substring(0, k) + incorrect.substring(k, incorrect.length());
											
											if(dictionary.contains(test)) 
												replace_options.add(test);
											
										}
										
										
										//Swap method
										
										for(int l = 0; l < incorrect.length()-1; l++) {
											
											char[] testArr = incorrect.toCharArray();
											char temp = testArr[l];
											testArr[l] = testArr[l+1];
											testArr[l+1] = temp;
											
											String tester = new String(testArr);
											
											if(dictionary.contains(tester)) 
												replace_options.add(tester);
											
											
											
										}
										
										//Split
										
										for(int m = 0; m < incorrect.length(); m++) {
											
											String test1 = incorrect.substring(0, m+1);
											String test2 = incorrect.substring(m+1, incorrect.length());
											

											if(dictionary.contains(test1) & dictionary.contains(test2))
												replace_options.add(test1 + " " + test2);

										}
										
										
										if(replace_options.size() == 0) {
											
											System.out.println("There are no suggestions.");
											w2.setReplaced(true);
											w2.setReplacement(s);
											
											
											if(punctuation.length() == 0)
												corrected.write(s + " ");
											
											else
												corrected.write(s + punctuation + " ");
											
											
										}
										
										else {
										
										
											boolean bool2 = false;
											int replace_choice;
										

											do {
											
											
												System.out.print("Replace with ");
										
												for(int n = 0; n < replace_options.size(); n++) {
											
													System.out.print("(" + n+1 + ")" + replace_options.get(n));
											
												}
										
												replace_choice = scan.nextInt();
											
												if(replace_choice > 0 & replace_choice < replace_options.size())
													bool2 = true;
											
												else
													System.out.println("Error: choose valid option: ");
											
											} while(bool2 == false);
										
									
											w2.setReplaced(true);
											w2.setReplacement(replace_options.get(replace_choice - 1));
											
											
							
											
											
											if(punctuation.length() == 0)
												corrected.write(w2.getReplacement() + " ");
											
											else
												corrected.write(w2.getReplacement() + punctuation + " ");
									
								
											
										}
									
									
								}
									else if(choice == 'q')
										ask = false;
									
							
								
								}
								
								
								else {
									
									if(punctuation.length() == 0)
										corrected.write(s + " ");
									
									else
										corrected.write(s + punctuation + " ");
									
									
									
								}
								
									
							}
							
						

						
					}
					
					
					}
					
					else {
						
						if(punctuation.length() == 0)
							corrected.write(s + " ");
						
						else
							corrected.write(s + punctuation + " ");
						
						
						
					}
					
		
					
				}
					corrected.write("\n");
					
				
			}
		
		
		
		System.out.println("Spell Check Complete! Please choose an option:");
		
		for(int i = 0; i < misTable.length; i ++)
			misTable[i] = new ArrayList();

		
		
	}
	
	
	
	
	
	
	public static boolean contains(ArrayList<Word>[] arr, String s) {
		
		
		for(int i = 0; i < arr.length; i++) {
			
			for(int j = 0; j < arr[i].size(); j++) {
				
				if(arr[i].get(j).getText().equals(s)) {
					
					return true;
					
				}
				
				
			}
			
		}
		
		return false;
			
			
		}
	
	
	
	public static Word getWord(ArrayList<Word>[] arr, String s) {
		
		
		for(int i = 0; i < arr.length; i++) {
			
			for(int j = 0; j < arr[i].size(); j++) {
				
				if(arr[i].get(j).getText().equals(s)) {
					
					return arr[i].get(j);
					
				}
				
				
			}
			
		}
		
		return null;
		
			
			
		}
	
	
	public static int getIndex(Character c) {
		
		int index;
		
		if(Character.isLowerCase(c)) {
			
			int int_firstLetter = (int) c;
			index = int_firstLetter - 71;
			
		}
			
			
		else {
			
			int int_firstLetter = (int) c;
			index = int_firstLetter - 65;
			
		}
		
		return index;
		
		
	}
	
	

	

}
