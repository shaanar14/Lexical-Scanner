/*
    Project 1b
    Author: Shaan Arora, C3236359
    LexicalScanner Class
        Main component of the lexical analyser that uses a DFSM object to assist in generating tokens based on the file thats read in
        The file read in is stored in a StringBuilder object and all tokens generated are stored in an ArrayList object
 */

import java.io.FileReader;
import java.io.IOException;
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
    //TODO consider a look ahead function instead of constantly using this.lookUp(i+1)
    //TODO grouping invalid chars and returning them as a single token instead of a token for each individual invalid char
    public Token getToken()
    {
        Token temp = new Token();
        boolean invalid = false;
        StringBuilder lex = new StringBuilder();
        //Check to see if we have a single or multi line comment
        //if we dont have a single line comment, check for a multiline comment
        if(!this.findSLComment())
        {
            //if we have a multiline comment then either this.pos will be the index of the char after / or the index of the eof char
            this.findMLComment();
        }
        for(int i = this.pos; i < this.input.length(); i++)
        {
            char c = this.lookUp(i);
            //everytime we look at a char increment column number
            this.colNo++;
            //TODO work out logic for isLetter and isDigit
            if(DFSM.isLetter(c))
            {
                //TODO use cases, strings and identifiers which also handles keywords
                lex.append(c);
                this.pos = i;
            }
            else if(DFSM.isDigit(c))
            {
                //TODO use cases - integer literals, float literals, as part of an identifier
                //add the number to lex
                lex.append(c);
                this.pos = i;
            }
            else if(DFSM.isDelim(c))
            {
                //generate a token for the delimeter
                temp = this.machine.delimMachine(c,this.lineNo, this.colNo);
                //update pos so its now at the index of the char after the delimeter
                this.pos = i + 1;
                //break so we can immedietaly return temp
                break;
            }
            else if(DFSM.isOperator(c))
            {
                // %= doesnt exist
                if(this.lookUp(i+1) == '=' && c != '%')
                {
                    //add the operator to lex
                    lex.append(c);
                    //the char after the operator is an = so add it to lex
                    lex.append(this.lookUp(i+1));
                    //generate composite operator machine
                    temp = this.machine.compositeOpMachine(lex.toString(), this.lineNo, this.colNo);
                }
                else
                {
                    //char c is jus a single operator so generate a token for it
                    temp = this.machine.operatorMachine(c,this.lineNo, this.colNo);
                    //set pos to be the index of the char after the operator
                    this.pos = i + 1;
                }
                //break so we can return temp
                break;
            }
            else if (c == '_')
            {
                this.pos = i;
                lex.append(c);
                if(!DFSM.isLetter(this.lookUp(i+1)) || !DFSM.isDigit(this.lookUp(i+1)))
                {
                    //if the char after the underscore is not a letter or a digit then return a TUNDF token
                    temp.setTokenID(62);
                    temp.setLexeme(lex.toString());
                    break;
                }
            }
            else if(c == '!')
            {
                this.pos = i;
                lex.append(c);
                if(this.lookUp(i+1) == '=')
                {
                    lex.append(this.lookUp(i+1));
                    temp = this.machine.compositeOpMachine(lex.toString(), this.lineNo, this.colNo);
                }
                else{temp = this.machine.errorMachine(lex.toString(), this.lineNo,this.colNo);}
                break;
            }
            else if(c == '"')
            {
                //check to see what the char after the quotation mark is
                if(this.lookUp(i+1) == '\n')
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
                else if(this.lookUp(i+1) == '"')
                {
                    lex.append(c);
                    lex.append(this.lookUp(i+1));
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
                //any spaces or tabs we just add it to lex more important for cases such as string literals
                if (c != '\n'){lex.append(c);}
                else
                {
                    //currently c is at index i so we want to skip it
                    this.pos = i+1;
                    //we have reached a new line so update lineNo
                    this.lineNo++;
                    //this should act as returning when we see a newline character
                    break;
                }
            }
            else if(DFSM.isInvalid(c))
            {
                this.pos = i;
                lex.append(c);
                //if statement should allow for grouping of invalid chars such that we do not need to return a single token for each individual invalid char
                //c is an invalid char so check to see if the char after is valid
                if(!DFSM.isInvalid(this.lookUp(i+1)))
                {
                    temp.setTokenID(62);
                    temp.setLexeme(lex.toString());
                    //set pos to be the index of the valid char
                    this.pos = i+1;
                    //return a TUNDF token for all the invalid chars
                    break;
                }
            }
            else if(c == '\u001a')
            {
                //we have reached the end of the file
                this.eof = true;
                //set the token ID to be that of T_EOF
                temp.setTokenID(0);
                //break statement so we can immediately return the token
                break;
            }
        }
        //add the token we just generated
        this.stream.add(temp);
        return temp;
    }

    //Returns the next valid token from a given source file
    //Preconditions: this.machine.isBufferEmpty() == true
    //Postconditions: returns the next valid token
    //TODO rework
    /*public Token nextToken()
    {
        //Should return the next valid token, passing in an empty string because we do not want to add anything to our buffer
        return this.machine.baseMachine("", this.lineNo, this.colNo);
    }*/

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
            char c = this.lookUp(i);
            if(c == '/')
            {
                //if the char after the / is a -
                if(this.lookUp(i+1) == '-')
                {
                    //look ahead again and see if we have a second dash
                    if(this.lookUp(i+2) == '-')
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
    //Preconditions: readFile() has been called
    //Postconditions: returns true if a multiline comment has been found and updates this.pos to point to be the index of the char after the end tag of the comment
    public boolean findMLComment()
    {
        boolean commentStart = false, commentEnd = false;
        for(int i = this.pos; i < this.input.length(); i++)
        {
            char c = lookUp(i);
            if(c == '/')
            {
                if(this.lookUp(i+1) == '*')
                {
                    if(this.lookUp(i+2) == '*')
                    {
                        commentStart = true;
                        //i + 3 because the char at is / , the char at i + 1 is * and the char at i + 2 is *
                        this.pos = i + 3;
                        break;
                    }
                }
            }
        }
        //if comment is true, it should break and we end up here
        while(this.lookUp(this.pos) != '*' && this.lookUp(this.pos) != '\u001a') {this.pos++;}
        //we know the char at this.pos is a * or the end of file character because thats how we broke out of the while loop
        //could refactor to switch case
        if(this.lookUp(this.pos) == '*')
        {
            if(this.lookUp(this.pos+1) == '*')
            {
                if(this.lookUp(this.pos+2) == '/')
                {
                    commentEnd = true;
                    //we know that the char at index this.pos is a * and at this.pos+1 is a * and at this.pos+2 is a / hence +=3
                    //if the char at this.pos + 3 is a new line character then update pos to be the char after the new line character
                    this.pos += this.lookUp(this.pos + 3) == '\n' ? 4 : 3;
                }
            }
        }
        else if(this.lookUp(this.pos) == '\u001a'){commentEnd = true;}
        return (commentStart && commentEnd);
    }
    
    //Getter for eof, determnes if we have reached the end of a file
    //Postconditions: A1.scan has been declared and initialised
    //Postconditions: returns true if have reached the end of a file, otherwise false
    public boolean eof(){return this.eof;}

    //Getter for the entire stream of tokens generated
    //Preconditions: none
    //Preconditions: will return the ArrayList object which is acting as storage for all the tokens being generated
    public ArrayList<Token> getStream() {return this.stream;}

    //Getter for the input so we can return the file we have read in as a StringBuilder object
    //Preconditions: readFile() has been called thus input has been populate
    //Postconditions: returns a StringBuilder object which contains the file read in by readFile()
    public StringBuilder getInput() {return this.input;}

    //Look ahead or look behind function
    //Preconditions: readFile() has been called thus this.input has been populated
    //Postconditions: returns the char at index it
    private char lookUp(int i) {return this.input.charAt(i);}
}