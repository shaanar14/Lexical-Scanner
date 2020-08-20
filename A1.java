/*
    Project 1b
    Author: Shaan Arora, C3236359
    A1 Class
        Main driver for the entire lexical analyser/scanner
        Creates a single Scanner object which will run the scanner
        Hopefully outputs all found tokens and all lexical errors
*/
public class A1
{
    public static void main(String[] args)
    {
        //TODO create lexical scanner object
        //Scanner scanner = new Scanner();
        //call readFile function from scanner object passing in args[0]
        //error check that args[0] does exist
        //scanner.readFile(args[0]);
        Token testToken = new Token(54);
        Token testToken2 = new Token(56);
        Token testToken3 = new Token(57);
        Token testToken4 = new Token(58);
        Token testToken5 = new Token(59);
        Token testToken6 = new Token(60);
        Token testToken7 = new Token(61);
        Token testToken8 = new Token(62);
        //Token testToken = new Token(0);
        System.out.println(testToken);
        System.out.println(testToken2);
        System.out.println(testToken3);
        System.out.println(testToken4);
        System.out.println(testToken5);
        System.out.println(testToken6);
        System.out.println(testToken7);
        System.out.println(testToken8);
    }
    //TODO need to read in a file
    //TODO need to tokenize each line of the file based on the lexical rules of CD20
    //TODO figure out how to integrate the token.txt file into the lexical analyzer
    //TODO write a small driver similar to the one in project specs
    //TODO create an output controller to handle error messages for scanner and future phases
}
