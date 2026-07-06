package me.akos.musicplayer;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

public class Song {
    private String songTitle;
    private String songArtist;
    private String songLength;
    private String filePath;

    public Song(String filePath) {
        this.filePath = filePath;
        try {
            AudioFile audioFile = AudioFileIO.read(new File(filePath));

            Tag tag = audioFile.getTag();
            if (tag != null) {
                songTitle = new File(filePath).getName().replace(".mp3", "").split("-")[1];
                songArtist = new File(filePath).getName().replace(".mp3", "").split("-")[0];
            }
            else  {
                songTitle = "N/A";
                songArtist = "N/A";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongLength() {
        return songLength;
    }

    public String getFilePath() {
        return filePath;
    }
}
