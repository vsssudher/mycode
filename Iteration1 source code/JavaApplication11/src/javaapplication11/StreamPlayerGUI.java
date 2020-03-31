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
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.List;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
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
import javax.swing.Box;
import javax.swing.JButton;
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
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import sun.audio.AudioStream;

/**
 *
 * @author sudheer and apoorva
 */
public class StreamPlayerGUI extends JFrame {
    
    BasicPlayer player;
    JTable table;
    JScrollPane scrollPane; 
    JPanel main;
    JButton play,pause,next,prev,stop;//the three buttons
    JLabel  nowPlaying;
    private JTextField filename = new JTextField();
    String allTitle,data,pathToMp3,title;
    int CurrentSelectedRow;
    ButtonListener bl;
    String songNamePlaying;
    
    public StreamPlayerGUI() throws SQLException {
        player = new BasicPlayer();
        main = new JPanel();
      
        //FlowLayout layoutManager = new FlowLayout(FlowLayout.RIGHT);
       // FlowLayout layoutManager = new FlowLayout(FlowLayout.RIGHT);
        bl = new ButtonListener();
        
        //create the table
        String[] columns = {"File", "Title","Artist","Album","Years","Comment","Genre"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table = new JTable(model);
        
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
                         Statement stmt=con.createStatement();
        
           stmt.executeUpdate("INSERT INTO PROJECT.MUSICPLAYER (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
           
                    model.insertRow(0, new Object[]{f.getAbsolutePath(), id3v2Tag.getTitle(), id3v2Tag.getArtist(),id3v2Tag.getAlbum(),id3v2Tag.getYear(),id3v2Tag.getComment() , id3v2Tag.getGenreDescription()});
                        row++;
                                            
                        }
                                         
                                        } else {
                                             //ADD SONG
                   Mp3File mp3file = null;
                     mp3file = new Mp3File(f.getAbsolutePath());
                        if (mp3file.hasId3v2Tag()) {
                        ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                        String album = id3v2Tag.getAlbum();
                         Statement stmt=con.createStatement();
        
           stmt.executeUpdate("INSERT INTO PROJECT.MUSICPLAYER (FILEPATH,TITLE,ARTIST,ALBUM,YEARS,COMMENT,GENRE) "
          +"VALUES ('"+f.getAbsolutePath()+"', '"+id3v2Tag.getTitle()+"', '"+id3v2Tag.getArtist()+"', '"+id3v2Tag.getAlbum()+"', '"+id3v2Tag.getYear()+"', '"+id3v2Tag.getComment()+"', '"+id3v2Tag.getGenreDescription()+"')");
            
                    model.insertRow(row, new Object[]{f.getAbsolutePath(), id3v2Tag.getTitle(), id3v2Tag.getArtist(),id3v2Tag.getAlbum(),id3v2Tag.getYear(), id3v2Tag.getComment(), id3v2Tag.getGenreDescription()});
                        row++;
                                            
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
    
      
    
        //Display Menu
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem addSong = new JMenuItem("Add a song");
        //Add Song in Menu
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
        
        Container containerPane = this.getContentPane();
        containerPane.setLayout(new BorderLayout());
        this.setJMenuBar(menubar);
        JScrollPane scrollPane = new JScrollPane(table);
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
                        
                        String album = id3v2Tag.getAlbum();
                        title =  id3v2Tag.getTitle();
                         Statement stmt = con.createStatement();
                         
                           
                         
                         
                         
                         
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
                        Boolean songNotExist = true;
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
