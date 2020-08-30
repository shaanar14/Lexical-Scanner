/*
    Project 1b
    Author: Shaan Arora, C3236359
    Scanner Class
        Main component of the lexical analyser that will use a DFA object to process each line of a file
        Each line will be processed as a string and fed into the DFA object
        The DFA object will return token/s which will be stored in a LinkedList from the standard Java library
 */
import java.lang.String;
public class LexicalScanner
{
    private final DFSM machine;
    private final int lineNo;

    //Default constructor
    //Preconditions:
    //Postconditions: intilise private memeber variables to default values;
    public LexicalScanner()
    {
        this.machine = new DFSM();
        //TODO figure out where to calculate line and column number
        this.lineNo = 1;
    }

    //Generates a Token object
    //Preconditions: A1.scan.hasNextLine() == true
    //Postconditions: returns the next valid token as a Token object based on what A1.scan.next() returns
    //TODO need to work out how to generate EOF token
    public Token getToken()
    {
        //Could just pass A1.scan.next() straight into the function
        String input = A1.scan.next();
        return this.machine.keywordMachine(input, this.lineNo, 0); 
    }

    //Print function to print a token
    //Preconditions: t != null
    //Postconditions: prints out Token object t
    //TODO working out how to do wrapping when the line reaches 60 characters
    public void printToken(Token t)
    {
        System.out.print(t);
    }

    //Determine if we have reached the end of a file
    //Postconditions: A1.scan has been declared and initialised
    //Postconditions: returns true if have reached the end of a file, otherwise false
    public boolean eof()
    {
        boolean eof;
        eof = !A1.scan.hasNextLine();
        return eof;
    }

}