package me.akos.musicplayer;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class MusicPlayer extends PlaybackListener {
    private  static final Object playSignal = new Object();
    private GUI gui;
    private Song currentSong;
    public Song getCurrentSong() {
        return currentSong;
    }
    private AdvancedPlayer advancedPlayer;
    private boolean isPaused;
    private int currentFrame;
    public void setCurrentFrame(int frame) {
        currentFrame = frame;
    }
    private int currentTimeMilli;
    public void setCurrentTimeMilli(int timeMilli) {
        currentTimeMilli = timeMilli;
    }

    public MusicPlayer(GUI gui) {
        this.gui = gui;
    }

    public void loadSong(Song song) {
        currentSong = song;

        if (currentSong != null) {
            playCurrentSong();
        }
    }

    public  void playCurrentSong() {
        if (currentSong == null) return;
        try {
            FileInputStream fileInputStream = new FileInputStream(currentSong.getFilePath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            advancedPlayer = new AdvancedPlayer(bufferedInputStream);
            advancedPlayer.setPlayBackListener(this);
            startMusicThread();
            startPlaybackSliderThread();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseSong() {
        if (advancedPlayer != null) {
            isPaused = true;
            stopSong();
        }
    }

    public void stopSong() {
        if (advancedPlayer != null) {
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer = null;
        }
    }

    private void startMusicThread() {
        new  Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(isPaused) {
                        synchronized (playSignal) {
                            isPaused = false;
                            playSignal.notify();
                        }
                        advancedPlayer.play(currentFrame, Integer.MAX_VALUE);
                    }
                    else  {
                        advancedPlayer.play();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startPlaybackSliderThread() {
        new  Thread(new Runnable() {
            @Override
            public void run() {
                if (isPaused) {
                    try {
                        synchronized (playSignal) {
                            playSignal.wait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                while(!isPaused) {
                    try {
                        currentTimeMilli++;
                        int calculatedFrame = (int) ((double)currentTimeMilli * 1.8 * currentSong.getFrameRate());
                        gui.setPlaybackSliderValue(calculatedFrame);
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void playbackStarted(PlaybackEvent evt) {
        System.out.println("playback Started");
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        System.out.println("Playback Finished");
        if (isPaused) {
            currentFrame += (int) ((double) evt.getFrame() * currentSong.getFrameRate());

        }
    }
}
