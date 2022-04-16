public class CurrentAccount extends Account
{
    // instance variables 
    private double limit;// overdrawn limit

    public CurrentAccount(int theAccountNumber, int thePIN, //constructer with set overdrawn limit 
      double theAvailableBalance, double theTotalBalance,double limit)
    {
        super(theAccountNumber,thePIN,theAvailableBalance,theTotalBalance);
        this.limit=limit;
    }
    
    public CurrentAccount(int theAccountNumber, int thePIN, //default constructer 
      double theAvailableBalance, double theTotalBalance)
    {
        super(theAccountNumber,thePIN,theAvailableBalance,theTotalBalance);
        limit=10000;                                    // default overdrawn limit
    }

    public double GetLimit()
    {
        return limit;
    }
    
    public void SetLimit(double  value )
    {
         limit=value ;
    }
    
}