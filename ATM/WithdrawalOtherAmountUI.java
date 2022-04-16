import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WithdrawalOtherAmountUI extends ATM
 {
	private Transaction withdrawal;
	private JPanel basePanel;
	private BankDatabase info;
	private int accountNum;
	private JPanel inputPanel;
	private JTextField userInput;
	private int inputamount;
	private CashDispenser cashDispenser;
	Keyhandler keyHandler = new Keyhandler();
	public int amount = 0;
	public boolean exiting = true;
	
	JLabel[] exitLabel;
	JPanel exitMessagePanel;
	
	private static final String[] labelNames = {"<html>Please input the amount<br>(*Only available for multiple of HKD100)</html>",
			 "" , "", "","","",""};
    
	
	private  String[] names = { "", "", "",
            "", "", "Cancel transction",
            "", ""};
      
    private JLabel[] label = new JLabel[labelNames.length];
	
	private static final String[] withdrawalLabelNames = {"", "", "",    //26-33line is making component for comfirmation panel
													"", "", "",
													"", "", "", 
															};
	private static final String[] withrawalButtonNames = {"Yes", "", "No"};
	private JPanel withdrawalMessagePanel;
	private JLabel[] withdrawalLabel = new JLabel[withdrawalLabelNames.length];
	private JButton[] withdrawalButtons = new JButton[withrawalButtonNames.length];
    
	
	private int row = 5, col = 5;
	
	public WithdrawalOtherAmountUI (JPanel bPanel, int userAccountNumber, BankDatabase atmBankDatabase, 
            CashDispenser atmCashDispenser )
	{
			 cashDispenser = atmCashDispenser;
			 basePanel = bPanel;
			 withdrawal = new Withdrawal (userAccountNumber, atmBankDatabase, 
      			 	 					  atmCashDispenser);
			 accountNum= withdrawal.getAccountNumber();
		     info= atmBankDatabase;
		
		     inputPanel = new JPanel();
		     inputPanel.setLayout(new GridLayout(row, col, 10, 10));	
			
			JButton[] buttons = new JButton[names.length];
	        for(int i = 0; i < names.length; i++) {
	        	
	        	if(!(i==4)){          						//free space for label                 
	                buttons[i] = new JButton(names[i]);   	//create button 
	                WithdrawalKeyHandler handler = new WithdrawalKeyHandler();		 //create handler
	                buttons[i].addActionListener(handler);  //add handler into buttons 
	                inputPanel.add(buttons[i]);   			//add buttons into panel
	                if( buttons[i].getText() == ""){   		//do not display the buttons that are not using
	                    buttons[i].setVisible(false);
	                } 
	               }
	        	else
	        	{
	        		for(int j = 0; j < labelNames.length; j++) {
	        			label[j] = new JLabel(labelNames[j]);
	        			label[j].setVerticalAlignment(SwingConstants.BOTTOM);
	        			inputPanel.add(label[j]);
	        		}
	    			userInput = new JTextField(); 
	    			userInput.addKeyListener(keyHandler);
	    			inputPanel.add(userInput);
	        	}
	        }
			
	        basePanel.add(inputPanel);   
		    basePanel.revalidate();
			
	}
	
	private class Keyhandler extends KeyAdapter
	{
		public void keyTyped( KeyEvent event) 
		{
			if(KeyEvent.getKeyText(event.getKeyChar()) == "Enter" )
			{
				inputamount = Integer.parseInt(userInput.getText());
				
				if (inputamount >= 0) 
				{
					if (inputamount %100 ==0 && inputamount >= 0 && inputamount > 0) 
					{
		            	amount = inputamount;
		            	confirmation();
		            	
					}
					else
					{
						//JOptionPane.showMessageDialog(null, "Sorry. Only the multiples of HKD100 are allowed. Please try again.");
						label[3].setText("<html>Sorry. Only the multiples of HKD100 are allowed.<br>Please try again.</html>");
						userInput.setText("");
						//basePanel.remove(inputPanel);   
			            //basePanel.repaint();
			            //WithdrawalOtherAmountUI inputPanel = new WithdrawalOtherAmountUI(basePanel, accountNum, info, 
							//	 														 cashDispenser );
					}
				}
			}
		}
	}
	
	
	private class WithdrawalKeyHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent event ) {      
	        String nextUI = event.getActionCommand();      
	        switch (nextUI) {                                   
	            case "Cancel transction" :
	            basePanel.remove(inputPanel);    
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
				basePanel.add(exitMessagePanel);
	            basePanel.repaint();
				basePanel.revalidate();
	            Timer timer2 = new Timer();   //create time object
				timer2.schedule(new TakeCardTask(), 1000);   //after 1 seconds, "cancelling transaction" will change to thank you message and then return to login page.
				timer2.schedule(new CancelTask(), 4000);											
	            break;
	            }
	        }

	}
	/*public void confirmation() 
	{
		int confirmation = JOptionPane.showConfirmDialog(null, "You are going to withdrawal $" + amount, "Confirmation",JOptionPane.YES_NO_OPTION);
		if (confirmation == 0)
		{
			execute();
			if (exiting == true)
			exit();
		}
		
		else
		{
			basePanel.remove(inputPanel);   
			   basePanel.repaint();
			   WithdrawalOtherAmountUI inputPanel = new WithdrawalOtherAmountUI(basePanel, accountNum, info, 
						 cashDispenser ); 
		}
   
	}
	*/
	
	public void confirmation() 
	{
		label[3].setText("");
		userInput.setText("");
		basePanel.remove(inputPanel);
		basePanel.repaint();
		basePanel.revalidate();
		withdrawalMessagePanel = new JPanel();   //create panel
		withdrawalMessagePanel.setLayout(new GridLayout(4, 3, 0, 0));   
		for(int i = 0; i < withdrawalLabelNames.length; i++) {   //create label
			if(i == 4)
				withdrawalLabel[i] = new JLabel("Do you want to withdraw HKD"+amount+"?");
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
				basePanel.add(inputPanel);
				basePanel.repaint();
				basePanel.revalidate();
			}
		}
	}
	
	public void execute()
	{
		
		boolean cashDispensed = false;
	    double availableBalance;
	      BankDatabase bankDatabase = withdrawal.getBankDatabase(); 
	      
	     
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
					JOptionPane.showMessageDialog( null, "\nInsufficient cash available in the ATM." +
						"\n\nPlease choose a smaller amount.");
					basePanel.remove(inputPanel);
					basePanel.remove(withdrawalMessagePanel);
					basePanel.repaint();
					WithdrawalOtherAmountUI inputPanel = new WithdrawalOtherAmountUI(basePanel, accountNum, info, 
						 cashDispenser );
					exiting = false;
				}
	        }
			else
			{
				JOptionPane.showMessageDialog(null, "\nInsufficient funds in your account." +
	                   "\n\nPlease choose a smaller amount.");
				basePanel.remove(inputPanel);
				basePanel.remove(withdrawalMessagePanel);
				basePanel.repaint();
				WithdrawalOtherAmountUI inputPanel = new WithdrawalOtherAmountUI(basePanel, accountNum, info, 
							cashDispenser );
				exiting = false;
			}
		}
	}
	public void exit()
	{
		Timer timer = new Timer();   //create time object
		timer.schedule(new ChangeTakeTextTask(), 3000);   //after 1 seconds, "cancelling transaction" will change to thank you message and then return to login page.
		timer.schedule(new ExitWithdrawalMessageTask(), 6000);   //
		
		
		/*
		int exit = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Exit",JOptionPane.YES_NO_OPTION);
		   if(exit == 0)   
			{   
			   JOptionPane.showMessageDialog(null, "\nPlease take your card now.");
			   JOptionPane.showMessageDialog(null, "\nPlease take your cash now.");
			   JOptionPane.showMessageDialog(null, "\nThank you! Bye bye!");
			   basePanel.remove(inputPanel);   
			   basePanel.repaint();
			   returnToLogin(info, basePanel, cashDispenser);
		    }
		   else 
		   {
			   JOptionPane.showMessageDialog(null, "\nPlease take your cash now."); 
			   basePanel.remove(inputPanel);   
			   basePanel.repaint();
			   ATMMainMenu(basePanel,accountNum, info); 
		   }*/
	} 
	
	public class TakeCardTask extends TimerTask {   //this task executed when user choose cancel transaction at the first withdraw panel.
		public void run() {
			exitLabel[4].setText("<html>Please take the card!<br>Thank You!</html>");
		}
	}
	public class CancelTask extends TimerTask {   //exit to login page
		public void run() {
			basePanel.remove(exitMessagePanel);
			basePanel.repaint();
			returnToLogin(info, basePanel, cashDispenser);
		}		
	}
	
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
} 
	
	

