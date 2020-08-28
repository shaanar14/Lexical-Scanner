/*
    Project 1b
    Author: Shaan Arora, C3236359
    Scanner Class
        Main component of the lexical analyser that will use a DFA object to process each line of a file
        Each line will be processed as a string and fed into the DFA object
        The DFA object will return token/s which will be stored in a LinkedList from the standard Java library
 */
import java.io.*;
import java.lang.String;
import java.util.*;
public class LexicalScanner
{
    //array list to store all the tokens we find when we run the lexical scanner
    private ArrayList<Token> stream;
    //private DFA object that will consume a string to see if it is lexically valid, if it is then return a token
    private DFSM machine;
    private StringBuilder inputStream, string, error;
    private int lineNo;
    private boolean eof;
    public LexicalScanner()
    {
        this.stream = new ArrayList<>();
        this.machine = new DFSM();
        this.inputStream = new StringBuilder();
        this.string = new StringBuilder();
        this.error = new StringBuilder();
        this.lineNo = 0;
        this.eof = false;
    }

    //gets the next valid token from inputStream
    //Preconditions: inputStream.length() !=
    //Postconditions: chars from inputStream are consumed and deleted from it and collected to form the next valid token
    //                  the next valid token is returned as Token object containing the appropriate tokenID
    public Token getToken()
    {
        //base case condition for recursion
        if(this.inputStream.length() == 0){return null;}
        //Token object so the function is a single exit-point function
        //can't create object here, null pointer exception
        Token t = new Token();
        //TODO try and reduce the amount of booleans
        boolean letter = false, digit = false, delim = false, op = false, ws = false, dot = false, invalid = false, eq = false;
        for(int i = 0; i < this.inputStream.length(); i++)
        {
            //Capture the char at i in inputStream
            char c = this.inputStream.charAt(i);
            //Consume/delete the char at i
            this.inputStream.deleteCharAt(i);
            if(isLetter(c))
            {
                //we have consumed a letter
                letter = true;
                //if char c is a letter add it to our string
                this.string.append(c);
                //we have found a valid char
                invalid = false;
                //recursive call to keep consuming chars
                this.getToken();
            }
            //TODO fix logic and handle float logic
            else if(isDigit(c) || c == '.')
            {
                //if char c is a dot
                if( c == '.')
                {
                    //if we have already found a dot and we havent consumed a digit or a letter
                    if(dot && (!digit || !letter))
                    {
                        //add the other dot to the string
                        this.string.append(c);
                        //might need to dot = false;
                        //generate a token for the additional dot found.
                        //since . is a delim set delim to true?
                        delim = true;
                        t = this.machine.delimMachine(c, lineNo, 0);
                    }
                    //if we havent found a dot and we have been consuming digits
                    else
                    {
                        //dot found
                        dot = true;
                        delim = true;
                        //if c is a dot then add it
                        this.string.append(c);
                    }
                    //recursive call to keep consuming
                    this.getToken();
                }
                //if we have consumed at least one letter and not seen a dot then add the digit as normal
                if(letter && !dot)
                {
                    //add the digit
                    this.string.append(c);
                    //recursive call
                    return this.getToken();
                }
                //consumed char is a digit so set digit to be true
                digit = true;
                //we have found a valid char
                invalid = false;
                //if letter is not true and we have seen a dot then we just consume the digit as normal?
                this.string.append(c);
                this.getToken();
            }
            else if(isOperator(c))
            {
                //if the current char is = and we haven't consumed a letter or digit
                if((c == '=') && (!letter || !digit))
                {
                    //equal sign found
                    eq = true;
                    //add = to string
                    this.string.append(c);
                    //generate a token for the char found/consume which is =
                    t = this.machine.operatorMachine(c, lineNo, 0);
                }
                //if we have found a an = operator and we have consumed an operator then it is a composite operator token
                else if(eq && op)
                {
                    //composite operator token generated so set eq to false
                    eq = false;
                    //add char to string
                    this.string.append(c);
                    //generator token
                    t = this.machine.compositeOpMachine(this.string, lineNo, 0);
                }
                //operator found
                op = true;
                //add the char to string
                this.string.append(c);
                //we have found a valid char
                invalid = false;
                this.getToken();
            }
            //TODO fix entire case
            else if(isDelim(c))
            {
                //delim = true;
                //we have found a valid char
                invalid = false;
                //add the delimeter to string
                this.string.append(c);
                t = this.machine.delimMachine(c,lineNo, 0);

            }
            //generating keyword, identifier, integer and real tokens when a whitespace is found
            else if(isWhiteSpace(c))
            {
                //if we have already found a whitespace then just skip, hoping this handles the case of having \r\n
                if(ws) {break;}
                ws = true;
                if(letter)
                {
                    letter = false;
                    //if string contains both letters and digits, generate identifier token
                    if(digit)
                    {
                        digit = false;
                        t = this.machine.indentifierMachine(this.string, lineNo, 0);
                    }
                    else
                    {
                        //otherwise send consumed chars to see if they match a keyword, if it doesn't returns identifier token
                        t = this.machine.keywordMachine(this.string, lineNo, 0);
                    }
                }
                //on whitespace found return integer or float token depending on the value of dot
                else if(digit)
                {
                    //since we are generating a token and not consuming anymore, set digit to false
                    digit = false;
                    //if we have been consuming digits and there is a dot
                    if(dot)
                    {
                        dot = false;
                        t = this.machine.floatMachine(this.string, lineNo);
                    }
                    //if a dot hasn't been found/consumed
                    else
                    {
                        t = this.machine.integerMachine(this.string, lineNo, 0);
                    }
                }
                //valid char found
                invalid = false;
            }
            else if(c == '!')
            {
                //if we have found an equal sign then and a digit or letter has not been consumed
                if(eq && (!letter || !digit))
                {
                    //valid char found
                    invalid = false;
                    //we have join an equal sign with a valid operator so eq is set to false;
                    eq = false;
                    //add ! to string
                    this.string.append(c);
                    t = this.machine.compositeOpMachine(this.string, lineNo, 0);
                }
                //if we just have ! on its own then that is an invalid char, so add it to error and set invald to true
                else
                {
                    this.error.append(c);
                    invalid = true;
                }
                //recursive call
                this.getToken();
            }
            //underscores are only valid for the start of identifiers
            else if(c == '_')
            {
                //if we have consumed a letter or digit
                if(letter && digit)
                {
                    //valid char for this case found
                    invalid = false;
                    //add the char to string
                    this.string.append(c);
                    //create an identifier token
                    t = this.machine.indentifierMachine(this.string, lineNo, 0);
                }
                else
                {
                    //invalid char
                    invalid = true;
                    this.error.append(c);
                }
                //recursive call to keep consuming chars
                this.getToken();
            }
            else if(isInvalid(c))
            {
                //invalid char found
                invalid = true;
                this.error.append(c);
                this.getToken();
            }
            //generate eof token
            /*else if(c == '\u001a')
            {
                invalid = false;
                this.eof = true;
                t.setTokenID(0);
                t.setLineNo(lineNo);
            }*/
        }
        //if we have found any invalid chars then generate a lexical error token
        if(invalid){t = this.machine.errorMachine(this.error, lineNo, 0);}
        return t;
    }

