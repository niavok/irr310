package com.irr310.i3d.view.drawable;

import com.irr310.common.tools.Log;
import com.irr310.i3d.I3dRessourceManager;

public class InsetDrawable extends Drawable {

    private String drawableName = null;
    private Integer insetTop = null;
    private Integer width = null;
    private Integer height = null;
    private Integer insetLeft = null;
    private Integer insetRight = null;
    private Integer insetBottom = null;
    private Drawable innerDrawable = null;
    
    public InsetDrawable() {
    }

    @Override
    public void setBounds(float left, float top, float right, float bottom) {
        super.setBounds(left, top, right, bottom);
        if(innerDrawable == null) {
            loadDrawable();
        }
        innerDrawable.setBounds(left, top, right, bottom);
    }

    private void loadDrawable() {
        if(innerDrawable == null) {
            Drawable baseDrawable = I3dRessourceManager.getInstance().loadDrawable(drawableName);
            
            // Compute dimension
            if(insetLeft== null) {
                if(insetRight == null || width == null) {
                    insetLeft = 0;
                } else {
                    insetLeft = baseDrawable.getIntrinsicWidth() - insetRight - width;
                }
            }
            
            if(insetTop== null) {
                if(insetBottom == null || height == null) {
                    insetTop = 0;
                } else {
                    insetTop = baseDrawable.getIntrinsicHeight() - insetBottom - height;
                }
            }
            
            if(insetRight== null) {
                if(width == null) {
                    insetRight = 0;
                } else {
                    insetRight = baseDrawable.getIntrinsicWidth() - insetLeft - width;
                }
            }
            
            if(insetBottom == null) {
                if(height == null) {
                    insetBottom = 0;
                } else {
                    insetBottom = baseDrawable.getIntrinsicHeight() - insetTop - height;
                }
            }
            
            if(height == null) {
                height = baseDrawable.getIntrinsicHeight() - insetTop - insetBottom;
            }
            
            if(width == null) {
                width = baseDrawable.getIntrinsicWidth() - insetLeft - insetRight;
            }
            
            
            if(baseDrawable instanceof BitmapDrawable) {
                BitmapDrawable bitmap = (BitmapDrawable) baseDrawable;
                BitmapDrawable clipBitmap = new BitmapDrawable(bitmap.getImage());
                clipBitmap.setClipTop(insetTop);
                clipBitmap.setClipLeft(insetLeft);
                clipBitmap.setClipBottom(insetBottom);
                clipBitmap.setClipRight(insetRight);
                innerDrawable = clipBitmap;
                
            } else {
                Log.warn("Unsupported Inset drawable for type '"+innerDrawable.getClass().getCanonicalName()+"'");
            }
            
        }
        
    }

    @Override
    public void close() {
        innerDrawable.close();
    }

    @Override
    public int getIntrinsicWidth() {
        if(innerDrawable == null) {
            loadDrawable();
        }
        return innerDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        if(innerDrawable == null) {
            loadDrawable();
        }
        return innerDrawable.getIntrinsicHeight();
    }

    @Override
    public void draw() {
        innerDrawable.draw();
    }
    
    @Override
    public void begin(int glType) {
        innerDrawable.begin(glType);
    }
    
    @Override
    public void end() {
        innerDrawable.end();
    }
    
    @Override
    public void vertex(float x, float y) {
        innerDrawable.vertex(x, y);
    }

    public void setDrawableName(String drawableName) {
        this.drawableName = drawableName;
    }

    public void setInsetTop(Integer insetTop) {
        this.insetTop = insetTop;
    }

    public void setInsetLeft(Integer insetLeft) {
        this.insetLeft = insetLeft;
    }

    public void setInsetBottom(Integer insetBottom) {
        this.insetBottom = insetBottom;
    }

    public void setInsetRight(Integer insetRight) {
        this.insetRight = insetRight;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

}
