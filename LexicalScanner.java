/*
    Project 1b
    Author: Shaan Arora, C3236359
    Scanner Class
        Main component of the lexical analyser that will use a DFA object to process each line of a file
        Each line will be processed as a string and fed into the DFA object
        The DFA object will return token/s which will be stored in a LinkedList from the standard Java library
 */
import java.lang.String;
import java.util.ArrayList;

public class LexicalScanner
{
    //Private member variables
    //One for DFSM object to handle processing and determining tokens
    private final DFSM machine;
    //Holds all generated tokens
    private ArrayList<Token> stream;
    //One for holding the number of lines in a file and one for holding the column number
    private int lineNo, colNo;
    //end of file marker
    private boolean eof;

    //Default constructor
    //Preconditions:
    //Postconditions: intilise private memeber variables to default values;
    public LexicalScanner()
    {
        this.machine = new DFSM();
        this.stream = new ArrayList<>();
        this.lineNo = 0;
        this.colNo = 0;
        this.eof = false;
    }

    //Generates and return the next valid Token object
    //Preconditions: A1.scan.hasNextLine() == true
    //Postconditions: returns the next valid token as a Token object based on what A1.scan.next() returns
    public Token getToken()
    {
        Token temp;
        //Using scan.nextLine() because scan.next() does not include whitespaces
        //adding the new line character because scan.nextLine() does not include it when it returns the line as a String
        String s = "";
        //if the buffer in this.machine is empty we can add the next string for tokenizing
        if (this.machine.isBufferEmpty())
        {
            s += A1.scan.nextLine() + "\n";
            this.lineNo++;
            //ball park guess on column number by using the length of s which is scan.next()
            this.colNo = s.length();
            //generate the next valid token
            temp = this.machine.baseMachine(s, this.lineNo, this.colNo);
        }
        //if the buffer in this.machine is not empty then generate the next valid token, should handle the cases such as 123. or 123abc or CD20(
        else{temp = this.nextToken();}
        this.eof = false;
        //add the token we just generated to our output stream
        this.stream.add(temp);
        if(temp.getTokenID() == 0 && !A1.scan.hasNextLine()){this.eof = true;}
        //return the token generated
        return temp;
    }

    //Returns the next valid token from a given source file
    //Preconditions: this.machine.isBufferEmpty() == true
    //Postconditions: returns the next valid token
    public Token nextToken()
    {
        //Should return the next valid token, passing in an empty string because we do not want to add anything to our buffer
        return this.machine.baseMachine("", this.lineNo, this.colNo);
    }

    //Print function to print a token
    //Preconditions: t != null
    //Postconditions: adds the Token object t to the output StringBuilder and prints output with formatting
    public void printToken(Token t)
    {
        //if the size of this.stream is a multiple of 11 then wrap
        //this means that if a line of output is up to 60 characters, the next token is printed with its lexeme then wrapped
        if(this.stream.size() % 11 == 0){System.out.print(t + "\n");}
        else{System.out.print(t);}
    }

    //Determine if we have reached the end of a file
    //Postconditions: A1.scan has been declared and initialised
    //Postconditions: returns true if have reached the end of a file, otherwise false
    public boolean eof(){return this.eof;}
}