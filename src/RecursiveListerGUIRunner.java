import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class RecursiveListerGUIRunner extends JFrame
{
    JFileChooser chooser = new JFileChooser();
    File selectedFile;
    Path file;

    JPanel mainPnl;
    JPanel subMain;

    JPanel titlePnl;
    JLabel titleLbl;

    JPanel recursiveFileListPnl;
    JPanel controlPnl;

    JTextArea recursiveFileListTA;
    JScrollPane listScroller;

    JButton startBtn;
    JButton quitBtn;

    public RecursiveListerGUIRunner() {
        mainPnl = new JPanel();
        mainPnl.setLayout(new GridLayout(2,1));

        createDisplayPanel();
        subMain = new JPanel();
        subMain.setLayout(new GridLayout(1, 1));
        subMain.setBorder(new TitledBorder(new EtchedBorder(), "List"));

        subMain.add(recursiveFileListPnl);

        mainPnl.add(subMain);

        createControlPanel();
        mainPnl.add(controlPnl);

        add(mainPnl);
        setSize(900,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createTitlePanel()
    {
        titlePnl = new JPanel();
        titleLbl = new JLabel("Recursive File Lister");
        titleLbl.setFont(new Font("Monospaced", Font.PLAIN, 20));
        titlePnl.add(titleLbl);
    }

    private void createDisplayPanel()
    {
        createTitlePanel();
        recursiveFileListPnl = new JPanel();
        recursiveFileListTA = new JTextArea(15,100);
        recursiveFileListTA.setFont(new Font("Monospaced", Font.PLAIN, 12));
        listScroller = new JScrollPane(recursiveFileListTA);

        recursiveFileListPnl.add(titlePnl);
        recursiveFileListPnl.add(listScroller);
    }

    private void createControlPanel()
    {
        controlPnl = new JPanel();
        controlPnl.setBorder(new TitledBorder(new EtchedBorder(), "Controls"));

        startBtn = new JButton("Start");
        controlPnl.add(startBtn);
        startBtn.addActionListener((ActiveEvent_ae) ->
        {
            recursiveFileListTA.setText("");

            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);

            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                file = selectedFile.toPath();
            }

            try {
                createList(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        quitBtn = new JButton("Quit");
        controlPnl.add(quitBtn);
        quitBtn.addActionListener((ActiveEvent_ae) ->
        {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?","Select an Option", JOptionPane.YES_NO_CANCEL_OPTION);
            if(input == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        });
    }

    public void createList(Path path) throws Exception
    {
        BasicFileAttributes attribute = Files.readAttributes(path, BasicFileAttributes.class);

        if(attribute.isDirectory())
        {
            DirectoryStream<Path> paths = Files.newDirectoryStream(path);
            recursiveFileListTA.append(path.getFileName() + "\n");

            for(Path subPath: paths)
            {
                createList(subPath);
            }
        }
        else
        {
            recursiveFileListTA.append(path.getFileName() + "\n");
        }
    }
}
