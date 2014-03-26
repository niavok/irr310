package com.irr310.client.graphics.ether.activities.shipcamera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.Ship;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.View;
import com.irr310.server.ai.ShipDriver;
import com.irr310.server.engine.system.SystemEngine;

import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent.KeyAction;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class ShipDriverView extends View {

    private int mBaseMouseX;
    private int mBaseMouseY;
    private boolean mSteering;
    private int mCurrentMouseY;
    private int mCurrentMouseX;
    
    public ShipDriverView(Ship ship, SystemEngine systemEngine) {
        
        final ShipDriver driver = systemEngine.getDriver(ship);
        layoutParams.setLayoutWidthMeasure(LayoutMeasure.MATCH_PARENT);
        layoutParams.setLayoutHeightMeasure(LayoutMeasure.MATCH_PARENT);
        
        setOnKeyListener(new OnKeyEventListener() {

            @Override
            public boolean onKeyEvent(V3DKeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyAction.KEY_PRESSED) {
                    if( keyEvent.getKeyCode() == Keyboard.KEY_ADD) {
                        driver.setLinearVelocityCommand(new Vec3(0,20,0));
                    } else if( keyEvent.getKeyCode() == Keyboard.KEY_SUBTRACT) {
                        driver.setLinearVelocityCommand(new Vec3(0,-10,0));
                    } else if( keyEvent.getKeyCode() == Keyboard.KEY_NUMPAD0) {
                        driver.setLinearVelocityCommand(new Vec3(0,0,0));
                    }                    
                }
                Log.log("key char="+keyEvent.getCharacter()+ " code="+keyEvent.getKeyCode());
                
                return false;
            }
        });
        
        setOnMouseListener(new OnMouseEventListener() {

            @Override
            public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
                Log.log("driver action="+mouseEvent.getAction());
                switch (mouseEvent.getAction()) {
                    case MOUSE_PRESSED:
                        if(mouseEvent.getButton() == 1) {
                            mBaseMouseX = mouseEvent.getX();
                            mBaseMouseY = mouseEvent.getY();
                            mCurrentMouseX = mouseEvent.getX();
                            mCurrentMouseY = mouseEvent.getY();
                            mSteering = true;
                        }    
                        break;
                    case MOUSE_RELEASED:
                        if(mouseEvent.getButton() == 1) {
                            mSteering = false;
                            driver.setAngularVelocityCommand(new Vec3(0, 0, 0));
                        }
                        break;
                    case MOUSE_DRAGGED:
                    case MOUSE_MOVED:
                        if(mSteering) {
                            mCurrentMouseX = mouseEvent.getX();
                            mCurrentMouseY = mouseEvent.getY();
                            
                            double zAngularVelocity = (mCurrentMouseX - mBaseMouseX);
                            double xAngularVelocity = (mCurrentMouseY - mBaseMouseY);
                            
                            float maxRotationSpeed = 200;
                            float rotationSpeed =  (float) Math.sqrt(zAngularVelocity * zAngularVelocity + xAngularVelocity * xAngularVelocity);
    
                            if(rotationSpeed > maxRotationSpeed) {
                                xAngularVelocity =  xAngularVelocity *  maxRotationSpeed / rotationSpeed;
                                zAngularVelocity =  zAngularVelocity *  maxRotationSpeed / rotationSpeed;
                            }
                            
                            xAngularVelocity /= -200;
                            zAngularVelocity /= -200;
                            
                            xAngularVelocity = xAngularVelocity * xAngularVelocity * (xAngularVelocity > 0 ? 1 : -1);
                            zAngularVelocity = zAngularVelocity * zAngularVelocity * (zAngularVelocity > 0 ? 1 : -1);
                            
                            driver.setAngularVelocityCommand(new Vec3(xAngularVelocity, 0, zAngularVelocity));
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        
    }

    @Override
    public void onDraw(Graphics g) {
//        g.setColor(new Color(1.0f, 0.0f, 0.0f, 0.1f));
//        g.drawFilledRectangle(0, 0, 300, 300);
        if(mSteering) {
            
            Color backColor = new Color(0.0f, 0.0f, 1.0f, 0.1f);
            Color lineColor = new Color(0.0f, 0.0f, 1.0f, 0.5f);
            Color activeColor = new Color(1.0f, 0.0f, 0.0f, 0.5f);
            
            
            float x = mBaseMouseX;
            float y =getLayoutParams().getHeight()- mBaseMouseY;
            float cx = mCurrentMouseX;
            float cy =getLayoutParams().getHeight()- mCurrentMouseY;
            float maxRadius = 200;
            float deadRadius = 5;
            
            g.drawRing(x, y, maxRadius, 0, backColor, backColor, 32);
            
            g.drawRing(x, y, maxRadius , maxRadius-1, lineColor, lineColor, 32);
            g.drawRing(x, y, deadRadius , deadRadius-1, lineColor, lineColor, 32);
            g.setColor(lineColor);
            g.drawFilledRectangle(x+deadRadius, y-0.5f, maxRadius, 1);
            g.drawFilledRectangle(x-deadRadius, y-0.5f, -maxRadius, 1);
            g.drawFilledRectangle(x-0.5f, y+deadRadius, 1, maxRadius);
            g.drawFilledRectangle(x-0.5f, y-deadRadius, 1, -maxRadius);
            
            float activeRadius =  (float) Math.sqrt((mCurrentMouseY - mBaseMouseY) * (mCurrentMouseY - mBaseMouseY) + (mCurrentMouseX - mBaseMouseX) * (mCurrentMouseX - mBaseMouseX));
            
            if(activeRadius > maxRadius) {
                cx =  (cx - x ) *  maxRadius / activeRadius +x;
                cy =  (cy - y ) *  maxRadius / activeRadius +y;
//                cy = cy * maxRadius / activeRadius;
                activeRadius = maxRadius;
                
            }
            
            if(activeRadius > deadRadius) {
                g.drawRing(x, y, activeRadius , activeRadius-1, activeColor, activeColor, 32);
                g.drawRing(cx, cy, deadRadius, 0, activeColor, activeColor, 32);
                g.setColor(activeColor);
                
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex3f(x + deadRadius * (cx-x) / activeRadius, y + deadRadius * (cy-y) / activeRadius, 0);
                GL11.glVertex3f(x + (maxRadius + 20) * (cx-x) / activeRadius, y + (maxRadius + 20) * (cy-y) / activeRadius, 0);
                GL11.glEnd();
            }
            
            
            
//            g.drawFilledRectangle(mBaseMouseX, , mCurrentMouseX - mBaseMouseX,  - (mCurrentMouseY - mBaseMouseY));
        }
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMeasure() {
        // TODO Auto-generated method stub

    }

    @Override
    public View duplicate() {
        // TODO Auto-generated method stub
        return null;
    }

}
