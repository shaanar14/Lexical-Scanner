/*
    Project 1b
    Author: Shaan Arora, C3236359
    DFA Class
        Contains all functionality to create a deterministic finite state machine
        that will take a string and either reject it or accept it & output its associated token
 */
import java.lang.String;
public class DFSM
{
    //Private Member Variables
    //private member variable for a counter that will keep track which state the DFSM is in
    private int stateCounter;
    //the input string as a StringBuffer that will be fed into the DFSM.
    //StringBuffer allows us to add and delete chars as we need to 
    private StringBuffer input;

    //Default constructor
    //Preconditions: none
    //Postconditions: private member variables are initialized to default values
    public void DFSM()
    {
        this.stateCounter = 0;
        this.input = new StringBuffer();
    }

    //Parameter constructor
    //Preconditions: inputString is not empty
    //Postconditions: stateCounter and token are intilized and the value of input is assigned the value of inputString
    public void DFSM(String inputString)
    {
        this.stateCounter = 0;
        this.input = new StringBuffer();
    }
}
