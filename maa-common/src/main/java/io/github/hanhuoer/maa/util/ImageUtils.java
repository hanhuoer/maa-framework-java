package io.github.hanhuoer.maa.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author H
 */
public class ImageUtils {

    public static String toBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();

        byte[] encode = Base64.getEncoder().encode(bytes);
        String imgBase64 = new String(encode).trim();
        imgBase64 = imgBase64.replaceAll("\n", "").replaceAll("\r", "");
        return "data:image/jpg;base64," + imgBase64;
    }

}
