/*
 * Copyright (C) 2025 Prof. Dr. David Buzatto
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

import br.com.davidbuzatto.jsge.animation.frame.ImageAnimationFrame;
import br.com.davidbuzatto.jsge.animation.frame.SpriteMapAnimationFrame;
import br.com.davidbuzatto.jsge.image.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface com métodos utilitários para animações.
 * 
 * @author Prof. Dr. David Buzatto
 */
public interface AnimationUtils {
    
    /**
     * Cria uma lista de ImageAnimationFrame a partir de uma lista de imagens.
     * 
     * @param images Uma lista de imagens.
     * @return Uma lista de ImageAnimationFrame
     */
    public static List<ImageAnimationFrame> getImageAnimationFrameList( List<Image> images ) {
        
        List<ImageAnimationFrame> frames = new ArrayList<>();
        
        for ( Image image : images ) {
            frames.add( new ImageAnimationFrame( image ) );
        }
        
        return frames;
        
    }
    
    /**
     * Cria uma lista de SpriteMapAnimationFrame a partir de uma imagem de uma mapa de sprite.
     * 
     * @param spriteMapImage Uma imagem de um mapa de sprite.
     * @param quantity Quantidade de quadros que serão gerados (recortes que serão feitos no mapa de sprites).
     * @param x Coordenada x do início do recorte da imagem do mapa de sprite.
     * @param y Coordenada y do início do recorte da imagem do mapa de sprite.
     * @param width Largura do recorte do mapa de sprite.
     * @param height Altura do recorte do mapa de sprite.
     * @param backwards Se o mapa de sprites deve ser mapeado do fim para o começo.
     * @return Uma lista de SpriteMapAnimationFrame
     */
    public static List<SpriteMapAnimationFrame> getSpriteMapAnimationFrameList( Image spriteMapImage, int quantity, double x, double y, double width, double height, boolean backwards ) {
        
        List<SpriteMapAnimationFrame> frames = new ArrayList<>();
        
        if ( backwards ) {
            for ( int i = quantity - 1; i >= 0; i-- ) {
                frames.add( new SpriteMapAnimationFrame( spriteMapImage, x + i * width, y, width, height ) );
            }
        } else {
            for ( int i = 0; i < quantity; i++ ) {
                frames.add( new SpriteMapAnimationFrame( spriteMapImage, x + i * width, y, width, height ) );
            }
        }
        
        return frames;
        
    }
    
    /**
     * Cria uma lista de SpriteMapAnimationFrame a partir de uma imagem de uma mapa de sprite.
     * A imagem é processada da esquerda para a direita a partir da coordenada inicial fornecida.
     * 
     * @param spriteMapImage Uma imagem de um mapa de sprite.
     * @param quantity Quantidade de quadros que serão gerados (recortes que serão feitos no mapa de sprites).
     * @param x Coordenada x do início do recorte da imagem do mapa de sprite.
     * @param y Coordenada y do início do recorte da imagem do mapa de sprite.
     * @param width Largura do recorte do mapa de sprite.
     * @param height Altura do recorte do mapa de sprite.
     * @return Uma lista de SpriteMapAnimationFrame
     */
    public static List<SpriteMapAnimationFrame> getSpriteMapAnimationFrameList( Image spriteMapImage, int quantity, double x, double y, double width, double height ) {
        return AnimationUtils.getSpriteMapAnimationFrameList( spriteMapImage, quantity, x, y, width, height, false );
    }
    
    /**
     * Cria uma lista de SpriteMapAnimationFrame a partir de uma imagem de um mapa de sprite.
     * 
     * @param spriteMapImage Uma imagem de um mapa de sprite.
     * @param quantity Quantidade de quadros que serão gerados (recortes que serão feitos no mapa de sprites).
     * @param width Largura do recorte do mapa de sprite.
     * @param height Altura do recorte do mapa de sprite.
     * @param backwards Se o mapa de sprites deve ser mapeado do fim para o começo.
     * @return Uma lista de SpriteMapAnimationFrame
     */
    public static List<SpriteMapAnimationFrame> getSpriteMapAnimationFrameList( Image spriteMapImage, int quantity, double width, double height, boolean backwards ) {
        return AnimationUtils.getSpriteMapAnimationFrameList( spriteMapImage, quantity, 0, 0, width, height, backwards );
    }
    
