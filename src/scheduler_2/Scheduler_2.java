package scheduler_2;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Scheduler_2 {

    public static void main(String[] args) {
        Sound sound = new Sound();
        sound.openSound();
        
        try {
            UIManager.LookAndFeelInfo[] ex = UIManager.getInstalledLookAndFeels();

            for(int i = 0; i < ex.length; ++i) {
                UIManager.LookAndFeelInfo info = ex[i];
                if (info.getName().equalsIgnoreCase("Windows")) {
                    UIManager.setLookAndFeel(info.getClassName());
                }
                else if(info.getName().equalsIgnoreCase("Nimbus")) {
                    UIManager.setLookAndFeel(info.getClassName());
                }
            }
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | UnsupportedLookAndFeelException exc) {
            System.out.println(exc);
        }
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SchedulerFrame f = new SchedulerFrame();
                f.setSize(800, 350);
                iconochka(f);
                f.setTitle("Scheduler");
                f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                f.addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent e) {
                        f.pushInMap(true);
                        f.writePath(true);
                        sound.openSound();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            System.out.println("Sleep in Read Button!!!");
                        }
                        System.exit(0);
                    }
                });
                location(f);
                f.setVisible(true);
            }
        });
    }
    
    public static void iconochka(JFrame f) {
        URL aboutUrl = f.getClass().getResource("/img/notebook.png");
        Image img = Toolkit.getDefaultToolkit().getImage(aboutUrl);
        f.setIconImage(img);
    }
    
    public static void location(JFrame f) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        
        f.setLocation(screenWidth - f.getWidth(), screenHeight - f.getHeight() - 40);
    }
}
