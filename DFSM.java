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
    //TODO logic to generate a token when the buffer is not empty e.g. _123 will return TUNDF _ then how do I generate a token for 123 on the next function call
    public Token baseMachine(String input, int lineNo, int colNo)
    {
        //add the String input to our buffer
        this.buffer.append(input);
        //track if we have seen a quotation mark or not
        //boolean qmFound = false;
        char c = this.buffer.charAt(0);
        if(isLetter(c))
        {
            return this.keywordMachine(lineNo, colNo);
        }
        else if(isDigit(c))
        {
            return this.integerMachine(lineNo, colNo);
        }
        else if(isOperator(c))
        {
            //if the char at index 0 is an operator, check to see the char at the next index
            if(isOperator(this.buffer.charAt(1))){return this.compositeOpMachine(lineNo, colNo);}
            else{return this.operatorMachine(lineNo, colNo);}
        }
        else if(isDelim(c))
        {
            if(c == '"' )
            {
                //TODO check to see if have an additional quotation mark
                this.buffer.deleteCharAt(0);
                return this.stringMachine(lineNo,colNo);
            }
            return this.delimMachine(lineNo, colNo);
        }
        else if(c == '_')
        {
            return this.indentifierMachine(this.buffer.toString(), lineNo, colNo);
        }
        else if(c == '!' && this.buffer.charAt(1) == '=')
        {
            return this.compositeOpMachine(lineNo, colNo);
        }
        else if(isInvalid(c))
        {
            return this.errorMachine(lineNo, colNo);
        }
        //if all else fails return null?
        return null;
    }

    //Checks if the buffer is empty
    //Postconditions: none
    //Preconditions: returns true if the length of the buffer for the current DFSM object is 0, otherwise return false
    public boolean isBufferEmpty()
    {
        if(this.buffer.length() == 0){return true;}
        return false;
    }

    //Smaller DFSM for processing and handling keywords
    //Preconditions: line.length() != 0 and lineNo >= 1
    //Postconditions: return a Token object for a keyword
    //TODO rework logic
    public Token keywordMachine(int lineNo, int colNo)
    {
        Token t = new Token();
        //t.setLexeme(input);
        t.setLineNo(lineNo);
        t.setColNo(colNo);
        //boolean to see if we have matched a keyword
        boolean kMatched = false;
        String input = "";
        for(Keywords k : Keywords.values())
        {
            //should handle/enforce CD is uppercase
            if(k == Keywords.K0 && input.equals(k.getKeyWord()))
            {
                t.setTokenID(1);
                kMatched = true;
            }
            //if the keyword is not CD20 then handle it as any of the other keywords
            else if(input.equalsIgnoreCase(k.getKeyWord()))
            {
                //ID for the token is calculated by the index of the keyword matched in the Keywords enum + 1
                int index = k.ordinal() + 1;
                t.setTokenID(index);
                //we have matched input to a keyword
                kMatched = true;
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
        t.setLineNo(0);
        t.setColNo(0);
        StringBuilder lex = new StringBuilder();
        //we know that the char at 0 is an _ so check to see if the char at index 1 is a letter
        if(!isLetter(this.buffer.charAt(1)))
        {
            //Underscore not followed by a letter is a lexical error
            t.setTokenID(62);
            t.setLexeme("_");
            //since we have determined that the underscore is not followed by a letter we can delete the underscore from the buffer
            this.buffer.deleteCharAt(0);
        }
        else
        {
            while(!isWhiteSpace(this.buffer.charAt(0)))
            {
                if(isInvalid(this.buffer.charAt(0))){break;}
                lex.append(this.buffer.charAt(0));
            }
            //when we reach a whitespace then we can set the tokenID and lexeme and return that token
            t.setTokenID(58);
            t.setLexeme(lex.toString());
        }
        return t;
    }

    //Smaller DFSM for processing and handling integer literals and determining if line is actually a float/real literal
    //Preconditions: this.buffer.length() != 0
    //Postconditions: returns a integer literal Token object where any digits in buffer become its lexeme, lineNo for its line number and colNo for its column number
    public Token integerMachine(int lineNo, int colNo)
    {
        Token t = new Token();
        t.setLineNo(0);
        t.setColNo(0);
        StringBuilder lex = new StringBuilder();
        boolean dot = false;
        //we already know that the we have a digit in the buffer at index 0
        while(isDigit(this.buffer.charAt(0)))
        {
            lex.append(this.buffer.charAt(0));
            this.buffer.deleteCharAt(0);
        }
        if(this.buffer.charAt(0) == '.')
        {
        }
        return t;
    }

    //Smaller DFSM for processing and handling string literals
    //Preconditions: this.buffer.length != 0
    //Postconditions: returns a string literal token object
    public Token stringMachine(int lineNo, int colNo)
    {
        Token t = new Token();
        t.setLineNo(lineNo);
        t.setColNo(colNo);
        StringBuilder lex = new StringBuilder();
        //we alread know that we have already seen a quotation mark and consumed it (deleted it from buffer)
        //iterate over our buffer until we see another quotation mark
        while(this.buffer.charAt(0) != '"')
        {
            if(isInvalid(this.buffer.charAt(0)))
            {
                break;
            }
            lex.append(this.buffer.charAt(0));
            this.buffer.deleteCharAt(0);
        }
        //once we reach another quotation mark then the string literal is done
        this.buffer.deleteCharAt(0);
        t.setTokenID(61);
        t.setLexeme(lex.toString());
        return t;
    }

    //Smaller DFSM for processing and handling delimeters
    //Preconditions:
    //Postconditions:
    public Token delimMachine(int lineNo, int colNo)
    {
        Token t = new Token();
        t.setLexeme(String.valueOf(this.buffer.charAt(0)));
        t.setLineNo(lineNo);
        t.setColNo(colNo);
        switch(this.buffer.charAt(0))
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
        this.buffer.deleteCharAt(0);
        return t;
    }

    //Smaller DFSM for processing and handling operators
    //Preconditions: none
    //Postconditions: returns a Token object containing an ID based on what c is, lineNo as its line number and colNo as its column number
    public Token operatorMachine(int lineNo, int colNo)
    {
        Token t = new Token();
        //convert c into a String since thats how we are capturing/storing lexemes for tokens
        t.setLexeme(String.valueOf(this.buffer.charAt(0)));
        t.setLineNo(lineNo);
        t.setColNo(colNo);
        //based on what c is, set the ID of the Token object to its corresponding value
        switch(this.buffer.charAt(0))
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
        //delete the char in our buffer that we just created a token for
        this.buffer.deleteCharAt(0);
        //return the token object we just created
        return t;
    }

    //Smaller DFSM for processing and handling composite operators such as >=
    //Preconditions: line is not "" (an empty string) and lineNo >= 1
    //Postconditions: a Token object is returned containing a token ID for a composite operator and its associated line & column no
    public Token compositeOpMachine(int lineNo, int colNo)
    {
        //we already know that the char in the buffer at index 0 is an operator and at index 1 is a =
        Token t = new Token();
        t.setLineNo(lineNo);
        t.setColNo(colNo);
        StringBuilder lex = new StringBuilder();
        //depending on what the first character of input is, set the ID of the Token object to its corresponding value and add that operator to lex
        switch(this.buffer.charAt(0))
        {
            case '<': t.setTokenID(47); lex.append(this.buffer.charAt(0)); break;
            case '>': t.setTokenID(48); lex.append(this.buffer.charAt(0)); break;
            //the only case where ! is accepted ever
            case '!': t.setTokenID(49); lex.append(this.buffer.charAt(0)); break;
            case '=': t.setTokenID(50); lex.append(this.buffer.charAt(0)); break;
            case '+': t.setTokenID(51); lex.append(this.buffer.charAt(0)); break;
            case '-': t.setTokenID(52); lex.append(this.buffer.charAt(0)); break;
            case '*': t.setTokenID(53); lex.append(this.buffer.charAt(0)); break;
            case '/': t.setTokenID(54); lex.append(this.buffer.charAt(0)); break;
        }
        //append the = which is part of the composite operator
        lex.append(this.buffer.charAt(1));
        //lex now contains an operator and an equal sign so we set that as the lexeme for the token
        t.setLexeme(lex.toString());
        //delete both chars at index 0 and 1 because they both form a composite operator
        this.buffer.deleteCharAt(0); this.buffer.deleteCharAt(1);
        return t;
    }

    //Smaller DFSM for handling lexical errors and creating the apropriate TUNDF token
    //Preconditions: s != " "
    //Postconditions: returns a TUNDF token, meaning that s is a lexical error in regards to our scanner
    public Token errorMachine(int lineNo, int colNo) {return new Token(62, "",lineNo,colNo);}

    //Helper functions to determine what kind of char c is
    //Preconditions: none
    //Postconditions: returns true if c is a letter otherwise false
    private boolean isLetter(char c) {return Character.isLetter(c);}
    //Preconditions: none
    //Postconditons: returns true if c is a digit otherwise false
    private boolean isDigit(char c) {return Character.isDigit(c);}

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