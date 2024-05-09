/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myz.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * @author yazan
 */
public class myzTableView extends TableView
{
    myzScene       m_scene       = null;
    Pane           m_parentPane  = null;
    myzContextMenu m_menu        = null;

    public myzTableView()
    {
        super();
        this.setOnMouseClicked(new myzTableview_actionAdapter(this));
    }
    public void setParentPane(Pane pane)
    {
        m_parentPane = pane;
        m_menu       = new myzContextMenu(this);
    }

    public Pane getParentPane()
    {
        return m_parentPane;
    }

    public void setReSizeOnParentSize(boolean b)
    {
        if (b && m_parentPane != null)
        {
            prefHeightProperty().bind(m_parentPane.heightProperty());
            prefWidthProperty().bind(m_parentPane.widthProperty());
        }

    }
    public void setTableData(Vector  vData)
    {
        List list = new ArrayList();
        vData.stream().forEach(list::add);
        setItems(FXCollections.observableList(list));
        refresh();
    }





    public boolean canAdd()
    {
        return true;
    }
    public boolean canDelete()
    {
        return true;
    }

    public void add()
    {

    }
    public void delete()
    {

    }

    public void clickedOnRow()
    {

    }

    public void dbClickOnRow()
    {

    }



}


class myzContextMenu extends ContextMenu
{
    myzTableView m_table;
    public myzContextMenu(myzTableView table)
    {
        super();
        m_table         = table;
        MenuItem add    = new MenuItem("إضافة ");
        add.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                m_table.add();
            }
        });
        if ( m_table.canAdd())
        {
            this.getItems().add(add);
        }

        MenuItem delete = new MenuItem("حذف ");
        delete.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                m_table.delete();
            }
        });
        if ( m_table.canDelete())
        {
            this.getItems().add(delete);
        }
       m_table.setContextMenu(this);
    }

}

class myzTableview_actionAdapter implements EventHandler
{
    private myzTableView m_table;
    myzTableview_actionAdapter (myzTableView table)
    {
        m_table = table;
    }

    @Override
    public void handle(Event event)
    {
        MouseEvent mouseEvent = (MouseEvent) event ;

        if(mouseEvent.getClickCount() == 1)
            m_table.clickedOnRow();
        if(mouseEvent.getClickCount() == 2)
            m_table.dbClickOnRow();
    }


}