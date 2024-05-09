/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import com.myz.log.logWriter;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Montazar Hamoud
 */
public class myzAttachPanel extends HBox
{
    //Constructor
    public myzAttachPanel(int mode)
    {

        if(mode == MODE_ENABLE)
            initAttachPanelEvents();

    }
    //Class Member
    public static final int MODE_DISABLE = 0 ;
    public static final int MODE_ENABLE  = 1 ;
    //Data member
    ScrollPane   m_scrollPane        = null ;

    //Methods
    private void initAttachPanelEvents()
    {
        setWidth(USE_PREF_SIZE);
        setHeight(150);

        setOnDragOver(new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent event)
            {
                setStyle("-fx-background-color:#fffff2;");
                mouseDragOver(event);
            }

        });
        setOnDragDropped(new EventHandler<DragEvent>()
        {
            @Override
            public void handle(final DragEvent event)
            {
                mouseDragDropped(event);
            }
        });
        setOnDragExited(new EventHandler<DragEvent>()
        {
            @Override
            public void handle(final DragEvent event)
            {
                setStyle("-fx-border-color: #C6C6C6;");
            }
        });
    }

    public void mouseDragDropped(final DragEvent e)
    {
        final Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles())
        {
            List<File> files  = db.getFiles();
            files.stream().forEach((file) ->
            {
                createImageView(file);
            });

            success = true;
        }
        e.setDropCompleted(success);
        e.consume();
    }

    private  void mouseDragOver(final DragEvent e)
    {
        final Dragboard db = e.getDragboard();

        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".png")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg")
                || db.getFiles().get(0).getName().endsWith(".Gif")
                || db.getFiles().get(0).getName().endsWith(".MYZ");


        if (db.hasFiles())
        {
            if (isAccepted)
            {
                e.acceptTransferModes(TransferMode.COPY);
            }
        }
        else
        {
            e.consume();
        }
    }


    private boolean saveImage(File file)
    {
        return true ;
    }

    private void createImageView(File file)
   {
        try
        {
//            BufferedImage oldBi = SwingFXUtils.fromFXImage(image, null) ;
//            BufferedImage newBi = Thumbnails.of(oldBi).size(150, 150).asBufferedImage();
//            image     = SwingFXUtils.toFXImage( oldBi , null);

            Image     image     = new Image ( new FileInputStream (file)  );
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED , new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    if(event.getClickCount() == 2)
                    {
                        Stage   window = new Stage();
                        window.initStyle(StageStyle.DECORATED);
                        window.initModality(Modality.APPLICATION_MODAL);
                        window.setResizable(true);
                        window.setMaximized(true);

                        ImageView iv = new ImageView(imageView.getImage() );
                        ScrollPane scrollPane = new ScrollPane(iv);
                        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                        scrollPane.setFitToWidth(true);
                        scrollPane.setFitToHeight(true);
                        scrollPane.setMinSize(300 , 100);

                        Scene scene = new Scene(scrollPane , 900 , 700);
                        window.setScene(scene);
                        window.showAndWait();

                    }

                }
            });
            getChildren().add(imageView);
        }
        catch(Exception ex)
        {
            logWriter.write(ex);
        }
    }


}
