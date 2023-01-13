import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import java.awt.Color;

public class CopyToClipBoard {
	
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	public JFrame frmCopyPaste;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CopyToClipBoard window = new CopyToClipBoard();
					window.frmCopyPaste.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CopyToClipBoard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		Connection conn = MyConnection.getConnection();
		
		frmCopyPaste = new JFrame();
		frmCopyPaste.setTitle("Copy & Paste App");
		frmCopyPaste.setBounds(100, 100, 321, 398);
		frmCopyPaste.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCopyPaste.getContentPane().setLayout(null);
		frmCopyPaste.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 305, 359);
		frmCopyPaste.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Add Sentences:");
		lblNewLabel.setBounds(19, 36, 103, 14);
		panel.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(29, 65, 266, 124);
		panel.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setBorder(new MatteBorder(1, 1, 2, 1, (Color) new Color(0, 0, 0)));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea);
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String query = "INSERT INTO sentence (sentences) VALUE (?)";
				
				try {
					pst = conn.prepareStatement(query);
					pst.setString(1, textArea.getText());
					
					int k = pst.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(null, "Sentence Saved Succesfully！");
						
						textArea.setText("");
					}
				}
				catch(SQLException e1) {
					JOptionPane.showMessageDialog(null, e1);
					
				}
				
			}
		});
		btnNewButton.setBounds(186, 211, 89, 23);
		panel.add(btnNewButton);
		
		JLabel lblNewLabel_2 = new JLabel("Add Items");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(120, 11, 78, 14);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Manual Select→");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblNewLabel_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new PasteItem().setVisible(true);
				frmCopyPaste.dispose();
			}
		});
		lblNewLabel_3.setBounds(200, 334, 95, 14);
		panel.add(lblNewLabel_3);
		
		JButton btnNewButton_1 = new JButton("Copy");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String query1 = "SELECT * FROM sentence ORDER BY RAND() LIMIT 1";
				
				try {
					pst = conn.prepareStatement(query1);
					
					rs = pst.executeQuery();
					if(rs.next()) {
						
						Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
						StringSelection strsel = new StringSelection(rs.getString("sentences"));
						clip.setContents(strsel, strsel);
						JOptionPane.showMessageDialog(null, "Sentence was succesfully copied!");
					}		
				}
				catch(SQLException e2) {
					JOptionPane.showMessageDialog(null, e2);
					
				}
			}
		});
		btnNewButton_1.setBounds(95, 282, 113, 35);
		panel.add(btnNewButton_1);
		
		JLabel lblNewLabel_1 = new JLabel("Copy Random Sentence");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(79, 257, 140, 14);
		panel.add(lblNewLabel_1);
		

	}
}
