package de.fuwa.bomberman.app;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetLoader {

    private Map<String, Image> singleImages = new HashMap<>();
    private Map<String, List<Image>> animatedImages = new HashMap<>();

    public Image loadSingleImage(String path) {
        Image image;
        if (singleImages.containsKey(path)) {
            return singleImages.get(path);
        }

        try {
            image = ImageIO.read(new File(path));
            singleImages.put(path, image);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Image> loadAnimatedGif(String path) {

        // this method took some ideas from
        // https://stackoverflow.com/questions/8933893/convert-each-animated-gif-frame-to-a-separate-bufferedimage

        List<Image> list;
        if ((list = animatedImages.get(path)) != null) {
            return list;
        }

        try {
            ImageReader imageReader = new GIFImageReader(new GIFImageReaderSpi());
            ImageInputStream inputStream = ImageIO.createImageInputStream(new File(path));
            imageReader.setInput(inputStream, false);

            List<Image> frames = new ArrayList<>();
            for (int i = 0; i < imageReader.getNumImages(true); i++) {
                Image image = imageReader.read(i);
                frames.add(image);
            }
            this.animatedImages.put(path, frames);
            return frames;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
