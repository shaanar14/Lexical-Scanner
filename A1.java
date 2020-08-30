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
        while(!lexical.eof())
        {
            Token temp = lexical.getToken();
            lexical.printToken(temp);
        }
        scan.close();
    }
}