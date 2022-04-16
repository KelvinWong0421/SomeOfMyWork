import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WithdrawalUI extends ATM
{
   private int amount; 
   private JPanel withdrawalPanel;
   private JPanel basePanel;
   private Transaction withdrawal;		// account information database
   private int userAccountNumber;
   private BankDatabase info;
   private CashDispenser cashDispenser;
   private JLabel text1;
   public boolean exiting = true;
   
   private static final String[] names = { "", "", "$100",
										   "" , "Other amounts", "$500",
										   ""			     , "", "$1000",
										   "", "Cancel transaction", "", "", "",};
	private int row = 5, col = 5;	
	
	private static final String[] withdrawalLabelNames = {"", "", "",    //26-33line is making component for comfirmation panel
													"", "", "",
													"", "", "", 
															};
	private static final String[] withrawalButtonNames = {"Yes", "", "No"};
	private JPanel withdrawalMessagePanel;
	private JLabel[] withdrawalLabel = new JLabel[withdrawalLabelNames.length];
	private JButton[] withdrawalButtons = new JButton[withrawalButtonNames.length];
	
	JLabel[] exitLabel;
	JPanel exitMessagePanel;
	
	Handler handler = new Handler(); 
	
	public WithdrawalUI(JPanel bPanel, int userAccountNumber, BankDatabase atmBankDatabase, 
			            CashDispenser atmCashDispenser )
	  {
		  cashDispenser = atmCashDispenser;
		withdrawal = new Withdrawal (userAccountNumber, atmBankDatabase, 
				                	   atmCashDispenser);
		  this.userAccountNumber = userAccountNumber;
	        info = atmBankDatabase;
	        basePanel = bPanel;
	  
	        withdrawalPanel = new JPanel();
	        withdrawalPanel.setLayout(new GridLayout(row, col, 10, 10));
	  
	        text1 = new JLabel ("<html><p>Please choose a withdrawal amount</p></html>");
	        withdrawalPanel.add(text1);
	        
	        JButton[] buttons = new JButton[names.length];
	        for ( int count = 0; count < names.length; count++)
			{
				buttons[ count ] = new JButton( names[ count ] );
				Handler handler = new Handler();
				buttons[count].addActionListener(handler);  
				withdrawalPanel.add(buttons[count]);  
				if( buttons[count].getText() == "")
				{   
					buttons[count].setVisible(false);
				} 
			}
	        basePanel.add(withdrawalPanel);  
			basePanel.revalidate();
	  }
	
	
	
	private class Handler implements ActionListener 		// create Handler class
	{
		public void actionPerformed( ActionEvent event )
		{
			String nextUI = event.getActionCommand();	
			switch (nextUI) {								// the choices of the withdrawal amount
			case "$100" : amount = 100;
						  confirmation();
				break;
			case "$500" : amount = 500;
						  confirmation();
				break;
			case "$1000" : amount = 1000;
						  confirmation(); 
				break;
			case "Other amounts" : 
				basePanel.remove(withdrawalPanel);
		        basePanel.repaint();
				WithdrawalOtherAmountUI inputPanel = new WithdrawalOtherAmountUI(basePanel, userAccountNumber, info, 
     																			 cashDispenser );
	            break;
			case "Cancel transaction" :
				//JOptionPane.showMessageDialog(null, "\nCanceling transaction..." );	// message of cancaling the transaction
				basePanel.remove(withdrawalPanel);   
				String[] exitLabelNames = { "", "", "", 
											"", "<html>Cancelling transaction...<br><br></html>", "",
											"", "", "", 
											"", "", ""};
				exitMessagePanel = new JPanel();
				exitMessagePanel.setLayout(new GridLayout(4, 3, 0, 0));
				exitLabel = new JLabel[exitLabelNames.length];
				for(int i = 0; i < exitLabelNames.length; i++) {   //create label
					exitLabel[i] = new JLabel(exitLabelNames[i]);
					exitMessagePanel.add(exitLabel[i]);
				}
				basePanel.add(exitMessagePanel);   //add exit panel
				basePanel.revalidate();
				basePanel.repaint();
				Timer timer2 = new Timer();   //create time object
				timer2.schedule(new TakeCardTask(), 1000);   //after 1 seconds, "cancelling transaction" will change to thank you message and then return to login page.
				timer2.schedule(new ExitWithdrawalTask(), 4000);
				//ATMMainMenu(basePanel,userAccountNumber, info);		// back to main menu
				break;
			
			}
		}
	}

	/*public void confirmation() 
	{
		int confirmation = JOptionPane.showConfirmDialog(null, "You are going to withdrawal $" + amount, 
													"Confirmation",JOptionPane.YES_NO_OPTION); // confirm the amount
		if (confirmation == 0)
		{
			execute();
			if (exiting == true)
			exit();
			  /* JOptionPane.showMessageDialog(null, "\nPlease take your card now.");
			   JOptionPane.showMessageDialog(null, "\nPlease take your cash now."); 
			   JOptionPane.showMessageDialog(null, "\nThank you! Bye bye!");
			   basePanel.remove(withdrawalPanel);   
			   basePanel.repaint();
			   returnToLogin(info, basePanel);
		}
		else
		{
			basePanel.remove(withdrawalPanel);   
			   basePanel.repaint();
			   WithdrawalUI withdrawalPanel = new WithdrawalUI(basePanel, userAccountNumber, info, 
					   							cashDispenser ); 
		}
   
	}*/
	
	public void confirmation() 
	{
		basePanel.remove(withdrawalPanel);
		basePanel.repaint();
		basePanel.revalidate();
		withdrawalMessagePanel = new JPanel();   //create panel
		withdrawalMessagePanel.setLayout(new GridLayout(4, 3, 0, 0));   
		for(int i = 0; i < withdrawalLabelNames.length; i++) {   //create label
			if(i == 4)
				withdrawalLabel[i] = new JLabel("Do you want you withdraw HKD"+amount+"?");
			else
				withdrawalLabel[i] = new JLabel(withdrawalLabelNames[i]);
			withdrawalMessagePanel.add(withdrawalLabel[i]);
		}
		WithdrawalHandler withdrawalHandler = new WithdrawalHandler();
		for(int i = 0; i < withrawalButtonNames.length; i++) { 
			withdrawalButtons[i] = new JButton(withrawalButtonNames[i]);  
			withdrawalButtons[i].addActionListener(withdrawalHandler); 
			withdrawalMessagePanel.add(withdrawalButtons[i]);   
			if( withdrawalButtons[i].getText() == ""){ 
				withdrawalButtons[i].setVisible(false);
			} 
		}
		basePanel.add(withdrawalMessagePanel);   //add exit panel
		basePanel.revalidate();   //
	}
	
	public void execute()
	{
		
		
		boolean cashDispensed = false;
	    double availableBalance;
	      BankDatabase bankDatabase = withdrawal.getBankDatabase(); 
	      
		  Timer timer3 = new Timer();
	     
		if ( amount != 0 )
		{
			availableBalance = 
		               bankDatabase.getAvailableBalance(withdrawal.getAccountNumber());
		 
			if ( amount <= availableBalance )
			{   
			 
				if ( cashDispenser.isSufficientCashAvailable( amount ) )
				{
               
					bankDatabase.debit(withdrawal.getAccountNumber(), amount );
               
					cashDispenser.dispenseCash(amount); 
					cashDispensed = true; 
					
					withdrawalLabel[4].setText("<html>Please take the card!<br><br></html>");
					
					exit();
				}
				else
				{
					//JOptionPane.showMessageDialog( null, "\nInsufficient cash available in the ATM." +
					//      "\n\nPlease choose a smaller amount.");
					withdrawalLabel[4].setText("<html>Insufficient cash available in the ATM.<br>Please choose a smaller amount.</html>");
					exiting = false;
					timer3.schedule(new UnsuccessfulExecuteTask(), 3000); 
				}
			}
			else
			{
				//JOptionPane.showMessageDialog(null, "\nInsufficient funds in your account." +
				//           "\n\nPlease choose a smaller amount.");
				withdrawalLabel[4].setText("<html>Insufficient funds in your account.<br>Please choose a smaller amount.</html>");
				exiting = false; 
				timer3.schedule(new UnsuccessfulExecuteTask(), 3000); 
			}
        }
	}
	
	public void exit()
	{
		Timer timer = new Timer();   //create time object
		timer.schedule(new ChangeTakeTextTask(), 3000);   //after 1 seconds, "cancelling transaction" will change to thank you message and then return to login page.
		timer.schedule(new ExitWithdrawalMessageTask(), 6000);   //
		
		/*int exit = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Exit",JOptionPane.YES_NO_OPTION);	// exit the program
		if(exit == 0)   
		{   
			JOptionPane.showMessageDialog(null, "\nPlease take your card now.");
			JOptionPane.showMessageDialog(null, "\nPlease take your cash now."); 
			JOptionPane.showMessageDialog(null, "\nThank you! Bye bye!");
			basePanel.remove(withdrawalPanel);   
			basePanel.repaint();
			returnToLogin(info, basePanel);
		}
		else 
		{
			JOptionPane.showMessageDialog(null, "\nPlease take your cash now."); 
			basePanel.remove(withdrawalPanel);   
			basePanel.repaint();
			ATMMainMenu(basePanel,userAccountNumber, info); 
		}*/
	} 
	
	//250-263 line will be ran when user choose "100", "500" or "1000" button
	public class ChangeTakeTextTask extends TimerTask {   //this task is executed when user take either "100", "500" or "1000" HKD
		public void run() {                 //before this task is executed the message should be "Please take the card!"
			withdrawalLabel[4].setText("<html>Please take the cash!<br>Thank You!</html>");
		}
	}
	
	public class ExitWithdrawalMessageTask extends TimerTask {    //after ChangeTakeTextTask, this task will be execute.
		public void run() {                                 //change the interface to login page
			basePanel.remove(withdrawalMessagePanel);
			basePanel.repaint();
			returnToLogin(info, basePanel, cashDispenser);
		}		
	}

	//266-278 line will be ran when user choose to cancel transaction
	public class TakeCardTask extends TimerTask {   //this task executed when user choose cancel transaction at the first withdraw panel.
		public void run() {
			exitLabel[4].setText("<html>Please take the card!<br>Thank You!</html>");
		}
	}
	
	public class ExitWithdrawalTask extends TimerTask {   //exit to login page
		public void run() {
			basePanel.remove(exitMessagePanel);
			basePanel.repaint();
			returnToLogin(info, basePanel, cashDispenser);
		}		
	}
	
	private class WithdrawalHandler implements ActionListener 
	{
		public void actionPerformed( ActionEvent event )
		{
			withdrawalButtons[0].setVisible(false);
			withdrawalButtons[2].setVisible(false);
			
			if(event.getActionCommand() == "Yes")
			{
				execute();
			}
			else 
			{
				basePanel.remove(withdrawalMessagePanel);
				basePanel.add(withdrawalPanel);
				basePanel.repaint();
				basePanel.revalidate();
			}
		}
	}
	
	public class UnsuccessfulExecuteTask extends TimerTask {
		public void run() {
			basePanel.remove(withdrawalMessagePanel);
			basePanel.add(withdrawalPanel);
			basePanel.repaint();
			basePanel.revalidate();
		}		
	}
}

	

	
