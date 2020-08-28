import java.util.ArrayList;

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
        LexicalScanner lexical = new LexicalScanner();
        assert(args.length != 1) : "File name required";
        lexical.readFile(args[0]);
        Token temp = lexical.getToken();
        System.out.println(temp);
        /*DFSM machine = new DFSM();
        //String test = "CD20";
        StringBuffer buff = new StringBuffer("123abc");
        ArrayList<Token> tokens = machine.integerMachine(buff, 1);
        System.out.println(tokens);*/
    }
}