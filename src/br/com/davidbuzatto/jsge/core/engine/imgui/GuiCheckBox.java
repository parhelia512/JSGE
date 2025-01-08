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
package br.com.davidbuzatto.jsge.core.engine.imgui;

import br.com.davidbuzatto.jsge.collision.CollisionUtils;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.math.Vector2;

/**
 * Um componente caixa de seleção.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class GuiCheckBox extends GuiTextComponent {
    
    protected boolean selected;
    
    public GuiCheckBox( double x, double y, double width, double height, String text, EngineFrame engine ) {
        this.bounds = new Rectangle( x, y, width, height );
        this.text = text;
        this.e = engine;
    }
    
    @Override
    public void update( double delta ) {
        
        if ( visible && enabled ) {
            
            Vector2 mousePos = e.getMousePositionPoint();

            if ( CollisionUtils.checkCollisionPointRectangle( mousePos, bounds ) ) {
                mouseState = GuiComponentState.MOUSE_OVER;
                if ( e.isMouseButtonPressed( e.MOUSE_BUTTON_LEFT ) ) {
                    mouseState = GuiComponentState.MOUSE_PRESSED;
                    selected = !selected;
                } else if ( e.isMouseButtonDown( e.MOUSE_BUTTON_LEFT ) ) {
                    mouseState = GuiComponentState.MOUSE_DOWN;
                }
            } else {
                mouseState = GuiComponentState.MOUSE_OUT;
            }
            
        } else {
            mouseState = GuiComponentState.MOUSE_OUT;
        }
        
    }
    
    @Override
    public void draw() {
        
        if ( visible ) {
            
            e.setStrokeLineWidth( 1 );

            if ( enabled ) {

                switch ( mouseState ) {
                    case MOUSE_OUT:
                        drawBoundsAsRectangle( MOUSE_OUT_BORDER_COLOR, selected );
                        drawTextAfterBounds( MOUSE_OUT_TEXT_COLOR );
                        break;
                    case MOUSE_OVER:
                        drawBoundsAsRectangle( MOUSE_OVER_BORDER_COLOR, selected );
                        drawTextAfterBounds( MOUSE_OVER_TEXT_COLOR );
                        break;
                    case MOUSE_PRESSED:
                        drawBoundsAsRectangle( MOUSE_DOWN_BORDER_COLOR, selected );
                        drawTextAfterBounds( MOUSE_DOWN_TEXT_COLOR );
                        break;
                    case MOUSE_DOWN:
                        drawBoundsAsRectangle( MOUSE_DOWN_BORDER_COLOR, selected );
                        drawTextAfterBounds( MOUSE_DOWN_TEXT_COLOR );
                        break;
                }

            } else {
                drawBoundsAsRectangle( DISABLED_BORDER_COLOR, selected );
                drawTextAfterBounds( DISABLED_TEXT_COLOR );
            }
            
        }
        
    }

    public void setSelected( boolean selected ) {
        this.selected = selected;
    }
    
    public boolean isSelected() {
        return this.selected;
    }
    
    public boolean isPressed() {
        return mouseState == GuiComponentState.MOUSE_PRESSED;
    }
    
}
