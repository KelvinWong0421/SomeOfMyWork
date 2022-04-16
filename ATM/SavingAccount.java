
public class SavingAccount extends Account
{
    // instance variables 
    private double InterestRate ;

    public SavingAccount(int theAccountNumber, int thePIN, //constructer with InterestRate 
      double theAvailableBalance, double theTotalBalance,double rate)
    {
        super(theAccountNumber,thePIN,theAvailableBalance,theTotalBalance);
        InterestRate=rate;
    }
    
    public SavingAccount(int theAccountNumber, int thePIN, //default constructer 
      double theAvailableBalance, double theTotalBalance)
    {
        super(theAccountNumber,thePIN,theAvailableBalance,theTotalBalance);
        InterestRate=0.1;
    }

    public double GetInterestRate()
    {
        return InterestRate;
    }
    
    public void SetInterestRate(double rate)
    {
         InterestRate=rate;
    }
    
}
