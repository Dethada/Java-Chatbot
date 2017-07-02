/*
 * music player for the bot
 */
package chatbot;

import static chatbot.ChatBotGUI_V2.Display;
import static chatbot.ChatBotGUI_V2.notiBar;
import static chatbot.ChatBotGUI_V2.notiBar2;
import static chatbot.ChatBotGUI_V2.musicStatus;
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
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author David
 */
public class music {

    public static boolean stopped;
    public static int songNo;
    long pauseLocation;
    long songTotalLength;
    boolean playing;
    boolean fileChoosen;
    String currentSong;
    String temp;
    String settingsName = "config.tut";
    public static Properties prop = new Properties();

    FileInputStream FIS;
    BufferedInputStream BIS;
    ArrayList<String> songs = new ArrayList<>();
    Random randomGenerator = new Random();
    public static File folder;
    ArrayList<String> playList = new ArrayList<>();
    Player player;

    public void Stop() {

        if (player != null) {
            player.close();
            playing = false;

            Display.setText("Open your music folder to play songs");
            notiBar.setText("Remeber to key in your ATS!");
            notiBar2.setText("Remeber to key in your ATS!");
            musicStatus.setText("Stopped");
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

            Display.setText(currentSong);
            notiBar.setText(currentSong);
            notiBar2.setText(currentSong);
            musicStatus.setText("Playing");
        } catch (FileNotFoundException | JavaLayerException ex) {

        } catch (IOException ex) {
            Logger.getLogger(music.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    player.play();

                    if (player.isComplete()) {
                        songNo++;
                        currentSong = songs.get(songNo);
                        Play(folder + "\\" + currentSong);
                    }
                } catch (JavaLayerException ex) {

                }

            }
        }.start();
    }

    public void Resume() throws IOException {
        // reshuffle playlist if music was stopped
        if (stopped) {
            restart();
            stopped = false;
        } else if (!playing) {
            Display.setText(currentSong);
            notiBar.setText(currentSong);
            notiBar2.setText(currentSong);
            musicStatus.setText("Playing");
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
        }
    }

    public void next() {
        playing = true;
        songNo++;
        currentSong = playList.get(songNo);
        Display.setText(currentSong);
        notiBar.setText(currentSong);
        notiBar2.setText(currentSong);
        musicStatus.setText("Playing");
        pauseLocation = 0;
        Play(folder + "\\" + currentSong);
    }

    public void prev() {
        playing = true;
        songNo--;
        currentSong = playList.get(songNo);
        Display.setText(currentSong);
        notiBar.setText(currentSong);
        notiBar2.setText(currentSong);
        musicStatus.setText("Playing");
        pauseLocation = 0;
        Play(folder + "\\" + currentSong);
    }

    // lets first user choose music dir else jus play from the saved dir
    public void chooseDir() {
        if (!getProp("fileChoosen").equals("yes")) {
            JFileChooser chooser = new JFileChooser("D:\\Media");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            songs.clear();

            int returnVal = chooser.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File songpaths = chooser.getSelectedFile();
                String dirPath = songpaths + "";
                folder = new File(dirPath);
                File[] listOfFiles = folder.listFiles();
                saveProp("musicDir", "" + folder);

                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile() && listOfFile.getName().endsWith(".mp3")) {
                        songs.add(listOfFile.getName());
                    } else if (listOfFile.isDirectory()) {
                        System.out.println("Directory " + listOfFile.getName());
                    }
                }

                getSong();

                currentSong = playList.get(songNo);
                Stop();
                Play(folder + "\\" + currentSong);
                saveProp("fileChoosen", "yes");
            } else {
                System.out.println("No Selection ");
            }
        } else {
            songs.clear();
            folder = new File(getProp("musicDir"));
            File[] listOfFiles = folder.listFiles();

            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile() && listOfFile.getName().endsWith(".mp3")) {
                    songs.add(listOfFile.getName());
                } else if (listOfFile.isDirectory()) {
                    System.out.println("Directory " + listOfFile.getName());
                }
            }

            getSong();

            currentSong = playList.get(songNo);
            Stop();
            Play(folder + "\\" + currentSong);
        }
    }

    // reshuffles playlist
    public void restart() {
        getSong();

        currentSong = playList.get(songNo);
        Play(folder + "\\" + currentSong);
    }

    // gets the songs in the dir and shuffles them into a playlist
    public void getSong() {

        playList.clear();
        for (int i = 0; i < songs.size(); i++) {
            String name = songs.get(randomGenerator.nextInt(songs.size()));

            if (!playList.contains(name)) {
                playList.add(name);
            } else {
                i--;
            }
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

}
