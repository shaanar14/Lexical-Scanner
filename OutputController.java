/*
    COMP3290 Project
    Author: Shaan Arora, C3236359
    OutputController Class
        Contains all functionality to create a listing, generate the out for all structures of the compiler and report errors
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.Buffer;
import java.util.ArrayList;

public class OutputController
{
    //Member Variables
    //The file that has been read which is stored as a StringBuilder object by a LexicalScanner object
    private StringBuilder input;
    //Current line number, used for when writing the listing file with the line number at the start of each line
    private int currentLineNo;
    //Stores all Token objects generated by a LexicalScanner object
    private ArrayList<Token> tokens;
    //Stores all Token objects that classify as a lexical error so after the program listing we can output them all with their associated line number
    private ArrayList<Token> lexicalErrors;
    //one for syntax errors

    //Default Constructor
    public OutputController()
    {
        this.input = new StringBuilder();
        this.currentLineNo = 1;
        this.tokens = new ArrayList<>();
        this.lexicalErrors = new ArrayList<>();
    }

    //Parameter Constructor;
    public OutputController(StringBuilder i, int l, ArrayList<Token> t, ArrayList<Token> le)
    {
        this.input = i;
        this.currentLineNo = l;
        this.tokens = t;
        this.lexicalErrors = le;
    }

    public void generateListingFile()
    {
        assert this.input.length() != 0;
        try
        {
            FileWriter fw = new FileWriter("listing.txt");
            this.augmentInput();
            fw.write(this.getInput().toString());
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //Private Helper Function for generateListing()
    //Preconditions:
    //PostConditions: augments the StringBuilder object of the current OutputController to have the line number at the start of each line
    private void augmentInput()
    {
        assert this.getInput().length() != 0;
        //Augment input for the first line
        this.getInput().insert(0, this.getCurrentLineNo() + " ");
        //increment so we have line number 2
        this.currentLineNo++;
        for(int i = 0; i < this.getInput().length(); i++)
        {
            //remove the special end of file marker
            /*if(this.getInput().charAt(i) == '\u001a')
            {
                this.input.deleteCharAt(i);
            }*/
            if(this.getInput().charAt(i) == '\n')
            {
                if((this.getInput().charAt(i+1) == '\u001a'))
                {
                    this.input.deleteCharAt(i+1);
                    break;
                }
                this.input.insert(i+1, this.getCurrentLineNo() + " " );
                this.currentLineNo++;
            }
        }
    }

    public void reportLexicalError()
    {
        assert !this.lexicalErrors.isEmpty();
        System.out.println("Lexical Errors:");
        for(Token t : this.lexicalErrors)
        {
            System.out.printf("%d %s\n", t.getLineNo(), t.toString());
        }
    }










    //Setters

    //Preconditions: A LexicalScanner object has been declare & intialized and then readFile() has been called to generate its own StringBuilder to pass in
    //Postconditions: Assigns the StringBuilder object of i to the private member variable of the current OutputController object
    public void setInput(StringBuilder i) {this.input = i;}

    //Preconditions: None
    //Postconditions: Assigns the value of l to the currentLineNo of the current OutputController object
    public void setCurrentLineNo(int l) {this.currentLineNo = l;}

    //Preconditions: A LexicalScanner object has been declared & initialized followed by readFile() and then getToken() or nextToken() to generate at least one Token object
    //                  so we can pass in that object's stream into the current OutputController object
    //Postconditions: Assigns an ArrayList object of type Token that is acting as storage for all Token objects generated by a LexicalScanner object
    public void setTokens(ArrayList<Token> t) {this.tokens = t;}

    //Preconditions: An OutputController & a LexicalScanner object have both been declared & initialized.
    //                  The LexicalScanner object needs to have called readFile() followed by getToken() or nextToken() so that at least one Token object is generated
    //                  The OutputController needs to have called setTokens()
    //Postconditions: Assigns an ArrayList of type Token that contains any Token objects that have been classified as a lexical error by a LexicalScanner object
    public void setLexicalErrors(ArrayList<Token> le) {this.lexicalErrors = le;}

    //Getters

    //Preconditions: A LexicalScanner objject has been declared & initialized and readFile() has been called
    //               Current OutputController object has been declared & initialized and setInput() has been called passing in the StringBuilder object generated
    //                  by the LexicalScanner object as a result of readFile() being called
    //Postconditions: Returns a StringBuilder object containing the entire file that has been read by a LexicalScanner object
    public StringBuilder getInput() {return this.input;}

    //Preconditions: Current OutputController has been declared & initialized
    //Postconditions: Returns how many lines the object is currently up to in regards to generating a listing file with the line numbers at the start of each line
    public int getCurrentLineNo() {return this.currentLineNo;}

    //Preconditions: A LexicalScanner object has been declared & initialized, readFile() and getToken() or nextToken() has been called
    //               Current OutputController object has been declared and initialized and setTokens() has been called passing in the ArrayList generated by the
    //                  LexicalScanner object
    //PostConditions: Returns an ArrayList object of type Token that contains all Token objects generated by a LexicalScanner object
    public ArrayList<Token> getTokens() {return this.tokens;}


    //Preconditions: An OutputController & a LexicalScanner object have both been declared & initialized.
    //                  The LexicalScanner object needs to have called readFile() followed by getToken() or nextToken() so that at least one Token object is generated
    //                  The OutputController needs to have called setTokens() with the ArrayList generated by the LexicalScanner object passed in
    //Postconditions: Returns an ArrayList of type Token that contains any Token objects that have been classified as a lexical error by a LexicalScanner object
    public ArrayList<Token> getLexicalErrors() {return this.lexicalErrors;}
}