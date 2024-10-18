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
package br.com.davidbuzatto.jsge.geom;

import br.com.davidbuzatto.jsge.core.Engine;
import java.awt.Color;
import java.awt.geom.Path2D;

/**
 * Classe para representação de um caminho em duas dimensões.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Path implements Drawable {
    
    public Path2D.Double path = new Path2D.Double();

    /**
     * Move para a coordenada desejada.
     * 
     * @param x coordenada x do ponto desejado.
     * @param y coordenada y do ponto desejado.
     */
    public void moveTo( double x, double y ) {
        path.moveTo( x, y );
    }

    /**
     * Cria uma linha do ponto atual até o ponto desejado.
     * 
     * @param x coordenada x do ponto desejado.
     * @param y coordenada y do ponto desejado.
     */
    public void lineTo( double x, double y ) {
        path.lineTo( x, y );
    }

    /**
     * Cria uma curva Bézier quadrática do ponto atual até o ponto desejado.
     * 
     * @param cx coordenada x do ponto de controle.
     * @param cy coordenada y do ponto de controle.
     * @param x coordenada x do ponto desejado.
     * @param y coordenada y do ponto desejado.
     */
    public void quadTo( double cx, double cy, double x, double y ) {
        path.quadTo( cx, cy, x, y );
    }

    /**
     * Cria uma curva Bézier cúbica do ponto atual até o ponto desejado.
     * 
     * @param c1x coordenada x do primeiro ponto de controle.
     * @param c1y coordenada y do primeiro ponto de controle.
     * @param c2x coordenada x do segundo ponto de controle.
     * @param c2y coordenada y do segundo ponto de controle.
     * @param x coordenada x do ponto desejado.
     * @param y coordenada y do ponto desejado.
     */
    public void cubicTo( double c1x, double c1y, double c2x, double c2y, double x, double y ) {
        path.curveTo( c1x, c1y, c2x, c2y, x, y );
    }

    /**
     * Fecha o caminho, ligando com uma linha do ponto inicial até o último ponto criado.
     */
    public void closePath() {
        path.closePath();
    }

    @Override
    public void draw( Engine engine, Color color ) {
        engine.drawPath( this, color );
    }

    @Override
    public void fill( Engine engine, Color color ) {
        engine.fillPath( this, color );
    }

    @Override
    public String toString() {
        return path.toString();
    }

}
