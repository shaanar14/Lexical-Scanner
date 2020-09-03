/*
    Project 1b
    Author: Shaan Arora, C3236359
    Keywords Enum
        Enum for the reserved keywords so that we can check that an identifier names is not the same as a keyword
        CD20 keyword is the only exception that has to be uppercase
        using K# as a naming convention for each enum where # is just a number so that I can store the keywords as strings
 */

public enum Keywords
{
    K0("CD20"),    K1("constants"), K2("types"), K3("is"),     K4("arrays"), K5("main"),
    K6("begin"),   K7("end"),       K8("array"), K9("of"),     K10("func"),  K11("void"),
    K12("const"),  K13("int"),      K14("real"), K15("bool"),  K16("for"),   K17("repeat"),
    K18("until"),  K19("if"),       K20("else"), K21("input"), K22("print"), K23("println"),
    K24("return"), K25("not"),      K26("and"),  K27("or"),    K28("xor"),   K29("true"),
    K30("false");

    //private String member variable for the enum to hold the actual keyword
    private final String keyWord;

    //Constructor for the enum
    Keywords(String word) {this.keyWord = word;}

    //Getter for the member variable
    public String getKeyWord() {return this.keyWord;}
}