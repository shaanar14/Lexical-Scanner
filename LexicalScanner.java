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
    //One for holding the number of lines in a file
    private int lineNo, colNo;
    private ArrayList<Token> stream;
    //Default constructor
    //Preconditions:
    //Postconditions: intilise private memeber variables to default values;
    public LexicalScanner()
    {
        this.machine = new DFSM();
        this.lineNo = 0;
        this.colNo = 0;
        this.stream = new ArrayList<>();
    }

    //Generates and return the next valid Token object
    //Preconditions: A1.scan.hasNextLine() == true
    //Postconditions: returns the next valid token as a Token object based on what A1.scan.next() returns
    public Token getToken()
    {
        //since this checks if we have next line in our file incrememnt lineNo, if we reach the end of file generate the TEOF token
        if(!this.eof()){this.lineNo++;}
        else
        {
            if(!this.machine.isBufferEmpty())
            {
                return this.nextToken();
            }
            //create the TEOF Token object
            Token end = new Token(0, "", this.lineNo,0);
            //add the TEOF token to our output stream
            this.stream.add(end);
            //return the TEOF Token object
            return end;
        }
        //Using scan.nextLine() because scan.next() does not include whitespaces
        //adding the new line character because scan.nextLine() does not include it when it returns line as a String
        String s = A1.scan.nextLine() + "\n";
        //ball park guess on column number by using the length of s which is scan.next()
        this.colNo = s.length();
        //single line comment found ignore the rest of the line
        if(s.charAt(0) == '/' && s.charAt(1) == '-' && s.charAt(2) == '-')
        {
            A1.scan.nextLine();
        }
        //multiline comment ignore everything until we see the closing tag or eof
        else if(s.charAt(0) == '/' && s.charAt(1) == '*' && s.charAt(2) == '*')
        {
            do
            {
                //TODO test this now that I have changed it from while to do while
                if((A1.scan.next().charAt(0) == '*' && s.charAt(1) == '*' && s.charAt(2) == '/') || this.eof()) {break;}
                else{A1.scan.nextLine();}
            } while(A1.scan.hasNextLine());
        }
        //if the buffer in this.machine is empty we can add the next string for tokenizing
        else if(this.machine.isBufferEmpty())
        {
            //generate the next valid token
            Token temp = this.machine.baseMachine(s, this.lineNo, this.colNo);
            //add the token we just generated to our output stream
            this.stream.add(temp);
            //return the token generated
            return temp;
        }
        //if the buffer in this.machine is not empty then generate the next valid token, should handle the cases such as 123. or 123abc or CD20(
        else
        {
            Token temp = this.nextToken();
            this.stream.add(temp);
            return temp;
        }
        //TODO find something else to return
        return null;
    }

    //Print function to print a token
    //Preconditions: t != null
    //Postconditions: adds the Token object t to the output StringBuilder and prints output with formatting
    public void printToken(Token t)
    {
        //if Token t has the ID of a TUNDF token then just print it as that token is already formatted in toString() in Token class
        if(t.getTokenID() == 62){System.out.print(t);}
        //Print output formatted to be wrap after length of 60 hopefully
        else{System.out.printf("%1.60s",t);}
    }

    //Returns the next valid token from a given source file
    //Preconditions: this.machine.isBufferEmpty() == false
    //Postconditions
    public Token nextToken()
    {
        //Should return the next valid token
        return this.machine.baseMachine("", this.lineNo, this.colNo);
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