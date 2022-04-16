

// Withdrawal.java
// Represents a withdrawal ATM transaction

public class Withdrawal extends Transaction
{
   private int amount; // amount to withdraw
   private CashDispenser cashDispenser; // reference to cash dispenser
   

   // Withdrawal constructor
   public Withdrawal( int userAccountNumber, BankDatabase atmBankDatabase,
		   CashDispenser atmCashDispenser)
   {
      // initialize superclass variables
      super( userAccountNumber, atmBankDatabase );
      
      // initialize references to keypad and cash dispenser
      
   } // end Withdrawal constructor

   // perform transaction
   public void execute()
   {
      boolean cashDispensed = false; // cash was not dispensed yet
      double availableBalance; // amount available for withdrawal

      BankDatabase bankDatabase = getBankDatabase(); 
  
         // obtain a chosen withdrawal amount from the user 

         {
            // get available balance of account involved
            availableBalance = 
               bankDatabase.getAvailableBalance( getAccountNumber() );
      
            // check whether the user has enough money in the account 
            if ( amount <= availableBalance )
            {   
               // check whether the cash dispenser has enough money
               if ( cashDispenser.isSufficientCashAvailable( amount ) )
               {
                  // update the account involved to reflect withdrawal
                  bankDatabase.debit( getAccountNumber(), amount );
                  
                  cashDispenser.dispenseCash( amount ); // dispense cash
                  cashDispensed = true; // cash was dispensed
               }
            }// end if
		}
	}   // end method execute 
}
    // end class Withdrawal