package drainundrain;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Editor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea = new JTextArea(20, 50);
	private JFileChooser fileChooser = new JFileChooser();
	
	private static final int EDIT_ROWS = 20;
	private static final int EDIT_COLS = 40;
	private static final int NUM_CHARS = 15;
	
	JTextArea editor = new JTextArea(EDIT_ROWS, EDIT_COLS);
	JTextField searchField = new JTextField (NUM_CHARS);
	JCheckBox searchCaseSensitiveBox = new JCheckBox("Case sensitive", true);
	JCheckBox reverseSearchBox = new JCheckBox ("Reverse search", false);
	
	public Editor() {
		
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		FileFilter xmlFile = new FileNameExtensionFilter("XML Files", "xml");
		fileChooser.setFileFilter(xmlFile);
		
		add(scrollPane);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu jMenuFile = new JMenu("File");
		JMenu jMenuOperation = new JMenu("Operation");
		JMenu jMenuDC = new JMenu("Data Center");
		
		menuBar.add(jMenuFile);
		menuBar.add(jMenuDC);
		menuBar.add(jMenuOperation);
		jMenuFile.add(Open);
		jMenuFile.add(Save);
		jMenuFile.addSeparator();
		jMenuFile.add(Exit);
		jMenuDC.add(Southwest);
		jMenuDC.add(Georgetown);
		jMenuOperation.add(Drain);
		jMenuOperation.add(Undrain);
	    jMenuOperation.add(Search);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setTitle("Drain and Undrain Tool.");
		setVisible(true);
	}
	
	Action Southwest = new AbstractAction("Southwest/Roanoke") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			JButton jButton = new JButton("Southwest");
	        jButton.setBounds(60, 80, 150, 20);
		}
	};
	
	Action Georgetown = new AbstractAction("Georgetown") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
				
		}
	};
	
	Action Drain = new AbstractAction("Drain") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
                File file = new File("config/plugin-cfg.xml");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = "", oldtext = "";
                while((line = reader.readLine()) != null) {
                    oldtext += line + "\r\n";
                }
                reader.close();
                String newtext = oldtext.replaceAll("LoadBalanceWeight=\"2\"", "LoadBalanceWeight=\"0\"");
           
                FileWriter writer = new FileWriter("config/plugin-cfg.xml.drained");
                writer.write(newtext);writer.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
		}
	};
	
	Action Undrain = new AbstractAction("Undrain") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
                File file = new File("config/plugin-cfg.xml");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = "", oldtext = "";
                while((line = reader.readLine()) != null) {
                    oldtext += line + "\r\n";
                }
                reader.close();
                String newtext = oldtext.replaceAll("LoadBalanceWeight=\"0\"", "LoadBalanceWeight=\"2\"");
           
                FileWriter writer = new FileWriter("config/plugin-cfg.xml.undrained");
                writer.write(newtext);writer.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }	
		}
	};
	
	Action Search = new AbstractAction("Search") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			String editorText = editor.getText();			
			String searchValue = searchField.getText();
			System.out.println(searchValue);
			
			if (searchCaseSensitiveBox.getSelectedObjects() == null) {
				editorText = editorText.toLowerCase();
				searchValue = searchValue.toLowerCase();
			}
			
			int start;
			if (reverseSearchBox.getSelectedObjects() == null) {
				start = editorText.indexOf(searchValue, editor.getSelectionEnd());
			}
			else {
				start = editorText.lastIndexOf(searchValue, editor.getSelectionStart()-1);
			}
			
			if (start != -1) {
				editor.setCaretPosition (start);
				editor.moveCaretPosition (start + searchValue.length());
				editor.getCaret().setSelectionVisible(true);
			}
		}
	};
	
	Action Open = new AbstractAction("Open plugin-cfg.xml File") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				openFile(fileChooser.getSelectedFile().getAbsolutePath());
			}	
		}
	};
	
	Action Save = new AbstractAction("Save File") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			saveFile();
		}	
	};
	
	Action Exit = new AbstractAction("Exit") {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			exitTool();
			
		}
	};
	
	private void exitTool() {
		System.exit(0);
		
	}
	
	public void openFile(String fileName) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(fileName);
			textArea.read(fileReader, null);
			fileReader.close();
			setTitle(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveFile() {
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			FileWriter fileWriter = null;
		    try {
		    	fileWriter = new FileWriter(fileChooser.getSelectedFile().getAbsolutePath() + ".xml");
		    	textArea.write(fileWriter);
		    	fileWriter.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		}
	}
	
	public static void main(String[] args) {
		new Editor();
	}
}
