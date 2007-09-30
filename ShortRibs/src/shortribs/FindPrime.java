/*
 * FindPrime.java
 * 
 * Created on Sep 21, 2007, 11:34:45 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shortribs;
import java.math.BigInteger;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Kaiser
 */

public class FindPrime extends Thread{
    JTextArea Output;
    JTextField TotalNumbersTextField;
    JTextField NumberBeingTestedTextField;
    JTextField NumberOfPrimesTextField;
    BigInteger FirstNumberBigInteger = BigInteger.ONE;
    BigInteger SecondNumberBigInteger = BigInteger.ONE;
    BigInteger ToBeTestedBigInteger = BigInteger.ZERO;
    int PrimesFoundInteger = 0;
    int NumbersTestedInteger = 3; //No need to test before f(3) as these numbers are not prime
    
    FindPrime(JTextArea OutputTextArea, JTextField NumberBeingTested, JTextField TotalNumbers, JTextField NumberOfPrimes) {
        Output = OutputTextArea;
        NumberBeingTestedTextField = NumberBeingTested;
        TotalNumbersTextField = TotalNumbers;
        NumberOfPrimesTextField = NumberOfPrimes;
    }
    
    @Override
    public void run()
    {
        Primality CheckPrime = new Primality();
        String OutputString = "";
        String OutputXMLString = "";
        String PathString = System.getProperty("user.dir").toString() + "\\data";
        String FileNameString = "";
        boolean IsNumberPrimeBoolean = false;
        ArrayList Factors = new ArrayList();
        
        while (true)
        {
            //Start XML String
            OutputXMLString = OutputXMLString.concat("<?xml version=\"1.0\"?>" + "\n");
            OutputXMLString = OutputXMLString.concat("<fibonacciNumber>" + "\n");
            
            //Find the Fib Number to be tested
            ToBeTestedBigInteger = FirstNumberBigInteger.add(SecondNumberBigInteger);
            
            //Write XML for <number>
            OutputXMLString = OutputXMLString.concat("    <number>" + ToBeTestedBigInteger + "</number>" + "\n");
            
            //Write XML for <function>
            OutputXMLString = OutputXMLString.concat("    <function>" + NumbersTestedInteger + "</function>"+ "\n");
           
            //Update the UI
            NumberBeingTestedTextField.setText(ToBeTestedBigInteger.toString());
           
           //Check to see if Fib number is Prime
            Factors = CheckPrime.TestPrime(ToBeTestedBigInteger);
           if(Factors.isEmpty())
           {
               //If it is prime increase the prime counter
               PrimesFoundInteger++;
               
               //Used for XML writing
               IsNumberPrimeBoolean = true;
               
               //Add the number to the UI
               OutputString = OutputString.concat( "F(" + (NumbersTestedInteger) + ") = " +ToBeTestedBigInteger.toString()+ "\n");
               Output.setText(OutputString);
               
           }
            //Write XML for <prime> as true
            OutputXMLString = OutputXMLString.concat("    <prime>" + IsNumberPrimeBoolean + "</prime>" + "\n");
            
            if (IsNumberPrimeBoolean == false)
            {
                for (int i=0;i<Factors.size();i++)
                {
                    OutputXMLString = OutputXMLString.concat("<factor>" + Factors.get(i) + "</factor>");
                }
            }
            //End XML String
            OutputXMLString = OutputXMLString.concat("</fibonacciNumber>");

           //Update UI with number of primes found
           NumberOfPrimesTextField.setText(Integer.toString(PrimesFoundInteger));
           
           //Create our File Name
           FileNameString =  NumbersTestedInteger + "." + ToBeTestedBigInteger + ".xml";
           
           //Write our file
           FileOutput WriteFile = new FileOutput();
           WriteFile.WriteXML(PathString, FileNameString, OutputXMLString);
           
           //Get Variables ready for the next test
           FirstNumberBigInteger = SecondNumberBigInteger;
           SecondNumberBigInteger = ToBeTestedBigInteger;
           IsNumberPrimeBoolean = false;
           OutputXMLString = "";
           
           //Update UI and variable with the total numbers tested
           NumbersTestedInteger++;
           TotalNumbersTextField.setText(Integer.toString(NumbersTestedInteger));
           
        }
    }
}

