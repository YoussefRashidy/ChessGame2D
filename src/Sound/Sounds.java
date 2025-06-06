package Sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sounds
{
    private Clip moveSound ;
    private Clip captureSound ;

    public Sounds() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        URL sound = getClass().getResource("/sounds/move-self.wav") ;
        if (sound == null)
        {
            System.err.println("move-self.wav not found");
        }
        else
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound);
            moveSound = AudioSystem.getClip();
            moveSound.open(audioInputStream);
        }
        sound = getClass().getResource("/sounds/capture.wav") ;
        if (sound == null)
        {
            System.err.println("capture.wav not found");
        }
        else
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound);
            captureSound = AudioSystem.getClip();
            captureSound.open(audioInputStream);
        }
    }
    public void playMoveSound()
    {
            moveSound.stop();
            moveSound.setFramePosition(0);
            moveSound.start();
    }

    public void playCaptureSound()
    {
            captureSound.stop();
            captureSound.setFramePosition(0);
            captureSound.start();
    }



}
