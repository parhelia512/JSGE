/*
 * Copyright (C) 2026 Prof. Dr. David Buzatto
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.davidbuzatto.jsge.sound;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.MathUtils;
import br.com.davidbuzatto.jsge.core.utils.CoreUtils;
import com.goxr3plus.streamplayer.enums.Status;
import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.FloatControl;

/**
 * A class for representing music tracks.
 * Use it for music, i.e., sounds with a duration greater than 10 seconds.
 *
 * @author Prof. Dr. David Buzatto
 */
public class Music {
    
    private static class GainControl extends FloatControl {
        public GainControl() {
            super( FloatControl.Type.MASTER_GAIN, 0.0f, 1.0f, 0.1f, 1, 1.0f, "" );
        }
    }
    
    private class InternalPlayer extends StreamPlayer implements StreamPlayerListener {

        File file;
        InputStream is;
        URL url;

        InternalPlayer() {
            getOutlet().setGainControl( new GainControl() );
            addStreamPlayerListener( this );
        }

        InternalPlayer( File file ) {
            this();
            this.file = file;
        }

        InternalPlayer( InputStream is ) {
            this();
            this.is = is;
        }

        InternalPlayer( URL url ) {
            this();
            this.url = url;
        }

        void playNow() {
            try {
                setSpeedFactor( pitch );
                boolean ok = false;
                if ( file != null ) {
                    open( file );
                    ok = true;
                } else if ( is != null ) {
                    open( is );
                    ok = true;
                } else if ( url != null ) {
                    open( url );
                    ok = true;
                }
                if ( ok ) {
                    play();
                    applyPlaybackSettings();
                }
            } catch ( StreamPlayerException exc ) {
                EngineFrame.traceLogError(CoreUtils.stackTraceToString( exc ) );
            }
        }

        void applyPlaybackSettings() {
            setGain( getEffectiveGain() );
            setBalance( (float) pan );
            setPan( pan );
        }

        @Override
        public void opened( Object o, Map<String, Object> map ) {
        }

        @Override
        public void progress( int i, long l, byte[] bytes, Map<String, Object> map ) {
        }

        @Override
        public void statusUpdated( StreamPlayerEvent spe ) {
            if ( spe.getPlayerStatus() == Status.EOM && looping ) {
                executor.execute( () -> {
                    playNow();
                });
            }
        }

    }

    private static ExecutorService executor = Executors.newFixedThreadPool( 10 );
    private static final List<Music> ACTIVE_MUSIC = new CopyOnWriteArrayList<>();

    private InternalPlayer internalPlayer;
    private double volume;
    private double pan;
    private double pitch;
    private boolean looping;
    
    /**
     * Puts the music in a valid state.
     */
    private Music() {
        this.volume = 1.0;
        this.pan = 0.0;
        this.pitch = 1.0;
        this.looping = false;
        ACTIVE_MUSIC.add( this );
    }

    /**
     * Creates a music track using the file path.
     *
     * @param filePath Path to the file.
     */
    public Music( String filePath ) {
        this();
        internalPlayer = new InternalPlayer( new File( filePath ) );
    }

    /**
     * Creates a music track using an input stream.
     *
     * @param is Input stream.
     */
    public Music( InputStream is ) {
        this();
        internalPlayer = new InternalPlayer( is );
    }

    /**
     * Creates a music track using a URL.
     *
     * @param url URL
     */
    public Music( URL url ) {
        this();
        internalPlayer = new InternalPlayer( url );
    }

    /**
     * Unloads a music track, releasing its resources.
     */
    public void unload() {
        ACTIVE_MUSIC.remove( this );
        internalPlayer.reset();
    }
    
    /**
     * Plays the music track.
     */
    public void play() {
        executor.execute( () -> {
            internalPlayer.playNow();
        });
    }
    
    /**
     * Stops playing the music track.
     */
    public void stop() {
        internalPlayer.stop();
    }
    
    /**
     * Pauses the music track.
     */
    public void pause() {
        internalPlayer.pause();
    }
    
    /**
     * Resumes playback of the music track.
     */
    public void resume() {
        internalPlayer.resume();
    }
    
