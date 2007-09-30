/*
 * ShortRibsView.java
 */

package shortribs;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * The application's main frame.
 */
public class ShortRibsView extends FrameView {

    
    @SuppressWarnings("unchecked")
    public ShortRibsView(SingleFrameApplication app) {
        super(app);
        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        
        //Read XML Files
        try
        {
        File DataFolder = new File(System.getProperty("user.dir") + "\\data");
        File[] FileList = DataFolder.listFiles();
        ArrayList FunctionArrayList = new ArrayList();
        ArrayList FibArrayList = new ArrayList();
        ArrayList SortedFunctionArrayList = new ArrayList();
        ArrayList SortedFibArrayList = new ArrayList();
        
        //Now we start sorting the files since they will come in out of order
        //First we get the file names split and put in the right arraylist
        for (int i = 0; i < FileList.length; i++ )
        {
            StringTokenizer FileNameTokenizer = new StringTokenizer(FileList[i].getName(), ".");
            
            FunctionArrayList.add(FileNameTokenizer.nextToken());
            FibArrayList.add(FileNameTokenizer.nextToken());     
        }
        
        //Now we sort the function list
        for (int i = 0; i < FunctionArrayList.size(); i++)
        {
            String FunctionToTest = FunctionArrayList.get(i).toString();
            int WhereIsItSmaller = 0;
            boolean IsFinallySmaller = false ;
            
            for (int n = 0; IsFinallySmaller == false & n < SortedFunctionArrayList.size(); n++)
            {
                if (Integer.parseInt(FunctionToTest) < Integer.parseInt(SortedFunctionArrayList.get(n).toString()))
                {
                    IsFinallySmaller = true;
                    WhereIsItSmaller = n;
                }
            }
            
            if (IsFinallySmaller)
            {
                SortedFunctionArrayList.add(WhereIsItSmaller,FunctionToTest);
            }else{
                SortedFunctionArrayList.add(FunctionToTest);
            }
        }
        
        for (int i = 0; i < FibArrayList.size(); i++)
        {
            String FibToTest = FibArrayList.get(i).toString();
            int WhereIsItSmaller = 0;
            boolean IsFinallySmaller = false ;
            
            for (int n = 0; IsFinallySmaller == false & n < SortedFibArrayList.size(); n++)
            {
                if (Integer.parseInt(FibToTest) < Integer.parseInt(SortedFibArrayList.get(n).toString()))
                {
                    IsFinallySmaller = true;
                    WhereIsItSmaller = n;
                }
            }
            
            if (IsFinallySmaller)
            {
                SortedFibArrayList.add(WhereIsItSmaller,FibToTest);
            }else{
                SortedFibArrayList.add(FibToTest);
            }
        }
        String FileOutputString = "";
         //Testing our sorted list
    for (int i = 0; i < SortedFunctionArrayList.size(); i++)
    {
        FileOutputString = FileOutputString.concat("F(" + SortedFunctionArrayList.get(i).toString());
        FileOutputString = FileOutputString.concat(") = ");
        FileOutputString = FileOutputString.concat(SortedFibArrayList.get(i).toString());
        FileOutputString = FileOutputString.concat("\n");
    }
        OutputTextArea.setText(FileOutputString);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        }

    
    
   

    @Action
    public void showAboutBox(ActionEvent e) {
        if (aboutBox == null) {
            JFrame mainFrame = ShortRibsApp.getApplication().getMainFrame();
            aboutBox = new ShortRibsAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ShortRibsApp.getApplication().show(aboutBox);
    }
    
    @Action
    public void showOptionsBox(ActionEvent e) {
        if (optionsBox == null) {
            JFrame mainFrame = ShortRibsApp.getApplication().getMainFrame();
            optionsBox = new ShortRibsOptionsBox();
            optionsBox.setLocationRelativeTo(mainFrame);
        }
        ShortRibsApp.getApplication().show(optionsBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        StartButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        OutputTextArea = new javax.swing.JTextArea();
        CurrentNumberLabel = new javax.swing.JLabel();
        CurrentNumberTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        TotalNumbersLabel = new javax.swing.JLabel();
        PrimesFoundLabel = new javax.swing.JLabel();
        PrimesFoundTextField = new javax.swing.JTextField();
        TotalNumbersTextField = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        optionsMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(shortribs.ShortRibsApp.class).getContext().getResourceMap(ShortRibsView.class);
        StartButton.setText(resourceMap.getString("StartButton.text")); // NOI18N
        StartButton.setName("StartButton"); // NOI18N
        StartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StartButtonMouseClicked(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        OutputTextArea.setColumns(20);
        OutputTextArea.setEditable(false);
        OutputTextArea.setRows(5);
        OutputTextArea.setName("OutputTextArea"); // NOI18N
        jScrollPane1.setViewportView(OutputTextArea);

        CurrentNumberLabel.setText(resourceMap.getString("CurrentNumberLabel.text")); // NOI18N
        CurrentNumberLabel.setName("CurrentNumberLabel"); // NOI18N

        CurrentNumberTextField.setEditable(false);
        CurrentNumberTextField.setName("jTextField1"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        TotalNumbersLabel.setText(resourceMap.getString("TotalNumbersLabel.text")); // NOI18N
        TotalNumbersLabel.setName("TotalNumbersLabel"); // NOI18N

        PrimesFoundLabel.setText(resourceMap.getString("PrimesFoundLabel.text")); // NOI18N
        PrimesFoundLabel.setName("PrimesFoundLabel"); // NOI18N

        PrimesFoundTextField.setEditable(false);
        PrimesFoundTextField.setText(resourceMap.getString("PrimesFoundTextField.text")); // NOI18N
        PrimesFoundTextField.setName("PrimesFoundTextField"); // NOI18N

        TotalNumbersTextField.setEditable(false);
        TotalNumbersTextField.setName("jTextField1"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(TotalNumbersLabel)
                    .add(PrimesFoundLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, PrimesFoundTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .add(TotalNumbersTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(TotalNumbersTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(TotalNumbersLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(PrimesFoundTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(PrimesFoundLabel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(CurrentNumberTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                    .add(CurrentNumberLabel)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                    .add(jLabel4)
                    .add(StartButton))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(CurrentNumberLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(CurrentNumberTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(StartButton)
                .add(139, 139, 139))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(shortribs.ShortRibsApp.class).getContext().getActionMap(ShortRibsView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        toolsMenu.setText(resourceMap.getString("toolsMenu.text")); // NOI18N
        toolsMenu.setName("toolsMenu"); // NOI18N

        optionsMenuItem.setAction(actionMap.get("showOptionsBox")); // NOI18N
        optionsMenuItem.setName("optionsMenuItem"); // NOI18N
        optionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(optionsMenuItem);

        menuBar.add(toolsMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 286, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(statusMessageLabel)
                    .add(statusAnimationLabel)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void StartButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StartButtonMouseClicked
// TODO add your handling code here
        FindPrime FindPrimeNumbersThread = new FindPrime(OutputTextArea,CurrentNumberTextField,TotalNumbersTextField,PrimesFoundTextField);
        FindPrimeNumbersThread.setPriority(1);
        FindPrimeNumbersThread.start();
        busyIconTimer.start();
        StartButton.setEnabled(false);
        
}//GEN-LAST:event_StartButtonMouseClicked

private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_exitMenuItemActionPerformed

private void optionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsMenuItemActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_optionsMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CurrentNumberLabel;
    private javax.swing.JTextField CurrentNumberTextField;
    private javax.swing.JTextArea OutputTextArea;
    private javax.swing.JLabel PrimesFoundLabel;
    private javax.swing.JTextField PrimesFoundTextField;
    private javax.swing.JButton StartButton;
    private javax.swing.JLabel TotalNumbersLabel;
    private javax.swing.JTextField TotalNumbersTextField;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem optionsMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenu toolsMenu;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private JDialog optionsBox;
}
