package me.akos.musicplayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class GUI extends JFrame {

    public static final Color FRAME_COLOR = Color.BLACK;
    public static final Color TEXT_COLOR = Color.WHITE;

    private MusicPlayer musicPlayer;
    private JFileChooser jFileChooser;
    private JLabel songTitle, songArtist;
    private JPanel playbackBtns;
    private JSlider playbackSlider;

    public GUI() {
        super("Music Player");

        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(FRAME_COLOR);

        musicPlayer = new MusicPlayer();
        jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("C:\\Users\\czmor\\Music\\Resentvul"));
        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
        addComponents();
    }

    private void addComponents() {
        addToolbar();

        JLabel songImage = new JLabel(loadImage("src/assets/Screenshot_20260706_223006_Music_Player.jpg"));
        songImage.setBounds(0, 50, getWidth() - 20, 225);
        add(songImage);

        songTitle = new JLabel("Song Title");
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        songArtist = new JLabel("Artist");
        songArtist.setBounds(0, 320, getWidth() - 10, 30);
        songArtist.setFont(new Font("Dialog", Font.PLAIN, 18));
        songArtist.setForeground(TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);

        playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth()/2 - 300/2, 365, 300, 40);
        playbackSlider.setBackground(null);
        add(playbackSlider);

        addPlaybackBtns();
    }

    private void addToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBounds(0, 0, getWidth(), 20);
        toolBar.setFloatable(false);

        JMenuBar menuBar = new JMenuBar();

        JMenu songMenu = new JMenu("Song");
        menuBar.add(songMenu);
        JMenuItem loadSong = new JMenuItem("Load Song");
        loadSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = jFileChooser.showOpenDialog(GUI.this);
                File selectedFile = jFileChooser.getSelectedFile();

                if (result == JFileChooser.APPROVE_OPTION && selectedFile != null) {
                    Song song = new Song(selectedFile.getPath());
                    musicPlayer.loadSong(song);
                    updateSongTitleAndArtist(song);
                    updatePlaybackSlider(song);
                    enablePause();

                }
            }
        });
        songMenu.add(loadSong);

        JMenu playListMenu = new JMenu("Play List");
        menuBar.add(playListMenu);
        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        playListMenu.add(createPlaylist);
        JMenuItem loadPlaylist = new JMenuItem("Load Playlist");
        playListMenu.add(loadPlaylist);

        toolBar.add(menuBar);

        add(toolBar);
    }

    private void addPlaybackBtns() {
        playbackBtns = new JPanel();
        playbackBtns.setBounds(0, 435, getWidth() - 10, 80);
        playbackBtns.setBackground(null);

        JButton prevBtn = new JButton(loadImage("src/assets/previous.png"));
        prevBtn.setBorderPainted(false);
        prevBtn.setBackground(null);
        playbackBtns.add(prevBtn);

        JButton playBtn = new JButton(loadImage("src/assets/play.png"));
        playBtn.setBorderPainted(false);
        playBtn.setBackground(null);
        playBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enablePause();
                musicPlayer.playCurrentSong();
            }
        });
        playbackBtns.add(playBtn);

        JButton pauseBtn = new JButton(loadImage("src/assets/pause.png"));
        pauseBtn.setBorderPainted(false);
        pauseBtn.setBackground(null);
        pauseBtn.setVisible(false);
        pauseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enablePlay();
                musicPlayer.pauseSong();
            }
        });
        playbackBtns.add(pauseBtn);

        JButton nextBtn = new JButton(loadImage("src/assets/next.png"));
        nextBtn.setBorderPainted(false);
        nextBtn.setBackground(null);
        playbackBtns.add(nextBtn);

        add(playbackBtns);
    }

    private ImageIcon loadImage(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            return new ImageIcon(image);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void updateSongTitleAndArtist(Song song) {
        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
    }

    private void enablePause() {
        JButton playBtn = (JButton) playbackBtns.getComponent(1);
        JButton pauseBtn = (JButton) playbackBtns.getComponent(2);

        playBtn.setVisible(false);
        playBtn.setEnabled(false);
        pauseBtn.setVisible(true);
        pauseBtn.setEnabled(true);
    }

    private void enablePlay() {
        JButton playBtn = (JButton) playbackBtns.getComponent(1);
        JButton pauseBtn = (JButton) playbackBtns.getComponent(2);

        playBtn.setVisible(true);
        playBtn.setEnabled(true);
        pauseBtn.setVisible(false);
        pauseBtn.setEnabled(false);
    }

    private void updatePlaybackSlider(Song song) {
        playbackSlider.setMaximum(song.getMp3File().getFrameCount());
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        JLabel labelBeginning = new JLabel("00:00");
        labelBeginning.setFont(new Font("Dialog", Font.BOLD, 18));
        labelBeginning.setForeground(TEXT_COLOR);

        JLabel labelEnd = new JLabel(song.getSongLength());
        labelEnd.setFont(new Font("Dialog", Font.BOLD, 18));
        labelEnd.setForeground(TEXT_COLOR);

        labelTable.put(0, labelBeginning);
        labelTable.put(song.getMp3File().getFrameCount(), labelEnd);

        playbackSlider.setLabelTable(labelTable);
        playbackSlider.setPaintLabels(true);
    }
}
