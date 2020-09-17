/*
    Project 1b
    Author: Shaan Arora, C3236359
    Factory Class
        Contains all functionality to generate Token objects with specific ID's
 */

import java.lang.String;

public class Factory
{
    //Generates and returns an identifier or keyword token
    //Preconditions: lex.length() != 0
    //Postconditions: return a Token object for an identifier token, if lex matches a keyword then a Token object for that keyword is returned
    public static Token identifierToken(StringBuilder lex, int lineNo, int colNo)
    {
        Token t = new Token("", lineNo, colNo);
        //after consuming chars, if the the first char in lex is not an _ then check to see if lex is a keyword
        int id = keywordMatch(lex);
        //if lex does match a keyword then set the ID for the keyword matched
        if(id != -1){t.setTokenID(id);}
        //if its not a keyword or the char at index 0 in lex is a _ then set the ID for an identifier and set the lexeme
        else{t.setTokenID(58); t.setLexeme(lex.toString());}
        return t;
    }

    //Helper function for indentifierMachine() to match keywords
    //Preconditions: lex.length() != 0
    //Postconditions: checks to see if lex is equal to a keyword and returns its ID otherwise returns -1
    //TODO might have to rework such that fi is tokenised as if
    private static int keywordMatch(StringBuilder lex)
    {
        for(Keywords k : Keywords.values())
        {
            //should handle/enforce CD is uppercase
            if(k == Keywords.K0 && lex.toString().equals(k.getKeyWord()))
            {
                return 1;
            }
            //if the keyword is not CD20 then handle it as any of the other keywords
            else if(lex.toString().equalsIgnoreCase(k.getKeyWord()))
            {
                //ID for the token is calculated by the index of the keyword matched in the Keywords enum + 1
                return k.ordinal() + 1;
            }
        }
        return -1;
    }

    //Generates integer literal tokens
    //Preconditions: lex.length() != 0
    //Postconditions: returns a integer literal Token object
    public static Token integerLiteral(StringBuilder lex, int lineNo, int colNo) {return new Token(59, lex.toString(), lineNo, colNo);}

    //Generates float literal tokens
    //Preconditions: lex.length() != 0
    //Postconditions returns a float literal Token object
    public static Token floatLiteral(StringBuilder lex, int lineNo, int colNo) {return new Token(60, lex.toString(), lineNo, colNo);}

    //Generates string literal tokes
    //PreconditonsL lex.length() != 0
    //Postconditions: returns a string literal Token object
    public static Token stringLiteral(StringBuilder lex, int lineNo, int colNo) {return new Token(61, lex.toString(), lineNo, colNo);}

    //Generates a Token object based on what delimeter c is
    //Preconditions: isDelim(c) == true
    //Postconditions: returns a Token object containg the token ID for what delimeter c is and its line & column number
    public static Token delimToken(char c, int lineNo, int colNo)
    {
        switch(c)
        {
            case ',': return new Token(32, "", lineNo, colNo);
            case '[': return new Token(33, "", lineNo, colNo);
            case ']': return new Token(34, "", lineNo, colNo);
            case '(': return new Token(35, "", lineNo, colNo);
            case ')': return new Token(36, "", lineNo, colNo);
            case ':': return new Token(46, "", lineNo, colNo);
            case ';': return new Token(56, "", lineNo, colNo);
            case '.': return new Token(57, "", lineNo, colNo);
            //default case being that we return a TUNDF token
            default:  return new Token(62, String.valueOf(c), lineNo, colNo);
        }
    }

    //Generates a Token object based on what operator c is
    //Preconditions: isOperator(c) == true
    //Postconditions: returns a Token object containg the token ID for what kind of operator c is and its line & column number
    public static Token operatorToken(char c, int lineNo, int colNo)
    {
        //based on what c is, set the ID of the Token object to its corresponding value
        switch(c)
        {
            case '=': return new Token(37, "", lineNo, colNo);
            case '+': return new Token(38, "", lineNo, colNo);
            case '-': return new Token(39, "", lineNo, colNo);
            case '*': return new Token(40, "", lineNo, colNo);
            case '/': return new Token(41, "", lineNo, colNo);
            case '%': return new Token(42, "", lineNo, colNo);
            case '^': return new Token(43, "", lineNo, colNo);
            case '<': return new Token(44, "", lineNo, colNo);
            case '>': return new Token(45, "", lineNo, colNo);
            //default case being that we return a TUNDF token
            default:  return new Token(62, String.valueOf(c), lineNo, colNo);
        }
    }

    //Generates a Token object based on what kind of operator c is
    //Preconditions: lex.length() == 2 and lex.charAt(1) == '='
    //Postconditions: a Token object is returned containing a token ID for a composite operator and its line & column number
    public static Token compositeOpToken(String lex, int lineNo, int colNo)
    {
        assert lex.length() == 2 : "paramter lex has to be of length 2";
        //we already know that the char at index 1 is a = so we just check to see what the operator at index 0 is
        //depending on what the first character of input is, set the ID of the Token object to its corresponding value and add that operator to lex
        switch(lex.charAt(0))
        {
            case '<': return new Token(47, "", lineNo, colNo);
            case '>': return new Token(48, "", lineNo, colNo);
            //the only case where ! is accepted ever
            case '!': return new Token(49, "", lineNo, colNo);
            case '=': return new Token(50, "", lineNo, colNo);
            case '+': return new Token(51, "", lineNo, colNo);
            case '-': return new Token(52, "", lineNo, colNo);
            case '*': return new Token(53, "", lineNo, colNo);
            case '/': return new Token(54, "", lineNo, colNo);
            default:  return new Token(62, "", lineNo, colNo);
        }
    }

    //Generates a Token object which means lex is a lexical error in regards to our scanner
    //Preconditions: lex.length() != 0
    //Postconditions: returns a Token object with the ID for TUNDF and its line & column number
    public static Token errorToken(String lex, int lineNo, int colNo) {return new Token(62, lex, lineNo, colNo);}

    //Helper functions to determine what kind of char c is, changed to be static so LexicalScanner.java can access them
    //Preconditions: none
    //Postconditions: returns true if c is a letter otherwise false
    public static boolean isLetter(char c) {return Character.isLetter(c);}

    //Preconditions: none
    //Postconditons: returns true if c is a digit otherwise false
    public static boolean isDigit(char c) {return Character.isDigit(c);}

    //Preconditions: none
    //Postconditions: returns true if c is an operator otherwise false
    public static boolean isOperator(char c) {return c == '+' || c == '-' || c == '/' || c == '*' || c == '=' || c == '<' || c == '>' || c == '^' || c == '%';}

    //Preconditions: none
    //Postconditions: return true if c is a delimeter otherwise false
    public static boolean isDelim(char c) {return c == '(' || c == ')' || c == '[' || c == ']' || c == ';' || c == ':' || c == ',' || c == '.';}

    //Preconditions: none
    //Postconditons: returns true if c is a whitespace character  otherwise false, the new line character is considered a whitespace
    public static boolean isWhiteSpace(char c) {return Character.isWhitespace(c);}

    //Preconditions: none
    //Postconditions: returns true if c is an invalid character otherwise false
    public static boolean isInvalid(char c)
    {return c == '@' || c == '?' || c == '#' || c == '$' || c == '!' || c == '`' || c == '~';}
}