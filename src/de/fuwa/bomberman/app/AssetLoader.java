package de.fuwa.bomberman.app;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The AssetLoader provides the functionality to load static images and animated images (gif).
 * Images are only loaded once and referenced for later use.
 */
public class AssetLoader {

    private Map<String, BufferedImage> singleImages = new HashMap<>();
    private Map<String, BufferedImage[]> animatedImages = new HashMap<>();

    /**
     * Loads the image with the specified path.
     * This image will consist of only one frame.
     * @param path the path of the image
     * @return the image with the specified path
     */
    public BufferedImage loadSingleImage(String path) {
        BufferedImage image;
        // first we check if that image has been loaded already
        if (singleImages.containsKey(path)) {
            return singleImages.get(path);
        }

        try {
            // we need to load the image
            image = ImageIO.read(new File(path));
            singleImages.put(path, image);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method will load and return the animated gif image
     * plus a single (static) image of the first frame of that gif.
     * @param path the path of the gif image
     * @return an Image[] where [0] is the static image and [1] is the animated image
     */
    public Image[] loadAnimatedGif(String path) {
        BufferedImage[] images;
        if ((images = animatedImages.get(path)) != null) {
            return images;
        }
        // we need to create a new array
        images = new BufferedImage[2];

        // load static image
        images[0] = loadSingleImage(path);

        // load animated image
        //    images[1] = new ImageIcon(path).getImage();

        // add images to map
        this.animatedImages.put(path, images);

        // return array
        return images;
    }

}
