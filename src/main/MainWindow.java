package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JButton btnNewButton;
	private JLabel lblNewLabel;
	private Proxy proxy;
	private InternetProxy internetProxy;
	
	private boolean activated = false;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
				
		proxy = new Proxy();
		internetProxy = new InternetProxy();
		
		setTitle("Blablaland Hack");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 726, 598);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnNewButton = new JButton("Activer le proxy");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!activated){
					activated = true;
					btnNewButton.setText("Désactiver le proxy");
					lblNewLabel.setForeground(Color.GREEN);
					lblNewLabel.setText("Proxy activé");
					
					try {
						internetProxy.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//proxy.start();
					
				}else{
					activated = false;
					btnNewButton.setText("Activer le proxy");
					lblNewLabel.setForeground(Color.RED);
					lblNewLabel.setText("Proxy désactivé");
					
					internetProxy.stop();
					
				}
				
			}
		});
		btnNewButton.setBounds(10, 11, 141, 38);
		contentPane.add(btnNewButton);
		
		lblNewLabel = new JLabel("Proxy d\u00E9sactiv\u00E9");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(161, 20, 102, 20);
		contentPane.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 60, 690, 466);
		contentPane.add(panel);
	}
}
