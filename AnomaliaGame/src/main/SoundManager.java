package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class SoundManager {
    private static final Map<String, Clip> cache = new ConcurrentHashMap<>();

    public static void play(String path) {
        try {
            Clip clip = cache.computeIfAbsent(path, SoundManager::loadClip);
            if (clip == null) return;
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            System.out.println("Falha ao tocar som: " + path);
        }
    }

    private static Clip loadClip(String path) {
        try {
            InputStream is = new BufferedInputStream(
                SoundManager.class.getClassLoader().getResourceAsStream(path)
            );
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (Exception e) {
            return null;
        }
    }
}