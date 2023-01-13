import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class DeleteItem extends JFrame {
	
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	public JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeleteItem frame = new DeleteItem();
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
	public DeleteItem() {
		setTitle("Copy & Paste App");
		
		Connection conn = MyConnection.getConnection();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 247, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 231, 335);
		contentPane.add(panel);
		panel.setLayout(null);
		
		
		JLabel lblNewLabel = new JLabel("Select Item to delete:");
		lblNewLabel.setBounds(27, 63, 129, 14);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Delete Item");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(86, 11, 98, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("←Back");
		lblNewLabel_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new PasteItem().setVisible(true);
				dispose();

			}
		});
		lblNewLabel_2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(10, 310, 46, 14);
		panel.add(lblNewLabel_2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(53, 88, 113, 154);
		panel.add(scrollPane);
		
		
		
		JList list = new JList();
		list.setBorder(new MatteBorder(1, 1, 2, 1, (Color) new Color(0, 0, 0)));
		list.setBackground(new Color(248, 248, 255));
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		String query = "SELECT * FROM sentence";
		
		try {
			DefaultListModel model = new DefaultListModel();
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			while(rs.next()) {
				String newIncidents = rs.getString("sentence_id");
				model.addElement(newIncidents);
			}
			list.setModel(model);
		}catch (SQLException e1) {
			e1.printStackTrace();
		}
		scrollPane.setViewportView(list);
		
		JButton btnNewButton = new JButton("Delete");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				String Sel = (String) list.getSelectedValue();
				String delete1 = "DELETE FROM sentence WHERE sentence_id = ?";
				String refresh = "SELECT * FROM sentence";
				
				try {
					DefaultListModel model = new DefaultListModel();
					pst = conn.prepareStatement(delete1);
					pst.setString(1, Sel);
					pst.executeUpdate();
					
					rs = pst.executeQuery(refresh);
					
					while(rs.next()) {
						JOptionPane.showMessageDialog(null, "Item was deleted!");
						
							String newItem = rs.getString("sentence_id");
							model.addElement(newItem);				
						}
						list.setModel(model);		
					}				
				catch(SQLException e2) {
					JOptionPane.showMessageDialog(null, e2);
					
				}
			}
		});
		btnNewButton.setBounds(63, 253, 89, 23);
		panel.add(btnNewButton);
		
/*		JButton btnNewButton_1 = new JButton("Delete All");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String delete = "DELETE FROM sentence WHERE sentence_id > 0";
				
				try {
					pst = conn.prepareStatement(delete);		
					pst.executeUpdate();
					
					rs = pst.executeUpdate();
					
					while(k==2) {
						JOptionPane.showMessageDialog(null, "All Data Succesfully deleted!");
						}
					}
					catch(SQLException e3) {
						JOptionPane.showMessageDialog(null, e3);
					}
			}
		});
		btnNewButton_1.setBounds(66, 289, 89, 23);
		panel.add(btnNewButton_1);*/
	}
}
