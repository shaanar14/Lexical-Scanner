/*
    Project 1b
    Author: Shaan Arora, C3236359
    Scanner Class
        Main component of the lexical analyser that will use a DFA object to process each line of a file
        Each line will be processed as a string and fed into the DFA object
        The DFA object will return token/s which will be stored in a LinkedList from the standard Java library
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Scanner;

public class LexicalScanner
{
    //Private member variables
    //One for DFSM object to handle processing and determining tokens
    private final DFSM machine;
    //Holds all generated tokens
    private ArrayList<Token> stream;
    //Stores the entire file being read in and thus tokenized
    private StringBuilder input;
    //One for holding the number of lines in a file, one for holding the column number and the other to mark which index we are currently up to in input
    private int lineNo, colNo, pos;
    //end of file marker
    private boolean eof;

    //Default constructor
    //Preconditions:
    //Postconditions: intilise private memeber variables to default values;
    public LexicalScanner()
    {
        this.machine = new DFSM();
        this.stream = new ArrayList<>();
        this.input = new StringBuilder();
        this.lineNo = 0;
        this.colNo = 0;
        this.pos = 0;
        this.eof = false;
    }

    //Generates and return the next valid Token object
    //Preconditions: A1.scan.hasNextLine() == true
    //Postconditions: returns the next valid token as a Token object based on what A1.scan.next() returns
    //TODO consider a look ahead function instead of constantly using this.input.charAt(i+1)
    public Token getToken()
    {
        Token temp = new Token();
        StringBuilder lex = new StringBuilder();
        for(int i = this.pos; i < this.input.length(); i++)
        {
            char c = this.input.charAt(i);
            //everytime we look at a char increment column number
            this.colNo++;
            if(DFSM.isLetter(c))
            {
                lex.append(c);

                this.pos = i;
            }
            else if(DFSM.isDigit(c))
            {
                //add the number to lex
                lex.append(c);
                this.pos = i;
            }
            else if(c == '"')
            {
                //check to see what the char after the quotation mark is
                if(this.input.charAt(i+1) == '\n')
                {
                    //add the quotation mark to lex if the char after it is the new line character
                    lex.append(c);
                    temp.setTokenID(62);
                    temp.setLexeme(lex.toString());
                    //+2 because we are currently looking at char at i and we know char at i+1 is a whitespace so
                    //update our position marker to the char after the whitespace
                    this.pos = i + 2;
                    //break so we can skip straight to the return statement
                    break;
                }
                //if we are looking at a " and the char after it is a " then add them to lex and return a TUNDF token
                else if(this.input.charAt(i+1) == '"')
                {
                    lex.append(c);
                    lex.append(this.input.charAt(i+1));
                    //plus +2 same logic as above
                    this.pos = i + 2;
                    temp.setTokenID(62);
                    temp.setLexeme(lex.toString());
                    //break so we can skip straight to the return statement
                    break;
                }
                this.pos = i;
            }
            else if (DFSM.isWhiteSpace(c))
            {
                if (c != '\n'){lex.append(c);}
                else
                {
                    //currently c is at index i so we want to skip it
                    this.pos = i+1;
                    this.lineNo++;
                    //this should act as returning when we see a newline character
                    break;
                }
            }
            else if(c == '\u001a')
            {
                this.eof = true;
                temp.setTokenID(0);
                break;
            }
        }
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

    //Reads the entire file called fileName and stores all of it in a StringBuilder object with a special end of file character at the very end
    //Preconditions: fileName is a valid file in the directory and fileName != ""
    //Postconditions: the entire file is read line by line with the new line character added at the end which is then stored in a StringBuilder object
    //                  for later use
    public void readFile(String fileName)
    {
        try
        {
            FileReader f = new FileReader(fileName);
            Scanner scan = new Scanner(f);
            while(scan.hasNextLine())
            {
                //adding the newline character because nextLine() function does not include it
                //since nextLine() excludes line seperators I dont think I need to worry about system specific line seperator characters
                String s = scan.nextLine() + "\n";
                this.input.append(s);
            }
            //if scan does not have another line to read then add the end of file character to our input
            this.input.append('\u001a');
            scan.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //Searches this.input for a single line comment
    //Preconditions: readFile() has been called
    //Postconditons: returns true if the line is a comment and updates this.pos accordingly, otherwise false
    public boolean findSLComment()
    {
        boolean comment = false;
        for(int i = this.pos; i < this.input.length(); i++)
        {
            //capture the char at index i
            char c = this.input.charAt(i);
            if(c == '/')
            {
                //if the char after the / is a -
                if(this.input.charAt(i+1) == '-')
                {
                    //look ahead again and see if we have a second dash
                    if(this.input.charAt(i+2) == '-')
                    {
                        comment = true;
                    }
                }
            }
            //if the char we are currently looking at is the new line character and have confirm that the line is a comment then update pos
            if(c == '\n' && comment)
            {
                //update pos such that is the index of the char after the new line character
                //the char at i + 1 will either be the eof marker if there is one line in the file or the char at the start of the next line
                this.pos = i + 1;
                break;
            }
        }
        return comment;
    }

    //Searches this.input for a multiline comment
    public boolean findMLComment()
    {
        boolean comment = false;
        for(int i = this.pos; i < this.input.length(); i++)
        {
            char c = this.input.charAt(i);
            if(c == '/')
            {
                if(this.input.charAt(i+1) == '*')
                {
                    if(this.input.charAt(i+2) == '*')
                    {
                        comment = true;
                    }
                }
            }

        }
        return comment;
    }
    
    //Determine if we have reached the end of a file
    //Postconditions: A1.scan has been declared and initialised
    //Postconditions: returns true if have reached the end of a file, otherwise false
    public boolean eof(){return this.eof;}

    //Getter for the entire stream of tokens generated
    //Preconditions: none
    //Preconditions: will return the ArrayList object which is acting as storage for all the tokens being generated
    public ArrayList<Token> getStream() {return this.stream;}
}