    /**
     * Checks whether the music track is playing.
     *
     * @return True if the music track is playing, false otherwise.
     */
    public boolean isPlaying() {
        return internalPlayer.isPlaying();
    }
    
    /**
     * Checks whether the music track is stopped.
     *
     * @return True if the music track is stopped, false otherwise.
     */
    public boolean isStopped() {
        return internalPlayer.isStopped();
    }
    
    /**
     * Checks whether the music track is paused.
     *
     * @return True if the music track is paused, false otherwise.
     */
    public boolean isPaused() {
        return internalPlayer.isPaused();
    }
    
    /**
     * Checks whether the music track is seeking.
     *
     * @return True if the music track is seeking, false otherwise.
     */
    public boolean isSeeking() {
        return internalPlayer.isSeeking();
    }
    
    /**
     * Sets the volume of the music track.
     *
     * @param volume The volume of the music track, ranging from 0.0 to 1.0.
     */
    public void setVolume( double volume ) {
        this.volume = MathUtils.clamp( volume, 0.0, 1.0 );
        applyVolume();
    }

    /**
     * Sets the stereo panning of the music track. Panning requires a stereo
     * audio source; mono audio cannot be panned.
     *
     * @param pan The panning of the music track, ranging from -1.0 (left) to
     * 1.0 (right), where 0.0 is the center.
     */
    public void setPan( double pan ) {
        this.pan = MathUtils.clamp( pan, -1.0, 1.0 );
        internalPlayer.setBalance( (float) this.pan );
        internalPlayer.setPan( this.pan );
    }

    /**
     * Sets the pitch of the music track. The pitch also changes the playback
     * speed. As the pitch is defined while the audio line is being created, a
     * change only takes effect the next time the track starts playing. Pitch is
     * not supported for OGG/Vorbis audio; it works with PCM (WAV) and MP3.
     *
     * @param pitch The pitch of the music track, where 1.0 is the original
     * pitch.
     */
    public void setPitch( double pitch ) {
        this.pitch = pitch < 0.0 ? 0.0 : pitch;
        internalPlayer.setSpeedFactor( this.pitch );
    }

    /**
     * Sets whether the music track should restart automatically when it
     * reaches its end.
     *
     * @param looping True to loop the music track, false otherwise.
     */
    public void setLooping( boolean looping ) {
        this.looping = looping;
    }

    /**
     * Checks whether the music track is set to loop.
     *
     * @return True if the music track is set to loop, false otherwise.
     */
    public boolean isLooping() {
        return looping;
    }

    /**
     * Computes the effective gain of the music track, combining its own volume
     * with the engine master volume.
     *
     * @return The effective gain, ranging from 0.0 to 1.0.
     */
    private double getEffectiveGain() {
        double gain = volume * EngineFrame.getMasterVolume();
        if ( gain <= 0.01 ) {
            gain = 0;
        }
        return gain;
    }

    /**
     * Applies the current effective gain to the internal player.
     */
    private void applyVolume() {
        internalPlayer.setGain( getEffectiveGain() );
    }

    /**
     * Reapplies the effective gain of every active music track. Used by the
     * engine when the master volume changes.
     */
    public static void refreshMasterVolume() {
        for ( Music music : ACTIVE_MUSIC ) {
            music.applyVolume();
        }
    }
    
    /**
     * Seeks to a position in the music track. The underlying player seeks in
     * whole seconds, so the fractional part of the position is discarded.
     *
     * @param position Position in seconds of the desired moment.
     */
    public void seek( double position ) {
        try {
            internalPlayer.seekTo( (int) position );
        } catch ( StreamPlayerException exc ) {
            EngineFrame.traceLogError(CoreUtils.stackTraceToString( exc ) );
        }
    }

    /**
     * Gets the duration of the music track.
     *
     * @return Duration of the music track in seconds.
     */
    public double getTimeLength() {
        return internalPlayer.getDurationInMilliseconds() / 1000.0;
    }

    /**
     * Gets the elapsed playback time of the music track.
     *
     * @return The elapsed playback time in seconds.
     */
    public double getTimePlayed() {
        return internalPlayer.getEncodedStreamPosition() / (double) internalPlayer.getTotalBytes() * ( internalPlayer.getDurationInMilliseconds() / 1000.0 );
    }
    
}
