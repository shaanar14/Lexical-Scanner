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
    public Token identifierToken(StringBuilder lex, int lineNo, int colNo)
    {
        Token t = new Token(lex.toString(), lineNo, colNo);
        //whitespace, operator, delimeter or invalid char found so we can set the tokenID and lexeme and return that token
        if(lex.charAt(0) != '_')
        {
            //after consuming chars, if the the first char in lex is not an _ then check to see if lex is a keyword
            int id = this.keywordMatch(lex);
            //if lex does match a keyword then set the ID for the keyword matched
            if(id != -1){t.setTokenID(id);}
            //if its not a keyword or the char at index 0 in lex is a _ then set the ID for an identifier
            else{t.setTokenID(58);}
        }
        return t;
    }

    //Helper function for indentifierMachine() to match keywords
    //Preconditions: lex.length() != 0
    //Postconditions: checks to see if lex is equal to a keyword and returns its ID otherwise returns -1
    private int keywordMatch(StringBuilder lex)
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
    public Token integerLiteral(StringBuilder lex, int lineNo, int colNo) {return new Token(59, lex.toString(), lineNo, colNo);}

    //Generates float literal tokens
    //Preconditions: lex.length() != 0
    //Postconditions returns a float literal Token object
    public Token floatLiteral(StringBuilder lex, int lineNo, int colNo) {return new Token(60, lex.toString(), lineNo, colNo);}

    //Generates string literal tokes
    //PreconditonsL lex.length() != 0
    //Postconditions: returns a string literal Token object
    public Token stringLiteral(StringBuilder lex, int lineNo, int colNo) {return new Token(61, lex.toString(), lineNo, colNo);}

    //Generates a Token object based on what delimeter c is
    //Preconditions: isDelim(c) == true
    //Postconditions: returns a Token object containg the token ID for what delimeter c is and its line & column number
    public Token delimToken(char c, int lineNo, int colNo)
    {
        switch(c)
        {
            case ',': return new Token(32, String.valueOf(c), lineNo, colNo);
            case '[': return new Token(33, String.valueOf(c), lineNo, colNo);
            case ']': return new Token(34, String.valueOf(c), lineNo, colNo);
            case '(': return new Token(35, String.valueOf(c), lineNo, colNo);
            case ')': return new Token(36, String.valueOf(c), lineNo, colNo);
            case ':': return new Token(46, String.valueOf(c), lineNo, colNo);
            case ';': return new Token(56, String.valueOf(c), lineNo, colNo);
            case '.': return new Token(57, String.valueOf(c), lineNo, colNo);
            //default case being that we return a TUNDF token
            default:  return new Token(62, String.valueOf(c), lineNo, colNo);
        }
    }

    //Generates a Token object based on what operator c is
    //Preconditions: isOperator(c) == true
    //Postconditions: returns a Token object containg the token ID for what kind of operator c is and its line & column number
    public Token operatorToken(char c, int lineNo, int colNo)
    {
        //based on what c is, set the ID of the Token object to its corresponding value
        switch(c)
        {
            case '=': return new Token(37, String.valueOf(c), lineNo, colNo);
            case '+': return new Token(38, String.valueOf(c), lineNo, colNo);
            case '-': return new Token(39, String.valueOf(c), lineNo, colNo);
            case '*': return new Token(40, String.valueOf(c), lineNo, colNo);
            case '/': return new Token(41, String.valueOf(c), lineNo, colNo);
            case '%': return new Token(42, String.valueOf(c), lineNo, colNo);
            case '^': return new Token(43, String.valueOf(c), lineNo, colNo);
            case '<': return new Token(44, String.valueOf(c), lineNo, colNo);
            case '>': return new Token(45, String.valueOf(c), lineNo, colNo);
            //default case being that we return a TUNDF token
            default:  return new Token(62, String.valueOf(c), lineNo, colNo);
        }
    }

    //Generates a Token object based on what kind of operator c is
    //Preconditions: lex.length() == 2 and lex.charAt(1) == '='
    //Postconditions: a Token object is returned containing a token ID for a composite operator and its line & column number
    public Token compositeOpToken(String lex, int lineNo, int colNo)
    {
        //we already know that the char at index 1 is a = so we just check to see what the operator at index 0 is
        //depending on what the first character of input is, set the ID of the Token object to its corresponding value and add that operator to lex
        switch(lex.charAt(0))
        {
            case '<': return new Token(47, lex, lineNo, colNo);
            case '>': return new Token(48, lex, lineNo, colNo);
            //the only case where ! is accepted ever
            case '!': return new Token(49, lex, lineNo, colNo);
            case '=': return new Token(50, lex, lineNo, colNo);
            case '+': return new Token(51, lex, lineNo, colNo);
            case '-': return new Token(52, lex, lineNo, colNo);
            case '*': return new Token(53, lex, lineNo, colNo);
            case '/': return new Token(54, lex, lineNo, colNo);
            default:  return new Token(62, lex, lineNo, colNo);
        }
    }

    //Generates a Token object which means lex is a lexical error in regards to our scanner
    //Preconditions: lex.length() != 0
    //Postconditions: returns a Token object with the ID for TUNDF and its line & column number
    public Token errorToken(String lex, int lineNo, int colNo) {return new Token(62, lex, lineNo, colNo);}

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
    public static boolean isDelim(char c) {return c == '(' || c == ')' || c == '[' || c == ']' || c == '.' || c == ';' || c == ':';}

    //Preconditions: none
    //Postconditons: returns true if c is a whitespace character  otherwise false, the new line character is considered a whitespace
    public static boolean isWhiteSpace(char c) {return Character.isWhitespace(c);}

    //Preconditions: none
    //Postconditions: returns true if c is an invalid character otherwise false
    public static boolean isInvalid(char c) {return c == '@' || c == '?' || c == '#' || c == '!';}
}