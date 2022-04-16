
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BalanceInquiryUI extends ATM
{   
        private JPanel basePanel;
        private JPanel viewBalancePanel;
        private Transaction balance;
        
        private int accountnNumber;
        private BankDatabase info;
        
        private JLabel displayTotal;
        private JLabel displayAvailable;
        private double total;
        private double available;
	private  String[] names = { "", "", "",
                                    "", "", "",
                                    "", "", "",
                                    "", "", "Return"};
	private int row = 4, col = 3;										
	
	public BalanceInquiryUI(JPanel bPanel,int accountNumber,BankDatabase atmDataBase) 
	{
		balance = new BalanceInquiry(accountNumber, atmDataBase);
		this.accountnNumber=accountNumber;
	        info=atmDataBase;
	        
	        basePanel = bPanel;
	        
	        total=info.getTotalBalance(accountNumber);
	        available=info.getAvailableBalance(accountNumber);
	        displayTotal = new JLabel("",JLabel.CENTER );
	        displayAvailable = new JLabel("",JLabel.CENTER );
	        
		viewBalancePanel = new JPanel();
		viewBalancePanel.setLayout(new GridLayout(row, col, 10, 10));
		
		JButton[] buttons = new JButton[names.length];
		 for(int i = 0; i < names.length; i++) {
                 if(i!=4 && i!=7){                           //free space for label
                 buttons[i] = new JButton(names[i]);   //create button 
                 Handler handler = new Handler();   //create handler
                 buttons[i].addActionListener(handler);   //add handler into buttons
                 viewBalancePanel.add(buttons[i]);   //add buttons into  panel
                 if( buttons[i].getText() == ""){   //do not display the buttons that are not using
                 buttons[i].setVisible(false);
                 } 
                 }
                 if(i==4){
                 displayTotal.setText("Total Balance: "+total);
                 displayTotal.setFont(new Font("Serif", Font.PLAIN, 16));
                 viewBalancePanel.add(displayTotal);
                 }
                 if(i==7){
                 displayAvailable.setText("Available Balance: "+available);
                 displayAvailable.setFont(new Font("Serif", Font.PLAIN, 16));
                 viewBalancePanel.add(displayAvailable);
                 }
               
                }
              
		basePanel.add(viewBalancePanel);   //add view balance pane into base panel
		basePanel.revalidate();
	}
	
	private class Handler implements ActionListener   // create Handler class
	{
	public void actionPerformed( ActionEvent event ) {		
		String nextUI = event.getActionCommand();	// get what user choose		
		switch (nextUI) {									
		case "Return" :
			basePanel.remove(viewBalancePanel);   
			basePanel.repaint();
			ATMMainMenu(basePanel,accountnNumber,info);
			break;
			}
		}
	}
	
}