/*
 * Alarm Clock
 */
package chatbot;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author David
 */
public class Clock {

    private final DateTimeFormatter dateFormat;
    private final DateTimeFormatter timeFormat;
    private LocalDateTime currentDateTime;
    private String currentDate;
    private String currentTime;
    private String alarmTime;

    private FileInputStream fis;
    private BufferedInputStream bis;
    private Player player;

    public Clock() {
        this.alarmTime = "";
        this.dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        this.timeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
        start();
    }

    public void setAlarmTime(String time) {
        alarmTime = time;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    private void start() {
        new Thread("Clock") {
            @Override
            public void run() {
                while (true) {
                    currentDateTime = LocalDateTime.now();
                    currentTime = timeFormat.format(currentDateTime);
                    currentDate = dateFormat.format(currentDateTime);

                    GUI.setTime(timeFormat.format(currentDateTime));
                    GUI.setDisplayDate(currentDate);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }

                    if (currentTime.equals(alarmTime)) {
                        GUI.printf("Alarm rang at " + currentTime);
                        GUI.player.pause();
                        ring(Paths.get(".").toAbsolutePath().normalize().toString() + "\\alarm.mp3");
                        GUI.setmusicDisplay("Alarm ringing");
                        GUI.setnotiBarChat("Alarm ringing");
                        GUI.setmusicStatus("Alarm ringing");
                    }
                } // for
            } // run()
        }.start();
    }

    // Sound the alarm
    private void ring(String path) {
        try {
            fis = new FileInputStream(path);
            bis = new BufferedInputStream(fis);

            player = new Player(bis);

        } catch (FileNotFoundException | JavaLayerException ex) {
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    player.play();

                    if (player.isComplete()) {
                    }
                } catch (JavaLayerException ex) {
                }
            }
        }.start();
    }

    public void stopAlarm() {
        if (player != null) {
            player.close();

            GUI.setmusicDisplay("Open your music folder to play songs");
            GUI.setnotiBarChat("Remeber to key in your ATS!");
            GUI.setmusicStatus("Stopped");
            GUI.setnoOfSongs("");
        }
    }
}
