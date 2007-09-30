/*
 * Primality.java
 * 
 * Created on Sep 20, 2007, 2:08:06 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shortribs;
import java.math.BigInteger;
import java.util.ArrayList;
/**
 *
 * @author Kaiser
 */
public class Primality 
{
    
    @SuppressWarnings("unchecked")
    public ArrayList TestPrime(BigInteger NumberToTest)
    {
        BigInteger CanidateBigInteger = NumberToTest;
        BigInteger DivisorBigInteger = BigInteger.ONE;
        Integer TotalDivisorsInteger = 0;
        ArrayList FactorsArrayList = new ArrayList();
        
        while (DivisorBigInteger.compareTo(CanidateBigInteger)< 0)
        {
            if (DivisorBigInteger.compareTo(BigInteger.ONE) == 0 || DivisorBigInteger.compareTo(CanidateBigInteger) == 0 ){
                //Do nothing
             }else if (BigInteger.ZERO.compareTo(CanidateBigInteger.remainder(DivisorBigInteger)) == 0){
                TotalDivisorsInteger++;
                FactorsArrayList.add(DivisorBigInteger.toString());
            }
            DivisorBigInteger = DivisorBigInteger.add(BigInteger.ONE);
        }       
        return FactorsArrayList;     
    }
}
