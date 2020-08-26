/*
    Project 1b
    Author: Shaan Arora, C3236359
    Scanner Class
        Main component of the lexical analyser that will use a DFA object to process each line of a file
        Each line will be processed as a string and fed into the DFA object
        The DFA object will return token/s which will be stored in a LinkedList from the standard Java library
 */
import java.io.*;
import java.lang.String;
import java.util.*;
public class LexicalScanner
{
    //array list to store all the tokens we find when we run the lexical scanner
    private ArrayList<Token> stream;
    //private DFA object that will consume a string to see if it is lexically valid, if it is then return a token
    private DFSM machine;
    //Same as StringBuffer but faster
    private StringBuilder inputStream;
    //check if we have reached the end of the file
    private boolean eof;
    //TODO: remake this function that reads a file
    public LexicalScanner()
    {
        this.stream = new ArrayList<>();
        this.machine = new DFSM();
        this.inputStream = new StringBuilder();
        this.eof = false;
    }
    public Token getToken()
    {
        StringBuilder output = new StringBuilder();
        int lineNo = 0, colNo = 0;
        //if we have reach the end of the file return out of the function call
        if(this.inputStream.length() == 0)
        {
            return null;
        }
        for(int i = 0; i < this.inputStream.length(); i++)
        {
            //loop until we hit a the newline, carriage return
            //if i want to make it system specific, can use System.lineSeparator()
            if(this.inputStream.charAt(i) == '\n' || this.inputStream.charAt(i) == '\r') {break;}
            if(this.inputStream.charAt(i) == '\u001a')
            {
                this.eof = true;
                break;
            }
            output.append(this.inputStream.charAt(i));
            colNo++;
        }
        lineNo++;
        //generate the next valid token
        Token temp = this.machine.keywordMachine(output, lineNo, colNo);
        //adding 2 to accomodate for the fact that that we skipped \n and \r
        this.inputStream.delete(0,(output.length()+2));
        //System.out.println(this.inputStream);
        this.stream.add(temp);
        return temp;
    }
    public void readFile(String fileName)
    {
        //Token test = new Token();
        try
        {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader scan = new BufferedReader(fr);
            int i;
            while((i = scan.read()) != -1)
            {
                char c = (i == 9) ? '\t' : (char) i;
                this.inputStream.append(c);
            }
            //append EOF marker to the end of our inputstream
            char testChar = '\u001a';
            this.inputStream.append(testChar);
        }
        catch (IOException e)
        {
            System.out.println("File Error");
            e.printStackTrace();
        }
    }
}