/*
    Project 1b
    Author: Shaan Arora, C3236359
    DFA Class
        Contains all functionality to create a deterministic finite state machine
        that will take a string and either reject it or accept it & output its associated token
 */
import java.lang.String;
import java.util.ArrayList;

public class DFSM
{
    //Private Member Variables
    private StringBuilder buffer;
    //Enum for the reserved keywords so that we can assert that an identifier name is not the same as a keyword ignore case
    //CD20 keyword is the only exception that has to be uppercase
    //using K# as a naming convention for each enum where # is just a number so that I can store the keywords as strings
    //TODO consider putting them in LexicalScanner.java or in a seperate file
    private enum Keywords
    {
        K0("CD20"),    K1("constants"), K2("types"), K3("is"),     K4("arrays"), K5("main"),
        K6("begin"),   K7("end"),       K8("array"), K9("of"),     K10("func"),  K11("void"),
        K12("const"),  K13("int"),      K14("real"), K15("bool"),  K16("for"),   K17("repeat"),
        K18("until"),  K19("if"),       K20("else"), K21("input"), K22("print"), K23("println"),
        K24("return"), K25("not"),      K26("and"),  K27("or"),    K28("xor"),   K29("true"),
        K30("false");
        //private String member variable for the enum to hold the actual keyword
        private final String keyWord;
        //constructor for the enum
        Keywords(String word)
        {
            this.keyWord = word;
        }
        //Getter for the member variable
        //Preconditions: none
        //Postconditions: return the keyWord of the current enum
        public String getKeyWord()
        {
            return this.keyWord;
        }
    }

    //Default constructor
    //Preconditions: none
    //Postconditions: private member variables are initialized to default values
    public DFSM()
    {
        this.buffer = new StringBuilder();
    }

    //Base processing machine for tokens, determines which subsystem DFSM input will be sent to so it can generate the appropriate token
    //Preconditions: input != " "
    //Postconditions: returns a Token object based on what input is
    public Token baseMachine(StringBuilder input, int lineNo, int colNo)
    {
        if(input.length() == 0){return null;}
        //iterate through the buffer
        for(int i = 0; i < input.length(); i++)
        {
            //Capture the char at index i
            char c = input.charAt(i);
            if(isAlphaNumeric(c))
            {
                //add the char to our buffer
                this.buffer.append(c);
                //delete the char
                input.deleteCharAt(i);
                this.baseMachine(input, lineNo, colNo);
            }
        }
        return null;
    }
    //Smaller DFSM for processing and handling keywords
    //Preconditions: line.length() != 0 and lineNo >= 1
    //Postconditions: return a Token object for a keyword
    //TODO eliminate whitespace e.g. CD20spaceProgramName or CD20tabProgramName
    public Token keywordMachine(String input, int lineNo, int colNo)
    {
        //Checks for CD20 and enforces uppercase on CD
        Token t = new Token();
        t.setLexeme(input);
        t.setLineNo(lineNo);
        t.setColNo(colNo);
        //boolean to see if we have matched a keyword
        boolean kMatched = false;
        for(Keywords k : Keywords.values())
        {
            if(k == Keywords.K0 && input.equals(k.getKeyWord()))
            {
                //t = new Token(1, Keywords.K0.getKeyWord(), lineNo, 0);
                t.setTokenID(1);
                kMatched = true;
                //return t;
            }
            //if the keyword is not CD20 then handle it as any of the other keywords
            //change input from a StringBuilder to a String so we can ignore is case
            else if(input.equalsIgnoreCase(k.getKeyWord()))
            {
                int index = k.ordinal() + 1;
                //t = new Token(index, k.getKeyWord(), lineNo, 0);
                t.setTokenID(index);
                kMatched = true;
                //break;
                //System.out.println("Other keyword recognised");
                //return t;
            }
        }
        //if we havent matched a keyword then make the ID for t to be the ID for an identifier token
        if(kMatched == false){t.setTokenID(58);}
        return t;
    }

