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
        //String array of the CD20 keywords
        String[] k = {"constants", "types","is", "arrays","main","begin","end","array",
                      "of","func","void","const","int","real","bool","for","repeat","until",
                      "if","else","input","print","println","return","not","and","or","xor","true","false"};
        //Array of specific enum values from Tokens that are classified as tokens
        Tokens[] t = {Tokens.TCNST, Tokens.TTYPS, Tokens.TTTIS, Tokens.TARRS, Tokens.TMAIN, Tokens.TBEGN,
                      Tokens.TTEND, Tokens.TARAY, Tokens.TTTOF, Tokens.TFUNC, Tokens.TVOID, Tokens.TCONS,
                      Tokens.TINTG, Tokens.TREAL, Tokens.TBOOL, Tokens.TTFOR, Tokens.TREPT, Tokens.TUNTL,
                      Tokens.TIFTH, Tokens.TELSE, Tokens.TINPT, Tokens.TPRIN, Tokens.TPRLN, Tokens.TRETN,
                      Tokens.TNOTT, Tokens.TTAND, Tokens.TTTOR, Tokens.TTXOR, Tokens.TTRUE, Tokens.TFALS};
        String match = lex.toString();
        if(match.equals("CD20")) return new Token(Tokens.TCD20, "", lineNo, colNo);
        //length of k and t are the same which is 30
        for(int i = 0; i < k.length; i++)
        {
            if(match.equalsIgnoreCase(k[i]))
            {
                return new Token(t[i], "", lineNo, colNo);
            }
        }
        //if all else fails return identifier token
        return new Token(Tokens.TIDEN, lex.toString(), lineNo, colNo);
    }

    //Generates integer literal tokens
    //Preconditions: lex.length() != 0
    //Postconditions: returns a integer literal Token object
    public static Token integerLiteral(StringBuilder lex, int lineNo, int colNo)
    {return new Token(Tokens.TILIT, lex.toString(), lineNo, colNo);}

    //Generates float literal tokens
    //Preconditions: lex.length() != 0
    //Postconditions returns a float literal Token object
    public static Token floatLiteral(StringBuilder lex, int lineNo, int colNo)
    {return new Token(Tokens.TFLIT, lex.toString(), lineNo, colNo);}

    //Generates string literal tokes
    //PreconditonsL lex.length() != 0
    //Postconditions: returns a string literal Token object
    public static Token stringLiteral(StringBuilder lex, int lineNo, int colNo)
    {return new Token(Tokens.TSTRG, lex.toString(), lineNo, colNo);}

    //Generates a Token object based on what delimeter c is
    //Preconditions: isDelim(c) == true
    //Postconditions: returns a Token object containg the token ID for what delimeter c is and its line & column number
    public static Token delimToken(char c, int lineNo, int colNo)
    {
        switch(c)
        {
            case ',': return new Token(Tokens.TCOMA, "", lineNo, colNo);
            case '[': return new Token(Tokens.TRBRK, "", lineNo, colNo);
            case ']': return new Token(Tokens.TLBRK, "", lineNo, colNo);
            case '(': return new Token(Tokens.TLPAR, "", lineNo, colNo);
            case ')': return new Token(Tokens.TRPAR, "", lineNo, colNo);
            case ':': return new Token(Tokens.TCOLN, "", lineNo, colNo);
            case ';': return new Token(Tokens.TSEMI, "", lineNo, colNo);
            case '.': return new Token(Tokens.TDOTT, "", lineNo, colNo);
            //default case being that we return a TUNDF token
            default:  return new Token(Tokens.TUNDF, String.valueOf(c), lineNo, colNo);
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
            case '=': return new Token(Tokens.TEQUL, "", lineNo, colNo);
            case '+': return new Token(Tokens.TPLUS, "", lineNo, colNo);
            case '-': return new Token(Tokens.TMINS, "", lineNo, colNo);
            case '*': return new Token(Tokens.TSTAR, "", lineNo, colNo);
            case '/': return new Token(Tokens.TDIVD, "", lineNo, colNo);
            case '%': return new Token(Tokens.TPERC, "", lineNo, colNo);
            case '^': return new Token(Tokens.TCART, "", lineNo, colNo);
            case '<': return new Token(Tokens.TLESS, "", lineNo, colNo);
            case '>': return new Token(Tokens.TGRTR, "", lineNo, colNo);
            //default case being that we return a TUNDF token
            default:  return new Token(Tokens.TUNDF, String.valueOf(c), lineNo, colNo);
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
            case '<': return new Token(Tokens.TLEQL, "", lineNo, colNo);
            case '>': return new Token(Tokens.TGEQL, "", lineNo, colNo);
            //the only case where ! is accepted ever
            case '!': return new Token(Tokens.TNEQL, "", lineNo, colNo);
            case '=': return new Token(Tokens.TEQEQ, "", lineNo, colNo);
            case '+': return new Token(Tokens.TPLEQ, "", lineNo, colNo);
            case '-': return new Token(Tokens.TMNEQ, "", lineNo, colNo);
            case '*': return new Token(Tokens.TSTEQ, "", lineNo, colNo);
            case '/': return new Token(Tokens.TDVEQ, "", lineNo, colNo);
            default:  return new Token(Tokens.TUNDF, "", lineNo, colNo);
        }
    }

    //Generates a Token object which means lex is a lexical error in regards to our scanner
    //Preconditions: lex.length() != 0
    //Postconditions: returns a Token object with the ID for TUNDF and its line & column number
    public static Token errorToken(String lex, int lineNo, int colNo)
    {return new Token(Tokens.TUNDF, lex, lineNo, colNo);}

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