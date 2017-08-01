/*
 * Clock
 */
package chatbot;

import static chatbot.ChatBot.doc;
import static chatbot.ChatBot.mc;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;

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
    private final Music mcAlarm;

    public Clock() {
        this.alarmTime = "";
        this.mcAlarm = new Music();
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
    
    public void stopAlarm() {
        mcAlarm.Stop();
    }

    private void start() {
        // Clock
        new Thread("Clock") {
            @Override
            public void run() {
                while (true) {
                    currentDateTime = LocalDateTime.now();
                    currentTime = timeFormat.format(currentDateTime);
                    currentDate = dateFormat.format(currentDateTime);

                    ChatBot.setTime(timeFormat.format(currentDateTime));
                    ChatBot.setDisplayDate(currentDate);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }

                    if (currentTime.equals(alarmTime)) {
                        try {
                            doc.insertString(doc.getLength(), "Chatbot: Alarm rang at " + currentTime + "\n", null);
                        } catch (BadLocationException ex) {
                            Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        mc.Pause();
                        System.out.println(Paths.get(".").toAbsolutePath().normalize().toString() + "\\alarm.mp3");
                        mcAlarm.Play(Paths.get(".").toAbsolutePath().normalize().toString() + "\\alarm.mp3");
                        ChatBot.setmusicDisplay("Alarm ringing");
                        ChatBot.setnotiBarChat("Alarm ringing");
                        ChatBot.setmusicStatus("Alarm ringing");
                    }

                } // for
            } // run()
        }.start();
    }
}
