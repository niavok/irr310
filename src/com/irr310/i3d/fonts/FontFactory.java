package com.irr310.i3d.fonts;

import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;

import com.irr310.client.ClientConfig;
import com.irr310.common.tools.Log;
import com.irr310.i3d.view.drawable.BitmapFactory;

public class FontFactory {

    private FontMetrics fontMetrics;
    private AssemblyLine assemblyLine = new AssemblyLine();
    private Alphabet alphabet;
    private int squarePixel = 0;
    private int safetyMargin = 1;
    private String name;

    static final int FONTGL_MAGIC_NUMBER = 1649040;
    static final int FONTGL_VERSION = 1;
    
    static public Map<String, Font> fontCache = new HashMap<String, Font>(); 
    
    /**
     * GLFont file format
     * 
     * WARNING: change the version number is the format is changed !
     * 
     * int  (4 bytes) : magic number
     * int  (4 bytes) : version
     * int  (4 bytes) : height
     * int  (4 bytes) : n char count
     * 
     * ... n times
     * 
     * int  (4 bytes) : pixmap x
     * int  (4 bytes) : pixmap y
     * int  (4 bytes) : pixmap width
     * int  (4 bytes) : pixmap char width 
     * char (1 byte) : pixmap character
     */
    
    
    public FontFactory() {
    }

    public void free() {
        // TODO Auto-generated method stub

    }

    public Font generateFont(String fontCode, float fontSize, String fontStyle) {

        
        
        String style = "R";
        
        if(fontStyle.equals("bold")) {
            style = "B";
        }
        
        name = fontCode+"-"+style+"-"+fontSize;
        
        Font font = null;
        
        font = loadCachedFont(name);
        
        if(font == null) {
            /*
             * if(labelstyle.getStyle() == "bold") { style = "B"; }
             */
            java.awt.Font awtFont = null;
            try {
                awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("fonts/" + fontCode + "-" + style + ".ttf"));
                awtFont = awtFont.deriveFont(fontSize);
            } catch (FontFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            font = renderStandardFont(awtFont);
        }
        
        return font;
    }

    private Font loadCachedFont(String name) {
        
        if(fontCache.containsKey(name)) {
            return fontCache.get(name);
        }
        
        File cachedFontDescriptor = new File(ClientConfig.getCacheDirectoryPath("fonts/"+name+".glfont"));
        File cachedFontImage = new File(ClientConfig.getCacheDirectoryPath("fonts/"+name+".png"));
        
        if(!cachedFontDescriptor.exists() || !cachedFontImage.exists()) {
            return null;
        }
        
        BufferedImage img = BitmapFactory.loadImage(cachedFontImage);
        if(img == null) {
            return null;
        }
        
        Font font = null;
        
        
        
        try {
            
            FileInputStream fileInputStream = new FileInputStream(cachedFontDescriptor);
            FileChannel fileChannel = fileInputStream.getChannel();
            ByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            
            int magicNumber = buffer.getInt();
            int version = buffer.getInt();
            int height = buffer.getInt();
            int count = buffer.getInt();
            
            if(magicNumber != FONTGL_MAGIC_NUMBER) {
                Log.error("Failed to loadglfont file "+cachedFontDescriptor.getAbsolutePath()+" : magic number is '"+FONTGL_MAGIC_NUMBER+"' but '"+magicNumber+"' is expected.");
            }
            
            if(version != FONTGL_VERSION) {
                Log.error("Failed to loadglfont file "+cachedFontDescriptor.getAbsolutePath()+" : version is '"+FONTGL_VERSION+"' but '"+version+"' is expected.");
            }
            
            Hashtable<Character, CharacterPixmap> hashtable = new Hashtable<Character, CharacterPixmap>();
            
            for(int i = 0; i < count; i++) {
                int x = buffer.getInt();
                int y = buffer.getInt();
                int width = buffer.getInt();
                int charWidth = buffer.getInt();
                char character = buffer.getChar();
                
                CharacterPixmap cp = new CharacterPixmap(null, x, y, width, height, character, charWidth);
                hashtable.put(cp.getCharacter(), cp);
            }
            
            font = new Font(img, hashtable, height);
            fontCache.put(name, font);
            
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            Log.error("Failed to initialize reader on glfont file "+cachedFontDescriptor.getAbsolutePath());
            return null;
        } catch (IOException e) {
            Log.error("Failed to read on glfont file "+cachedFontDescriptor.getAbsolutePath());
            return null;
        }
        
        return font;
    }

    
    
    public Font renderStandardFont(java.awt.Font awtFont) {

        BufferedImage baseImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) baseImage.getGraphics();
        fontMetrics = g.getFontMetrics(awtFont);

        alphabet = Alphabet.getDefaultAlphabet();

        assemblyLine.addStage(new Clear());
        assemblyLine.addStage(new DrawCharacter(java.awt.Color.WHITE, true));

        Font f = createFont();

