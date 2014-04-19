package com.irr310.i3d.view.drawable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.irr310.common.tools.Log;

public class BitmapFactory {

    static Map<File, BufferedImage> imageCache  = new HashMap<File, BufferedImage>();
    
    public static BufferedImage loadImage(File imageFile) {
        
        BufferedImage image;
        
        image = imageCache.get(imageFile);
        
        if(image == null) {
        
            try {
                image = ImageIO.read(imageFile);
                imageCache.put(imageFile, image);
            } catch (IOException e) {
                Log.error("Invalid image " + imageFile.getAbsolutePath());
                return null;
            }
        }
        return image;
    }

    public BitmapDrawable loadPngDrawable(File file) {
        return new BitmapDrawable(loadImage(file));
    }

    public BitmapDrawable loadJpgDrawable(File file) {
        return new BitmapDrawable(loadImage(file));
    }
    
    public static void clearCache() {
        imageCache.clear();
    }

}
