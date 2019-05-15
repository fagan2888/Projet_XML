package ui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.GUIController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;

public class GUI extends JFrame {
	private final JLabel lblFileChooser = new JLabel("Select your file .xml");
	private final JButton btnFileChooser = new JButton("No file selected");
	private final JButton btnSubmit = new JButton("Submit");

	private final JFileChooser chooser = new JFileChooser();
	
	private final GUIController controller;
	public GUI(GUIController controller) {
		this.controller = controller;
		
		buildComponent();
	}
	
	public void buildComponent() {
		this.getContentPane().removeAll();
		getContentPane().add(lblFileChooser, BorderLayout.WEST);
		getContentPane().add(btnFileChooser, BorderLayout.EAST);
		getContentPane().add(btnSubmit, BorderLayout.SOUTH);
		
		btnFileChooser.setPreferredSize(new Dimension(600,30));
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML File", "xml");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(new File("").getAbsoluteFile());
		
		btnFileChooser.addActionListener(new ActionListener()
	    {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = chooser.showOpenDialog(btnFileChooser);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	File file = chooser.getSelectedFile();
			    	if(file.exists()) {
			    		btnFileChooser.setText(file.getPath());
			    		controller.notifySetFile(file);
			    	}
			    }
			}
	    });
		
		btnSubmit.addActionListener(new ActionListener()
	    {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controller.notifySendQuery();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    });
		
	    
	    
	    

		this.setTitle("XML Ask");
	    this.pack();
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setVisible(true);
	}

}
