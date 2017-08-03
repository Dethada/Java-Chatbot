/*
 * music player for the bot
 */
package chatbot;

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

    private boolean stopped;
    private int songNo;
    private File folder;
    private long pauseLocation;
    private long songTotalLength;
    private boolean playing;
    private String currentSong;
    private final String settingsName;
    private final Properties prop = new Properties();
    private FileInputStream fis;
    private BufferedInputStream bis;
    private final ArrayList<String> songs;
    private final ArrayList<String> playList;
    private final Random rng = new Random();
    private Player player;

    public Music() {
        this.songs = new ArrayList<>();
        this.playList = new ArrayList<>();
        this.settingsName = "config.tut";
    }

    public void stop() {

        if (player != null) {
            player.close();
            playing = false;

            GUI.setmusicDisplay("Open your music folder to play songs");
            GUI.setnotiBarChat("Remeber to key in your ATS!");
            GUI.setmusicStatus("Stopped");
            GUI.setnoOfSongs("");
        }
    }

    public void pause() {
        if (player != null) {
            GUI.setmusicStatus("Paused");
            playing = false;
            try {
                pauseLocation = fis.available();
                player.close();
            } catch (IOException ex) {
            }
        }
    }

    public void play(String path) {
        playing = true;

        try {
            fis = new FileInputStream(path);
            bis = new BufferedInputStream(fis);

            player = new Player(bis);

            songTotalLength = fis.available();

            setDisplayPlaying();
        } catch (FileNotFoundException | JavaLayerException ex) {
        } catch (IOException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Thread("Music") {
            @Override
            public void run() {
                try {
                    player.play();

                    if (player.isComplete()) {
                        songNo++;
                        currentSong = playList.get(songNo);
                        play(folder + "\\" + currentSong);
                    }
                } catch (JavaLayerException ex) {
                }
            }
        }.start();
    }

    public void resume() throws IOException {
        if (!songs.isEmpty()) {
            // reshuffle playlist if music was stopped
            if (stopped) {
                createPlaylist();
                currentSong = playList.get(songNo);
                play(folder + "\\" + currentSong);
                stopped = false;
            } else if (!playing) {
                setDisplayPlaying();
                try {
                    fis = new FileInputStream(folder + "\\" + currentSong);
                    bis = new BufferedInputStream(fis);

                    player = new Player(bis);

                    fis.skip(songTotalLength - pauseLocation);
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
        play(folder + "\\" + currentSong);
    }

    public void prev() {
        playing = true;
        songNo--;
        currentSong = playList.get(songNo);
        setDisplayPlaying();
        pauseLocation = 0;
        play(folder + "\\" + currentSong);
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
                stop();
                play(folder + "\\" + currentSong);
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
            stop();
            play(folder + "\\" + currentSong);
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

    public String getFolder() {
        return "" + folder;
    }

    public int getSongNo() {
        return songNo;
    }

    public boolean isPlaying() {
        if (stopped) {
            return false;
        } else {
            return true;
        }
    }

    public void setStopped(boolean x) {
        stopped = x;
    }

    private void getSongs(File[] listOfFiles) {
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".mp3")) {
                songs.add(file.getName());
            }
        }
    }

    // gets the songs in the dir and shuffles them into a playlist
    private void createPlaylist() {
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

    private void saveProp(String title, String value) {
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
        GUI.setmusicDisplay(currentSong.substring(0, currentSong.length() - 4));
        GUI.setnotiBarChat(currentSong.substring(0, currentSong.length() - 4));
        GUI.setmusicStatus("Playing");
        GUI.setnoOfSongs(songNo + 1 + "/" + playList.size());
    }

} // End Music class
