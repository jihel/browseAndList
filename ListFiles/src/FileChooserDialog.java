import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class FileChooserDialog
{

    private ArrayList<Image> listImage;
    private final String DEFAULT_TEST_PATH = "/Users/MW/Documents/wkp/bash/getListOfFile/thumb";

    public static void main(String[] args)
    {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("JComboBox Test");
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Select Files");

        JTextField imgPath = new JTextField();
        imgPath.setSize(50, 20);
        imgPath.setMinimumSize(new Dimension(50, 20));
        imgPath.setText("relative/path/for/images");
        JLabel lblPath = new JLabel();
        lblPath.setText("relative path for out");

        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                ArrayList<Image> listImage = new ArrayList<Image>();
                //JFileChooser fileChooser = new JFileChooser();
                //get Main class ref
                FileChooserDialog fcd = new FileChooserDialog();

                //launch file chooser window
                JFileChooser fc = fcd.showFileChooser();

            }
        });

        frame.add(lblPath);
        frame.add(imgPath);
        frame.add(button);

        frame.pack();
        frame.setVisible(true);

        FileChooserDialog fcd = new FileChooserDialog();
        JFileChooser fc = fcd.showFileChooser();

        fc.setCurrentDirectory(new File("/Users/MW/Documents/wkp/bash/getListOfFile/thumb"));
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    private String[] extensions = new String[]{"jpg", "png", "jpeg", "cr2"};

    public JFileChooser showFileChooser()
    {
        JFileChooser fileChooser = new JFileChooser();

        JFileChooser fc = fileChooser;

        listImage = new ArrayList<Image>();
        fc.setCurrentDirectory(new File(DEFAULT_TEST_PATH));
        fc.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        javax.swing.filechooser.FileFilter jpegFilter = new ExtensionFileFilter(null, extensions);
        //fileChooser.addChoosableFileFilter(new ImageFilter());
        fileChooser.addChoosableFileFilter(jpegFilter);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            File[] selectedFile = fc.getSelectedFiles();
            File dir = fc.getCurrentDirectory();

            if (dir.isDirectory())
            {
                System.out.println(dir.getName() + " is a directory");

                File[] list = dir.listFiles();

                System.out.println(list.length + " number");
                        /*for (file in list)
                        {

                        }    */
                int ln = list.length;


                for (int i = 0; i < ln; i++)
                {
                    File f = list[i];
                    System.out.println(" name " + f.getName());
                    String path = f.getAbsolutePath().toLowerCase();
                    for (int j = 0; j < extensions.length; j++)
                    {
                        String extension = extensions[j];

                        System.out.println(j + " : " + extensions[j] + " filter: " + path.endsWith(extensions[j]));
                        if (path.endsWith(extensions[j]) && (path.charAt(path.length() - extension.length() - 1)) == '.')
                        {
                            listImage.add(new Image(f.getName()));
                        }
                    }


                }

                System.out.println(" listImage size " + listImage.size());
            }
            System.out.println("list of files " + selectedFile.length);

            try
            {
                buildOutFile(listImage);
            } catch (IOException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
          /*  for (Image i : listImage)
            {
                System.out.println("\t id " + i.id);
                System.out.println("\t name" + i.name);
            }   */

        }

        return fc;
    }


    private void buildOutFile(ArrayList<Image> listImage) throws IOException
    {

        System.out.println("build out file");

        JSONObject obj = new JSONObject();
        JSONArray imgs = new JSONArray();

        for (Image i : listImage)
        {
            System.out.println("\t id " + i.id);
            System.out.println("\t name" + i.name);
            imgs.add(i);


        }
        obj.put("images", imgs);

        StringWriter out = new StringWriter();
        StringWriter out2 = new StringWriter();

        try
        {
            imgs.writeJSONString(out);
            obj.writeJSONString(out2);
        } catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println(out.toString());
        System.out.println("obj");
        System.out.println(out2.toString());

        try
        {
            FileWriter fw = new FileWriter(DEFAULT_TEST_PATH+"/testImg.json");
            fw.write(imgs.toJSONString());
            fw.flush();
            fw.close();

            fw = new FileWriter(DEFAULT_TEST_PATH+"/testObj.json");
            fw.write(obj.toJSONString());
            fw.flush();
            fw.close();



        }       catch ( IOException e)
        {
            e.printStackTrace();
        }

    }
}

class Image implements JSONAware
{

    public String name;
    public String path = ".";
    public int id;
    public String desc = "";
    public static int inc;

    public Image(String n)
    {
        id = inc++;
        name = n;
    }


    @Override
    public String toJSONString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("{");

        sb.append(JSONObject.escape("id"));
        sb.append(":");
        sb.append(id);

        sb.append(",");

        sb.append(JSONObject.escape("imageName"));
        sb.append(":");
        sb.append("\"" + JSONObject.escape(name) + "\"");

        sb.append(",");

        sb.append(JSONObject.escape("desc"));
        sb.append(":");
        sb.append("\"" + JSONObject.escape(desc) + "\"");

        sb.append(",");

        sb.append(JSONObject.escape("path"));
        sb.append(":");
        sb.append("\"" + JSONObject.escape(path) + "\"");


        sb.append("}");

        return sb.toString();  //To change body of implemented methods use File | Settings | File Templates.
    }
}

class ImageFilter extends javax.swing.filechooser.FileFilter
{
    public boolean accept(File file)
    {
        String filename = file.getName().toLowerCase();
        String[] extensions = {"jpg", "png", "jpeg", "cr2"};

        for (int i = 0; i < extensions.length; i++)
        {
            String extension = extensions[i];

            System.out.println(i + " : " + extensions[i] + " filter: " + filename.endsWith(extensions[i]));
            if (filename.endsWith(extensions[i]) && (filename.charAt(filename.length() - extension.length() - 1)) == '.')
            {
                return true;
            }
        }
        return false;
    }

    public String getDescription()
    {
        return "*.jpg";
    }
}


class ExtensionFileFilter extends javax.swing.filechooser.FileFilter
{
    String description;

    String extensions[];

    public ExtensionFileFilter(String description, String extension)
    {
        this(description, new String[]{extension});
    }

    public ExtensionFileFilter(String description, String extensions[])
    {
        if (description == null)
        {
            this.description = extensions[0] + "{ " + extensions.length + "} ";
        } else
        {
            this.description = description;
        }
        this.extensions = (String[]) extensions.clone();
        toLower(this.extensions);
    }

    private void toLower(String array[])
    {
        for (int i = 0, n = array.length; i < n; i++)
        {
            array[i] = array[i].toLowerCase();
        }
    }

    public String getDescription()
    {
        return description;
    }

    public boolean accept(File file)
    {
        if (file.isDirectory())
        {
            return true;
        } else
        {
            String path = file.getAbsolutePath().toLowerCase();
            for (int i = 0, n = extensions.length; i < n; i++)
            {
                String extension = extensions[i];
                if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.'))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