    //reads the file with name fileName and populates inputStream char by char
    //Preconditions: fileName != ""
    //Postconditions: inputStream is populated char by char for every line in the file called fileName
    public void readFile(String fileName)
    {
        try
        {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader scan = new BufferedReader(fr);
            int i;
            while((i = scan.read()) != -1)
            {
                char c = (i == 9) ? '\t' : (char) i;
                this.inputStream.append(c);
            }
            //append EOF marker to the end of our inputstream
            char testChar = '\u001a';
            this.inputStream.append(testChar);
        }
        catch (IOException e)
        {
            System.out.println("File Error");
            e.printStackTrace();
        }
        System.out.println("readFile() " + this.inputStream);
    }

    //Helper functions to determine what kind of char c is
    private boolean isLetter(char c) {return Character.isLetter(c);}

    private boolean isDigit(char c) {return Character.isDigit(c);}

    private boolean isOperator(char c)
    {
        return c == '+' || c == '-' || c == '/' || c == '*' || c == '=' || c == '<' || c == '>' || c == '^' || c == '%';
    }

    private boolean isDelim(char c) {return c == '(' || c == ')' || c == '[' || c == ']' || c == '.' || c == ';' || c == ':' || c == '"';}

    private boolean isWhiteSpace(char c) {return Character.isWhitespace(c);}

    private boolean isInvalid(char c) {return c == '@' || c == '?' || c == '#';}

    private boolean isAlphaNumeric(char c) {return this.isDigit(c) || this.isLetter(c) || c == '_';}

    public boolean eof() {return this.eof;}
}