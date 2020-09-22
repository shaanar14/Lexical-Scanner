/*
    COMP3290 Project
    Author: Shaan Arora, C3236359
    OutputController Class
        Contains all functionality to create a listing, generate the out for all structures of the compiler and report errors
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class OutputController
{
    //Member Variables
    //The file that has been read which is stored as a StringBuilder object by a LexicalScanner object
    private StringBuilder input;
    //Current line number, used for when writing the listing file with the line number at the start of each line
    private int currentLineNo;
    //Stores all Token objects generated by a LexicalScanner object
    private ArrayList<Token> tokenStream;
    //Stores all Token objects that classify as a lexical error so after the program listing we can output them all with their associated line number
    private ArrayList<Token> lexicalErrors;
    //one for syntax errors

    //Default Constructor
    public OutputController()
    {
        this.input = new StringBuilder();
        this.currentLineNo = 1;
        this.tokenStream = new ArrayList<>();
        this.lexicalErrors = new ArrayList<>();
    }

    //Parameter Constructor
    public OutputController(StringBuilder i, int l, ArrayList<Token> t, ArrayList<Token> le)
    {
        this.input = i;
        this.currentLineNo = l;
        this.tokenStream = t;
        this.lexicalErrors = le;
    }

    //Preconditions: LexicalScanner & OutputController object have been declared and initialized.
    //                  readFile() call from the LexicalScanner object to read the source file
    //                  setInput() with the LexicalScanner object getInput() call as its parameter to populate the OutputController objects input private member variable
    //Postconditions: Creates a file with the name of which is whatever the value of fileName is and then writes the original source code to that file
    //                  with the line numbers at the very beginning of each line
    //TODO test
    public void generateListingFile(String fileName)
    {
        assert this.getInput().length() != 0;
        try
        {
            //Creates a new file with the name of the String value passed in
            FileWriter fw = new FileWriter(fileName);
            //Augment our original input to include the line number at the beginning of each line which is returned as a StringBuilder object
            //the String version of that StringBuilder object is written to the file created above
            String output = this.augmentInput().toString();
            fw.write(output);
            //if we have found lexical errors then write them
            if(!this.getLexicalErrors().isEmpty())
            {
                //write the error tokens at the end of the file
                fw.write("\n\nLexical Errors Found (" + this.getLexicalErrors().size() + "):\n");
                //StringBuilder object storing all error tokens found
                StringBuilder errors = new StringBuilder();
                //Count will be our marker for the line number of our lexical errors
                /*int count = this.getLexicalErrors().get(0).getLineNo();
                errors.append(String.format("\nLine %d:", count));*/
                int count = 0;
                //For every Token object in that is classified as a lexical error we write the errors at the end of the file
                for (Token t : this.getLexicalErrors())
                {
                    errors.append(String.format("Line %d, Column %d: %s\n", t.getLineNo(), t.getColNo(), t.getLexeme()));
                }
                //Write all error tokens to the listing file
                fw.write(errors.toString());
            }
            else
            {
                fw.write("\n\n0 Lexical Errors Found");
            }
            fw.close();
        }
        catch (IOException e) {e.printStackTrace();}
    }

    //Helper Function for generateListingFile()
    //Preconditions: LexicalScanner & OutputController object have been declared and initialized.
    //                  readFile() from the LexicalScanner object have been called
    //                  setInput() with the LexicalScanner object getInput() call as its parameter to populate the OutputController objects input private member variable
    //PostConditions: Augments the StringBuilder object of the current OutputController to have the line number at the start of each line
    //TODO test
    private StringBuilder augmentInput()
    {
        //make sure that we actually have a file read in and input has been populated
        assert this.getInput().length() != 0;
        //Create a new StringBuilder object that is a copy of the private member StringBuilder object
        //might change it to this.getInput().toString()
        StringBuilder output = new StringBuilder(this.getInput());
        //Augment the new output StringBuilder object for the first line
        //using insert with an offset such that we can put the line number at the very beginning
        output.insert(0, this.getCurrentLineNo() + " ");
        //increment the marker so we start the for loop at line number 2
        this.currentLineNo++;
        //might have to do output.length() - 1 because of indexing issues
        for(int i = 0; i < output.length(); i++)
        {
            //chec if the char at index i is the new line character
            if(output.charAt(i) == '\n')
            {
                //if the char after the new line character is the special end of file character
                //then remove both chars
                //setLength() sets the internal buffer size of the StringBuilder object and works better than deleteCharAt(int)
                if(output.charAt(i+1) == '\u001a')
                {
                    output.setLength(output.length() - 2);
                    break;
                }
                //every time we reach a new line char add the line number at the start of the new line
                //the offset for the insert function is the char after the new line character
                output.insert(i+1, this.getCurrentLineNo() + " " );
                //update the marker that keeps track of which line number we are up to
                this.currentLineNo++;
            }
        }
        //output.setLength(output.length() - 1);
        //Return the StringBuilder object which is an augmented version of the original input
        return output;
    }

    //Preconditions:
    //Postconditions:
    public void reportLexicalError()
    {
        assert !this.getLexicalErrors().isEmpty();
        System.out.println("Lexical Errors:");
        for(Token t : this.getLexicalErrors())
        {
            System.out.printf("%d %s\n", t.getLineNo(), t.toString());
        }
    }

    //Preconditions: ls has been declared & initialized, readFile() has been called on that object
    //Postconditions: Outputs the LexicalScanner object passed in to the terminal as per the specs of assignment 1
    //possibly extract error tokens from here
    public void outputLexicalScanner(LexicalScanner ls)
    {
        assert ls.getInput().length() != 0;
        do
        {
            Token temp = ls.getToken();
            ls.printToken(temp);
            //populate errors here?
            if(temp.getTokenID() == 62)
            {
                this.lexicalErrors.add(temp);
            }
        } while(!ls.isEoF());
    }




    //public void outputSyntaxTree(SyntaxTree st)
    //reconsider function name
    //st.preOrderTraversal()






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
    public void setTokenStream(ArrayList<Token> t) {this.tokenStream = t;}

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
    public ArrayList<Token> getTokenStream() {return this.tokenStream;}


    //Preconditions: An OutputController & a LexicalScanner object have both been declared & initialized.
    //                  The LexicalScanner object needs to have called readFile() followed by getToken() or nextToken() so that at least one Token object is generated
    //                  The OutputController needs to have called setTokens() with the ArrayList generated by the LexicalScanner object passed in
    //Postconditions: Returns an ArrayList of type Token that contains any Token objects that have been classified as a lexical error by a LexicalScanner object
    public ArrayList<Token> getLexicalErrors() {return this.lexicalErrors;}
}