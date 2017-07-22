/*
 * music player for the bot
 */
package chatbot;

import static chatbot.ChatBot.Display;
import static chatbot.ChatBot.musicStatus;
import static chatbot.ChatBot.noOfSongs;
import static chatbot.ChatBot.notiBarChat;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author David
 */
public class Music {

    boolean stopped;
    int songNo;
    File folder;
    private long pauseLocation;
    private long songTotalLength;
    private boolean playing;
    private String currentSong;
    private final String settingsName;
    private final Properties prop = new Properties();
    private FileInputStream FIS;
    private BufferedInputStream BIS;
    private final ArrayList<String> songs;
    private final ArrayList<String> playList;
    private final Random rng = new Random();
    private Player player;

    public Music() {
        this.songs = new ArrayList<>();
        this.playList = new ArrayList<>();
        this.settingsName = "config.tut";
    }

    public void Stop() {

        if (player != null) {
            player.close();
            playing = false;

            Display.setText("Open your music folder to play songs");
            notiBarChat.setText("Remeber to key in your ATS!");
            musicStatus.setText("Stopped");
            noOfSongs.setText("");
        }
    }

    public void Pause() {
        if (player != null) {
            musicStatus.setText("Paused");
            playing = false;
            try {
                pauseLocation = FIS.available();
                player.close();
            } catch (IOException ex) {
            }
        }
    }

    public void Play(String path) {
        playing = true;

        try {
            FIS = new FileInputStream(path);
            BIS = new BufferedInputStream(FIS);

            player = new Player(BIS);

            songTotalLength = FIS.available();

            setDisplayPlaying();
        } catch (FileNotFoundException | JavaLayerException ex) {
        } catch (IOException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    player.play();

                    if (player.isComplete()) {
                        songNo++;
                        currentSong = playList.get(songNo);
                        Play(folder + "\\" + currentSong);
                    }
                } catch (JavaLayerException ex) {
                }
            }
        }.start();
    }

    public void Resume() throws IOException {
        if (!songs.isEmpty()) {
            // reshuffle playlist if music was stopped
            if (stopped) {
                createPlaylist();
                currentSong = playList.get(songNo);
                Play(folder + "\\" + currentSong);
                stopped = false;
            } else if (!playing) {
                setDisplayPlaying();
                try {
                    FIS = new FileInputStream(folder + "\\" + currentSong);
                    BIS = new BufferedInputStream(FIS);

                    player = new Player(BIS);

                    FIS.skip(songTotalLength - pauseLocation);
                    pauseLocation = 0;
                } catch (JavaLayerException ex) {
                }

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            player.play();
                        } catch (JavaLayerException ex) {
                        }
                    }
                }.start();
            } // else if
        } // if
    }

    public void next() {
        playing = true;
        songNo++;
        currentSong = playList.get(songNo);
        setDisplayPlaying();
        pauseLocation = 0;
        Play(folder + "\\" + currentSong);
    }

    public void prev() {
        playing = true;
        songNo--;
        currentSong = playList.get(songNo);
        setDisplayPlaying();
        pauseLocation = 0;
        Play(folder + "\\" + currentSong);
    }

    // lets first user choose music dir else jus play from the saved dir
    public void chooseDir() throws IndexOutOfBoundsException, NullPointerException {
        // if directory is not yet set
        if (!getProp("fileChoosen").equals("yes")) {
            // Open directory chooser
            JFileChooser chooser = new JFileChooser("D:\\Media");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

            int option = chooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                folder = new File("" + chooser.getSelectedFile());
                File[] listOfFiles = folder.listFiles();

                getSongs(listOfFiles);
                createPlaylist();

                currentSong = playList.get(songNo);
                Stop();
                Play(folder + "\\" + currentSong);
                saveProp("musicDir", "" + folder);
                saveProp("fileChoosen", "yes");
            } else {
                JOptionPane.showMessageDialog(null, "No selection", "Error", 0);
            }
            // if directory is already set
        } else {
            songs.clear();
            folder = new File(getProp("musicDir"));
            File[] listOfFiles = folder.listFiles();

            getSongs(listOfFiles);
            createPlaylist();

            currentSong = playList.get(songNo);
            Stop();
            Play(folder + "\\" + currentSong);
        }
    }

    public void getSongs(File[] listOfFiles) {
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".mp3")) {
                songs.add(file.getName());
            }
        }
    }

    // gets the songs in the dir and shuffles them into a playlist
    public void createPlaylist() {
        songNo = 0;
        playList.clear();
        for (int i = 0; i < songs.size(); i++) {
            String name = songs.get(rng.nextInt(songs.size()));

            if (!playList.contains(name)) {
                playList.add(name);
            } else {
                i--;
            }
        }
    }

    // changes the stored music dir
    public void changeDir() {
        JFileChooser chooser = new JFileChooser("D:\\Media");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File songpaths = chooser.getSelectedFile();
            String dirPath = songpaths + "";
            folder = new File(dirPath);
            saveProp("musicDir", "" + folder);
        }
    }

    public void saveProp(String title, String value) {
        try {
            prop.setProperty(title, value);
            prop.store(new FileOutputStream(settingsName), null);
        } catch (IOException e) {
        }
    }

    public String getProp(String title) {
        String value = "";

        try {
            prop.load(new FileInputStream(settingsName));
            value = prop.getProperty(title);
        } catch (IOException e) {
        }

        return value;
    }

    private void setDisplayPlaying() {
        Display.setText(currentSong.substring(0, currentSong.length() - 4));
        notiBarChat.setText(currentSong.substring(0, currentSong.length() - 4));
        musicStatus.setText("Playing");
        noOfSongs.setText(songNo + 1 + "/" + playList.size());
    }

} // End Music class
