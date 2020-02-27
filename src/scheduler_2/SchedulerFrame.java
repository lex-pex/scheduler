package scheduler_2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class SchedulerFrame extends JFrame {
    private JComboBox combo;
    private JComboBox foldersCombo;
    private JTextField entryField;
    private final JPanel buttonPane;
    private JTextArea textArea;
    private final JScrollPane scroll;
    private Map<String, Task> store;
    private String newTitle;
    private String newText;
    private final Font font;
    private final File startFolder;
    private volatile File saveFolder;

    private final JButton folderButton;
    private final JButton rmButton;
    
    Color backColor;
    Color textColor;
    
    public SchedulerFrame() {
        
        store = new HashMap<>();
        font = new Font("", 15, 15);
                
        backColor = new Color(140, 140, 140);
        textColor = new Color(30, 30, 30);
        
        foldersCombo = new JComboBox();
        foldersCombo.setBackground(backColor);
        foldersCombo.setForeground(textColor);
        foldersCombo.setFont(font);
        foldersCombo.setPreferredSize(new Dimension(180, 25));
        foldersCombo.setEditable(true);
        
        startFolder = new File("../TASK");
//        startFolder = new File("/home/lexis/Dropbox/scheduler/Scheduler_2");
        
        if (!startFolder.exists()) startFolder.mkdir();

        File [] files = startFolder.listFiles();
        
        for (File f : files) {
            if (f.isDirectory()) {
                foldersCombo.addItem("" + f.getName());
            }
        }
        
        saveFolder = new File("../TASK/" + foldersCombo.getSelectedItem());
        
        textArea = new JTextArea();
        textArea.setBackground(textColor);
        textArea.setForeground(backColor);
        textArea.setCaretColor(Color.GREEN);
        textArea.setFont(font);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.addFocusListener(new FocusAdapter(){
            public void focusLost(FocusEvent e) {
                pushInMap(true);
            }
            
        });
        scroll = new JScrollPane(textArea);
        
        entryField = new JTextField(15);
        entryField.setBackground(backColor);
        entryField.setForeground(textColor);
        entryField.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    textArea.setText("");
                    textArea.requestFocusInWindow();
                    pushInMap(false);
                }
            }
        });
        
        entryField.setFont(font);
        entryField.setSize(new Dimension(300, 30));
        
        // combo features 
    
        combo = new JComboBox();
        combo.setBackground(backColor);
        combo.setForeground(textColor);
        combo.setFont(font);
        combo.setPreferredSize(new Dimension(220, 25));
        combo.setEditable(true);

        combo.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (combo.getSelectedItem() != null) {
                    String select = (String) combo.getSelectedItem();
                    entryField.setText("");
                
                    if (store.containsKey(select)) {
                        textArea.setText(store.get(select).getText());
                        textArea.setCaretPosition(store.get(select).getCaretPosition());
                    } // else textArea.setText("");
                }
                textArea.requestFocusInWindow();
            }
        });
        
        foldersCombo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                writePath(false);    // boolean togglePath
                readPath();
            }
        });
        
        rmButton = new JButton("Rem");
        rmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (combo.getSelectedItem() != null) {
                    String delElement = (String)combo.getSelectedItem();
                    combo.removeItem(delElement);
                    if (store.containsKey(delElement))
                        store.remove(delElement);
                    if (!store.isEmpty())
                        textArea.setText(store.get((String)combo.getSelectedItem()).getText());
                    File delFile = new File(saveFolder.getPath() + "/" + delElement + ".txt");
                    if (delFile.exists()) delFile.delete();
                }
            }
        });
        
        folderButton = new JButton("New");
        folderButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    String folder = 
                            JOptionPane.showInputDialog("Enter name-folder");
                
                    if (!folder.trim().equals("")) {
                        new File("../TASK/" + folder).mkdir();
                        combo.removeAllItems();
                        store.clear();
                        foldersCombo.addItem(folder);
                        foldersCombo.setSelectedItem(folder);
                        entryField.requestFocusInWindow();
                    }
                
                } catch (NullPointerException exc) { }
            }
        });
        
        FlowLayout layout = new FlowLayout(); 
        layout.setAlignment(FlowLayout.LEFT); 
        
        buttonPane = new JPanel();
        buttonPane.setLayout(layout);
        buttonPane.add(entryField);
        buttonPane.add(combo);
        buttonPane.add(rmButton);
        buttonPane.add(foldersCombo);
        buttonPane.add(folderButton);

        add(buttonPane, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        
        readPath();
    }
        
    public void pushInMap(boolean folderEvent) {
        newTitle = entryField.getText();
        newText = textArea.getText();
        if (!newTitle.trim().equals("") && !store.containsKey(newTitle)) {
            combo.addItem(newTitle);
            combo.setSelectedItem(newTitle);
            store.put(newTitle, new Task(0, newText));
        } else if (combo.getSelectedItem() != null) {
            newTitle = (String) combo.getSelectedItem();
            store.put(newTitle, new Task(0, newText));
        } else if (!folderEvent)
        JOptionPane.showMessageDialog(SchedulerFrame.this, "Enter title");
    }
    
    public void readPath() {
        saveFolder = new File("../TASK/" + foldersCombo.getSelectedItem());
        combo.removeAllItems();
        store.clear();
        TaskReader r = new TaskReader(store, combo, saveFolder);
        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
            System.out.println("Join in readPath");
        }
        Object item = combo.getSelectedItem();
        if (item != null) {
            textArea.setText(store.get((String)item).getText());
        }
        else {
            textArea.setText("");
            entryField.requestFocusInWindow();
        }
    }
    
    public void writePath(boolean togglePath) {
        if (togglePath)
        saveFolder = new File("../TASK/" + foldersCombo.getSelectedItem());
        TaskWriter w = new TaskWriter(store, combo, saveFolder);
        Thread t = new Thread(w);
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
            System.out.println("Join in writePath");
        }
    }
    
}




















