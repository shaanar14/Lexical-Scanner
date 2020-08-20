/*
    Project 1b
    Author: Shaan Arora, C3236359
    Scanner Class
        Main component of the lexical analyser that will use a DFA object to process each line of a file
        Each line will be processed as a string and fed into the DFA object
        The DFA object will return token/s which will be stored in a LinkedList from the standard Java library
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.String;
import java.util.ArrayList;
public class Scanner
{
    //array list to store all the tokens we find when we run the lexical scanner
    private ArrayList<Token> stream;
    //private DFA object that will consume a string to see if it is lexically valid, if it is then return a token
    private DFSM machine;
    //TODO: remake this function that reads a file
    public void Scaner()
    {
        this.stream = new ArrayList<Token>();
        this.machine = new DFSM();
    }
    void readFile(String fileName)
    {
        try
        {
            File code = new File(fileName);
            java.util.Scanner reader = new java.util.Scanner(code);
            while (reader.hasNextLine())
            {
                //might need to handle carriage-return character as a whitespace character
                String line = reader.nextLine();
                System.out.println(line);
            }
            reader.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File Error");
            e.printStackTrace();
        }
    }
}