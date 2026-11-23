
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    private final Map<String, Clip> soundEffects;

    private Clip backgroundMusic;

    private float musicVolume;
    private float sfxVolume;

    private boolean musicEnabled;
    private boolean sfxEnabled;

    public AudioManager() {
        soundEffects = new HashMap<>();

        musicVolume = 0.7f;
        sfxVolume = 0.8f;

        musicEnabled = true;
        sfxEnabled = true;
    }

    public void playMusic(String path) {
        stopMusic();

        if (!musicEnabled) {
            return;
        }

        try {
            URL resource = getClass().getResource(path);

            if (resource == null) {
                System.err.println("Music not found: " + path);
                return;
            }

            try (AudioInputStream audio =
                         AudioSystem.getAudioInputStream(resource)) {

                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audio);

                setVolume(backgroundMusic, musicVolume);

                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusic.start();
            }

        } catch (Exception e) {
            System.err.println("Failed to play music: " + path);
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.close();
            backgroundMusic = null;
        }
    }

    public void loadSoundEffect(String name, String path) {
        try {
            URL resource = getClass().getResource(path);

            if (resource == null) {
                System.err.println("Sound effect not found: " + path);
                return;
            }

            try (AudioInputStream audio =
                         AudioSystem.getAudioInputStream(resource)) {

                Clip clip = AudioSystem.getClip();
                clip.open(audio);

                soundEffects.put(name, clip);
            }

        } catch (Exception e) {
            System.err.println("Failed to load sound effect: " + path);
            e.printStackTrace();
        }
    }

    public void playSoundEffect(String name) {
        if (!sfxEnabled) {
            return;
        }

        Clip clip = soundEffects.get(name);

        if (clip == null) {
            return;
        }

        if (clip.isRunning()) {
            clip.stop();
        }

        clip.setFramePosition(0);
        setVolume(clip, sfxVolume);
        clip.start();
    }

    private void setVolume(Clip clip, float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl control = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);

            float safeVolume = Math.max(0.0001f, Math.min(1.0f, volume));

            float decibels = (float) (
                    20.0 * Math.log10(safeVolume)
            );

            decibels = Math.max(
                    control.getMinimum(),
                    Math.min(control.getMaximum(), decibels)
            );

            control.setValue(decibels);
        }
    }

    public void setMusicVolume(float volume) {
        musicVolume = Math.max(0.0f, Math.min(1.0f, volume));

        if (backgroundMusic != null) {
            setVolume(backgroundMusic, musicVolume);
        }
    }

    public void setSfxVolume(float volume) {
        sfxVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    public void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;

        if (!enabled) {
            stopMusic();
        }
    }

    public void setSfxEnabled(boolean enabled) {
        sfxEnabled = enabled;
    }

    public void dispose() {
        stopMusic();

        for (Clip clip : soundEffects.values()) {
            clip.stop();
            clip.close();
        }

        soundEffects.clear();
    }
}