    //Smaller DFSM for processing and handling identifiers
    //Preconditions: line.length() != 0 and lineNo >= 1
    //Postconditions: return a Token object for an identifier token, if line matches a keyword then a Token object for that keyword is returned
    public Token indentifierMachine(String line, int lineNo, int colNo)
    {
        Token t = new Token();
        for(Keywords k : Keywords.values())
        {
            if(line.toString().equalsIgnoreCase(k.getKeyWord()))
            {
                return t = keywordMachine(line, lineNo, colNo);
            }
        }
        t.setTokenID(58);
        t.setLexeme(line.toString());
        t.setLineNo(lineNo);
        t.setColNo(colNo);
        return t;
    }

    //Smaller DFSM for processing and handling integer literals and determining if line is actually a float/real literal
    //Preconditions: line != " "
    //Postconditions: returns a Token object containing the ID for a TILIT token, the value of line as its lexeme, lineNo for its line number, colNo for its column number
    //TODO big rework
    /*public Token integerMachine(StringBuilder line, int lineNo, int colNo)
    {
        //StringBuilder buff = new StringBuilder(line);
        boolean error = false;
        String invalid = "";
        ArrayList<Token> t = new ArrayList<>();
        Token temp = new Token();
        StringBuilder b;
        //if the length is only 1 and that char is a digit
        if(line.length() == 1 && isDigit(line.charAt(0)))
        {
            //create token
            temp.setTokenID(59);
            temp.setLexeme(line.toString());
            temp.setLineNo(lineNo);
            temp.setColNo(colNo);
        }
        else
        {
            //return a TUNDF
        }
        for(int i = 0; i < line.length(); i++)
        {
            char c = line.charAt(i);
            if(!isDigit(c))
            {
                //offending char
                invalid += c;
                error = true;
            }
            //check to see if its a float/real
            if(c == '.')
            {
                if(!isDigit(line.charAt(i+1)))
                {
                    //if the char after a . is not a digit
                    invalid += c;
                    error = true;
                }
                //create a float/real token
            }
            //create a integer token
            b = new StringBuilder();
            b.append(c);
            temp.setTokenID(59);
            temp.setLexeme(b.toString());
            temp.setLineNo(lineNo);
            temp.setColNo(colNo);
        }
        System.out.println(invalid);
        b = new StringBuilder(invalid);
        Token invalidToken = indentifierMachine(b, lineNo, colNo);
        t.add(temp);
        t.add(invalidToken);
        return temp;
    }*/

    //Smaller DFSM for processing and handling float/real literals, will only be called in integerMachine()
    //Preconditions:
    //Postconditions:
    public Token floatMachine(StringBuilder line, int lineNo)
    {
        return null;
    }

    //Smaller DFSM for processing and handling string literals
    //Preconditions:
    //Postconditions:
    //TODO rework
    public Token stringMachine(StringBuilder line, int lineNo)
    {
        int end = line.length();
        Token t;
        //we already know that the is a " at the start because the condition for this function to be called
        /*if(line.charAt(end) != '"' || line.charAt(end) == '\n')
        {
            //if the char at the end is not a " or is the newline character
            //generate an error token
            return t = new Token(Tokens.TUNDF, line.toString(), lineNo, 0);
        }*/
        StringBuilder buff = new StringBuilder();
        //start at 1 because we know that the first char is a "
        for (int i = 1; i < end; i++)
        {
            char c = line.charAt(i);
            //if c is not the newline character
            while (c != '\n')
            {
                //check if there is a quotation mark in the middle of the string
                if(c == '"' && i != end)
                {
                    //generate a string literal token with what we have consumed already
                    t = new Token(Tokens.TSTRG, buff.toString(), lineNo, 0);
                    return t;
                }
                //if there is not newline character or " keep adding to the buffer
                buff.append(c);
            }
            //if char c is a newline character generate an error token

        }
        return null;
    }

    //Smaller DFSM for processing and handling delimeters
    //Preconditions:
    //Postconditions:
    public Token delimMachine(char c, int lineNo, int colNo)
    {
        Token t = new Token();
        t.setLexeme(String.valueOf(c));
        t.setLineNo(lineNo);
        t.setColNo(colNo);
        switch(c)
        {
            case ',': t.setTokenID(32); break;
            case '[': t.setTokenID(33); break;
            case ']': t.setTokenID(34); break;
            case '(': t.setTokenID(35); break;
            case ')': t.setTokenID(36); break;
            case ':': t.setTokenID(46); break;
            case ';': t.setTokenID(56); break;
            case '.': t.setTokenID(57); break;
        }
        return t;
    }

