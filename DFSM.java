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

    /*
      TODO write a function that handles whitespaces inbetween tokens such as equals space equals will return two equals tokens
      TODO consider refactoring to make baseMachine() a single exit-point function, reducing the number of return statements
      baseMachine will proccess the String line and will call a specific function or functions based on the value of line
        for example if line is an indentifier then it will call the indentifierMachine function to handle and process an indentifier token
      All spaces/tabs at the start of the line are including so we can properly count the column number of the token
        that is any indentation at the start of the line is carried through
      Preconditions: line is not "" (an empty string) lineNo is >= 1
      Postconditions: line and lineNo will be sent to a specific function or functions based on what the value of line is
                      A Token object will be declared and initialized by these functions and returned
                      The Token object returned will contain a tokenID, lexeme, line and column number
                        which is determined by the function/s it is sent to
     */
    public Token baseMachine(String line, int lineNo, int colNo)
    {
        //ensure that line is not empty
        if(!line.isEmpty())
        {
            //determine if line is a keyword
            for (Keywords k : Keywords.values())
            {
                //consider using compareTo() instead of equals()
                if(line.equals(k.getKeyWord()))
                {
                    //if line does match with a keyword then send line to a DFSM that handles keywords
                    //TODO decide where to check if line is upper or lower case in regards to CD20 keyword
                    return keywordMachine(line, lineNo);
                }
                //if line does not match a keyword then send line to be processed as an error
                else
                {
                    return errorMachine(line, lineNo);
                }
            }
            //determine if line is an indentifier
            if(isLetter(line.charAt(0)))
            {
                //indentifier is a smaller DFSM that determines if the line is an indentifier
                //also error checks to see if that indentifier is a keyword
                return indentifierMachine(line, lineNo);
            }
            //determine if line is an integer literal, integer() will include logic to check if it is a float
            if(isDigit(line.charAt(0)))
            {
                //if the char at index 0 is a digit then we send the line to a smaller DFSM to determine what kind of digit it is
                //  integer() contains logic to determine if it is a float literal
                return integerMachine(line, lineNo);
            }
            //switch for determining delimeters, operators and single/multiline comments in the case of a /
            switch(line.charAt(0))
            {
                //cases for delimeters
                case '[': case ']': case ',': case '(': case ')': case ':': case ';': case '.':
                    return delimMachine(line, lineNo);
                //if any of the operators below have an equals sign after them then send the line and its line number to
                //  the compositeOperator machine
                case '=': case '+': case '-': case '<': case '>': case '!':
                    if(line.charAt(1) == '='){return compositeOpMachine(line, lineNo);} else {return operatorMachine(line, lineNo);}
                //remaining operators
                case '^': case '%': return operatorMachine(line, lineNo);
                //case for String literals
                case '"': return stringMachine(line, lineNo);
                //special case for /, check to see if its being used for a single or multi line comment or as an operator
                case '/':
                    if(line.charAt(1) == '-' && line.charAt(2) == '-')
                    {
                        return singleCommentMachine(line, lineNo);
                    }
                    else if(line.charAt(1) == '*' && line.charAt(2) == '*')
                    {
                        return multiCommentMachine(line, lineNo);
                    }
                    else
                    {
                        return operatorMachine(line, lineNo);
                    }
                //special case for *, check to see if it is a composite operator, end of a multiline comment or a regular operator
                case '*':
                    if(line.charAt(1) == '=')
                    {
                        return compositeOpMachine(line, lineNo);
                    }
                    else if(line.charAt(1) == '*' && line.charAt(2) == '/')
                    {
                        return multiCommentMachine(line, lineNo);
                    }
                    else
                    {
                        return operatorMachine(line, lineNo);
                    }
                //if all else fails send the line to be produced as an error
                default: return errorMachine(line, lineNo);
            }
        }
        //if all of the above fail then send the line and its line number to the error processing machine
        return errorMachine(line, lineNo);
    }

    //Smaller DFSM for processing and handling keywords
    //Preconditions:
    //Postconditions:
    private Token keywordMachine(String line, int lineNo)
    {
        Token token = null;
        return token;
    }

    //Smaller DFSM for processing and handling identifiers
    //Preconditions:
    //Postconditions:
    private Token indentifierMachine(String line, int lineNo)
    {
        return null;
    }

    //Smaller DFSM for processing and handling integer literals and determining if line is actually a float/real literal
    //Preconditions:
    //Postconditiosn:
    private Token integerMachine(String line, int lineNo)
    {

        return null;
    }

    //Smaller DFSM for processing and handling float/real literals, will only be called in integerMachine()
    //Preconditions:
    //Postconditions:
    private void floatMachine(String line, int lineNo)
    {
    }

    //Smaller DFSM for processing and handling string literals
    //Preconditions:
    //Postconditions:
    private Token stringMachine(String line, int lineNo)
    {
        return null;
    }

    //Smaller DFSM for processing and handling delimeters
    //Preconditions:
    //Postconditions:
    private Token delimMachine(String line, int lineNo)
    {
        return null;
    }

    //Smaller DFSM for processing and handling operators
    //Preconditions:
    //Postconditions:
    private Token operatorMachine(String line, int lineNo)
    {
        return null;
    }

    //Smaller DFSM for processing and handling composite operators such as >=
    //Preconditions: line is not "" (an empty string) and lineNo >= 1
    //Postconditions: a Token object is returned containing a token ID for a composite operator and its associated line & column no
    private Token compositeOpMachine(String line, int lineNo)
    {
        return null;
    }

    //Smaller DFSM for processing and handling single line comments
    //Preconditions:
    //Postconditions:
    private Token singleCommentMachine(String line, int lineNo)
    {

        return null;
    }

    //Smaller DFSM for processing and handling multiline comments
    //Preconditions:
    //Postconditiosn:
    private Token multiCommentMachine(String line, int lineNo)
    {
        return null;
    }

    //Smaller DFSM for handling lexical errors and creating the apropriate TUNDF token
    //Preconditions:
    //Postconditions:
    private Token errorMachine(String line, int lineNo)
    {
        return null;
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