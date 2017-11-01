package zipinspector;

import java.awt.*;
import java.io.*;
import java.util.jar.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import newcomp.components.*;
import java.io.File;

public class Frame extends JFrame {
  JPanel contentPane;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  Mask mask1 = new MyMask();
  Mask mask2 = new MyMask();
  JButton jButton1 = new JButton();

  //Construct the frame
  public Frame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception {
    contentPane = (JPanel)this.getContentPane();
    jLabel1.setText("Filename");
    contentPane.setLayout(gridBagLayout1);
    this.setSize(new Dimension(400, 300));
    this.setTitle("Frame Title");
    jLabel2.setText("Dirname");
    mask1.setMask("********************************************************************************");
    mask1.setText("");
    mask2.setMask("********************************************************************************");
    mask2.setText("");
    jButton1.setText("Submit");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    contentPane.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    contentPane.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    contentPane.add(mask1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
    contentPane.add(mask2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 0, 0), 0, 0));
    contentPane.add(jButton1, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  void jButton1_actionPerformed(ActionEvent e) {
    System.out.println("Processing... please wait.");
    File dir = new File(this.mask2.getText().trim());
    if (dir.isDirectory() == false) {
      System.out.println("This is not a directory: " + dir.getName());
      return;
    }
    processFiles(dir.listFiles(filter));
  }

  private void inspectJarZip(File file) {
    JarFile jar = null;
    boolean printed = false;
    //System.out.println("Inspecting " + file.getName());
    try {
      jar = new JarFile(file);
      Enumeration ens = jar.entries();
      while (ens.hasMoreElements()) {
        String name = ((JarEntry) ens.nextElement()).getName();
        if (name.toLowerCase().indexOf(this.mask1.getText().trim().toLowerCase()) >=
            0) {
          if (printed == false) {
          printed = true;
          System.out.println("Inspecting " + file.getAbsolutePath());
          }
          System.out.println("\tname: " + name);
        } else {
        //System.out.println("\t---name: " + name);
        }
      }
    } catch (Exception ex) {
      //ex.printStackTrace();
      System.out.println("erred");
    }
  }

  private void processFiles(File[] files) {
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        processFiles(files[i].listFiles(filter));
      } else {
        inspectJarZip(files[i]);
      }
    }
  }

  FilterJar filter = new FilterJar();

  class FilterJar implements FileFilter {
    public boolean accept(File file) {
      if (file.isDirectory() || file.getName().toLowerCase().endsWith(".jar") ||
          file.getName().toLowerCase().endsWith(".zip")) {
        return true;
      }
      return false;
    }
  }
}