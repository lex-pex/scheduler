package scheduler_2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.swing.JComboBox;

public class TaskWriter implements Runnable {
    private final Map<String, Task> store;
    JComboBox combo;
    String title;
    File folder;
    
    public TaskWriter(Map <String, Task> store, JComboBox combo, File folder) {
        this.store = store; this.combo = combo; this.folder = folder;
    }
    
    @Override
    public void run() {
        if (!folder.exists()) folder.mkdir();
        for (int i = 0; i < combo.getItemCount(); i ++) {
            String taskName = (String) combo.getItemAt(i); // combo.getSelectedItem(); 
            String file = taskName + ".txt";
            String path = folder.getPath();
            writeFile(new File(path + "/" + file), taskName);
        }
    }
    
    public void writeFile(File file, String taskName) {
        try (PrintWriter out = new PrintWriter(file, "UTF-8")) 
        {
            String [] lines = store.get(taskName).getText().split("\n");
            for (String line : lines)
                out.println(line);
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println(ex);
        }
        /* todo */
        try (OutputStream out = new FileOutputStream(file)) {
            byte [] array =  store.get(taskName).getText().getBytes();
            out.write(array);
            out.flush();
        } catch (IOException exc) {System.out.println(exc);}
        
    }       
}
