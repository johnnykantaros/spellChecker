import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellSorter {
	
	//I have neither given nor received unauthorized aid on this assignment
	
	static QuadraticProbingHashTable<String> dictionary = new QuadraticProbingHashTable<String>();
	static ArrayList<Word>[] misTable = new ArrayList[52];
	static char a;
	static File file;
	static String outfile;
	static String outfile2;
	static String outfile3;
	static BufferedWriter order;
	static BufferedWriter corrected;
	static BufferedWriter sorted;
	static String filename;
	static String[] filename_arr;

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
		
			filename = scan.next();
			filename_arr = filename.split("[.]");
			
			outfile = filename_arr[0] + "_order.txt";
			outfile2 = filename_arr[0] + "_corrected.txt";
			
			corrected = new BufferedWriter(new FileWriter(outfile2));
			order = new BufferedWriter(new FileWriter(outfile));
		
			
			file = new File(filename);
			
			do {
			
				System.out.println("Print words (p), enter new file (f), or quit (q) ?"); //Prompt for action
				a = scan.next().charAt(0);
			
				if(a == 'p') {
					
					for(int i = 0; i < misTable.length; i ++) //set mistable to empty arraylists
						misTable[i] = new ArrayList();
					
					readFile(file);
				}
					
			
				else if(a == 'q')
					System.out.println("\nGoodbye!");
				
				else if(a == 'f') {
					
					for(int i = 0; i < misTable.length; i ++)
						misTable[i] = new ArrayList();
					
				}
					
				
				else
					System.out.println("Error: please choose option from list:\n");
				
			} while(a != 'q');

		
		} while(a != 'q'); //keep running while user doesnt select quit
		
		order.close();
		corrected.close();
		
		
		writeSorted(); //write sorted.txt
		
		

	}
	
	/*
	 * 
	 * takes in a file (dictionary) and populates hashtable
	 * 
	 */
	
	public static void readDict(File f) throws IOException {
		
		Scanner fr = new Scanner(f);
		
		while(fr.hasNext()) {
				
				String s = fr.next();
				dictionary.insert(s);
				
			}
			
			
			
			
		}
	
	
	/*
	 * input: file (f)
	 * 
	 * Performs the bulk of the spell check
	 * 
	 * iterates through every word in file
	 * if word is misspelled, offers option to user (replace, ignore, next, quit)
	 * 
	 * for replace, method will try to find replacement word for user which they can elect to use
	 * for ignore, all future occurrences will be skipped
	 * for next, code will skip over the word
	 * for quit, the user will return to main menu
	 * 
	 * Will also populate order output file with order of misspelled words, and corrected file (same file with replacements)
	 * 
	 * 
	 */
	
	
		
	public static void readFile(File f) throws IOException {
		
		
		order.write("Order:\n\n");
		corrected.write("Corrected:\n\n");
		
		int linenum = 0;
		int index;
		Word w = null;
		boolean ask = true;
		
		Scanner fr = new Scanner(f);
		
		while(fr.hasNextLine()) {
				
				linenum += 1; //keep track of linenum
				
				String[] line = fr.nextLine().split(" ");
				
				for(int i = 0; i < line.length; i++) {
					
					String s = line[i];
					String punctuation = "";
					
					
					if(Character.isLetter(s.charAt(s.length() - 1)) == false & Character.isDigit(s.charAt(s.length() -1 ))== false) { //if the word ends with special character, omit that character
						
						punctuation = s.substring(s.length() - 1, s.length());
						s = s.substring(0, s.length() - 1);
						
					}
					
					
					if(!dictionary.contains(s)) { // if dictionary contains, simply write to corrected output file (below)
						


						order.write(s + " " + linenum + "\n");
					
					
					
						if(contains(misTable, s) == false) { // if not already in misTable
						
							w = new Word(s);
							w.addLineNum(linenum);
						
							
							index = getIndex(s.charAt(0)); //get index where it will be added
						
							
							misTable[index].add(w);	// add to misTable 

							
							if(ask != false) {
							
								System.out.println("--" + w.getText() + " " + linenum);
								
								Scanner scan = new Scanner(System.in);
								System.out.println("ignore all (i), replace all (r), next(n), or quit(q)?");
								
								char choice = scan.next().charAt(0);
								
									
								while(choice != 'i' & choice != 'r' & choice != 'n' & choice != 'q') {
									
									System.out.println("Error: option not in list. Try again: \n"); //make sure choice is applicable
									choice = scan.next().charAt(0);
									
								}
								
								if(choice == 'i') {
									
									w.setIgnored(true); //skip all future occurrences

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
											
											
											/*
											 * 
											 * Give user options for replacement
											 * 
											 */
										
										
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
								else if(choice == 'q') { //quit
									
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
							w2.addLineNum(linenum);
							
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
									
									
								
									else if(choice == 'r') { //same as before
										
										
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
										
										else { //same as before
										
										
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
										corrected.write(s + punctuation + " "); //include punctuation
									
									
									
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
		

		
		
	}
	
	
	
	/*
	 * takes in arraylist of word objects and a string
	 * returns boolean true/false
	 * 
	 * helper function to see if word already exists in misTable (already been misspelled)
	 * 
	 */
	
	
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
	
	
	/*
	 * 
	 * if contains == true
	 * 
	 * use getWord to actually retrieve word object
	 * 
	 */
	
	
	
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
	
	/*
	 * translates first letter of words into bucket index
	 * 
	 * 
	 */
	
	
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
	
	
	/*
	 * Quicksort method: 
	 * 
	 * takes in an array of arraylists (Word objects), a first index, and a last index
	 * 
	 * uses divide/conquer technique to sort each array list
	 * 
	 * 
	 */
	
	
	
	
	public static void quicksort(ArrayList<Word> arr, int first, int last) {
		

		if(last-first < 3) 
			
			insertionSort3(arr, first, last); //use insertion is only a few elements left; more efficient
			
		
		
		else if(last-first >= 3){
			
			int pivot = medOf3(arr); //find pivot
			
			int splitPoint = partition(arr, first, last, pivot);
			
			quicksort(arr, first, splitPoint -1);
			quicksort(arr, splitPoint + 1, last);
			
			
			
			
			
		
		
		
		}
	}
	
	
	/*
	 * MedOf3 takes in an arrayList and returns the index of the median (first element, middle element, end element)
	 * 
	 * useful for quick sort
	 */
	
	public static int medOf3(ArrayList<Word> arr) {
		
		Word left = arr.get(0);
		Word right = arr.get(arr.size()-1);
		Word mid = arr.get(arr.size()/2);
		
		if(left.compareTo(mid) < 0 & left.compareTo(right) > 0)
			return 0;
		
		else if(left.compareTo(mid) > 0 & left.compareTo(right) < 0)
			return 0;
		
		else if(right.compareTo(left) < 0 & right.compareTo(mid) > 0)
			return arr.size()-1;
		
		else if(right.compareTo(left) > 0 & right.compareTo(mid) < 0)
			return arr.size()-1;
		
		else if(mid.compareTo(left) < 0 & mid.compareTo(right) > 0)
			return arr.size()/2;
		
		else 
			return arr.size()/2;
		
	
		
		
	}
	
	/*
	 * Bulk of the quick sort algorithm
	 * 
	 * Takes in an arrayList, the first index, the last index, and the pivot value
	 * 
	 * returns the split point (integer) which will be used in quick sort to divide into smaller lists
	 * 
	 * 
	 */
	
	public static int partition(ArrayList<Word> A, int first, int last, int pivot) {
		
		Word temp = A.get(pivot);
		
		A.set(pivot, A.get(last)); //set pivot to last element
		
		A.set(last, temp); //set last to pivot, switch complete
		
		int i = 0;
		int j = A.size() - 2; //initialize i, j
		
		while(i < j) {
			

			while(A.get(i).compareTo(A.get(last)) < 0 & i < A.size()-1)
				i ++;
			
			while(A.get(j).compareTo(A.get(last)) > 0 & j >= 0)
				j--;
			
			Word temp2;
			
			if(i < j) {
				
				temp2 = A.get(i);
				A.set(i, A.get(j));
				A.set(j, temp2);
				

			
			}

		}
		Word temp3 = A.get(i);
		A.set(i, A.get(last));
		A.set(last, temp3);
		
		return i;
		
		
		

		
	}
	
	/*
	 * 
	 * Insertion sort: takes in arraylist, start index, end index
	 * 
	 * quickly sorts list (max three elements)
	 * 
	 */
	
	
	public static void insertionSort3(ArrayList<Word> A, int start, int end) {
		
		if(end-start == 2) { //three elements
		
			int i = start;
			int j = start+1;
			int k = end;
		
			if(A.get(j).compareTo(A.get(i)) < 0) { 
			
				Word temp = A.get(i);
				A.set(i, A.get(j));
				A.set(j, temp);
			
			}
		
			if(A.get(k).compareTo(A.get(j)) < 0) {
			
				Word temp2 = A.get(j);
				A.set(j, A.get(k));
				A.set(k, temp2);
			
				if(A.get(j).compareTo(A.get(i)) < 0) {
				
					Word temp3 = A.get(i);
					A.set(i, A.get(j));
					A.set(j, temp3);
				
				
				}
			}
			
			}
		
		else if(end-start == 1) { //two elements
			
			int i = start;
			int j = end;
			
			if(A.get(j).compareTo(A.get(i)) < 0) {
				
				Word temp = A.get(i);
				A.set(i, A.get(j));
				A.set(j, temp);
				
			}
			
			
			
		}
		
		
			
			
			}
	
	/*
	 * sorts misTable using quicksort and prints to output file
	 * 
	 * includes line numbers for duplicate/all words
	 * 
	 * 
	 */
	
	
	public static void writeSorted() throws IOException { 
		
		//Sort
		
		outfile3 = filename_arr[0] + "_sorted.txt";
		
		sorted = new BufferedWriter(new FileWriter(outfile3));
		
		sorted.write("Sorted\n");
		
		for(int i = 0; i < misTable.length; i++) {
			
			
			int start = 0;
			int end =  misTable[i].size() - 1;
			
			quicksort(misTable[i], start, end);
			
			
			
		}
		
		for(int j = 0; j < misTable.length; j++) {
			
			
			for(int k = 0; k < misTable[j].size(); k ++) {
			
				sorted.write(misTable[j].get(k).getText() + " ");
				
				for(int m = 0; m < misTable[j].get(k).getNums().size(); m ++)
					sorted.write(misTable[j].get(k).getNums().get(m) + " ");
				
				sorted.write("\n");
				
			}
			
			
		}
		
		sorted.close();
		
		
		
		
	}
			
		
		
		
	



}
