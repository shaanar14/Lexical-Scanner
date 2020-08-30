/*
    Project 1b
    Author: Shaan Arora, C3236359
    Token Class
        Contains all functionality for the tokens generated by the lexical scanner
*/
import java.lang.String;
import java.util.*;

public class Token
{
    //Member variables for a Token object
    //Tokens enum for the label and ID of the token object
    //private tokenID to store the ID of the token object
    //private lexeme for the lexeme associated with integer literals,float/real literals,string literals, identifiers and undefined
    //private lineNo for which line the token object is located at
    //private colNo for which column the token object starts haven't decided if a column starts at 0 or 1
    //static final TPRINT string array for output

    private Tokens tokenID;
    private String lexeme;
    private int lineNo;
    private int colNo;
    //static because we want TPRINT for all object instances of Token
    //final because TPRINT is used for output formatting and we do not want it to change
    static final String[] TPRINT = {
            "T_EOF ",
            "TCD20 ",	"TCONS ",	"TTYPS ",	"TTTIS ",	"TARRS ",	"TMAIN ",
            "TBEGN ",	"TTEND ",	"TARAY ",	"TTTOF ",	"TFUNC ",	"TVOID ",
            "TCNST ",	"TINTG ",	"TREAL ",	"TBOOL ",	"TTFOR ",	"TREPT ",
            "TUNTL ",	"TIFTH ",	"TELSE ",	"TINPT ",	"TPRIN ",	"TPRLN ",
            "TRETN ",	"TNOTT ",	"TTAND ",	"TTTOR ",	"TTXOR ",	"TTRUE ",
            "TFALS ",	"TCOMA ",	"TLBRK ",	"TRBRK ",	"TLPAR ",	"TRPAR ",
            "TEQUL ",	"TPLUS ",	"TMINS ",	"TSTAR ",	"TDIVD ",	"TPERC ",
            "TCART ",	"TLESS ",	"TGRTR ",	"TCOLN ",	"TLEQL ",	"TGEQL ",
            "TNEQL ",	"TEQEQ ",	"TPLEQ ",	"TMNEQ ",	"TSTEQ ",	"TDVEQ ",
            "TSEMI ",	"TDOTT ",
            "TIDEN ",	"TILIT ",	"TFLIT ",	"TSTRG ",	"TUNDF "};

    //Default Constructor
    //Preconditions: none
    //Postconditions: Private member variables set to default values
    public Token()
    {
        this.tokenID = null;
        this.lexeme = "";
        this.lineNo = 0;
        this.colNo = 0;
    }

    //Parameter Constructors
    //Preconditions: 0 <= i <= 62 but i !=55
    //Postconditions: for the current token object tokenID is assigned the enum i, other private member variables set to default values
    public Token(int i)
    {
        this.tokenID = Tokens.valueOf(i);
        this.lexeme = "";
        this.lineNo = 0;
        this.colNo = 0;
    }

    //Precondition: t is a valid enum
    //Postconditions: the tokenID of the current Token object is set to t, other private member variables set to default values
    public Token (Tokens t)
    {
        this.tokenID = t;
        this.lexeme = "";
        this.lineNo = 0;
        this.colNo = 0;
    }

    //Postconditions: 0 <= i <= 62 but i !=55,
    //Postconditions: for the current Token object tokenID is assigned the enum i, lexeme is assigned lex, lineNo is assigned line and colNo is assigned col
    public Token(int i, String lex, int line, int col)
    {
        this.tokenID = Tokens.valueOf(i);
        this.lexeme = lex;
        this.lineNo = line;
        this.colNo = col;
    }

    //Preconditions: value of parameters are not null
    //Postconditions: values of private member variables set to the values of the parameters
    public Token(Tokens t, String lex, int line, int col)
    {
        this.tokenID = t;
        this.lexeme = lex;
        this.lineNo = line;
        this.colNo = col;
    }

    //Copy constructor
    public Token(Token t) {this(t.tokenID, t.lexeme, t.lineNo, t.colNo);}

    //Setter for tokenName
    //Preconditions: name is not empty
    //Postconditions: tokenID of the current object is set to the value of i
    public void setTokenID(int i) {this.tokenID = Tokens.valueOf(i);}

    //Setter for lexeme
    //Preconditions: lex is not empty
    //Postconditions: lexeme of the current object is set to the value of lex
    public void setLexeme(String lex) {this.lexeme = lex;}

    //Setter for lineNo
    //Preconditions: line is >= 0
    //Postconditions: lineNo of the current object is set to the value of line
    public void setLineNo(int line) {this.lineNo = line;}

    //Setter for colNo
    //Preconditions: col >= 0
    //Postconditions: colNo of the current token object is set to the value of col
    public void setColNo(int col) {this.colNo = col;}

    //Getter for the ID number of the current token object
    //Preconditions: tokenID is not null
    //Postcondition: returns the ID number value of the current token object
    public int getTokenID() {return this.tokenID.getID();}

    //Getter for lexeme
    //Preconditions: none
    //Postconditions: return the lexeme value for the current token object
    public String getLexeme() {return this.lexeme;}

    //Getter for lineNo
    //Preconditions: none
    //Postconditions: return the value of lineNo for the current token object
    public int getLineNo() {return this.lineNo;}

    //Getter for colNo
    //Preconditions: none
    //Postconditions: return the value of colNo for the current token object
    public int getColNo()
    {
        return  this.colNo;
    }

    //Override to neatly format and output a token
    //Preconditions: current token object (this) is != null
    //Postconditions: returns a formatted String depending on what the ID of the token is, used for output
    @Override
    public String toString()
    {
        String output = "";
        //This is to fix indexing and out of bounds issues since 55 doesnt exist in the enum
        if(this.getTokenID() >= 56)
        {
            output = TPRINT[this.getTokenID() - 1];
        }
        else
        {
            output = TPRINT[this.getTokenID()];
        }
        //if the tokenID is that for an indentifier, integer literal, real literal or float literal print its lexeme
        if(this.getTokenID() == 58 || this.getTokenID() == 59 || this.getTokenID() == 60 || this.getTokenID() == 61)
        {
            output += this.getLexeme() + " ";
        }
        //if the token is TUNDF, format output for lexical error
        else if(this.getTokenID() == 62)
        {
            output += "\nlexical error " + this.getLexeme();
        }
        /*else
        {
            output += this.getLineNo() + " " + this.getColNo();
        }*/
        return output;
    }
}