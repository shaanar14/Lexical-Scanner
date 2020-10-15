/*
    Project 2
    Author: Shaan Arora, C3236359
    Tokens Enum
        Enum for the token IDs
        A HashMap object is used to match the enum to its ID.
 */

enum Tokens
{
    // Token value for end of file
    T_EOF,

    // The 30 keywords

    TCD20,	TCONS,	TTYPS,	TTTIS,	TARRS,	TMAIN,
    TBEGN,	TTEND,	TARAY,	TTTOF,	TFUNC,	TVOID,
    TCNST,	TINTG,	TREAL,	TBOOL,	TTFOR,	TREPT,
    TUNTL,	TIFTH,	TELSE,	TINPT,	TPRIN,	TPRLN,
    TRETN,	TNOTT,	TTAND,	TTTOR,	TTXOR,	TTRUE,
    TFALS,

    // the operators and delimiters
    TCOMA,	TLBRK,	TRBRK,	TLPAR,	TRPAR,
    TEQUL,	TPLUS,	TMINS,	TSTAR,	TDIVD,	TPERC,
    TCART,	TLESS,	TGRTR,	TCOLN,	TLEQL,	TGEQL,
    TNEQL,	TEQEQ,	TPLEQ,	TMNEQ,	TSTEQ,	TDVEQ,
    TSEMI,	TDOTT,

    // the tokens which need tuple values

    TIDEN,	TILIT,	TFLIT,	TSTRG,	TUNDF;
}