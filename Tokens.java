/*
    Project 1b
    Author: Shaan Arora, C3236359
    Tokens Enum
        Enum for the token IDs
        A HashMap object is used to match the enum to its ID.
 */

import java.util.HashMap;
import java.util.Map;

enum Tokens
{
    // Token value for end of file
    T_EOF(0),

    // The 30 keywords

    TCD20(1),	TCONS(2),	TTYPS(3),	TTTIS(4),	TARRS(5),	TMAIN(6),
    TBEGN(7),	TTEND(8),	TARAY(9),	TTTOF(10),	TFUNC(11),	TVOID(12),
    TCNST(13),	TINTG(14),	TREAL(15),	TBOOL(16),	TTFOR(17),	TREPT(18),
    TUNTL(19),	TIFTH(20),	TELSE(21),	TINPT(22),	TPRIN(23),	TPRLN(24),
    TRETN(25),	TNOTT(26),	TTAND(27),	TTTOR(28),	TTXOR(29),	TTRUE(30),
    TFALS(31),

    // the operators and delimiters
    TCOMA(32),	TLBRK(33),	TRBRK(34),	TLPAR(35),	TRPAR(36),
    TEQUL(37),	TPLUS(38),	TMINS(39),	TSTAR(40),	TDIVD(41),	TPERC(42),
    TCART(43),	TLESS(44),	TGRTR(45),	TCOLN(46),	TLEQL(47),	TGEQL(48),
    TNEQL(49),	TEQEQ(50),	TPLEQ(51),	TMNEQ(52),	TSTEQ(53),	TDVEQ(54),
    TSEMI(56),	TDOTT(57),

    // the tokens which need tuple values

    TIDEN(58),	TILIT(59),	TFLIT(60),	TSTRG(61),	TUNDF(62);

    //private id variable for the TokenID enum
    private final int id;

    //HashMap so we can map the integer to its corresponding enum
    //This allows us to get the enum from an integer and also the integer based on the enum
    private static final Map<Integer, Tokens> map = new HashMap<>();

    //Map each token ID number to its appropriate name/label
    //This will happen on loading time
    static
    {
        for (Tokens t : Tokens.values())
        {
            map.put(t.getID(), t);
        }
    }

    //Constructor for the enum
    Tokens(int id) {this.id = id;}

    //Get the enum based on the id
    public static Tokens valueOf(int id) {return map.get(id);}

    //return the ID of the token
    public int getID() {return this.id;}
}