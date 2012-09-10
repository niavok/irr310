package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.Texture;
import com.irr310.i3d.fonts.CharacterPixmap;
import com.irr310.i3d.fonts.Font;

public class TextView extends View {

    private String text = "plop";
    private Font font;
    String[] wrappedText;
    Color textColor = Color.darkblue;
    
    public TextView(Graphics g) {
        super(g);
        font = I3dContext.getInstance().getDefaultFont();
        
        wrappedText = new String[2];
        wrappedText[0] = "Bonjour ceci est un test de text assez long.";
        wrappedText[1] = "plop";
    }

    @Override
    public void doDraw() {
        
//          int localX = x + g.getTranslation().getX();
 //         int localXbase = localX;
   //       int localY = y + g.getTranslation().getY() - getLineHeight();

          int localX = 0;
          int localXbase = localX;
          int localY = 0;
        
          g.setColor(textColor);
          GL11.glEnable(GL11.GL_TEXTURE_2D);
          
          CharacterPixmap pixmap;

          boolean init = true;

          for (String text : wrappedText)
          {
            for (int i = 0; i < text.length(); i++)
            {
              final char c = text.charAt(i);
              if (c == '\r' || c == '\f' || c == '\t')
                continue;
              else if (c == ' ')
              {
                localX += font.getWidth(' ');
                continue;
              }
              pixmap = font.getCharPixMap(c);

              if (init)
              {
                Texture tex = pixmap.getTexture();

                if(tex != null) {
                    if (tex.hasAlpha())
                    {
                        GL11.glTexEnvf( GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE );
                    }
          
                    tex.bind();
                }
                GL11.glBegin(GL11.GL_QUADS);
                init = false;
              }

              final int imgWidth = pixmap.getWidth();
              final int imgHeight = pixmap.getHeight();

              final float endY = pixmap.getEndY();
              final float endX = pixmap.getEndX();

              final float startX = pixmap.getStartX();
              final float startY = pixmap.getStartY();

              GL11.glTexCoord2f(startX, startY);
              GL11.glVertex2i(localX, localY);

              GL11.glTexCoord2f(startX, endY);
              GL11.glVertex2i(localX, imgHeight + localY);

              GL11.glTexCoord2f(endX, endY);
              GL11.glVertex2i(imgWidth + localX, imgHeight + localY);

              GL11.glTexCoord2f(endX, startY);
              GL11.glVertex2i(imgWidth + localX, localY);

              localX += pixmap.getCharWidth();
            }
            //move to start of next line
            localY += font.getHeight();
            localX = localXbase;
          }
          if (!init)
            GL11.glEnd();
          GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
    
    @Override
    public View duplicate() {
        TextView view = new TextView(g);
        view.setId(getId());
        view.setTextColor(textColor);
        view.setLayout(getLayout().duplicate());
        
        return view;
    }
    
    public boolean doLayout(Layout parentLayout) {
        return true;
    }

    public void setText(String text) {
        this.text = text;
    }
}
