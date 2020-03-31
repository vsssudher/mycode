/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication11;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;
//import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;
import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.List;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.EventHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.out;
import static java.lang.reflect.Array.set;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Arrays.stream;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.sound.sampled.FloatControl;
import javax.swing.AbstractAction;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import static sun.audio.AudioPlayer.player;
import sun.audio.AudioStream;


/**
 *
 * @author sudheer and apoorva
 */

public class StreamPlayerGUI extends JFrame {
    JMenuItem menuitem1 = null,menuitem2 = null,menuitem3 = null;
          //String xyz=null;
    BasicPlayer player;
    BasicController control;
    JTable table;
    JScrollPane scrollPane,scrollPane1; 
    JPanel main,panelTree;
    JButton play,pause,next,prev,stop;//the three buttons
    JLabel  nowPlaying,timerlabel,timerlabel1;
    private JTextField filename = new JTextField();
    String allTitle,data,pathToMp3,title;
    int CurrentSelectedRow;
    ButtonListener bl;
    BasicListener b2;
    String songNamePlaying;
    JTree playList,library;
    DefaultMutableTreeNode PlayList,child;
    static DefaultMutableTreeNode node2,Library;
    JMenuItem mi;
    Container containerPane;
    boolean flag = true;
    boolean songNotExist=true;
    int prog,rownum;
    Timer timer,timer1;
   String xyzz = null;
   String b = null;
   Float FR;
   Long audioDurationInSeconds,lenSec=0L,ub,ui;
   int FS,valbar=0;
    int ib=1,timeInSeconds;
    int rowCount;
    static DefaultTableModel model;
    playListClassNew p;
    JMenuItem a[]=null;
    DefaultBoundedRangeModel modelbar = new DefaultBoundedRangeModel();
    JProgressBar bar = new JProgressBar(modelbar);
    JCheckBoxMenuItem repeatMenu,shuffleMenu;
     JMenu recentMenu;
    //Connection con;
    public StreamPlayerGUI() throws SQLException {
        player = new BasicPlayer();
        control = (BasicController) player;
        
        main = new JPanel();
        
        //FlowLayout layoutManager = new FlowLayout(FlowLayout.RIGHT);
       // FlowLayout layoutManager = new FlowLayout(FlowLayout.RIGHT);
        bl = new ButtonListener();
        
        b2 = new BasicListener();
        //create the table
        String[] columns = {"File", "Title","Artist","Album","Years","Comment","Genre"};
         model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
            
        //Database connection
        String query = "SELECT * FROM PROJECT.MUSICPLAYER";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement statement = con.prepareStatement(query);
        ResultSet Rs = statement.executeQuery();
        
                //Display of records
                while(Rs.next())
                {
                model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7)});
                }
         
          table.setModel(model);
         table.setAutoCreateRowSorter(true);
          //  TableColumnMana tcm = table.getColumnModel();
        //tcm.removeColumn( tcm.getColumn(0) );
        
        //Right click on table header
         JPopupMenu popHeader = new JPopupMenu();
           JCheckBoxMenuItem artistDisp = new JCheckBoxMenuItem("Artist");
           artistDisp.setSelected(true);
           JCheckBoxMenuItem albumDisp = new JCheckBoxMenuItem("Album");
           albumDisp.setSelected(true);
           JCheckBoxMenuItem commentDisp = new JCheckBoxMenuItem("Comment");
           commentDisp.setSelected(true);
           JCheckBoxMenuItem genreDisp = new JCheckBoxMenuItem("Genre");
           genreDisp.setSelected(true);
           JCheckBoxMenuItem yearDisp = new JCheckBoxMenuItem("Years");
           yearDisp.setSelected(true);
            
            artistDisp.addItemListener(new ItemListener() {
    @Override
    public void itemStateChanged(ItemEvent e) {
    
         
                
        if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
            //do something...
            table.getColumnModel().getColumn(2).setMaxWidth(50);
            table.getColumnModel().getColumn(2).setMinWidth(50);
        } else {//checkbox has been deselected
            //do something...
          table.getColumnModel().getColumn(2).setMinWidth(0);
table.getColumnModel().getColumn(2).setMaxWidth(0);
        }
    }
});
             albumDisp.addItemListener(new ItemListener() {
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
            //do something...
            table.getColumnModel().getColumn(3).setMaxWidth(50);
            table.getColumnModel().getColumn(3).setMinWidth(50);
        } else {//checkbox has been deselected
            //do something...
          table.getColumnModel().getColumn(3).setMinWidth(0);
table.getColumnModel().getColumn(3).setMaxWidth(0);
        };
    }
});
              commentDisp.addItemListener(new ItemListener() {
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
            //do something...
            table.getColumnModel().getColumn(5).setMaxWidth(50);
            table.getColumnModel().getColumn(5).setMinWidth(50);
        } else {//checkbox has been deselected
            //do something...
          table.getColumnModel().getColumn(5).setMinWidth(0);
table.getColumnModel().getColumn(5).setMaxWidth(0);
        };
    }
});
               genreDisp.addItemListener(new ItemListener() {
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
            //do something...
            table.getColumnModel().getColumn(6).setMaxWidth(50);
            table.getColumnModel().getColumn(6).setMinWidth(50);
        } else {//checkbox has been deselected
            //do something...
          table.getColumnModel().getColumn(6).setMinWidth(0);
table.getColumnModel().getColumn(6).setMaxWidth(0);
        };
    }
});
                yearDisp.addItemListener(new ItemListener() {
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
            //do something...
            table.getColumnModel().getColumn(4).setMaxWidth(50);
            table.getColumnModel().getColumn(4).setMinWidth(50);
        } else {//checkbox has been deselected
            //do something...
          table.getColumnModel().getColumn(4).setMinWidth(0);
table.getColumnModel().getColumn(4).setMaxWidth(0);
        };
    }
});
               popHeader.add(artistDisp);
               popHeader.add(albumDisp);
               popHeader.add(commentDisp);
               popHeader.add(genreDisp);
               popHeader.add(yearDisp);
           // popHeader.add(titleDisp);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        /*int col = table.columnAtPoint(e.getPoint());
        String name = table.getColumnName(col);
        System.out.println("Column index selected " + col + " " + name);*/
      
   
            if(SwingUtilities.isRightMouseButton(e))
               {
                   popHeader.show(e.getComponent(), e.getX(), e.getY());
               }
          /*   if(titleDisp.isSelected())
            {
                tcm.removeColumn(tcm.getColumn(1) );
            }*/
             
    }
});
        
        
        
       /*   
          private void handleMouseEvent(MouseEvent e) {
        canSelect = (e.getID() != MouseEvent.MOUSE_DRAGGED);
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            isDragging = false;
            pressEvent = e;
        } else if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
            if (!isDragging && pressEvent != null) {
                TransferHandler handler = getTransferHandler();
                handler.exportAsDrag(this, pressEvent, TransferHandler.COPY);
            }
            pressEvent = null;
            isDragging = true;
        } else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
            isDragging = false;
        }
    }
          */
          
          
          
          
          
          
          
          
          
          
          
            
            //PopUpMenu on Right Click
            JPopupMenu popup = new JPopupMenu();
            JMenuItem addingSong = new JMenuItem("Add a song");
            
            //Add song on right click
             addingSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addSong();
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
             });
            
            popup.add(addingSong);
            JMenuItem deletingSong = new JMenuItem("Delete a song");
            //Delete song on right click
                deletingSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteSong();
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
              
          });
            popup.add(deletingSong);
            
            JMenu addSongToPlaylist = new JMenu("Add song to playlist");
            //Delete song on right click
            
                    String query2 = "SELECT * FROM PROJECT.PLAYLIST";
       // Connection con = DatabaseConnection.getConnection();
        PreparedStatement statement2 = con.prepareStatement(query2);
        ResultSet Rs2 = statement2.executeQuery();
        
                //Display of records
            
                int i=0;
                String mm[] = new String[3];
           
                while(Rs2.next())
                {
                        //model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7)});
                      
                    System.out.println("javaapplication11.StreamPlayerGUI.<init>() dsfdsfsd");
                    
                    b = Rs2.getString(1);
                    String c = "menuitems"+i;
                    mm[i]=b;
                 
                   // addSongToPlaylist.add(mii);
                    i++;
                    
                   
                   
                }
                
                
                System.out.println("i val"+i);
              
                if(i==1)
                {
             menuitem1 = new JMenuItem(mm[0]);
             addSongToPlaylist.add(menuitem1);
                }
                if(i==2)
                {
                     menuitem1 = new JMenuItem(mm[0]);
              menuitem2 = new JMenuItem(mm[1]);
               addSongToPlaylist.add(menuitem1);
                addSongToPlaylist.add(menuitem2);
                }
                if(i==3)
                {
             menuitem1 = new JMenuItem(mm[0]);
             menuitem2 = new JMenuItem(mm[1]);
             menuitem3 = new JMenuItem(mm[2]);
              addSongToPlaylist.add(menuitem1);
               addSongToPlaylist.add(menuitem2);
                addSongToPlaylist.add(menuitem3);
                }
               
                
                
                
                if(i==1)
                {
                menuitem1.addActionListener(new ActionListener() {
                  @Override
      public void actionPerformed(ActionEvent ev) {
         // JMenuItem menuItem = (JMenuItem)ev.getSource();
          //System.out.println(".actionPerformed()"+menuItem.getText());
      
                try {
                    System.out.println("checking");
                  
                    
                    Connection con = DatabaseConnection.getConnection();
                    Statement stmt2 = con.createStatement();
                    String filePathTag = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
                    String titleTag = table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
                    String artistTag = table.getModel().getValueAt(table.getSelectedRow(), 2).toString();
                    String albumTag = table.getModel().getValueAt(table.getSelectedRow(), 3).toString();
                    String yearTag = table.getModel().getValueAt(table.getSelectedRow(), 4).toString();
                    int result = Integer.parseInt(yearTag);
                    String commentTag = table.getModel().getValueAt(table.getSelectedRow(), 5).toString();
                    String genreTag = table.getModel().getValueAt(table.getSelectedRow(), 6).toString();
                    
                    String xyz = menuitem1.getText().toUpperCase();
                    String sql1 = "INSERT INTO PROJECT."+xyz+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+filePathTag+"', '"+titleTag+"', '"+artistTag+"', '"+albumTag+"', '"+yearTag+"', '"+commentTag+"', '"+genreTag+"')";
                    System.out.println(".actionPerformed()"+sql1);
                     stmt2.executeUpdate(sql1);
                     TreePath tp = new TreePath(node2.getPath());
         playList.setSelectionPath(tp);
                    library.setSelectionPath(null);
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                            
      }
    });
             
    }
                
                
                
                 if(i==2)
                {
                menuitem1.addActionListener(new ActionListener() {
                  @Override
      public void actionPerformed(ActionEvent ev) {
         // JMenuItem menuItem = (JMenuItem)ev.getSource();
          //System.out.println(".actionPerformed()"+menuItem.getText());
      
                try {
                    System.out.println("checking");
                    //System.out.println(".actionPerformed() menu name"+mi.getText());
                   // System.out.println(".action hand" +ev.getActionCommand());
                    
                    Connection con = DatabaseConnection.getConnection();
                    Statement stmt2 = con.createStatement();
                    String filePathTag = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
                    String titleTag = table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
                    String artistTag = table.getModel().getValueAt(table.getSelectedRow(), 2).toString();
                    String albumTag = table.getModel().getValueAt(table.getSelectedRow(), 3).toString();
                    String yearTag = table.getModel().getValueAt(table.getSelectedRow(), 4).toString();
                    int result = Integer.parseInt(yearTag);
                    String commentTag = table.getModel().getValueAt(table.getSelectedRow(), 5).toString();
                    String genreTag = table.getModel().getValueAt(table.getSelectedRow(), 6).toString();
                    
                    String xyz = menuitem1.getText().toUpperCase();
                    String sql1 = "INSERT INTO PROJECT."+xyz+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+filePathTag+"', '"+titleTag+"', '"+artistTag+"', '"+albumTag+"', '"+yearTag+"', '"+commentTag+"', '"+genreTag+"')";
                    System.out.println(".actionPerformed()"+sql1);
                     stmt2.executeUpdate(sql1);
                      TreePath tp = new TreePath(node2.getPath());
         playList.setSelectionPath(tp);
           library.setSelectionPath(null);
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                            
      }
    });
                 menuitem2.addActionListener(new ActionListener() {
                  @Override
      public void actionPerformed(ActionEvent ev) {
         // JMenuItem menuItem = (JMenuItem)ev.getSource();
          //System.out.println(".actionPerformed()"+menuItem.getText());
      
                try {
                    System.out.println("checking");
                   
                    
                    Connection con = DatabaseConnection.getConnection();
                    Statement stmt2 = con.createStatement();
                    String filePathTag = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
                    String titleTag = table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
                    String artistTag = table.getModel().getValueAt(table.getSelectedRow(), 2).toString();
                    String albumTag = table.getModel().getValueAt(table.getSelectedRow(), 3).toString();
                    String yearTag = table.getModel().getValueAt(table.getSelectedRow(), 4).toString();
                    int result = Integer.parseInt(yearTag);
                    String commentTag = table.getModel().getValueAt(table.getSelectedRow(), 5).toString();
                    String genreTag = table.getModel().getValueAt(table.getSelectedRow(), 6).toString();
                    
                    String xyz = menuitem2.getText().toUpperCase();
                    String sql1 = "INSERT INTO PROJECT."+xyz+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+filePathTag+"', '"+titleTag+"', '"+artistTag+"', '"+albumTag+"', '"+yearTag+"', '"+commentTag+"', '"+genreTag+"')";
                    System.out.println(".actionPerformed()"+sql1);
                     stmt2.executeUpdate(sql1);
                      TreePath tp = new TreePath(node2.getPath());
         playList.setSelectionPath(tp);
           library.setSelectionPath(null);
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                            
      }
    });
             
    }
                 
                 
                 
                  if(i==3)
                {
                menuitem1.addActionListener(new ActionListener() {
                  @Override
      public void actionPerformed(ActionEvent ev) {
         // JMenuItem menuItem = (JMenuItem)ev.getSource();
          //System.out.println(".actionPerformed()"+menuItem.getText());
      
                try {
                    System.out.println("checking");
                   // System.out.println(".actionPerformed() menu name"+mi.getText());
                 //   System.out.println(".action hand" +ev.getActionCommand());
                    
                    Connection con = DatabaseConnection.getConnection();
                    Statement stmt2 = con.createStatement();
                    String filePathTag = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
                    String titleTag = table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
                    String artistTag = table.getModel().getValueAt(table.getSelectedRow(), 2).toString();
                    String albumTag = table.getModel().getValueAt(table.getSelectedRow(), 3).toString();
                    String yearTag = table.getModel().getValueAt(table.getSelectedRow(), 4).toString();
                    int result = Integer.parseInt(yearTag);
                    String commentTag = table.getModel().getValueAt(table.getSelectedRow(), 5).toString();
                    String genreTag = table.getModel().getValueAt(table.getSelectedRow(), 6).toString();
                    
                    String xyz = menuitem1.getText().toUpperCase();
                    String sql1 = "INSERT INTO PROJECT."+xyz+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+filePathTag+"', '"+titleTag+"', '"+artistTag+"', '"+albumTag+"', '"+yearTag+"', '"+commentTag+"', '"+genreTag+"')";
                    System.out.println(".actionPerformed()"+sql1);
                     stmt2.executeUpdate(sql1);
                      TreePath tp = new TreePath(node2.getPath());
         playList.setSelectionPath(tp);
           library.setSelectionPath(null);
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                            
      }
    });
                 menuitem3.addActionListener(new ActionListener() {
                  @Override
      public void actionPerformed(ActionEvent ev) {
         // JMenuItem menuItem = (JMenuItem)ev.getSource();
          //System.out.println(".actionPerformed()"+menuItem.getText());
      
                try {
                    System.out.println("checking");
                    
                    
                    Connection con = DatabaseConnection.getConnection();
                    Statement stmt2 = con.createStatement();
                    String filePathTag = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
                    String titleTag = table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
                    String artistTag = table.getModel().getValueAt(table.getSelectedRow(), 2).toString();
                    String albumTag = table.getModel().getValueAt(table.getSelectedRow(), 3).toString();
                    String yearTag = table.getModel().getValueAt(table.getSelectedRow(), 4).toString();
                    int result = Integer.parseInt(yearTag);
                    String commentTag = table.getModel().getValueAt(table.getSelectedRow(), 5).toString();
                    String genreTag = table.getModel().getValueAt(table.getSelectedRow(), 6).toString();
                    
                    String xyz = menuitem3.getText().toUpperCase();
                    
                    String sql1 = "INSERT INTO PROJECT."+xyz+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+filePathTag+"', '"+titleTag+"', '"+artistTag+"', '"+albumTag+"', '"+yearTag+"', '"+commentTag+"', '"+genreTag+"')";
                    System.out.println(".actionPerformed()"+sql1);
                     stmt2.executeUpdate(sql1);
                      TreePath tp = new TreePath(node2.getPath());
         playList.setSelectionPath(tp);
           library.setSelectionPath(null);
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                            
      }
    });
                    System.out.println("selected row "+table.getSelectedRow());
                 
                 
                  menuitem2.addActionListener(new ActionListener() {
                  @Override
      public void actionPerformed(ActionEvent ev) {
         // JMenuItem menuItem = (JMenuItem)ev.getSource();
          //System.out.println(".actionPerformed()"+menuItem.getText());
      
                try {
                    System.out.println("checking");
                    
                    
                    Connection con = DatabaseConnection.getConnection();
                    Statement stmt2 = con.createStatement();
                    String filePathTag = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
                    String titleTag = table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
                    String artistTag = table.getModel().getValueAt(table.getSelectedRow(), 2).toString();
                    String albumTag = table.getModel().getValueAt(table.getSelectedRow(), 3).toString();
                    String yearTag = table.getModel().getValueAt(table.getSelectedRow(), 4).toString();
                    int result = Integer.parseInt(yearTag);
                    String commentTag = table.getModel().getValueAt(table.getSelectedRow(), 5).toString();
                    String genreTag = table.getModel().getValueAt(table.getSelectedRow(), 6).toString();
                    
                    String xyz = menuitem2.getText().toUpperCase();
                    String sql1 = "INSERT INTO PROJECT."+xyz+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+filePathTag+"', '"+titleTag+"', '"+artistTag+"', '"+albumTag+"', '"+yearTag+"', '"+commentTag+"', '"+genreTag+"')";
                    System.out.println(".actionPerformed()"+sql1);
                     stmt2.executeUpdate(sql1);
                      TreePath tp = new TreePath(node2.getPath());
         playList.setSelectionPath(tp);
           library.setSelectionPath(null);
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                            
      }
    });
             
    }
                
                
                
                
                

              
            popup.add(addSongToPlaylist);
            
        
        MouseListener mouseListener = new MouseAdapter() {
           
            public void mousePressed(MouseEvent e) {
               CurrentSelectedRow = table.getSelectedRow();
               //Get local filepath
               if (e.getButton() == MouseEvent.BUTTON1 ) {
                  data = (String) table.getValueAt(table.getSelectedRow(), 0);
                  System.out.println(data);
            }
               //Generating menu on Right Click
               if(SwingUtilities.isRightMouseButton(e))
               {
                   popup.show(e.getComponent(), e.getX(), e.getY());
               }
             
               
            }
        };
        
        table.addMouseListener(mouseListener);
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(250);
       
        
        //JButtons to control music player
        prev = new JButton("Prev");
        prev.addActionListener(bl);
        play = new JButton("Play");
        play.addActionListener(bl);
         pause = new JButton("Pause/Resume");
        pause.addActionListener(bl);
         stop = new JButton("Stop");
        stop.addActionListener(bl);
       
         next = new JButton("Next");
        next.addActionListener(bl);
       
             
        nowPlaying = new JLabel("Now playing: nothing");
        //drag and drop table rows
        table.setDragEnabled(true);
        //table.setDrMode(DropMode.INSERT_ROWS);
       // table.setDropMode(DropMode.USE_SELECTION);
        table.setTransferHandler(new TransferHandler(){
  
          public int getSourceActions(JComponent c) {
                return DnDConstants.ACTION_COPY_OR_MOVE;
            }
  
            public Transferable createTransferable(JComponent comp)
            {
                JTable table=(JTable)comp;
                int row=table.getSelectedRow();
                int col=table.getSelectedColumn();
                 
                 String value = (String)table.getModel().getValueAt(row,0);
                System.out.println("valueeee "+value);
                StringSelection transferable = new StringSelection(value);
               // table.getModel().setValueAt(null,row,col);
                return transferable;
            }
            
        });
        
        //Drag and drop
        
    this.setDropTarget(new DropTarget() {
                @Override
                public synchronized void dragOver(DropTargetDragEvent dtde) {
                    Point point = dtde.getLocation();
                    int row = table.rowAtPoint(point);
                    if (row < 0) {
                        table.clearSelection();
                    } else {
                        table.setRowSelectionInterval(row, row);
                    }
                    dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
                }
               
                @Override
                public synchronized void drop(DropTargetDropEvent dtde) {
                  
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        Transferable t = dtde.getTransferable();
                        java.util.List fileList = null;
                        try {
                            fileList = (java.util.List) t.getTransferData(DataFlavor.javaFileListFlavor);
                            if (fileList.size() > 0) {
                                table.clearSelection();
                                Point point = dtde.getLocation();
                                int row = table.rowAtPoint(point);
                                DefaultTableModel model = (DefaultTableModel) table.getModel();
                                for (Object value : fileList) {
                                    if (value instanceof File) {
                                        File f = (File) value;
                                        if (row < 0) {
                                           Mp3File mp3file = null;
                     mp3file = new Mp3File(f.getAbsolutePath());
                        if (mp3file.hasId3v2Tag()) {
                        ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                        String album = id3v2Tag.getAlbum();
                        String title1 = id3v2Tag.getTitle();
                         songNotExist = true;
                             checkSong(title1);
                             System.out.println("mike testing");
                            if(songNotExist)
                            {
                                
                         Statement stmt=con.createStatement();
        
              stmt.executeUpdate("INSERT INTO PROJECT."+node2+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
           
                         
                                System.out.println("check selection"+library.isSelectionEmpty());
                              //  System.out.println(".dropp"+PlayList.getChildAt(1).);
                         
           stmt.executeUpdate("INSERT INTO PROJECT.MUSICPLAYER (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
             playListClassNew.refreshfunc();
                    model.insertRow(0, new Object[]{f.getAbsolutePath(), id3v2Tag.getTitle(), id3v2Tag.getArtist(),id3v2Tag.getAlbum(),id3v2Tag.getYear(),id3v2Tag.getComment() , id3v2Tag.getGenreDescription()});
                        row++;
                                            
                        }
                                        }   
                                        } else {
                                             //ADD SONG
                   Mp3File mp3file = null;
                     mp3file = new Mp3File(f.getAbsolutePath());
                        if (mp3file.hasId3v2Tag()) {
                        ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                        String album = id3v2Tag.getAlbum();
                        String title1 = id3v2Tag.getTitle();
                         songNotExist = true;
                             checkSong(title1);
                        if(songNotExist)
                        {
                             
                         Statement stmt=con.createStatement();
                          stmt.executeUpdate("INSERT INTO PROJECT."+node2+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
           
         System.out.println("check selection"+library.isSelectionEmpty());
           stmt.executeUpdate("INSERT INTO PROJECT.MUSICPLAYER (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
            playListClassNew.refreshfunc();
                    model.insertRow(row, new Object[]{f.getAbsolutePath(), id3v2Tag.getTitle(), id3v2Tag.getArtist(),id3v2Tag.getAlbum(),id3v2Tag.getYear(), id3v2Tag.getComment(), id3v2Tag.getGenreDescription()});
                        row++;
                        }        
                        }
         
                         }
                                    }
                                }
                            }
                        } catch (UnsupportedFlavorException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException ex) {
                            Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (UnsupportedTagException ex) {
                            Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidDataException ex) {
                            Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        dtde.rejectDrop();
                    }
                }

            });
         
            //Add Tree code
             //Add new panel       
         panelTree = new JPanel();
         
        
        //create the root nodes
         Library = new DefaultMutableTreeNode("Library");
         
         PlayList = new DefaultMutableTreeNode("PlayList");
         
         playList = new JTree(PlayList);
         library = new JTree(Library);
         TreePath tp = new TreePath(Library.getPath());
         library.setSelectionPath(tp);
          DefaultTreeModel model1 = (DefaultTreeModel) playList.getModel();
             
      DefaultMutableTreeNode root = (DefaultMutableTreeNode) model1.getRoot();
        //   System.out.println("root"+root);
      //DefaultMutableTreeNode child = new DefaultMutableTreeNode(playListName);
      //PlayList.add(child);
     
       
      String query1 = "SELECT * FROM PROJECT.PLAYLIST";
       // Connection con = DatabaseConnection.getConnection();
        PreparedStatement statement1 = con.prepareStatement(query1);
        ResultSet Rs1 = statement1.executeQuery();
        
                //Display of records
                while(Rs1.next())
                {
                //model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7)});
                    String a = Rs1.getString(1);
                     child = new DefaultMutableTreeNode(a);
                    System.out.println("root"+child.toString());
                model1.insertNodeInto(child, root, root.getChildCount());
               // TreePath path = new TreePath(child.getPath());
               // playList.setSelectionPath(path);
      playList.scrollPathToVisible(new TreePath(child.getPath()));
                }
            
         
         
         
        panelTree.add(library);
        panelTree.add(playList);
       
        // model.nodeStructureChanged(PlayList);
        panelTree.setPreferredSize(new Dimension(90, 90));
    
        
       library.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
         libraryfunc();
            TreePath tp = new TreePath(Library.getPath());
         library.setSelectionPath(tp);
         playList.setSelectionPath(null);
            }
        }
            
       });
       JPopupMenu popupTree = new JPopupMenu(); 
       
             JMenuItem openNewWindow = new JMenuItem("Open in new window");
            openNewWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          
           
                try {
                    p = new playListClassNew();
                    libraryfunc();
                    TreePath tp = new TreePath(Library.getPath());
                    library.setSelectionPath(tp);
                    playList.setSelectionPath(null);
                     p.setVisible(true);
                     p.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                     TreeNode[] a = Library.getPath();
                    // library.setSelectionPath(a);
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                 
                  
            }
            });
            popupTree.add(openNewWindow);
            JMenuItem deletePlaylist = new JMenuItem("Delete Playlist");
            deletePlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               DefaultMutableTreeNode node = (DefaultMutableTreeNode) playList.getSelectionPath().getLastPathComponent();
               String nodeName = node.toString();
               int inputPane = JOptionPane.showConfirmDialog(null, "Do you wish to delete playlist");
               
             if(inputPane == 0)
             {
                System.out.println(".actionPerformed()"+nodeName);
               model1.removeNodeFromParent(node);
               Connection con;
                try {

                    con = DatabaseConnection.getConnection();
                    Statement stmt1 = con.createStatement();
                    String query3 = "DROP TABLE "+node.toString().toUpperCase();
                    stmt1.executeUpdate(query3);
                     PreparedStatement statement6 = con.prepareStatement("DELETE FROM PROJECT.PLAYLIST WHERE PLAYLISTNAME = ?");
                    statement6.setString(1, nodeName);    
                    statement6.executeUpdate();
                    p.dispose();
                    libraryfunc();

                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                else
                {
                        
                        }
            }
               
             });
            popupTree.add(deletePlaylist);
       playList.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
             node2 = (DefaultMutableTreeNode)
                       playList.getLastSelectedPathComponent();
           
                System.out.println("object"+ playList.getLastSelectedPathComponent());
        //Database connection
        //String string = String.format("A String %s %2d", aStringVar, anIntVar);
         if (SwingUtilities.isRightMouseButton(e)) {

        int row = playList.getClosestRowForLocation(e.getX(), e.getY());
        playList.setSelectionRow(row);
        library.setSelectionPath(null);
        popupTree.show(e.getComponent(), e.getX(), e.getY());
    }
else
         {
        if( playList.getLastSelectedPathComponent() == PlayList)
        {
            
        }
        else
        {
          if (e.getClickCount() == 1) {
                library.setSelectionPath(null);
                //create the table
    String query1 = "SELECT * FROM PROJECT."+node2.toString().toUpperCase();
             
                System.out.println("mouseclick "+query1);
        Connection con;
                try {
                    con = DatabaseConnection.getConnection();
                    Statement stmt1 = con.createStatement();
                     ResultSet Rs1;
                     Rs1 = stmt1.executeQuery(query1);
                     model.setRowCount(0);
                     while(Rs1.next())
                {
                    System.out.println("output"+Rs1.getString(1));
                    
                model.addRow(new Object[]{Rs1.getString(1), Rs1.getString(2),Rs1.getString(3),Rs1.getString(4),Rs1.getString(5),Rs1.getString(6),Rs1.getString(7)});
                }
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
               
        
                //Display of records
               
            
          //table.setModel(model);
         
              /*  DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                       tree.getLastSelectedPathComponent();
                if (node == null) return;
                Object nodeInfo = node.getUserObject();*/
                // Cast nodeInfo to your object and do whatever you want
            }
            
        }
        }
    });
    

    
    
    
    JSlider slider;
        slider = new JSlider();
        
        slider.addChangeListener(new javax.swing.event.ChangeListener(){
           
        public void stateChanged(ChangeEvent ee)
        {
            //repaint();
            System.out.println(".stateChanged()"+prog);
             prog = slider.getValue();
             
             float v = (float)prog/100;
             System.out.println("float"+v);
            try {
                
              
                player.setGain(v);
                //   time.setText(prog / 1000 + "." + (prog % 1000) / 100);
                //if(prog!=ap)
                //skip(prog);
            } catch (BasicPlayerException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

         
        });
    
    


    
    
    
    
      
    
        //Display Menu
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        //Create Playlist in Menu
        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        createPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createPlaylist();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
         menu.add(createPlaylist);
         
        //Add Song in Menu 
        JMenuItem addSong = new JMenuItem("Add a song");
        
        addSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addSong();
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        menu.add(addSong);
        //Delete a song
        JMenuItem deleteSong = new JMenuItem("Delete a song");
          deleteSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                try {
                    deleteSong();
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
              
          });
        menu.add(deleteSong);
       
        //Open Application
         JMenuItem openSong = new JMenuItem("Open a song");
        menu.add(openSong);
        openSong.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                openSong();
             }
            });
         //Exit Application
        JMenuItem exitApp = new JMenuItem("Exit Application");
        menu.add(exitApp);
        exitApp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
            });
        menubar.add(menu);
        //control menu
        
        JMenu controls = new JMenu("Controls");
        JMenuItem playMenu = new JMenuItem("Play");
          playMenu.setAccelerator(KeyStroke.getKeyStroke("SPACE"));
        controls.add(playMenu);
        playMenu.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                try {
                    bl.playSong();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BasicPlayerException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedTagException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidDataException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
             }
            });
        JMenuItem nextMenu = new JMenuItem("Next");
        controls.add(nextMenu);
        KeyStroke ctrlR = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        nextMenu.setAccelerator(ctrlR);
        nextMenu.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                 
                try {
                    bl.nextSong();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BasicPlayerException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedTagException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidDataException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
             }
            });
        JMenuItem prevMenu = new JMenuItem("Previous");
        controls.add(prevMenu);
        KeyStroke ctrlH = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        prevMenu.setAccelerator(ctrlH);
        prevMenu.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                try {
                    bl.prevSong();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BasicPlayerException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedTagException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidDataException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
             }
            });
         recentMenu = new JMenu("Play Recent");
         bl.dispRec();
        controls.add(recentMenu);
        
        JMenuItem currentMenu = new JMenuItem("Go To Current Song");
        controls.add(currentMenu);
        currentMenu.setAccelerator(KeyStroke.getKeyStroke('L', CTRL_DOWN_MASK));
        currentMenu.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                 if(player.getStatus() == BasicPlayer.PLAYING||player.getStatus() == BasicPlayer.PAUSED)
                 {
                    table.changeSelection(rownum,0 , false,false);
                    table.scrollRectToVisible(table.getCellRect(rownum, 0, true));
                 }
                 else
                 {
                table.scrollRectToVisible(table.getCellRect(table.getSelectedRow(), 0, true));
                 }
             }
            });
        controls.addSeparator();
        JMenuItem incMenu = new JMenuItem("Increase Volume");
        controls.add(incMenu);
          incMenu.setAccelerator(KeyStroke.getKeyStroke('I', CTRL_DOWN_MASK));
        incMenu.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                 prog = slider.getValue();
             float v = (float) ((prog+5)/100);
             System.out.println("float"+v);
            try {
                player.setGain(v);
                  slider.setValue(prog+5);
                //   time.setText(prog / 1000 + "." + (prog % 1000) / 100);
                //if(prog!=ap)
                //skip(prog);
            } catch (BasicPlayerException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
             }
            });
        JMenuItem decMenu = new JMenuItem("Decrease Volume");
        controls.add(decMenu);
        decMenu.setAccelerator(KeyStroke.getKeyStroke('D', CTRL_DOWN_MASK));
        decMenu.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                prog = slider.getValue();
             float v = (float) ((prog-5)/100);
             System.out.println("float"+v);
            try {
                player.setGain(v);
                 slider.setValue(prog-5);
                //   time.setText(prog / 1000 + "." + (prog % 1000) / 100);
                //if(prog!=ap)
                //skip(prog);
            } catch (BasicPlayerException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
             }
            });
        controls.addSeparator();
         shuffleMenu = new JCheckBoxMenuItem("Shuffle");
        controls.add(shuffleMenu);
         shuffleMenu.addItemListener(new ItemListener() {
            @Override
              public void itemStateChanged(ItemEvent e) {
                 if(e.getStateChange() == ItemEvent.SELECTED) {
                    
        Connection con;
                   try {
                       con = DatabaseConnection.getConnection();
                       Statement stmt9 = con.createStatement();
        ResultSet Rs8 = stmt9.executeQuery("SELECT * FROM PROJECT.MUSICPLAYER");
         rowCount = 0;
         while(Rs8.next())
                       {
                           System.out.println("kjsdfnhdkjshf "+rowCount);
                           rowCount=rowCount+1;
                           //model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7)});
                           
                       } 
                   } catch (SQLException ex) {
                       Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                   }
       
                     if(player.getStatus()==BasicPlayer.UNKNOWN)
                    {
                        try {
                            bl.shufflef();
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (BasicPlayerException ex) {
                            Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedTagException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidDataException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
                    }
               
               //model.fireTableDataChanged();
                 }
             }
            });
         repeatMenu = new JCheckBoxMenuItem("Repeat");
        controls.add(repeatMenu);
        repeatMenu.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
               
             }
            });
        menubar.add(controls);
           timerlabel = new JLabel("00:00:00");
         timerlabel1 = new JLabel("00:00:00");
        main.add(prev);
         main.add(stop);
        
        main.add(pause);
       main.add(play);
       
        main.add(next);
       
        main.add(nowPlaying);
        modelbar.setMinimum(0);
        modelbar.setMaximum(100);
        main.add(timerlabel);
         main.add(bar);
        main.add(timerlabel1);
        
        bar.setStringPainted(true);
        
       
       
        
     /* slider.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });*/
        main.add(slider);

       
         containerPane = this.getContentPane();
        containerPane.setLayout(new BorderLayout());
        this.setJMenuBar(menubar);
         scrollPane1 = new JScrollPane(panelTree);
         scrollPane = new JScrollPane(table);
      
            containerPane.add(scrollPane1, BorderLayout.WEST);
        
        
        containerPane.add(scrollPane, BorderLayout.CENTER);
        containerPane.add(main, BorderLayout.SOUTH);
        //timerlabel.setLayout(new BoxLayout(timerlabel, BoxLayout.Y_AXIS));
        this.setSize(800,300);
        this.setTitle("MusicPlayer by Sudheer & Apoorva");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
    }
 
    public void barval()
        {
             bar.setValue(25);
        }
        public void deleteSong() throws SQLException 
        {
            Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps;
                if(table.getSelectedRow()<0)
                    {
                        JOptionPane.showMessageDialog(null, 
                              "Please select a song to delete and then proceed", 
                              "Alert Message", 
                              JOptionPane.WARNING_MESSAGE);
                    }
                else
                {
                try {
                     String query = "DELETE FROM PROJECT.MUSICPLAYER WHERE TITLE = ?";
                   
                    ps = con.prepareStatement(query);
                    data = (String) table.getValueAt(table.getSelectedRow(), 1);
                    ps.setString(1, data);
                    ps.executeUpdate();
                    String sql = "SELECT * FROM PROJECT.MUSICPLAYER";
           
               


PreparedStatement statement = con.prepareStatement(sql);
ResultSet Rs = statement.executeQuery();
                
                // Rs=statement.executeQuery();
                
                //Display of records
                DefaultTableModel model = (DefaultTableModel)table.getModel();
            model.getDataVector().removeAllElements();
           model.fireTableDataChanged();
                while(Rs.next())
                {
                model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7)});
                }
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
        }
        public void addSong() throws SQLException
        {
             JFileChooser c = new JFileChooser();
             Connection con = DatabaseConnection.getConnection();
            // Demonstrate "Open" dialog:
            int rVal = c.showOpenDialog(StreamPlayerGUI.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
               File file = c.getSelectedFile();             
               
               String filePath = file.getAbsolutePath();
                try {
                    //ADD SONG
                    Mp3File mp3file = null;
                     mp3file = new Mp3File(file.getAbsolutePath());
                        if (mp3file.hasId3v2Tag()) {
                        ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                        
                       // String  = id3v2Tag.getTitle();
                        title =  id3v2Tag.getTitle();
                         Statement stmt = con.createStatement();
                            checkSong(title);
                           
                         if(songNotExist)
                         {
                         
                         
           stmt.executeUpdate("INSERT INTO PROJECT.MUSICPLAYER (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+filePath+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            model.getDataVector().removeAllElements();
           model.fireTableDataChanged();
           String query = "SELECT * FROM PROJECT.MUSICPLAYER";
           // Connection con = DatabaseConnection.getConnection();
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet Rs = statement.executeQuery();
                
                //Display of records
                while(Rs.next())
                {
                model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7)});
                }
                        }
                        }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }   catch (UnsupportedTagException ex) { 
                        Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidDataException ex) {
                        Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } 
           
            }
                
            if (rVal == JFileChooser.CANCEL_OPTION) {
              filename.setText("You pressed cancel");
            
            }
        }
         public void addSongToPlaylist()
         {
             
         }
         
         
         //Add song to playlist function
         
         public void addSongPl(MenuElement mi1)
         {
             
         }
         
         
         
         
         
         
         
         public static String givename()
         {
             return node2.toString().toUpperCase();
         }
         
         
        //Create Playlist function
        public void createPlaylist() throws SQLException
        {
             String playListName= JOptionPane.showInputDialog("Enter Playlist name ");
             
             DefaultTreeModel model = (DefaultTreeModel) playList.getModel();
             
      DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        //   System.out.println("root"+root);
       child = new DefaultMutableTreeNode(playListName);
      //PlayList.add(child);
      Connection con = DatabaseConnection.getConnection();
      Statement st = con.createStatement();
      
        
           st.executeUpdate("INSERT INTO PROJECT.PLAYLIST (playlistname) "
          +"VALUES ('"+child+"')");
           String query = "CREATE TABLE " +child.toString()+
                   "(FILEPATH VARCHAR(255) not NULL, " +
                   " TITLE VARCHAR(255), " + 
                   " ARTIST VARCHAR(255), " + 
                   " ALBUM VARCHAR(255), " + 
                   " YEARS VARCHAR(255), " + 
                   " COMMENT VARCHAR(255), " + 
                   " GENRE VARCHAR(255), " + 
                   " PRIMARY KEY ( FILEPATH ))";
            System.out.println("sql "+query);
            st.executeUpdate(query);
            playlistopening();
        /*   PreparedStatement stmt=con.prepareStatement("CREATE TABLE ? " +
                   "(FILEPATH VARCHAR(255) not NULL, " +
                   " TITLE VARCHAR(255), " + 
                   " ARTIST VARCHAR(255), " + 
                   " ALBUM VARCHAR(255), " + 
                   " YEARS INTEGER, " + 
                   " COMMENT VARCHAR(255), " + 
                   " GENRE VARCHAR(255), " + 
                   " PRIMARY KEY ( FILEPATH ))");
       stmt.setObject(1, playListName);
       stmt.executeUpdate();*/
     
           
           
           
      model.insertNodeInto(child, root, root.getChildCount());
      playList.scrollPathToVisible(new TreePath(child.getPath()));
     
        }
        //Library tree click function
        public static void libraryfunc()
        {
                   String query = "SELECT * FROM PROJECT.MUSICPLAYER";
        Connection con;
                try {
                    con = DatabaseConnection.getConnection();
                    PreparedStatement statement = con.prepareStatement(query);
        ResultSet Rs = statement.executeQuery();
        
                //Display of records
                model.setRowCount(0);
                while(Rs.next())
                {
               
                model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7)});
                }
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        }
        //When playlist is created
        public void playlistopening()
        {
             String query1 = "SELECT * FROM PROJECT."+child.toString().toUpperCase();
             
                System.out.println("mouseclick "+query1);
        Connection con;
                try {
                    con = DatabaseConnection.getConnection();
                    Statement stmt1 = con.createStatement();
                     ResultSet Rs1;
                     Rs1 = stmt1.executeQuery(query1);
                     model.setRowCount(0);
                     while(Rs1.next())
                {
                    System.out.println("output"+Rs1.getString(1));
                    
                model.addRow(new Object[]{Rs1.getString(1), Rs1.getString(2),Rs1.getString(3),Rs1.getString(4),Rs1.getString(5),Rs1.getString(6),Rs1.getString(7)});
                }
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
            //Open Song function
              public void openSong()
              {
                   JFileChooser c = new JFileChooser();
            // Demonstrate "Open" dialog:
            int rVal = c.showOpenDialog(StreamPlayerGUI.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
               File file = c.getSelectedFile();
              
                
                //  System.out.println("getSelectedFile() : " + c.getSelectedFile().toString());
                 int lastIndex = c.getSelectedFile().toString().lastIndexOf('\\');
                            int length = c.getSelectedFile().toString().length();
                         String s =   c.getSelectedFile().getName();
                         Mp3File mp3file = null;
                     try {
                         mp3file = new Mp3File(file.getAbsolutePath());
                     } catch (IOException ex) {
                         Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                     } catch (UnsupportedTagException ex) {
                         Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                     } catch (InvalidDataException ex) {
                         Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                     }
                        if (mp3file.hasId3v2Tag()) {
                        ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                         title = id3v2Tag.getTitle();
                          //  System.out.println(".actionPerformed()"+title);
                        }
                         //System.out.println("s"+s);
                        checkSong(title);
                          
                          if(songNotExist)
                          {
                               try {
                    player.open(new URL("file:///" + c.getSelectedFile()));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BasicPlayerException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    player.play();
                    nowPlaying.setText("Now Playing: "+s);
                } catch (BasicPlayerException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
             if (rVal == JFileChooser.CANCEL_OPTION) {
              filename.setText("You pressed cancel");
             
            }
                          }
               
              }
              //check song
               public boolean checkSong(String title)
              {
                  for(int i=0;i<table.getRowCount();i++)
                         {
                            allTitle=((String) table.getValueAt(i, 1));
                             //str1.toLowerCase().contains(str2.toLowerCase())
                             if(title.toLowerCase().contains(allTitle.toLowerCase()))
                             {
                                  
                                  JOptionPane.showMessageDialog(null, 
                              "Song exists in the library", 
                              "Alert Message", 
                              JOptionPane.WARNING_MESSAGE);
                                 songNotExist = false;
                                
                             }
                         }
                   return songNotExist;
              }
               public String getNodeee(String nn)
                 {
                     return nn;
                 }
                class BasicListener implements BasicPlayerListener
               {
                    public BasicListener()
                    {
                        player.addBasicPlayerListener(this);

                    }
                    
         public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties)
  {
      
    // Pay attention to properties. It depends on underlying JavaSound SPI
    // MP3SPI provides mp3.equalizer.
    display("progress : "+properties.toString());
      System.out.println("timer"+microseconds);
      
      Object MicroSeconds = properties.get("mp3.position.microseconds");
                    long microsec = Long.valueOf(MicroSeconds.toString());
                     lenSec = Long.valueOf(Math.round(microsec / 1000000));
                    System.out.println("microsec "+microsec);
                 /*   Date d = new Date(lenSec * 1000L);
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
                    df.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String time = df.format(d);*/
                    
                  //  timer.setText(time);
                    //jbar.setMin(0);
                    
                            
                  //  jbar.setValue((int)lenSec);

  }

  /**
   * Notification callback for basicplayer events such as opened, eom ...
   * 
   * @param event
   */
  public void stateUpdated(BasicPlayerEvent event)
  {
    // Notification of BasicPlayer states (opened, playing, end of media, ...)
    display("stateUpdated : "+event.toString());
    
  }

  /**
   * A handle to the BasicPlayer, plugins may control the player through
   * the controller (play, stop, ...)
   * @param controller : a handle to the player
   */ 
  public void setController(BasicController controller)
  {
    display("setController : "+controller);
  }

  public void display(String msg)
  {
    if (out != null) out.println(msg);
  }

    
       public void opened(Object stream, Map properties){
            display("opened : "+properties.toString()); 
          FR = ((Float) properties.get("mp3.framerate.fps"));
          FS = ((int) properties.get("mp3.framesize.bytes"));
          
           System.out.println("fr "+FR);
            System.out.println("fs "+FS);
        }
                   
               }
    class ButtonListener implements ActionListener {

        @Override
        
        public void actionPerformed(ActionEvent e) {
            
            String url=null;
        
                     pathToMp3 = data;
                    
   
            try {
                Object s = e.getSource();
                Random rgen = new Random();
                if(s==play)
                {
                playSong();
                }
                 else if(s==stop)
                 {
                     if((player.getStatus()== BasicPlayer.PLAYING)||(player.getStatus()== BasicPlayer.PAUSED))
                     {
                          nowPlaying.setText("Now Playing: Nothing");
                         player.stop();
                     }
                      
                 }
                else if(s==pause)
                {
                    
                    if(player.getStatus()== BasicPlayer.PAUSED)
                    {
                   
                    
                    player.resume();
                   nowPlaying.setText("Now Playing: "+((String) table.getValueAt(table.getSelectedRow(), 1)));
                    }
                    else
                    {
                        player.pause();
                    }
                }
                
                  else if (s==next){
                      
                     
                   if(shuffleMenu.isSelected())
                   {
                       System.out.println("row count "+rowCount);
                       
                       int randomPosition = rgen.nextInt(rowCount);
                       System.out.println("random value "+randomPosition);
                       table.changeSelection(randomPosition, 0, false, false);
                       nextSong();
                   }
                   else
                   {
                       nextSong();
                   }
                    
                }
                else
                  {
                     
                      prevSong();
                  }
               
            } catch (MalformedURLException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Malformed url");
            } catch (BasicPlayerException ex) {
                System.out.println("BasicPlayer exception");
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedTagException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidDataException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public void timerfunc() throws IOException, UnsupportedTagException, InvalidDataException
        {
            
           
            
         
             File fff = new File(data);
              Mp3File mp3file = null;
                     mp3file = new Mp3File(fff.getAbsolutePath());
                     mp3file.getLengthInSeconds();
                     System.out.println("mp3 duration "+mp3file.getLengthInSeconds());
                        System.out.println("file length"+fff.length());
                        long filelength = fff.length();
                        audioDurationInSeconds = mp3file.getLengthInSeconds();
                       System.out.println("audio duration "+audioDurationInSeconds);
                 timeInSeconds =Math.round(audioDurationInSeconds);
                ui=0L;
                timer = new Timer(1000, new ActionListener(){      // Timer 4 seconds
            public void actionPerformed(ActionEvent e) {
                if(lenSec>=timeInSeconds)
                {
                    
                }
                //System.out.println("lensec "+lenSec);
                if(lenSec<timeInSeconds)
                {       
                timerlabel.setText(String.format("%02d",lenSec/3600)+":"+String.format("%02d",lenSec/60%60)+":"+String.format("%02d",lenSec%60));
                //ib=ib+1;
                }
                int fi;
                if(audioDurationInSeconds<100)
                {
                     fi = Math.round(audioDurationInSeconds/10);
                }
                else
                {
                 fi = Math.round(audioDurationInSeconds/100);
                }
                if(lenSec==0)
                {
                    
                }
                else
                {
                    if(lenSec!=0)
                    {
                if(lenSec%fi==0&&lenSec<audioDurationInSeconds&&lenSec>ui)
                {
                    barfunc();
                     ui = lenSec;
                    
                }
                }
                }
            }
        });
                      timer.start();
 timeInSeconds =Math.round(audioDurationInSeconds);
  ub=0L;
                      timer1 = new Timer(1000, new ActionListener(){      // Timer 4 seconds
            public void actionPerformed(ActionEvent e) {
                
                 int fi;
                  if(audioDurationInSeconds<100)
                {
                     fi = Math.round(audioDurationInSeconds/10);
                }
                else
                {
                 fi = Math.round(audioDurationInSeconds/100);
                }
                long ui=0L;
            
    if(lenSec<timeInSeconds)
    {
        
                if(lenSec>ub)
                {
                    System.out.println("lensec "+lenSec+"ub"+ub);
                    timerlabel1.setText(String.format("%02d",timeInSeconds/3600)+":"+String.format("%02d",timeInSeconds/60%60)+":"+String.format("%02d",timeInSeconds%60));
                timeInSeconds=timeInSeconds-1;
                ub=lenSec;
                
                }
                
            }
            }
        });
                      timer1.start();
                      
        }
        public void barfunc()
        {
            if(lenSec>=timeInSeconds)
            {
                modelbar.setValue(0);
            }
            else
            {
            valbar = valbar+1;
            modelbar.setValue(valbar);
        }
        }
        public void playSong() throws MalformedURLException, BasicPlayerException, SQLException, IOException, UnsupportedTagException, InvalidDataException
        {
            modelbar.setValue(0);
             if((player.getStatus()== BasicPlayer.PAUSED) || (player.getStatus() == BasicPlayer.STOPPED)||(player.getStatus() == BasicPlayer.PLAYING)||(table.getSelectedRow()>=0 &&(player.getStatus()== BasicPlayer.UNKNOWN)))
                    {
                
                        System.out.println("inside play song "+(String) table.getValueAt(table.getSelectedRow(), 1));
                   data = (String) table.getValueAt(table.getSelectedRow(), 0);
                   rownum = table.getSelectedRow();
                  songNamePlaying = ((String) table.getValueAt(table.getSelectedRow(), 1));
                  
                    player.open(new URL("file:///" + data));
                    String mmmu = "file:///" + pathToMp3;
                        System.out.println("mmu"+mmmu);
                        System.out.println("file path 2"+data);
                   
                    player.play();
                     recent(((String) table.getValueAt(table.getSelectedRow(), 1)));
                     dispRec();
                        System.out.println("buffer size"+player.getLineBufferSize());
                   
                   
                   
                   
                        System.out.println("gain value"+prog);
                    
                    nowPlaying.setText("Now Playing: "+((String) table.getValueAt(table.getSelectedRow(), 1)));
                    
                    //timer
                    
                    timerfunc();
                    
                       
                    }
        }
        
        public void nextSong() throws MalformedURLException, BasicPlayerException, SQLException, IOException, UnsupportedTagException, InvalidDataException
        {
            modelbar.setValue(0);
            if(table.getSelectedRow()==table.getRowCount()-1)
                    {
                        
                        table.changeSelection(0, 0, false, false);
                        data = (String) table.getValueAt(0, 0);
                    //table.changeSelection(table.getSelectedRow()+1,0 , false,false);
                    player.open(new URL("file:///" + data));
                    player.play();
                    if(shuffleMenu.isSelected())
                    {
                        
                    }
                    else
                    {
                     recent(((String) table.getValueAt(table.getSelectedRow(), 1)));
                     dispRec();
                    }
                    nowPlaying.setText("Now Playing: "+((String) table.getValueAt(0, 1)));
                    }
                    else if(table.getSelectedRow()>=0)
                    {
                         if(repeatMenu.isSelected())
                      {
                          data = (String) table.getValueAt(table.getSelectedRow(), 0);
                          playSong();
                      }
                         else
                         {
                    data = (String) table.getValueAt(table.getSelectedRow()+1, 0);
                    table.changeSelection(table.getSelectedRow()+1,0 , false,false);
                    player.open(new URL("file:///" + data));
                    player.play();
                     recent(((String) table.getValueAt(table.getSelectedRow(), 1)));
                     dispRec();
                    nowPlaying.setText("Now Playing: "+((String) table.getValueAt(table.getSelectedRow(), 1)));
                    }
                    }
                    else
                    {
                        
                    }
             timer.stop();
             timer1.stop();
             timerfunc();
        }
        public void prevSong() throws MalformedURLException, BasicPlayerException, SQLException, IOException, UnsupportedTagException, InvalidDataException
        {
            modelbar.setValue(0);
             if(table.getSelectedRow()>0)
                       {
                    data = (String) table.getValueAt(table.getSelectedRow()-1, 0);
                    table.changeSelection(table.getSelectedRow()-1,0 , false,false);
                    player.open(new URL("file:///" + data));
                    player.play();
                           recent(((String) table.getValueAt(table.getSelectedRow(), 1)));
                           dispRec();
                    nowPlaying.setText("Now Playing: "+((String) table.getValueAt(table.getSelectedRow(), 1)));
                       }
              timerfunc();
        }
    /*    public void recent() throws SQLException
        {
            String query = "SELECT * FROM PROJECT.RECENT FETCH FIRST 100 ROWS ONLY;";
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement statement = con.prepareStatement(query);
        ResultSet Rs = statement.executeQuery();
        
                //Display of records
                while(Rs.next())
                {
                model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7)});
                }
        }*/
        public void recent(String titlenam) throws SQLException
        {
            Connection con = DatabaseConnection.getConnection();
             Statement stmt=con.createStatement();
        
              stmt.executeUpdate("INSERT INTO PROJECT.RECENT(TITLE) "
          +"VALUES ('"+titlenam+"')");
        }
        public void dispRec() throws SQLException
        {
            recentMenu.removeAll();
            Connection con = DatabaseConnection.getConnection();
             Statement stmt=con.createStatement();
             String q = "SELECT * FROM PROJECT.RECENT FETCH FIRST 10 ROWS ONLY";
             
             ResultSet Rs = stmt.executeQuery(q);
             
                //Display of records
                while(Rs.next())
                {
                recentMenu.add(new JMenuItem(Rs.getString(1)));
                }
                
        }
        public void repeat() throws BasicPlayerException
        {
            while(repeatMenu.isSelected())
            {
                if(table.getSelectedRow()==-1)
                {
                    
                }
                else if(player.getStatus()== BasicPlayer.STOPPED)
                {
                    System.out.println("in repeat");
                player.play();
               
            }
                else
                {
                    
                }
            }
        }
        public void shufflef() throws MalformedURLException, BasicPlayerException, SQLException, IOException, UnsupportedTagException, InvalidDataException
        {
           
                       System.out.println("row count "+rowCount);
                        Random rgen = new Random();
                       int randomPosition = rgen.nextInt(rowCount);
                       System.out.println("random value "+randomPosition);
                       table.changeSelection(randomPosition, 0, false, false);
                       playSong();
                   
        }
       
    }
    
    
   
}
