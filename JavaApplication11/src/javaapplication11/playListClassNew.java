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
import java.awt.Rectangle;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.EventHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.ArrayList;
import static java.util.Arrays.stream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javax.swing.Box;
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
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import sun.audio.AudioStream;


/**
 *
 * @author sudheer and apoorva
 */
public class playListClassNew extends JFrame {
    
    BasicPlayer player;
    JTable table;
    JScrollPane scrollPane,scrollPane1; 
    JPanel main,panelTree;
    JButton play,pause,next,prev,stop;//the three buttons
    JLabel  nowPlaying;
    private JTextField filename = new JTextField();
    String allTitle,data,pathToMp3,title;
    int CurrentSelectedRow;
    ButtonListener bl;
    String songNamePlaying;
    static String treeNodeName;
    JTree playList,library;
    DefaultMutableTreeNode PlayList;
    JMenuItem mi;
    Container containerPane;
    boolean flag = true;
    boolean songNotExist;
    static DefaultTableModel model;

    //Connection con;
 
    public playListClassNew() throws SQLException {
        player = new BasicPlayer();
        main = new JPanel();
        
        //FlowLayout layoutManager = new FlowLayout(FlowLayout.RIGHT);
       // FlowLayout layoutManager = new FlowLayout(FlowLayout.RIGHT);
        bl = new ButtonListener();
        
        //create the table
        String[] columns = {"File", "Title","Artist","Album","Years","Comment","Genre"};
         model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table = new JTable(model);
        
        //Database connection
        treeNodeName = StreamPlayerGUI.givename();
       // treeNodeName = sss.givename();
        String query = "SELECT * FROM PROJECT."+treeNodeName;
        System.out.println("db tree name"+query);
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement statement = con.prepareStatement(query);
        ResultSet Rs = statement.executeQuery();
        
                //Display of records
                while(Rs.next())
                {
                model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7)});
                }
            
          table.setModel(model);
           table.getColumnModel().getColumn(0).setMinWidth(0);
