/*
    Project 1b
    Author: Shaan Arora, C3236359
    Token Class
        Contains all functionality for the tokens generated by the lexical scanner
*/

import java.lang.String;
import java.math.RoundingMode;
public class Token
{
    //Member variables for a Token object
    //tokenID to store the ID of the Token object
    //lexeme for the lexeme associated with integer literals,float/real literals,string literals, identifiers and undefined
    //lineNo for which line the Token object is located at
    // colNo for which column the Token object starts
    private Tokens tokenID;
    private String lexeme;
    private int lineNo;
    private int colNo;

    //static because we want TPRINT for all object instances of Token
    //final because TPRINT is used for output formatting and we do not want it to change
    private static final String[] TPRINT = {
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
    //Postconditions: Private member variables set to default values, using end of file token as default
    public Token()
    {
        this.tokenID = null;
        this.lexeme = "";
        this.lineNo = 0;
        this.colNo = 0;
    }

    //Preconditions: None
    //Postconditions: For the current Token object, assign the values of the parameters to the private member variables
    public Token(String lex, int line, int col)
    {
        this.tokenID = null;
        this.lexeme = lex;
        this.lineNo = line;
        this.colNo = col;
    }


    //Preconditions: i >= 0
    //Postconditions: For the current Token object, assign the values of the parameters to the private member variables,
    public Token(int i, String lex, int lineNo, int colNo)
    {
        assert i >= 0 : "Token ID number has to be greater or equal to 0";
        this.tokenID = Tokens.valueOf(i);
        this.lexeme = lex;
        this.lineNo = lineNo;
        this.colNo = colNo;
    }

    //Setters

    //Preconditions: i >= 0
    //Postconditions: tokenID of the current Token object is assigned the value of i
    public void setTokenID(int i) {this.tokenID = Tokens.valueOf(i);}

    //Preconditions: None
    //Postconditions: lexeme of the current Token object is assigned the value of lex
    public void setLexeme(String lex) {this.lexeme = lex;}

    //Preconditions: line is >= 0
    //Postconditions: lineNo of the current Token  object is assigned the value of line
    public void setLineNo(int line) {this.lineNo = line;}

    //Preconditions: col >= 0
    //Postconditions: colNo of the current Token object is assigned the value of col
    public void setColNo(int col) {this.colNo = col;}

    //Getters

    //Preconditions: tokenID is not null
    //Postcondition: Returns the ID number value of the current Token object
    public int getTokenID() {return this.tokenID.getID();}

    //Preconditions: None
    //Postconditions: Return the lexeme value for the current Token object
    public String getLexeme() {return this.lexeme;}

    //Preconditions: None
    //Postconditions: Return the value of lineNo for the current Token object
    public int getLineNo() {return this.lineNo;}

    //Preconditions: none
    //Postconditions: return the value of colNo for the current Token object
    public int getColNo() {return  this.colNo;}

    //Override to neatly format and output a Token object
    //Preconditions: Current token object (this) is != null
    //Postconditions: Returns a formatted String depending on what the ID of the token is, used for output
    @Override
    public String toString()
    {
        String output = "";
        //This is to fix indexing and out of bounds issues since 55 doesnt exist in the enum
        output = this.getTokenID() >= 56 ? TPRINT[this.getTokenID() - 1] : TPRINT[this.getTokenID()];
        //if the tokenID is that for an indentifier, integer literal, real literal or float literal print its lexeme
        //as per assignment spec the second field is rounded up in length to the next multiple of 6 characters
        if(this.getTokenID() == 58 || this.getTokenID() == 59 || this.getTokenID() == 60 || this.getTokenID() == 61)
        {
            //n will be the length of the lexeme
            double n = this.getLexeme().length();
            //double length = 0;
            //add padding if the size of the lexeme not 6
            if(n < 6)
            {
                while (this.getLexeme().length() != 5)
                {
                    String lex = this.getLexeme() + " ";
                    this.setLexeme(lex);
                }
                //update n if padding was added above
                n = this.getLexeme().length();
            }
            else if (n % 6 != 0)
            {
                //calculate the length such that we round up to the next multiple of 6 characters
                //ceiling function of the length of the lexeme divided by 6 and then times that value by 6
                //e.g. if n = 13 then length will be 18 which is the next multiple of 6 characters
                n = ((Math.ceil(n/6)) * 6);
            }
            //if the token is a string literal token add back the quotation marks
            output += this.getTokenID() == 61 ? String.format("\"%1." + (int) n + "s\" ", this.getLexeme()) : String.format("%1." + (int) n + "s", this.getLexeme()) + " ";

        }
        //if the token is TUNDF, TUNDF will print first then on a new line the lexeme for it then add the new line character
        else if(this.getTokenID() == 62)
        {
            output += "\nlexical error " + this.getLexeme();
        }
        return output;
    }
}