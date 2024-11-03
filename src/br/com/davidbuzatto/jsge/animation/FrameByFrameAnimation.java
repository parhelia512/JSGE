/*
 * Copyright (C) 2024 Prof. Dr. David Buzatto
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
package br.com.davidbuzatto.jsge.animation;

import br.com.davidbuzatto.jsge.animation.frame.AnimationFrame;
import java.util.List;

/**
 * Uma animação quadro a quadro. A partir de um tempo, muda-se o frame 
 * de desenho atual.
 * 
 * @param <FrameType> O tipo do frame da animação.
 * @author Prof. Dr. David Buzatto
 */
public class FrameByFrameAnimation<FrameType extends AnimationFrame> {
    
    private double timeCounter;
    private double timeToNextFrame;
    private double[] timesToNextFrame;
    
    private int currentFrame;
    private int maxFrames;
    
    private List<FrameType> frames;
    
    /**
     * Cria uma animação quadro a quadro.
     * 
     * @param timeToNextFrame Tempo para a transição de um quadro para outro.
     * @param frames Quadros da animação.
     */
    public FrameByFrameAnimation( double timeToNextFrame, List<FrameType> frames ) {
        this.timeToNextFrame = timeToNextFrame;
        this.frames = frames;
        this.maxFrames = frames.size();
    }
    
    /**
     * Cria uma animação quadro a quadro.
     * 
     * @param timesToNextFrame Tempos para a transição dos quadros.
     * @param frames Quadros da animação.
     */
    public FrameByFrameAnimation( double[] timesToNextFrame, List<FrameType> frames ) {
        this.timesToNextFrame = timesToNextFrame;
        this.frames = frames;
        this.maxFrames = frames.size();
    }
    
    /**
     * Atualiza a animação.
     * 
     * @param delta Variação do tempo.
     */
    public void update( double delta ) {
        
        timeCounter += delta;
        
        if ( timesToNextFrame == null ) {
            if ( timeCounter >= timeToNextFrame ) {
                timeCounter = 0;
                currentFrame = ( currentFrame + 1 ) % maxFrames;
            }
        } else {
            if ( timeCounter >= timesToNextFrame[currentFrame] ) {
                timeCounter = 0;
                currentFrame = ( currentFrame + 1 ) % maxFrames;
            }
        }
        
    }
    
    /**
     * Obtém o quadro atual.
     * 
     * @return O quadro atual da animação.
     */
    public FrameType getCurrentFrame() {
        return frames.get( currentFrame );
    }
    
    /**
     * Obtém a posição do quadro atual da animação.
     * 
     * @return A posição do quadro atual da animação.
     */
    public int getCurrentFramePosition() {
        return currentFrame;
    }
    
    /**
     * Obtém um quadro específico da animação.
     * 
     * @param index O índice do quadro.
     * @return Um quadro específico da animação ou null caso não exista.
     */
    public FrameType getFrame( int index ) {
        if ( index >= 0 && index < frames.size() ) {
            return frames.get( index );
        }
        return null;
    }
    
}
