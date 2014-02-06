import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: MW
 * Date: 15/01/14
 * Time: 16:49
 * To change this template use File | Settings | File Templates.
 * http://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
 */
public class MainApp extends JFrame
{




    public MainApp ()
    {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Choose a Directory");
        this.getContentPane().add(fc);

        fc.setVisible(true);

    }


    public static void main (String [] args)
    {
        JFrame frame = new MainApp();
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(300,300);
        frame.pack();
        frame.setVisible(true);

    }

}