    /**
     * Cria uma lista de SpriteMapAnimationFrame a partir de uma imagem de um mapa de sprite.
     * Os recortes são feitos a partir da coordenada (0, 0) da imagem do mapa de sprite, da esquerda para a direita.
     * 
     * @param spriteMapImage Uma imagem de um mapa de sprite.
     * @param quantity Quantidade de quadros que serão gerados (recortes que serão feitos no mapa de sprites).
     * @param width Largura do recorte do mapa de sprite.
     * @param height Altura do recorte do mapa de sprite.
     * @return Uma lista de SpriteMapAnimationFrame
     */
    public static List<SpriteMapAnimationFrame> getSpriteMapAnimationFrameList( Image spriteMapImage, int quantity, double width, double height ) {
        return AnimationUtils.getSpriteMapAnimationFrameList( spriteMapImage, quantity, 0, 0, width, height, false );
    }
    
    /**
     * Cria uma lista de SpriteMapAnimationFrame a partir de uma imagem de uma mapa de sprite.
     * A quantidade de quadros é gerada dada a imagem e a largura de recorte.
     * 
     * @param spriteMapImage Uma imagem de um mapa de sprite.
     * @param x Coordenada x do início do recorte da imagem do mapa de sprite.
     * @param y Coordenada y do início do recorte da imagem do mapa de sprite.
     * @param width Largura do recorte do mapa de sprite.
     * @param height Altura do recorte do mapa de sprite.
     * @param backwards Se o mapa de sprites deve ser mapeado do fim para o começo.
     * @return Uma lista de SpriteMapAnimationFrame
     */
    public static List<SpriteMapAnimationFrame> getSpriteMapAnimationFrameList( Image spriteMapImage, double x, double y, double width, double height, boolean backwards ) {
        return AnimationUtils.getSpriteMapAnimationFrameList( spriteMapImage, (int) ( spriteMapImage.getWidth() / width ), x, y, width, height, backwards );
    }
    
    /**
     * Cria uma lista de SpriteMapAnimationFrame a partir de uma imagem de uma mapa de sprite.
     * A imagem é processada da esquerda para a direita a partir da coordenada inicial fornecida.
     * A quantidade de quadros é gerada dada a imagem e a largura de recorte.
     * 
     * @param spriteMapImage Uma imagem de um mapa de sprite.
     * @param x Coordenada x do início do recorte da imagem do mapa de sprite.
     * @param y Coordenada y do início do recorte da imagem do mapa de sprite.
     * @param width Largura do recorte do mapa de sprite.
     * @param height Altura do recorte do mapa de sprite.
     * @return Uma lista de SpriteMapAnimationFrame
     */
    public static List<SpriteMapAnimationFrame> getSpriteMapAnimationFrameList( Image spriteMapImage, double x, double y, double width, double height ) {
        return AnimationUtils.getSpriteMapAnimationFrameList( spriteMapImage, x, y, width, height, false );
    }
    
    /**
     * Cria uma lista de SpriteMapAnimationFrame a partir de uma imagem de um mapa de sprite.
     * A quantidade de quadros é gerada dada a imagem e a largura de recorte.
     * 
     * @param spriteMapImage Uma imagem de um mapa de sprite.
     * @param width Largura do recorte do mapa de sprite.
     * @param height Altura do recorte do mapa de sprite.
     * @param backwards Se o mapa de sprites deve ser mapeado do fim para o começo.
     * @return Uma lista de SpriteMapAnimationFrame
     */
    public static List<SpriteMapAnimationFrame> getSpriteMapAnimationFrameList( Image spriteMapImage, double width, double height, boolean backwards ) {
        return AnimationUtils.getSpriteMapAnimationFrameList( spriteMapImage, 0, 0, width, height, backwards );
    }
    
    /**
     * Cria uma lista de SpriteMapAnimationFrame a partir de uma imagem de um mapa de sprite.
     * Os recortes são feitos a partir da coordenada (0, 0) da imagem do mapa de sprite, da esquerda para a direita.
     * A quantidade de quadros é gerada dada a imagem e a largura de recorte.
     * 
     * @param spriteMapImage Uma imagem de um mapa de sprite.
     * @param width Largura do recorte do mapa de sprite.
     * @param height Altura do recorte do mapa de sprite.
     * @return Uma lista de SpriteMapAnimationFrame
     */
    public static List<SpriteMapAnimationFrame> getSpriteMapAnimationFrameList( Image spriteMapImage, double width, double height ) {
        return AnimationUtils.getSpriteMapAnimationFrameList( spriteMapImage, 0, 0, width, height, false );
    }
    
}