    //Smaller DFSM for processing and handling operators
    //Preconditions: none
    //Postconditions: returns a Token object containing an ID based on what c is, lineNo as its line number and colNo as its column number
    public Token operatorMachine(char c, int lineNo, int colNo)
    {
        Token t = new Token();
        //convert c into a String since thats how we are capturing/storing lexemes for tokens
        t.setLexeme(String.valueOf(c));
        t.setLineNo(lineNo);
        t.setColNo(colNo);
        //based on what c is, set the ID of the Token object to its corresponding value
        switch(c)
        {
            case '=': t.setTokenID(37); break;
            case '+': t.setTokenID(38); break;
            case '-': t.setTokenID(39); break;
            case '*': t.setTokenID(40); break;
            case '/': t.setTokenID(41); break;
            case '%': t.setTokenID(42); break;
            case '^': t.setTokenID(43); break;
            case '<': t.setTokenID(44); break;
            case '>': t.setTokenID(45); break;
        }
        //return the token object we just created
        return t;
    }

    //Smaller DFSM for processing and handling composite operators such as >=
    //Preconditions: line is not "" (an empty string) and lineNo >= 1
    //Postconditions: a Token object is returned containing a token ID for a composite operator and its associated line & column no
    public Token compositeOpMachine(String input, int lineNo, int colNo)
    {
        Token t = new Token();
        //if the second char in input is not an =, lexical error
        if(input.charAt(1) != '=')
        {
            return t = this.errorMachine(input, lineNo, colNo);
        }
        t.setLexeme(input);
        t.setLineNo(lineNo);
        t.setColNo(colNo);
        //depending on what the first character of input is, set the ID of the Token object to its corresponding value
        switch(input.charAt(0))
        {
            case '<': t.setTokenID(47); break;
            case '>': t.setTokenID(48); break;
            case '!': t.setTokenID(49); break;
            case '=': t.setTokenID(50); break;
            case '+': t.setTokenID(51); break;
            case '-': t.setTokenID(52); break;
            case '*': t.setTokenID(53); break;
            case '/': t.setTokenID(54); break;
        }
        return t;
    }

    //Smaller DFSM for handling lexical errors and creating the apropriate TUNDF token
    //Preconditions: s != " "
    //Postconditions: returns a TUNDF token, meaning that s is a lexical error in regards to our scanner
    public Token errorMachine(String s, int lineNo, int colNo) {return new Token(62, s ,lineNo,colNo);}

    //Helper functions to determine what kind of char c is
    //Preconditions: none
    //Postconditions: returns true if c is a letter otherwise false
    private boolean isLetter(char c) {return Character.isLetter(c);}
    //Preconditions: none
    //Postconditons: returns true if c is a digit otherwise false
    private boolean isDigit(char c) {return Character.isDigit(c);}
    //Preconditions: none
    //Postconditions: returns true if c is a digit, letter or _ other wise false
    private boolean isAlphaNumeric(char c) {return this.isDigit(c) || this.isLetter(c) || c == '_';}
    //Preconditions: none
    //Postconditions: returns true if c is an operator otherwise false
    private boolean isOperator(char c) {return c == '+' || c == '-' || c == '/' || c == '*' || c == '=' || c == '<' || c == '>' || c == '^' || c == '%';}
    //Preconditions: none
    //Postconditions: return true if c is a delimeter otherwise false
    private boolean isDelim(char c) {return c == '(' || c == ')' || c == '[' || c == ']' || c == '.' || c == ';' || c == ':' || c == '"';}
    //Preconditions: none
    //Postconditons: returns true if c is a whitespace character otherwise false
    private boolean isWhiteSpace(char c) {return Character.isWhitespace(c);}
    //Preconditions:
    //Postconditions: returns true if c is an invalid character otherwise false
    private boolean isInvalid(char c) {return c == '@' || c == '?' || c == '#';}
}