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
    public static void main(String[] args)
    {
        assert(args.length != 1) : "File name required";
        String fileName = args[0];
        LexicalScanner lexical = new LexicalScanner();
        lexical.readFile(fileName);
        /*do
        {
            Token temp = lexical.getToken();
            lexical.printToken(temp);
        } while(!lexical.eof());*/
        boolean slTest = lexical.findSLComment();
        boolean mlTest = lexical.findMLComment();
        //har c = lexical.getCharAt();
        System.out.printf("%b %b", slTest, mlTest);
        System.exit(0);
    }
}