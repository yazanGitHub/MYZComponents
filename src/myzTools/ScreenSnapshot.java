/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myzTools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Montazar Hamoud
 */
public class ScreenSnapshot
{

    public static BufferedImage centerSnapshot(BorderPane pane)
    {
        //Convert to gray image
        BufferedImage bufImage  = SwingFXUtils.fromFXImage(pane.getCenter().snapshot(new SnapshotParameters() , null), null);
        BufferedImage grayImage = new BufferedImage(bufImage.getWidth() , bufImage.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
        Graphics      graphics  = grayImage.getGraphics();
        graphics.drawImage(bufImage , 0, 0, null);
        graphics.dispose();

        return grayImage ;

    }
}
