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
    private final Factory machine;
    //Holds all generated tokens
    private final ArrayList<Token> stream;
    //Stores the entire file being read in and thus tokenized
    private final StringBuilder input;
    //One for holding the number of lines in a file, one for holding the column number and the other to mark which index we are currently up to in input
    private int lineNo, colNo, pos;
    //end of file marker
    private boolean eof;

    //Default constructor
    //Preconditions: none
    //Postconditions: intilise private member variables to default values
    public LexicalScanner()
    {
        this.machine = new Factory();
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
    //TODO test function to see where for loop should go
    public Token getToken()
    {
        Token temp = new Token();
        boolean floatFound = false, qmFound = false, identifier = false;
        StringBuilder lex = new StringBuilder();
        //Check to see if we have a single or multi line comment
        //if we dont have a single line comment, check for a multiline comment
        if(!this.findSLComment())
        {
            //if we have a multiline comment then either this.pos will be the index of the char after / or the index of the eof char
            if(!this.findMLComment())
            {
                for(int i = this.pos; i < this.input.length(); i++)
                {
                    char c = this.lookUp(i);
                    //everytime we look at a char increment column number
                    this.colNo++;
                    //if we have found a quotation mark then we keep adding any char except \n until we see another quotation mark
                    if(qmFound)
                    {
                        //if we have found a quotation mark but c is the new line character then thats a lexical error
                        if(c == '\n')
                        {
                            temp = new Token(62, lex.toString(), this.lineNo, this.colNo);
                            //update pos to be the index of the char after the new line character
                            this.pos = i + 1;
                            break;
                        }
                        //c can be anything in terms of a string literal
                        lex.append(c);
                        this.pos = i;
                        //if the char after c is a quotation mark then generate a string literal token
                        if(this.lookUp(i+1) == '"')
                        {
                            temp = machine.stringLiteral(lex, this.lineNo, this.colNo);
                            //i+1 is another quotation mark so update pos to be the index of the char after it
                            this.pos += i + 2;
                            break;
                        }
                    }
                    else if(Factory.isLetter(c))
                    {
                        lex.append(c);
                        this.pos = i;
                        if(!Factory.isDigit(this.lookUp(i+1)) && !Factory.isLetter(this.lookUp(i+1)))
                        {
                            //if the char at i+1 is not a digit or a letter then generate a token
                            //will either make temp an identifier or keyword token depending on lex
                            temp = this.machine.identifierToken(lex, this.lineNo, this.colNo);
                            //update pos to be the index of the char at i+1
                            this.pos = i+ 1;
                            break;
                        }
                    }
                    else if(Factory.isDigit(c))
                    {
                        //TODO use cases - integer literals, float literals, as part of an identifier
                        //add the number to lex
                        lex.append(c);
                        this.pos = i;
                        if(Factory.isLetter(this.lookUp(i-1)))
                        {
                            //if the char before the digit is a letter
                            identifier = true;
                        }
                    }
                    else if(Factory.isDelim(c))
                    {
                        //generate a token for the delimeter
                        temp = this.machine.delimToken(c, this.lineNo, this.colNo);
                        //update pos so its now at the index of the char after the delimeter
                        this.pos = i + 1;
                        //break so we can immedietaly return temp
                        break;
                    }
                    else if(Factory.isOperator(c))
                    {
                        // %= doesnt exist
                        if(this.lookUp(i+1) == '=' && c != '%')
                        {
                            //add the operator to lex
                            lex.append(c);
                            //the char after the operator is an = so add it to lex
                            lex.append(this.lookUp(i+1));
                            //generate composite operator machine
                            temp = this.machine.compositeOpToken(lex.toString(), this.lineNo, this.colNo);
                        }
                        else
                        {
                            //char c is jus a single operator so generate a token for it
                            temp = this.machine.operatorToken(c,this.lineNo, this.colNo);
                            //set pos to be the index of the char after the operator
                            this.pos = i + 1;
                        }
                        //break so we can return temp
                        break;
                    }
                    //Use case where _ is a valid start to an identifier
                    else if (c == '_')
                    {
                        //update pos to be the index of the _
                        this.pos = i;
                        lex.append(c);
                        if(!Factory.isLetter(this.lookUp(i+1)) || !Factory.isDigit(this.lookUp(i+1)))
                        {
                            //if the char after the underscore is not a letter or a digit then return a TUNDF token
                            temp = new Token(62, lex.toString(), this.lineNo, this.colNo);
                            //update pos to be the index of the char at i+1
                            this.pos += i+1;
                            break;
                        }
                    }
                    else if(c == '"')
                    {
                        //check to see what the char after the quotation mark is
                        if(this.lookUp(i+1) == '\n')
                        {
                            temp.setTokenID(62);
                            temp.setLexeme(String.valueOf(c));
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
                            temp = new Token(62, lex.toString(), this.lineNo, this.colNo);
                            //break so we can skip straight to the return statement
                            break;
                        }
                        //not appending the quotation mark to lex because for string literal tokens we do not want " included in the lexeme
                        qmFound = true;
                        //update pos to be the index of the char after the quotation marks
                        this.pos = i+1;
                    }
                    else if (Factory.isWhiteSpace(c))
                    {
                        //any spaces or tabs we just add it to lex more important for cases such as string literals
                        if (c != '\n'){lex.append(c);}
                        else
                        {
                            //currently c is at index i so we want to skip it
                            this.pos = i+1;
                            //we have reached a new line so update lineNo
                            this.lineNo++;
                            if(identifier)
                            {
                                temp = this.machine.identifierToken(lex, this.lineNo, this.colNo);
                                break;
                            }
                        }
                    }
                    //TODO rework entire else if case
                    else if(Factory.isInvalid(c))
                    {
                        //update pos to be the index of the invalid char
                        this.pos = i;
                        //append the invalid char to lex
                        lex.append(c);
                        //edge case where ! is only valid when a = follows it
                        if(c == '!')
                        {
                            if(this.lookUp(i+1) == '=')
                            {
                                lex.append(this.lookUp(i + 1));
                                temp = this.machine.compositeOpToken(lex.toString(), this.lineNo, this.colNo);
                                break;
                            }
                        }
                        //if statement should allow for grouping of invalid chars such that we do not need to return a single token for each individual invalid char
                        //c is an invalid char so check to see if the char after is valid
                        if(!Factory.isInvalid(this.lookUp(i+1)))
                        {
                            temp = new Token(62, lex.toString(), this.lineNo, this.colNo);
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
                        //complete reset on this.pos so know its the index of the start of this.input
                        this.pos = 0;
                        //set the token ID to be that of T_EOF
                        temp = new Token(0, "", this.lineNo, this.colNo);
                        //break statement so we can immediately return the token
                        break;
                    }
                }
            }
        }
        //add the token we just generated
        this.stream.add(temp);
        //return the generated token
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
            //if comment is true
            //if the char we are currently looking at is the new line character and have confirm that the line is a comment then update pos
            if(c == '\n' && comment)
            {
                //update pos such that is the index of the char after the new line character only when we have found a comment
                //the char at i + 1 will either be the eof marker if there is one line in the file or the char at the start of the next line
                this.pos = i + 1;
                break;
            }
        }
        //if comment is false then this.pos should still be at the same value as when the function was called
        return comment;
    }

    //Searches this.input for a multiline comment
    //Preconditions: readFile() has been called
    //Postconditions: returns true if a multiline comment has been found and updates this.pos to point to be the index of the char after the end tag of the comment
    //TODO test function again to see what the value of this.pos is when the function returns false
    public boolean findMLComment()
    {
        boolean commentStart = false, commentEnd = false;
        int i = this.pos;
        for(; i < this.input.length(); i++)
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
        if(commentStart)
        {
            //if comment is true, it should break and we end up here
            while (this.lookUp(i) != '*' && this.lookUp(i) != '\u001a')
            {
                i++;
            }
            //we know the char at this.pos is a * or the end of file character because thats how we broke out of the while loop
            //could refactor to switch case
            if (this.lookUp(i) == '*')
            {
                if (this.lookUp(i + 1) == '*')
                {
                    if (this.lookUp(i + 2) == '/')
                    {
                        commentEnd = true;
                        //we know that the char at index this.pos is a * and at this.pos+1 is a * and at this.pos+2 is a / hence +=3
                        //if the char at this.pos + 3 is a new line character then update pos to be the char after the new line character
                        this.pos += this.lookUp(i + 3) == '\n' ? 4 : 3;
                    }
                }
            }
            else if(this.lookUp(i) == '\u001a'){commentEnd = true;}
        }
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