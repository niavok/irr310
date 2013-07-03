package com.irr310.i3d.fonts;

import java.awt.image.BufferedImage;
import java.util.Hashtable;

import com.irr310.i3d.I3dContext;
import com.irr310.i3d.Texture;
import com.irr310.i3d.TextureManager;


public class Font {
    
    private Hashtable<Character, CharacterPixmap> texHashMap  = null;
    private BufferedImage                         image       = null;
    private int                                   height      = 0;
    
    /**
     * Creates a new font.
     * 
     * @param map
     * @param texHashMap
     * @param height
     */
    public Font(BufferedImage map, Hashtable<Character, CharacterPixmap> texHashMap, int height)
    {
      this.texHashMap = texHashMap;
      this.image = map;
      this.height = height;
    }
    
    /**
     * Returns the width of the character in pixels.
     * 
     * @param s
     *          the character
     * @return the characters width in pixel
     */
    public int getWidth(char s)
    {
      CharacterPixmap cp = texHashMap.get(s);
      if (cp == null)
        cp = texHashMap.get('?');
      if (cp == null)
        return 0;
      return cp.getWidth();
    }
    
    /**
     * Returns the height of the font measured in pixel.
     * 
     * @return the height
     */
    public int getHeight()
    {
      return height;
    }
    
    /**
     * Returns the Pixmap the holds the given character within a texture. The character is
     * drawn in white. The rest of the texture is translucent. Each texture is cached as
     * long as the font object exists.
     * 
     * @param ch
     *          the character to be on the texture
     * @return the texture
     */
    public CharacterPixmap getCharPixMap(char ch)
    {
      //isn't this slower as the current? 
      //      if (texHashMap.get('a').getTexture() == null) {
      //          uploadToVideoMemory();
      //      }

      CharacterPixmap p = texHashMap.get(ch);

      if (p == null)
      {
        // System.err.println("Character '"+ch + "'=" + (int)ch+" has not been pre-rendered
        // in a Pixmap!!!");
        return texHashMap.get('?');
      }
      else
      {

        if (p.getTexture() == null)
        {
          uploadToVideoMemory();
        }

        return p;
      }
    }
    
    public Texture uploadToVideoMemory()
    {
      Texture tex = TextureManager.getTexture(image);

      for (CharacterPixmap cp : texHashMap.values())
      {
        cp.setTexture(tex);
      }
      
      return tex;
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    public Hashtable<Character, CharacterPixmap> getTexHashMap() {
        return texHashMap;
    }
    
}
