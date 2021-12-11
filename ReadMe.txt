READ ME

In this project you will find three java classes: Word.java, SpellSorter.java, and SpellChecker.java.




Word.java


--> In this class, I created a word object with a few data fields:

-text: string, actual text of word.

-ignored: boolean, if true, a duplicate word will be skipped. 

-replaced: boolean, if true, user has decided to replace word.

-replacement: string, word that acts as replacement when replaced = true.

-linenum: array list of integers, keeps track of line numbers for words for output file

Each of these data fields has corresponding setter / getter methods





SpellChecker & SpellSorter


--> These two classes are very similar, with the main difference being SpellSorter's extra output file.

Both of these files will complete the following operations:

-Take in dictionary file as parameter, store in Quadratic Probing Hash Table (QPHT)
-Prompt user for a file to be spell checked
-Prompt user whether they want to print words, enter new file, or quit 
-When user enters p, program will iterate through file, line by line
-for each misspelled word, it will store bucket style (to be sorted later) by their first letter (case sensitive)
-will prompt user what action they would like to choose for misspelled word
-will attempt to replace word with suggestions
-at the end of the program, it will output a corrected file, and an order file (chronological order of misspelled words)

** see code for description of each method

In SpellSorter

-used quicksort and insertionsort to sort each bucket of misspelled words and return file of sorted misspelled words.



** IF I had more time, I would try to implement more replacement methods. 

--> As of now, only the following three are implemented:


Delete:

Tries to delete each character in misspelled word to account for extra character typo


Swap:

Swaps each pair of adjacent characters to account for order typo



Split:

Tries to split the word in multiple 2 word combinations to account for user forgetting a space.