table.getColumnModel().getColumn(0).setMaxWidth(0);
            
        //Right click on table header
         JPopupMenu popHeader = new JPopupMenu();
            JCheckBoxMenuItem titleDisp = new JCheckBoxMenuItem("Title");
            
           
            
            popHeader.add(titleDisp);
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
            JMenuItem deletingSong = new JMenuItem("Remove Song from Playlist");
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
            
           /* JMenu addSongToPlaylist = new JMenu("Add song to playlist");
            //Delete song on right click
            
                    String query2 = "SELECT * FROM PROJECT.PLAYLIST";
       // Connection con = DatabaseConnection.getConnection();
        PreparedStatement statement2 = con.prepareStatement(query2);
        ResultSet Rs2 = statement2.executeQuery();
        
                //Display of records
            
                String playListArray[] = null;
                while(Rs2.next())
                {
                        //model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7)});
                      
                        
                    addSongToPlaylist.add(new JMenuItem(Rs2.getString(1)));
                 
                    // playListArray[i] = mi.getText();
                  
                }
           

           
//            addSongPl(mi);

               // System.out.println("main check"+mi.getText());
              
            popup.add(addSongToPlaylist);
            
        */
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
        //drag and drop
        table.setTransferHandler(new TransferHandler() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean canImport(TransferSupport support) {
                if (!support.isDrop()) {  // for the demo, we'll only support drops (not clipboard paste)
                    return false;
                }
                if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {  // we only import Strings
                    return false;
                }
                return true;
            }

            @Override
            public boolean importData(TransferSupport support) { // if we can't handle the import, say so               
                if (!canImport(support)) {
                    return false;
                }
                JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();// fetch the drop location
                int row = dl.getRow();
                String data; // fetch the data and bail if this fails
                try {
                    data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException e) {
                    return false;
                } catch (IOException e) {
                    return false;
                }
                System.out.println(".importData()"+data);
                String query = "SELECT * FROM PROJECT.MUSICPLAYER where FILEPATH = ?";
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement statement = con.prepareStatement(query);
                    statement.setString(1, data);
        ResultSet Rs = statement.executeQuery();
         while(Rs.next())
         {
             System.out.println(".importData()   "+Rs.getString(1));
                            System.out.println("row"+row);
                            model.insertRow(row, new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5),Rs.getString(6),Rs.getString(7)});
                            Statement st = con.createStatement();
                             st.executeUpdate("INSERT INTO PROJECT."+treeNodeName+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+Rs.getString(1)+"', '"+Rs.getString(2)+"', '"+Rs.getString(3)+"', '"+Rs.getString(4)+"', '"+Rs.getString(5)+"', '"+Rs.getString(6)+"', '"+Rs.getString(7)+"')");
        
         }
                } catch (SQLException ex) {
                    Logger.getLogger(playListClassNew.class.getName()).log(Level.SEVERE, null, ex);
                }
      
                String[] rowData = data.split(",");
               
               // String q = "Insert into PROJECT."
                Rectangle rect = table.getCellRect(row, 0, false);
                if (rect != null) {
                    table.scrollRectToVisible(rect);
                }
               // model.removeAllElements(); // demo stuff - remove for blog
                //model.insertElementAt(getNextString(count++), 0); // end demo stuff
                return true;
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
                  
                    if ((dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))) {
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
                                System.out.println("inside drop");
                         Statement stmt=con.createStatement();
                         
        
           stmt.executeUpdate("INSERT INTO PROJECT.MUSICPLAYER (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
           
           String mmm = "INSERT INTO PROJECT."+treeNodeName+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')";
           
                                System.out.println("drag drop "+mmm);
           
           
           
           stmt.executeUpdate("INSERT INTO PROJECT."+treeNodeName+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
           StreamPlayerGUI.libraryfunc();
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
                        title = id3v2Tag.getTitle();
                         checkSong(title);
                            if(songNotExist)
                            {
                                
                         Statement stmt=con.createStatement();
        
           stmt.executeUpdate("INSERT INTO PROJECT.MUSICPLAYER (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
            
           
           
            String mmm = "INSERT INTO PROJECT."+treeNodeName+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')";
           
                                System.out.println("drag drop "+mmm);
           
           
            stmt.executeUpdate("INSERT INTO PROJECT."+treeNodeName+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
           StreamPlayerGUI.libraryfunc();
           
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
                    } else if(dtde.isDataFlavorSupported(DataFlavor.stringFlavor)){
                        Transferable contents=dtde.getTransferable();
      if (contents!=null) {
         if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
               Point p=dtde.getLocation();
               int row=table.rowAtPoint(p);
               int col=table.columnAtPoint(p);
               String line=(String) contents.getTransferData(DataFlavor.stringFlavor);
               int start=0;
               int end=line.indexOf("\n");
                System.out.println("row"+row+"col"+col+"line"+line);
               if (end<0) {
                  table.setValueAt(line,row,col);
                  return;
               }
             /*  String[] tmp;
               while (end<=line.length()) {
                  tmp=getStringArray(line.substring(start,end),'\t');
                  for (int j=0;j<tmp.length;j++) setValueAt(tmp[j],row,col+j);
                  row++;
                  start=end+1;
                  if (start>=line.length()) break;
                  end=line.substring(start).indexOf("\n");
                  if (end>=0) end+=start;
                  else end=line.length();
               }*/
            } catch (Throwable e) {
               e.printStackTrace();
            }
         }
      }
                    }
                    else
                    {
                        dtde.rejectDrop();
                    }
                }

            });
         
            //Add Tree code
             //Add new panel       
         panelTree = new JPanel();
         
        
        //create the root nodes
        DefaultMutableTreeNode Library = new DefaultMutableTreeNode("Library");
         PlayList = new DefaultMutableTreeNode("PlayList");
         
         playList = new JTree(PlayList);
         library = new JTree(Library);
         
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
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(a);
                    System.out.println("root"+child.toString());
                model1.insertNodeInto(child, root, root.getChildCount());
      playList.scrollPathToVisible(new TreePath(child.getPath()));
                }
            
         
         
         
        panelTree.add(library);
        panelTree.add(playList);
       
        // model.nodeStructureChanged(PlayList);
        panelTree.setPreferredSize(new Dimension(90, 90));
    
    
       library.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
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
        }
            
       });
       JPopupMenu popupTree = new JPopupMenu();
             JMenuItem openNewWindow = new JMenuItem("Open in new window");
            openNewWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            
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
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                       playList.getLastSelectedPathComponent();
                System.out.println("object"+ playList.getLastSelectedPathComponent());
        //Database connection
        //String string = String.format("A String %s %2d", aStringVar, anIntVar);
         if (SwingUtilities.isRightMouseButton(e)) {

        int row = playList.getClosestRowForLocation(e.getX(), e.getY());
        playList.setSelectionRow(row);
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
                
                //create the table
        String query1 = "SELECT * FROM PROJECT."+node.toString().toUpperCase();
             
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
                 if(e.getClickCount() == 2)
            {
                System.out.println("in second frame");
                
              try {
                  
                  StreamPlayerGUI ss = new StreamPlayerGUI();
                  ss.setVisible(true);
                   containerPane = ss.getContentPane();
                   containerPane.removeAll();
                   containerPane.setLayout(new BorderLayout());
                  containerPane.add(scrollPane,BorderLayout.WEST);
                  containerPane.add(main,BorderLayout.SOUTH);    
                  /*  JDialog nonModalDialog = new JDialog(ss, "Non-Modal Dialog", ModalityType.MODELESS);
                  nonModalDialog.add(Box.createRigidArea(new Dimension(600, 600)));
                  nonModalDialog.pack();                                                                                                
                  nonModalDialog.setLocationByPlatform(true);
                  nonModalDialog.setVisible(true);
                  nonModalDialog.add(table);*/
                  
                  //nonModalDialog.add(main);
                  //nonModalDialog.add(table);
                  /*   boolean flag = false;
                  StreamPlayerGUI ss = new StreamPlayerGUI();
                  Container containerPane1 = new Container();
                  containerPane1.add(main);
                  containerPane1.add(table);
                  ss.setContentPane(containerPane1);
                  
                  //containerPane1.setcremove(panelTree);
                  main.add(prev);
                  main.add(stop);
                  
                  main.add(pause);
                  main.add(play);
                  
                  main.add(next);
                  
                  main.add(nowPlaying);
                  
                  
                  
                  
                  
                  containerPane1.setLayout(new BorderLayout());
                  //this.setJMenuBar(menubar);
                  //JScrollPane scrollPane1 = new JScrollPane(panelTree);
                  JScrollPane scrollPane = new JScrollPane(table);
                  //Container containerPane = ss.getContentPane();
                  containerPane1.add(scrollPane, BorderLayout.CENTER);
                  containerPane1.add(main, BorderLayout.SOUTH);
                  
                  
                  
                  
                  //  ss.revalidate();
                  //ss.repaint();
                  ss.setVisible(true); */
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
    

    
    
    
    
    
    
    
    
    
    
    
      
    
        //Display Menu
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        //Create Playlist in Menu
        /*JMenuItem createPlaylist = new JMenuItem("Create Playlist");
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
         */
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
        main.add(prev);
         main.add(stop);
        
        main.add(pause);
       main.add(play);
       
        main.add(next);
       
        main.add(nowPlaying);
        
        JSlider slider = new JSlider();
        slider.addChangeListener(new javax.swing.event.ChangeListener(){
           int prog ;
        public void stateChanged(ChangeEvent ee)
        {
            
            //repaint();
            //System.out.println(".stateChanged()"+prog);
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
        main.add(slider);

       
         containerPane = this.getContentPane();
        containerPane.setLayout(new BorderLayout());
        this.setJMenuBar(menubar);
         scrollPane1 = new JScrollPane(panelTree);
         scrollPane = new JScrollPane(table);
       
        //    containerPane.add(scrollPane1, BorderLayout.WEST);
        
        
        containerPane.add(scrollPane, BorderLayout.CENTER);
        containerPane.add(main, BorderLayout.SOUTH);
        
        this.setSize(800,300);
        this.setTitle("MusicPlayer by Sudheer & Apoorva");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
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
                     String query = "DELETE FROM PROJECT."+treeNodeName+" WHERE TITLE = ?";
                   
                    ps = con.prepareStatement(query);
                    data = (String) table.getValueAt(table.getSelectedRow(), 1);
                    ps.setString(1, data);
                    ps.executeUpdate();
                    String sql = "SELECT * FROM PROJECT."+treeNodeName;
           
               


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
            int rVal = c.showOpenDialog(playListClassNew.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
               File file = c.getSelectedFile();             
               
               String filePath = file.getAbsolutePath();
                try {
                    //ADD SONG
                    Mp3File mp3file = null;
                     mp3file = new Mp3File(file.getAbsolutePath());
                        if (mp3file.hasId3v2Tag()) {
                        ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                        
                        String album = id3v2Tag.getAlbum();
                        title =  id3v2Tag.getTitle();
                         Statement stmt = con.createStatement();
                            songNotExist = true;
                            checkSong(title);
                           
                         if(songNotExist)
                         {
                         
            stmt.executeUpdate("INSERT INTO PROJECT."+treeNodeName+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+filePath+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
           
                         
                         
           stmt.executeUpdate("INSERT INTO PROJECT.MUSICPLAYER (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+filePath+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            model.getDataVector().removeAllElements();
           model.fireTableDataChanged();
           String query = "SELECT * FROM PROJECT."+treeNodeName;
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
         public static void refreshfunc()
         {
              String query = "SELECT * FROM PROJECT."+treeNodeName;
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
         
         
         //Add song to playlist function
         /*
         public void addSongPl(MenuElement mi1)
         {
             
              mi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
         // JMenuItem menuItem = (JMenuItem)ev.getSource();
          //System.out.println(".actionPerformed()"+menuItem.getText());
        
                try {
                    System.out.println("checking");
                    System.out.println(".actionPerformed() menu name"+mi.getText());
                    
                    
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
                    String xyz = mi.getText().toUpperCase();
                    String sql1 = "INSERT INTO PROJECT."+xyz+" (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+filePathTag+"', '"+titleTag+"', '"+artistTag+"', '"+albumTag+"', '"+yearTag+"', '"+commentTag+"', '"+genreTag+"')";
                    System.out.println(".actionPerformed()"+sql1);
                     stmt2.executeUpdate(sql1);
                     
                } catch (SQLException ex) {
                    Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                            
      }
    });
         }
         
         
         
         
         
         */
         
         
        //Create Playlist function
        public void createPlaylist() throws SQLException
        {
             String playListName= JOptionPane.showInputDialog("Enter Playlist name ");
             
             DefaultTreeModel model = (DefaultTreeModel) playList.getModel();
             
      DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        //   System.out.println("root"+root);
      DefaultMutableTreeNode child = new DefaultMutableTreeNode(playListName);
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
        
            //Open Song function
              public void openSong()
              {
                   JFileChooser c = new JFileChooser();
            // Demonstrate "Open" dialog:
            int rVal = c.showOpenDialog(playListClassNew.this);
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
    class ButtonListener implements ActionListener {

        @Override
        
        public void actionPerformed(ActionEvent e) {
            
            String url=null;
           
                     pathToMp3 = data;
                   
            
            try {
                Object s = e.getSource();
                if(s==play)
                {
               
                    if((player.getStatus()== BasicPlayer.PAUSED) || (player.getStatus() == BasicPlayer.STOPPED)||(player.getStatus() == BasicPlayer.PLAYING)||(table.getSelectedRow()>=0 &&(player.getStatus()== BasicPlayer.UNKNOWN)))
                    {
               
                   data = (String) table.getValueAt(table.getSelectedRow(), 0);
                 
                  songNamePlaying = ((String) table.getValueAt(table.getSelectedRow(), 1));
                    player.open(new URL("file:///" + pathToMp3));
                    player.play();
                    nowPlaying.setText("Now Playing: "+((String) table.getValueAt(table.getSelectedRow(), 1)));
                       
                    }
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
                   
                    if(table.getSelectedRow()==table.getRowCount()-1)
                    {
                        table.changeSelection(0, 0, false, false);
                        data = (String) table.getValueAt(0, 0);
                    //table.changeSelection(table.getSelectedRow()+1,0 , false,false);
                    player.open(new URL("file:///" + data));
                    player.play();
                    nowPlaying.setText("Now Playing: "+((String) table.getValueAt(0, 1)));
                    }
                    else if(table.getSelectedRow()>=0)
                    {
                    data = (String) table.getValueAt(table.getSelectedRow()+1, 0);
                    table.changeSelection(table.getSelectedRow()+1,0 , false,false);
                    player.open(new URL("file:///" + data));
                    player.play();
                    nowPlaying.setText("Now Playing: "+((String) table.getValueAt(table.getSelectedRow(), 1)));
                    }
                    else
                    {
                        
                    }
                }
                else
                  {
                     
                       if(table.getSelectedRow()>0)
                       {
                    data = (String) table.getValueAt(table.getSelectedRow()-1, 0);
                    table.changeSelection(table.getSelectedRow()-1,0 , false,false);
                    player.open(new URL("file:///" + data));
                    player.play();
                    nowPlaying.setText("Now Playing: "+((String) table.getValueAt(table.getSelectedRow(), 1)));
                       }
                  }
               
            } catch (MalformedURLException ex) {
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Malformed url");
            } catch (BasicPlayerException ex) {
                System.out.println("BasicPlayer exception");
                Logger.getLogger(StreamPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
   
}