        return f;

    }

    /**
     * Creates the FengGUI font out of the AWT font. It uses the previously set
     * up AssemblyLine to render and modify each character in the alphabet.
     * 
     * @return FengGUI font
     */
    public Font createFont() {
        ArrayList<BufferedImage> charImages = new ArrayList<BufferedImage>();

        BufferedImage bi = null;

        for (char c : alphabet.getAlphabet()) {
            if (!Character.isSpaceChar(c)) // if processed character not the
                                           // blank
            {
                // generate BufferedImage that is big enough to display the
                // character
                bi = new BufferedImage(fontMetrics.getMaxAdvance(),
                                       fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent(),
                                       BufferedImage.TYPE_INT_ARGB);
            } else {
                bi = new BufferedImage(fontMetrics.charWidth(' '),
                                       fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent(),
                                       BufferedImage.TYPE_INT_ARGB);
            }

            assemblyLine.execute(fontMetrics, bi, c, safetyMargin);

            if (!Character.isSpaceChar(c)) {
                bi = cropImage(bi, c);
            }

            squarePixel += bi.getWidth() * bi.getHeight();

            charImages.add(bi);
        }

        Font font = buildCharTexture(charImages);

        return font;
    }

    private BufferedImage cropImage(BufferedImage bi, char c) {
        ColorModel cm = bi.getColorModel();

        int startX = 0;
        int endX = fontMetrics.charWidth(c);

        // coming from the right, going to the left
        for (int x = fontMetrics.charWidth(c); x < bi.getWidth(); x++) {
            boolean hasAlpha = false;

            for (int y = 0; y < bi.getHeight(); y++) {
                if (cm.getAlpha(bi.getRGB(x, y)) != 0) {
                    hasAlpha = true;
                    endX = x + 1;
                    break;
                }
            }

            if (!hasAlpha)
                break;
        }

        BufferedImage cropped = new BufferedImage(endX - startX, fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent(), bi.getType());
        cropped.getGraphics().drawImage(bi, -startX, 0, null);

        return cropped;
    }
    
    private Font buildCharTexture(ArrayList<BufferedImage> charImages)
    {
      Hashtable<Character, CharacterPixmap> hashtable = new Hashtable<Character, CharacterPixmap>();
      int length = ImageConverter.powerOf2((int) Math.ceil(Math.sqrt(squarePixel)));

      BufferedImage bi = ImageConverter.createGlCompatibleAwtImage(length, length);
      Graphics2D g = bi.createGraphics();
      Clear.clear(g, bi.getWidth(), bi.getHeight());

      int x = safetyMargin;
      int y = safetyMargin;

      int counter = 0;

      for (BufferedImage charImage : charImages)
      {
        if (charImage.getWidth() + x > bi.getWidth())
        {
          y += fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent() + safetyMargin;
          x = 0;
        }

        int xValue = x;
        int yValue = y;

        CharacterPixmap cp = new CharacterPixmap(null, xValue, yValue, charImage.getWidth(), fontMetrics.getMaxAscent()
            + fontMetrics.getMaxDescent(), alphabet.getAlphabet()[counter], fontMetrics
            .charWidth(alphabet.getAlphabet()[counter]));

        hashtable.put(cp.getCharacter(), cp);

        g.drawImage(charImage, xValue, yValue, null);

        x += charImage.getWidth() + safetyMargin;
        counter++;
      }

      Font font = new Font(bi, hashtable, fontMetrics.getMaxAscent()
               + fontMetrics.getMaxDescent());
      
      saveFontTo(font, name);
      //saveImageToDisk(bi, name+".png");

      return font;

    }
    
    public static void saveFontTo(Font font, String name) {
        
        File fontCacheDir = new File(ClientConfig.getCacheDirectoryPath("fonts/"));
        if(!fontCacheDir.exists()) {
            fontCacheDir.mkdirs();
        }
        
        saveImageToDisk(font.getImage(), ClientConfig.getCacheDirectoryPath("fonts/"+name+".png"));
        
        try {
            DataOutputStream output = new DataOutputStream(new FileOutputStream(ClientConfig.getCacheDirectoryPath("fonts/"+name+".glfont")));
            
            output.writeInt(FONTGL_MAGIC_NUMBER);
            output.writeInt(FONTGL_VERSION);
            output.writeInt(font.getHeight());
            output.writeInt(font.getTexHashMap().size());
            
            
            Set<Entry<Character,CharacterPixmap>> entrySet = font.getTexHashMap().entrySet();
            
            for (Entry<Character, CharacterPixmap> entry : entrySet) {
                CharacterPixmap value = entry.getValue();
                output.writeInt(value.getX());
                output.writeInt(value.getY());
                output.writeInt(value.getWidth());
                output.writeInt(value.getCharWidth());
                output.writeChar(value.getCharacter());
            }
            
            output.close();
            
        } catch (FileNotFoundException e) {
            Log.error("Failed open glfont cache file to write "+ClientConfig.getCacheDirectoryPath("fonts/"+name+".glfont"));
        } catch (IOException e) {
            Log.error("Failed to write cache glfont file "+ClientConfig.getCacheDirectoryPath("fonts/"+name+".glfont"));
        }
    }
    
    
    /**
     * Saves the given image to a PNG file.
     * @param bi the image to be saved
     * @param filename the file where to save the PNG in
     */
    public static void saveImageToDisk(BufferedImage bi, String filename)
    {
      String ending = filename.substring(filename.length() - 3, filename.length());
      try
      {
        ImageIO.write(bi, ending, new File(filename));
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }

    public static void clearCache() {
        fontCache.clear();        
    }
    

}
