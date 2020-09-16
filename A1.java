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
        assert(args.length != 1) : "File name required";
        String fileName = args[0];
        LexicalScanner lexical = new LexicalScanner();
        OutputController oc = new OutputController();
        //takes a stringbuilder object and a Token arraylist
        lexical.readFile(fileName);
        do
        {
            Token temp = lexical.getToken();
            lexical.printToken(temp);
        } while(!lexical.isEoF());
        oc.setInput(lexical.getInput());
        oc.setTokens(lexical.getStream());
        oc.generateListingFile();
        System.exit(0);
    }
}