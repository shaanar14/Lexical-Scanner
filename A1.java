/*
    Project 1b
    Author: Shaan Arora, C3236359
    A1 Class
        Main driver for the entire lexical analyser/scanner
        Creates a single Scanner object which will run the scanner
        Hopefully outputs all found tokens and all lexical errors
*/
import java.io.*;
import java.util.Scanner;

public class A1
{
    public static File fileName;
    public static Scanner scan;
    public static void main(String[] args)
    {
        assert(args.length != 1) : "File name required";
        fileName = new File(args[0]);
        try
        {
            scan = new Scanner(fileName);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        LexicalScanner lexical = new LexicalScanner();
        /*while(!lexical.eof())
        {
            Token temp = lexical.getToken();
            lexical.printToken(temp);
        }*/
        DFSM testMachine = new DFSM();
        String testString = "\"This is a test string\"", testString2 = "_123", testString3 = "<=", testString4 = "!=";
        Token t1 = testMachine.baseMachine(testString,0,0);
        Token t2 = testMachine.baseMachine(testString2,0,0);
        Token t3 = testMachine.baseMachine(testString3,0,0);
        Token t4 = testMachine.baseMachine(testString4,0,0);
        System.out.printf("%s %s %s %s", t1, t2, t3, t4);
        scan.close();
        System.exit(0);
    }
}