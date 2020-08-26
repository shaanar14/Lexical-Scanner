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
    //private member variable for a counter that will keep track which state the DFSM is in
    private int stateCounter;
    //the input string as a StringBuilder that will be fed into the DFSM.
    //StringBuilder allows us to add and delete chars as we need to 
    //private StringBuilder input;
    //Enum for the reserved keywords so that we can assert that an identifier name is not the same as a keyword ignore case
    //CD20 keyword is the only exception that has to be uppercase
    //using K# as a naming convention for each enum where # is just a number so that I can store the keywords as strings
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
        //Start state of the DFSM will be 0
        this.stateCounter = 0;
        //this.input = new StringBuilder();
    }

    //Smaller DFSM for processing and handling identifiers
    //Preconditions: line.length() != 0 and lineNo >= 1
    //Postconditions: return a Token object for an identifier token, if line matches a keyword then a Token object for that keyword is returned
    public Token indentifierMachine(StringBuilder line, int lineNo, int colNo)
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

    //Smaller DFSM for processing and handling keywords
    //Preconditions: line.length() != 0 and lineNo >= 1
    //Postconditions: return a Token object for a keyword
    //TODO eliminate whitespace e.g. CD20spaceProgramName or CD20tabProgramName
    public Token keywordMachine(StringBuilder input, int lineNo, int colNo)
    {
        //Checks for CD20 and enforces uppercase on CD
        Token t = new Token();
        for(Keywords k : Keywords.values())
        {
            if(k == Keywords.K0)
            {
                String s = input.toString();
                if(s.length() > 4)
                {
                    String sub = s.substring(4,s.length());
                    //System.out.println(sub);
                    //boolean test = sub.contains(" ");
                    //System.out.println(test);
                    t.setLexeme(sub);
                }
                //t = new Token(1, Keywords.K0.getKeyWord(), lineNo, 0);
                t.setTokenID(1);
                //t.setLexeme(input.toString());
                t.setLineNo(lineNo);
                t.setColNo(colNo);
                //System.out.println("CD20 keyword recognised");
            }
            //if the keyword is not CD20 then handle it as any of the other keywords
            //change input from a StringBuilder to a String so we can ignore is case
            else if(input.toString().equalsIgnoreCase(k.getKeyWord()))
            {
                int index = k.ordinal() + 1;
                //t = new Token(index, k.getKeyWord(), lineNo, 0);
                t.setTokenID(index);
                t.setLexeme(input.toString());
                t.setLineNo(lineNo);
                t.setColNo(colNo);
                //System.out.println("Other keyword recognised");
            }
        }
        /*t.setTokenID(62);
        t.setLexeme(input.toString());
        t.setLineNo(lineNo);*/
        return t;
    }

    //Smaller DFSM for processing and handling integer literals and determining if line is actually a float/real literal
    //Preconditions:
    //Postconditiosn:
    public ArrayList<Token> integerMachine(StringBuilder line, int lineNo, int colNo)
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
        return t;
    }

    //Smaller DFSM for processing and handling float/real literals, will only be called in integerMachine()
    //Preconditions:
    //Postconditions:
    public void floatMachine(StringBuilder line, int lineNo)
    {
    }

    //Smaller DFSM for processing and handling string literals
    //Preconditions:
    //Postconditions:
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
    public Token delimMachine(StringBuilder line, int lineNo)
    {
        return null;
    }

    //Smaller DFSM for processing and handling operators
    //Preconditions:
    //Postconditions:
    public Token operatorMachine(StringBuilder line, int lineNo)
    {
        return null;
    }

    //Smaller DFSM for processing and handling composite operators such as >=
    //Preconditions: line is not "" (an empty string) and lineNo >= 1
    //Postconditions: a Token object is returned containing a token ID for a composite operator and its associated line & column no
    public Token compositeOpMachine(StringBuilder line, int lineNo)
    {
        return null;
    }

    //Smaller DFSM for processing and handling single line comments
    //Preconditions:
    //Postconditions:
    public Token singleCommentMachine(StringBuilder line, int lineNo)
    {

        return null;
    }

    //Smaller DFSM for processing and handling multiline comments
    //Preconditions:
    //Postconditiosn:
    public Token multiCommentMachine(StringBuilder line, int lineNo)
    {
        return null;
    }

    //TODO might not need this
    //Smaller DFSM for handling lexical errors and creating the apropriate TUNDF token
    //Preconditions:
    //Postconditions:
    public Token errorMachine(StringBuilder s, int lineNo)
    {
        Token t = new Token(62, s.toString(),lineNo,0);
        return t;
    }

    //Private helper functions to determine if a char is a letter or a digit
    //Preconditions: none
    //Postconditions: returns true if c is a letter otherwise returns false
    private boolean isLetter(char c)
    {
        return Character.isLetter(c);
    }
    //Preconditions: none
    //Postconditions: returns true if c is a digit otherwise returns false
    private boolean isDigit(char c)
    {
        return Character.isDigit(c);
    }
}