package io;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by yao on 15/7/8.
 */
public class CaptureScreen {

    public static void captureScreen(String fileName, String folder) throws Exception {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        //保存路径
        File screenFile = new File(fileName);
        if (!screenFile.exists()) {
            screenFile.mkdir();
        }
        File f = new File(screenFile, folder);

        ImageIO.write(image, "jpg", f);
        //自动打开
        if (Desktop.isDesktopSupported()
                && Desktop.getDesktop().isSupported(Desktop.Action.OPEN))
            Desktop.getDesktop().open(f);
    }

    public static void main(String[] args) {
        try {
            captureScreen("/Users/yao/tmp/","1.jpg");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
