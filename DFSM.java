/*
    Project 1b
    Author: Shaan Arora, C3236359
    DFA Class
        Contains all functionality to create a deterministic finite state machine
        that will take a string and either reject it or accept it & output its associated token
 */
import java.lang.String;
public class DFSM
{
    //Private Member Variables
    //private member variable for a counter that will keep track which state the DFSM is in
    private int stateCounter;
    //the input string as a StringBuffer that will be fed into the DFSM.
    //StringBuffer allows us to add and delete chars as we need to 
    //private StringBuffer input;
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
        //this.input = new StringBuffer();
    }

    /*Setter for input
    //Preconditions: none
    //Postconditions: i is added to the end of the StringBuffer
    public void setInput(String i)
    {
        this.input.append(i);
    }*/
    //TODO build dfsm submachines for each use case
    //startMachine will proccess String line and will call specific processing functions depending on the value of line
    //  for example if line is an indentifier then it will call the indentifierMachine function to handle and process an indentifier token
    //Scanner class will handle all whitespaces before
    //TODO write a function that handles whitespaces inbetween tokens such as equals space equals will return two equals tokens
    //Preconditions: line is not ""  and line has had all white space removed and lineNo is >= 1
    //Postconditions: line and lineNo will be sent to smaller DFSMs to be processed and turned into tokens
    public void startMachine(String line, int lineNo, int colNo)
    {
        //ensure that line is not empty
        if(!line.isEmpty())
        {
            //determine if line is a keyword
            for (Keywords k : Keywords.values())
            {
                if(line.equals(k.getKeyWord()))
                {
                    //if line does match with a keyword then send line to a DFSM that handles keywords
                    //TODO decide where to check if line is upper or lower case in regards to CD20 keyword
                    keywordMachine(line, lineNo, colNo);
                }
                //if line does not match a keyword then send line to be processed as an error
                else
                {
                    errorMachine(line, lineNo, colNo);
                }
            }
            //determine if line is an indentifier
            if(isLetter(line.charAt(0)))
            {
                //indentifier is a smaller DFSM that determines if the line is an indentifier
                //also error checks to see if that indentifier is a keyword
                indentifier(line, lineNo);
            }
            //determine if line is an integer literal, integer() will include logic to check if it is a float
            if(isDigit(line.charAt(0)))
            {
                //if the char at index 0 is a digit then we send the line to a smaller DFSM to determine what kind of digit it is
                //  integer() contains logic to determine if it is a float literal
                integer(line, lineNo, colNo);
            }
            //switch for determining delimeters and operators
            switch(line.charAt(0))
            {
                case '^': operator(line, lineNo); break;
                case '%': operator(line, lineNo); break;
                case '[': delimeters(line, lineNo); break;
                case ']': delimeters(line, lineNo); break;
                case ',': delimeters(line, lineNo); break;
                case '(': delimeters(line, lineNo); break;
                case ')': delimeters(line, lineNo); break;
                case ':': delimeters(line, lineNo); break;
                case ';': delimeters(line, lineNo); break;
                case '.': delimeters(line, lineNo); break;
                case '"': stringMachine(line, lineNo); break; //quotation marks signify the start of a string literal
                case '=': if(line.charAt(1) == '='){compositeOperator(line, lineNo);} else {operator(line, lineNo);} break;
                case '+': if(line.charAt(1) == '='){compositeOperator(line, lineNo);} else {operator(line, lineNo);} break;
                case '-': if(line.charAt(1) == '='){compositeOperator(line, lineNo);} else {operator(line, lineNo);} break;
                case '*': if(line.charAt(1) == '='){compositeOperator(line, lineNo);} else {operator(line, lineNo);} break;
                case '/': if(line.charAt(1) == '='){compositeOperator(line, lineNo);} else {operator(line, lineNo);} break;
                case '<': if(line.charAt(1) == '='){compositeOperator(line, lineNo);} else {operator(line, lineNo);} break;
                case '>': if(line.charAt(1) == '='){compositeOperator(line, lineNo);} else {operator(line, lineNo);} break;
                case '!': if(line.charAt(1) == '='){compositeOperator(line, lineNo);} else {operator(line, lineNo);} break;
                default: errorMachine(line, lineNo); break;
            }
            //check to determine if line is a comment and if so which type
            if(line.charAt(0) == '/' && line.charAt(1) == '-' && line.charAt(2) == '-')
            {
                //if two - follow after the / then send the line and its lineNo to a smaller DFSM that processes single line comments
                singleComment(line, lineNo);
            }
            else if (line.charAt(0) == '/' && line.charAt(1) == '*' && line.charAt(2) == '*')
            {
                //if two * follow after the / then send the line and its lineNo to a smaller DFSM that processes multi line comments
                multiComment(line, lineNo);
            }
        }
        //if all of the above fail then send the line and its line number to the error processing machine
        errorMachine(line, lineNo);
    }













    //private helper functions to determine if a char is a letter or a digit
    private boolean isLetter(char c)
    {
        return Character.isLetter(c);
    }
    private boolean isDigit(char c)
    {
        return Character.isDigit(c);
    }
}