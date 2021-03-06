package com.polertech.api.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 二维码的生成需要借助本类
 * Created by Leo on 2016/6/30.
 */
public class MatrixToImageWriter {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private MatrixToImageWriter() {}


    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }


    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }


    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }
    /**
     * 图像转输出流
     */
    public static void writeToStream(String text, OutputStream stream) throws Exception {
        int width = 500;
        int height = 500;
        // 二维码的图片格式
        String format = "png";
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.MARGIN, 1);//设置白边大小0-4
        // 内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
                BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage image = toBufferedImage(bitMatrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format "
                    + format);
        }
    }
    public static BufferedImage createImage(String text) throws Exception {
        int width = 500;
        int height = 500;
        // 二维码的图片格式
        String format = "gif";
        Hashtable hints = new Hashtable();
        // 内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
                BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage image = toBufferedImage(bitMatrix);
        return image;
    }
    public static void writeToStream1(String text, OutputStream stream) throws Exception {
        int width = 500;
        int height = 500;
        // 二维码的图片格式
        String format = "png";
        Hashtable hints = new Hashtable();
        // 内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
                BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage image = toBufferedImage(bitMatrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format "
                    + format);
        }
    }


}

