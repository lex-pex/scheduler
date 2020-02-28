package scheduler_2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

public class TaskReader implements Runnable {
    private final Map<String, Task> store;
    private final JComboBox combo;
    File startFolder;

    public TaskReader(Map<String, Task> store, JComboBox combo, File folder) {
        this.combo = combo; this.store = store; 
        this.startFolder = folder;
    }

    @Override
    public void run() {
        try {
            if (startFolder.isDirectory()) enumer(startFolder);
            else readFile(startFolder);
        } catch (NullPointerException exc) {
            chooser(true);  // files and directories 
        }
    }
    
    public void enumer(File start) {
        if (start.isDirectory()) { 
            File [] files = start.listFiles();
            if (files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) continue;   // enumer(file); // inner folder
                    String fileName = file.getName();
                    if (!fileName.endsWith("txt")) continue;
                    String taskName = fileName.substring(0, fileName.length() - 4);
                    if (store.containsKey(taskName)) continue;
                    else readFile(file);
                }
            } // else JOptionPane.showMessageDialog(combo, "Folder " + (start.getName()) + " empty");
        } // else readFile(start);  
    }
    
    public void readFile(File file) {
        try (Scanner in = new Scanner(new FileInputStream(file), "UTF-8")) {
            String fileName = file.getName();
            String taskName = fileName.substring(0, fileName.length() - 4);
            String line;
            StringBuilder text = new StringBuilder();
            while (in.hasNextLine()) {
                line = in.nextLine();
                text.append(line).append("\n");
            }
            combo.addItem(taskName);
            store.put(taskName, new Task(0, text.toString()));
        } catch (FileNotFoundException exc) { System.out.println(exc); }
    }
    
    public void chooser(boolean dir) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        if (dir)
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        else 
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showOpenDialog(combo);
        if (chooser.getSelectedFile() != null)enumer(chooser.getSelectedFile());
    }
}
