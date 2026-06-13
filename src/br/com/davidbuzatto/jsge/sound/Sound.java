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
import br.com.davidbuzatto.jsge.core.utils.CoreUtils;
import br.com.davidbuzatto.jsge.math.MathUtils;
import com.goxr3plus.streamplayer.enums.Status;
import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.FloatControl;

/**
 * A class for representing sounds.
 * Use it for short sounds, less than 10 seconds long.
 *
 * @author Prof. Dr. David Buzatto
 */
public class Sound {
    
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
        
        void playWithFile() {
            try {
                setSpeedFactor( pitch );
                open( file );
                play();
                applyPlaybackSettings();
            } catch ( StreamPlayerException exc ) {
                EngineFrame.traceLogError( CoreUtils.stackTraceToString( exc ) );
            }
        }

        void playWithInputStream() {
            try {
                setSpeedFactor( pitch );
                open( is );
                play();
                applyPlaybackSettings();
            } catch ( StreamPlayerException exc ) {
                EngineFrame.traceLogError( CoreUtils.stackTraceToString( exc ) );
            }
        }

        void playWithUrl() {
            try {
                setSpeedFactor( pitch );
                open( url );
                play();
                applyPlaybackSettings();
            } catch ( StreamPlayerException exc ) {
               EngineFrame.traceLogError( CoreUtils.stackTraceToString( exc ) );
            }
        }

        void applyPlaybackSettings() {
            setGain( volume * EngineFrame.getMasterVolume() );
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
            if ( spe.getPlayerStatus() == Status.STOPPED ) {
                reset();
            }
        }
        
    }
    
    private static ExecutorService executor = Executors.newFixedThreadPool( 10 );
    
    private File file;
    private InputStream is;
    private URL url;

    private double volume;
    private double pan;
    private double pitch;

    /**
     * Puts the sound in a valid state.
     */
    private Sound() {
        this.volume = 1.0;
        this.pan = 0.0;
        this.pitch = 1.0;
    }

    /**
     * Creates a sound using the file path.
     *
     * @param filePath Path to the file.
     */
    public Sound( String filePath ) {
        this();
        this.file = new File( filePath );
    }

    /**
     * Creates a sound using an input stream.
     *
     * @param is Input stream.
     */
    public Sound( InputStream is ) {
        this();
        this.is = is;
    }

    /**
     * Creates a sound using a URL.
     *
     * @param url URL.
     */
    public Sound( URL url ) {
        this();
        this.url = url;
    }
    
    /**
     * Plays the sound.
     */
    public void play() {
        executor.execute( () -> {
            if ( file != null ) {
                InternalPlayer p = new InternalPlayer( file );
                p.playWithFile();
            } else if ( is != null ) {
                InternalPlayer p = new InternalPlayer( is );
                p.playWithInputStream();
            } else if ( url != null ) {
                InternalPlayer p = new InternalPlayer( url );
                p.playWithUrl();
            }
        });
    }

    /**
     * Sets the volume of the sound.
     *
     * @param volume The volume of the sound, ranging from 0.0 to 1.0.
     */
    public void setVolume( double volume ) {
        this.volume = MathUtils.clamp( volume, 0.0, 1.0 );
    }

    /**
     * Sets the stereo panning of the sound. Panning requires a stereo audio
     * source; mono audio cannot be panned.
     *
     * @param pan The panning of the sound, ranging from -1.0 (left) to 1.0
     * (right), where 0.0 is the center.
     */
    public void setPan( double pan ) {
        this.pan = MathUtils.clamp( pan, -1.0, 1.0 );
    }

    /**
     * Sets the pitch of the sound. The pitch also changes the playback speed.
     * Pitch is not supported for OGG/Vorbis audio; it works with PCM (WAV) and
     * MP3.
     *
     * @param pitch The pitch of the sound, where 1.0 is the original pitch.
     */
    public void setPitch( double pitch ) {
        this.pitch = pitch < 0.0 ? 0.0 : pitch;
    }

}
