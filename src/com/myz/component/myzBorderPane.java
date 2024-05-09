/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;

import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author Montazar Hamoud
 */
public abstract class myzBorderPane extends BorderPane
{
    //Constructore
    public myzBorderPane(String headerText)
    {
        //Set sub nodes at center
        m_center.setAlignment(Pos.CENTER);
        m_header.setAlignment(Pos.CENTER);

        //Set CSS
        m_center.setId("center");
        m_header.setId("header");
        m_footer.setId("footer");
        m_leftSidebar.setId("leftSidebar");
        m_rightSidebar.setId("rightSidebar");
        m_topHeaderText.setId("text");

        m_dropShadow.setOffsetX(5);
        m_dropShadow.setOffsetY(5);

        //Set header hight
        m_header.setMinHeight(75);

        //Adding text and DropShadow effect to it
        m_topHeaderText.setFont( Font.font("Courier New", FontWeight.BOLD , 28) );
        m_topHeaderText.setEffect(m_dropShadow);
        m_topHeaderText.setText(headerText);
        m_header.getChildren().add(m_topHeaderText);

        //Set parent nodes
        setCenter(m_center);
        setTop(m_header);
        setLeft(m_leftSidebar);
        setRight(m_rightSidebar);
        setBottom(m_footer);
    }

    //Data members
    GridPane     m_center       = new GridPane();
    HBox         m_header       = new HBox(5);
    VBox         m_leftSidebar  = new VBox(10);
    VBox         m_rightSidebar = new VBox(10);
    HBox         m_footer       = new HBox(5);

    /////////////////////////////////////////////////////////
    //                     TOP                            //
    /////////////////////////////////////////////////////////
    //DropShadow effect
    DropShadow m_dropShadow    = new DropShadow();
    Text       m_topHeaderText = new Text();

    //Methods
    public void addToCenter(Node node)
    {
        m_center.getChildren().add(node);
    }
    public void addToLeft(Node node)
    {
        m_leftSidebar.getChildren().add(node);
    }
    public void addToRight(Node node)
    {
        m_rightSidebar.getChildren().add(node);
    }
    public void addToHeader(Node node)
    {
        m_header.getChildren().add(node);
    }
    public void addToFooter(Node node)
    {
        m_footer.getChildren().add(node);
    }
    //Getter Methods
    public GridPane getCenterPane()
    {
        return m_center;
    }
    public VBox getLeftSideBar ()
    {
        return m_leftSidebar;
    }
    public VBox getRightSideBar ()
    {
        return m_rightSidebar;
    }
    public HBox getHeader()
    {
        return m_header;
    }
    public HBox getFooter()
    {
        return m_footer;
    }

    //Methods
    public void editHeaderText(String text)
    {
        m_topHeaderText.setText(text);
    }

    public final void setCenterOrientation(NodeOrientation orientation)
    {
        m_center.setNodeOrientation(orientation);
    }

    //Abstract methods
    public final void initFrame()
    {
        initCenter();
        initHeader();
        initFooter();
        initLeftSidebar();
        initRightSidebar();
        initFrameBasicData();
    }
    public abstract void initCenter();
    public abstract void initHeader();
    public abstract void initFooter();
    public abstract void initLeftSidebar();
    public abstract void initRightSidebar();
    public abstract void initFrameBasicData();
}
