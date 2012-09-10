package com.irr310.i3d.fonts;

import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class FontFactory {

    private FontMetrics fontMetrics;
    private AssemblyLine assemblyLine = new AssemblyLine();
    private Alphabet alphabet;
    private int squarePixel = 0;
    final private int safetyMargin = 0;
    private String name;

    public FontFactory() {
    }

    public void free() {
        // TODO Auto-generated method stub

    }

    public Font generateFont(String fontCode, float fontSize) {

        
        
        String style = "R";
        
        name = fontCode+"-"+style+"-"+fontSize;
        
        /*
         * if(labelstyle.getStyle() == "bold") { style = "B"; }
         */
        java.awt.Font font = null;
        try {
            font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("fonts/" + fontCode + "-" + style + ".ttf"));
            font = font.deriveFont(fontSize);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return renderStandardFont(font);
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

      int x = 0;
      int y = 0;

      int counter = 0;

      for (BufferedImage charImage : charImages)
      {
        if (charImage.getWidth() + x > bi.getWidth())
        {
          y += fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
          x = 0;
        }

        int xValue = x;
        int yValue = y;

        CharacterPixmap cp = new CharacterPixmap(null, xValue, yValue, charImage.getWidth(), fontMetrics.getMaxAscent()
            + fontMetrics.getMaxDescent(), alphabet.getAlphabet()[counter], fontMetrics
            .charWidth(alphabet.getAlphabet()[counter]));

        hashtable.put(cp.getCharacter(), cp);

        g.drawImage(charImage, xValue, yValue, null);

        x += charImage.getWidth();
        counter++;
      }

      saveImageToDisk(bi, name+".png");

      return new Font(bi, hashtable, fontMetrics.getMaxAscent()
          + fontMetrics.getMaxDescent());

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
    

}
