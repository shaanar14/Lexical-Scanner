/*
    Project 1b
    Author: Shaan Arora, C3236359
    LexicalScanner Class
        Main component of the lexical analyser that uses a DFSM object to assist in generating tokens based on the file thats read in
        The file read in is stored in a StringBuilder object and all tokens generated are stored in an ArrayList object
 */

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class LexicalScanner
{
    //Member variables
    //Storage for all generated Token objects
    private final ArrayList<Token> stream;
    //Stores the entire file being read in which will then be tokenized
    private final StringBuilder input;
    //Stores the number of lines in a file, the column number and to mark which char we are currently up to when tokenizing the input StringBuilder
    //  count is used for keep tracking of how many characters are currently in an output line
    private int lineNo, colNo, pos, count;
    //Marks if we have reach the end of the file
    private boolean eof;

    //Default constructor
    //Preconditions: None
    //Postconditions: Intialize private member variables to default values
    public LexicalScanner()
    {
        this.stream = new ArrayList<>();
        this.input = new StringBuilder();
        //Starting line number at 1
        this.lineNo = 1;
        this.colNo = 0;
        this.pos = 0;
        this.count = 0;
        this.eof = false;
    }

    //Generates and return the next valid Token object from input file read
    //Preconditions: this.readFile() has been called
    //Postconditions: Returns the next valid token as a Token object based on what A1.scan.next() returns
    public Token getToken()
    {
        Token temp = new Token();
        boolean floatFound = false, qmFound = false, identifier = false, slComment = false, mlComment = false;
        StringBuilder lex = new StringBuilder();
        for(int i = this.pos; i < this.input.length(); i++)
        {
            //Single line comment case terminates when a new line character is found
            if(slComment)
            {
                char c = this.lookUp(i);
                while(c != '\n')
                {
                    i++;
                    this.colNo++;
                    c = this.lookUp(i);
                }
                //+1 because we want the char after the new line character
                this.pos = i + 1;
                i += 1;
                //c == new line character so increment line number
                this.lineNo++;
                slComment = false;
            }
            //Multi line comment case which starts with /** and terminates either with match closing tag **/ or end of file
            else if(mlComment)
            {
                char c = this.lookUp(i);
                while(c != '\u001a')
                {
                    //reached the start of the end tag for the comment
                    if(c == '*')
                    {
                        if(this.lookUp(i+1) == '*')
                        {
                            if(this.lookUp(i+2) == '/')
                            {
                                i += 3;
                                this.pos = i;
                                //+3 because have seen three chars
                                this.colNo += 3;
                                mlComment = false;
                                //might need to change this because we just want to break out of the while loop
                                break;
                            }
                        }

                    }
                    if(c == '\n') {this.lineNo++;}
                    i++;
                    this.colNo++;
                    c = this.lookUp(i);
                }
            }
            char c = this.lookUp(i);
            //everytime we look at a char increment column number
            this.colNo++;
            //Specific case if we have found a quotation mark which we then we group any chars afterwards until the case terminates
            //case terminates at new line character which is invalid or a closing quotation mark
            if(qmFound)
            {
                //assign count to i which is our current index in input
                int count = i;
                //Single & because we want both to be evaluated
                while(c != '\n' & c != '"')
                {
                    //c is not the new line character or a quotation mark so we just add it to lex
                    lex.append(c);
                    //update count so that we can look at the next char
                    count++;
                    //reassign c
                    c = this.lookUp(count);
                    //update column number
                    this.colNo++;
                }
                if(c == '\n')
                {
                    temp = Factory.errorToken(lex.toString(), this.lineNo, this.colNo);
                    //update pos to be the char after the newline character
                    this.pos = count + 1;
                    //we have seen a new line character so update line number and column number;
                    this.lineNo++;
                    //break statement so we can add temp to the ArrayList acting as storage and return the token
                }
                //if c is at index count is a quotation mark that means we have seen a full string literal
                else
                {
                    temp = Factory.stringLiteral(lex, this.lineNo, this.colNo);
                    this.pos = count + 1;
                }
                //Update column number
                this.colNo++;
                //break statement so we can add temp to the ArrayList acting as storage and return the token
                break;
            }
            //Specific case for identifiers so we can consume and tokenize letters and/or digits
            if(identifier)
            {
                int count = i;
                while(Factory.isLetter(c) || Factory.isDigit(c))
                {
                    //c is a digit or a letter
                    lex.append(c);
                    //update count so that we can look at the next char
                    count++;
                    //reassign c
                    c = this.lookUp(count);
                    //update column number
                    this.colNo++;
                }
                temp = Factory.identifierToken(lex, this.lineNo, this.colNo);
                this.pos = count;
                this.colNo++;
                break;
            }
            //Specific case if we have found the pattern digit dot digit
            //Turns that pattern into a float literal token
            if(floatFound)
            {
                int count = i;
                while(Factory.isDigit(c))
                {
                    lex.append(c);
                    count++;
                    this.colNo++;
                    c = this.lookUp(count);
                }
                temp = Factory.floatLiteral(lex, this.lineNo, this.colNo);
                this.pos = count;
                this.colNo++;
                break;
            }
            //Identifier and Keywords use case
            //Determines if any letters are to be grouped and turned into an identifier or keyword token.
            //If a letter is found a digit follows it then it will group and classify them both as an identifier
            if(Factory.isLetter(c))
            {
                lex.append(c);
                this.pos = i;
                if(Factory.isDigit(this.lookUp(i+1)) || Factory.isLetter(this.lookUp(i+1)))
                {
                    identifier = true;
                }
                else
                {
                    temp = Factory.identifierToken(lex, this.lineNo, this.colNo);
                    this.pos = i + 1;
                    break;
                }
            }
            //Integer and Identifier use case
            //Any digits found are either turned into an integer literal token or if a dot is found then a float literal token or if t
            else if(Factory.isDigit(c))
            {
                //to check if lex is less than 64 bits use Long.SIZE
                //add the number to lex
                lex.append(c);
                this.pos = i;
                char l = this.lookUp(i+1);
                if(!Factory.isDigit(l) && (l != '.'))
                {
                    temp = Factory.integerLiteral(lex, this.lineNo, this.colNo);
                    this.pos = i + 1;
                    break;
                }
                //if l our look up char is a dot which is at index i + 1
                if(l == '.')
                {
                    //if the char after the . is not a digit then return a integer literal token
                    if(!Factory.isDigit(this.lookUp(i+2)))
                    {
                        temp = Factory.integerLiteral(lex, this.lineNo, this.colNo);
                        //we know that the char after the dot is a dot, update pos to be the index of the dot
                        this.pos = i + 1;
                        break;
                    }
                    //update i because we are still in the for loop
                    i += 1;
                    //add the dot to our StringBuilder object
                    lex.append(l);
                    //if the char after the dot is a digit then we have found a float
                    floatFound = true;
                }
            }
            //Divide operator and comment use case
            //This use case is above the operator use case so I am assuming it takes precedence specifically for the divide use case
            else if (c == '/')
            {
                if(this.lookUp(i+1) == '-')
                {
                    //look ahead again and see if we have a second dash
                    if (this.lookUp(i + 2) == '-')
                    {
                        //if we have /--
                        slComment = true;
                        //update pos to be the index of the char after /--
                        //might change this to +4
                        this.pos = i + 3;
                    }
                    //we have seen /- which is not a valid start for a single line comment so we just tokenise the / and - seperately
                    else
                    {
                        //the char after the / is not a - or the char after /- is not a - so just generate a token for the /
                        temp = Factory.operatorToken(c,this.lineNo, this.colNo);
                        //set pos to be the index of the char after the operator
                        this.pos = i + 1;
                        break;
                    }
                }
                else if(this.lookUp(i+1) == '*')
                {
                    if(this.lookUp(i+2) == '*')
                    {
                        //we have found /**
                        mlComment = true;
                        //+3 because we want the position marker to be at the index of the char after the /**
                        this.pos = i + 3;
                        i += 3;
                    }
                    else
                    {
                        //we have seen /* which is a not a valid start for a multiline comment so tokenize the / and * seperately
                        temp = Factory.operatorToken(c,this.lineNo, this.colNo);
                        //set pos to be the index of the char after the operator
                        this.pos = i + 1;
                        break;
                    }
                }
                else if(this.lookUp(i+1) == '=')
                {
                    //c is / so add it to lex
                    lex.append(c);
                    //char after / is a =
                    lex.append(this.lookUp(i+1));
                    //generate composite operator token for /=
                    temp = Factory.compositeOpToken(lex.toString(), this.lineNo, this.colNo);
                    //update pos to be the index of the char after the /=
                    this.pos = i + 2;
                    break;
                }
                //if we have not found /-- or /** then we treat the / as an operator and generate a token
                else
                {
                    //the char after the / is not a - or the char after /- is not a - so just generate a token for the /
                    temp = Factory.operatorToken(c,this.lineNo, this.colNo);
                    //set pos to be the index of the char after the operator
                    this.pos = i + 1;
                    break;
                }
            }
            //Operator and Composite operator use case
            //Captures plus, minus, multiply, modulus, equals, less than, greater than, caret for exponentials
            //If an equal sign follows immediately after the operators mentioned above thats captured all together as a composite operator and tokenized
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
                    temp = Factory.compositeOpToken(lex.toString(), this.lineNo, this.colNo);
                    //update pos to be the index after =
                    this.pos = i+2;
                }
                else
                {
                    //char c is jus a single operator so generate a token for it
                    temp = Factory.operatorToken(c,this.lineNo, this.colNo);
                    //set pos to be the index of the char after the operator
                    this.pos = i + 1;
                }
                //break so we can return temp
                break;
            }
            //Delimeter use case captures square brackets, regular parentheses, semicolon, colon, comma and dot outside of the float literal use case
            else if(Factory.isDelim(c))
            {
                //generate a token for the delimeter
                temp = Factory.delimToken(c, this.lineNo, this.colNo);
                //update pos so its now at the index of the char after the delimeter
                this.pos = i + 1;
                //break so we can immedietaly return temp
                break;
            }
            //Use case where an underscore is considered to be a valid start to an identifier
            else if (c == '_')
            {
                //update pos to be the index of the _
                this.pos = i;
                lex.append(c);
                if(!Factory.isLetter(this.lookUp(i+1)))
                {
                    if (!Factory.isDigit(this.lookUp(i + 1)))
                    {
                        //if the char after the underscore is not a letter or a digit then return a TUNDF token
                        temp = new Token(62, lex.toString(), this.lineNo, this.colNo);
                        //update pos to be the index of the char at i+1
                        this.pos = i + 1;
                        break;
                    }
                }
                identifier = true;
            }
            //String literal use case
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
                //not appending the quotation mark to lex because for string literal tokens we do not want " included in the lexeme
                qmFound = true;
                //update pos to be the index of the char after the quotation marks
                this.pos = i+1;
            }
            if(c == '!')
            {
                //add the !
                lex.append(c);
                if(this.lookUp(i+1) == '=')
                {
                    //if the char after the ! is a = then that is a valid composite error so generate a token for it
                    lex.append(this.lookUp(i + 1));
                    temp = Factory.compositeOpToken(lex.toString(), this.lineNo, this.colNo);
                    //update pos to be the index of the char after the = in !=
                    this.pos = i + 2;
                    break;
                }
                //if the char after the ! is a valid char then just generate an error token for ! and any invalid chars before it
                if(!Factory.isInvalid(this.lookUp(i+1)) || (this.lookUp(i+1) == '!'))
                {
                    temp = new Token(62, lex.toString(), this.lineNo, this.colNo);
                    //set pos to be the index of the valid char
                    this.pos = i+1;
                    //return a TUNDF token for all the invalid chars
                    break;
                }
            }
            //Invalid Char case, attempts to group as many invalid chars as possible
            else if(Factory.isInvalid(c))
            {
                //update pos to be the index of the invalid char
                this.pos = i;
                //append the invalid char to lex
                lex.append(c);
                //if statement should allow for grouping of invalid chars such that we do not need to return a single token for each individual invalid char
                //c is an invalid char so check to see if the char after is valid
                if(!Factory.isInvalid(this.lookUp(i+1)) || (this.lookUp(i+1) == '!'))
                {
                    temp = new Token(62, lex.toString(), this.lineNo, this.colNo);
                    //set pos to be the index of the valid char
                    this.pos = i+1;
                    //return a TUNDF token for all the invalid chars
                    break;
                }
            }
            //Whitespaces use case, tokens are generally returned when a whitespace is found but that is handled in the use cases above
            else if (Factory.isWhiteSpace(c))
            {
                //skip any whitespaces we find
                this.pos = i+1;
                //we have reached a new line so update lineNo
                //maybe call findSLComment at the end of every new line
                if(c == '\n')
                {
                    this.lineNo++;
                }
                //returning tokens when we see a whitespace
            }
            //End of file use case since I added a special character to mark the end of the file
            else if(c == '\u001a')
            {
                //we have reached the end of the file
                this.eof = true;
                //complete reset on this.pos so know its the index of the start of this.input, I think this will help with nextToken()
                this.pos = 0;
                //set the token ID to be that of T_EOF
                temp = new Token(0, "", this.lineNo, this.colNo);
                //break statement so we can immediately return the token
                break;
            }
        }
        //Add the Token object thats been generated to the ArrayList object acting as storage
        this.stream.add(temp);
        //add to hash table here?
        //Return the generated Token object so we can output it
        return temp;
    }

    //Returns the next valid token from a given source file
    //Preconditions: LexicalScanner object declared and intialized and readFile() has been called
    //Postconditions: Returns the next valid token
    public Token nextToken() {return this.getToken();}

    //Print function to print a token
    //Preconditions: t != null
    //Postconditions: Outputs the Token object t, if we reach 60 characters then print the object and wrap and if t is an error Token its printed on new lines
    public void printToken(Token t)
    {
        if(t.getTokenID() == 62)
        {
            System.out.print(this.count == 0 ? t + "\n" : "\n" + t + "\n");
            this.count = 0;
        }
        else
        {
            //the length of t.toString() is a multiple of six
            this.count += t.toString().length();
            if(this.count <= 60) {System.out.print(t);}
            else
            {
                System.out.print(t + "\n");
                this.count = 0;
            }
        }
    }

    //Reads the entire file called fileName and stores all of it in a StringBuilder object with a special end of file character at the very end
    //Preconditions: fileName is a valid file in the directory and fileName != ""
    //Postconditions: The entire file is read line by line with the new line character added at the end which is then stored in a StringBuilder object
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

    //Determines if the current LexicalScanner object has reached the end of a file when it is going through input and generating Token objects
    //Postconditions: LexicalScanenr object is declared and intialized, readFile() and  getToken() have been called
    //Postconditions: Returns true if we have reach the end of file character during tokenization in getToken()
    public boolean isEoF(){return this.eof;}

    //Look ahead or look behind function for when we are trying to tokenize the file read in by the current LexicalScanner object
    //Preconditions: readFile() has been called thus this.input has been populated
    //Postconditions: Returns the char at index i
    private char lookUp(int i) {return this.input.charAt(i);}

    //Getter for the entire object that is storing the Token objects generated
    //Preconditions: LexicalScanner object has been declared and intialized, readFile() and getToken() have been called such that stream.size() >= 1
    //Preconditions: Returns an ArrayList of Token objects which is acting as storage for all the Token objects generated from a file that has been read
    public ArrayList<Token> getStream() {return this.stream;}

    //Getter for the input of the current LexicalScanner object so we can return the file we have read in as a StringBuilder object
    //Preconditions: LexicalScanner object has been declared and initalized readFile() has been called thus input has been populated
    //Postconditions: Returns a StringBuilder object which contains the file read in by readFile()
    public StringBuilder getInput() {return this.input;}
}