package scheduler_2;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Sound {
    URL openSoundUrl;
    AudioClip openSound;
    
    public Sound() {
        openSoundUrl = this.getClass().getResource("/sound/mechanicLowSound.wav");
        openSound = Applet.newAudioClip(openSoundUrl);
    }
    public void openSound() {
            openSound.play();
    }
    
}








