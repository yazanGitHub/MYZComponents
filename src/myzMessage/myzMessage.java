package myzMessage;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * @author yazan
 */
public class myzMessage
{
    private static Stage   m_window;
    private static Scene   m_scene;
    private static boolean m_answer;

    public static void alertMessage(String alertMessage)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("تحذير");
        alert.setHeaderText(alertMessage);
        alert.setContentText("");
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.getDialogPane().setStyle("-fx-font-weight: bold");
        alert.showAndWait();
    }
    public static void noteMessage(String note)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("معلومات");
        alert.setHeaderText(note);
        alert.setContentText("");
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.getDialogPane().setStyle("-fx-font-weight: bold");
        alert.showAndWait();
    }
    public static boolean confirmMessage(String confirmMessage)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("تأكيد");
        alert.setHeaderText(confirmMessage);
        alert.setContentText("");
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.getDialogPane().setStyle("-fx-font-weight: bold");

        final Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    public static void noteMessage(String note , ResourceBundle bundle)
    {
        Stage   m_window = new Stage();
        m_window.initStyle(StageStyle.DECORATED);
        m_window.initModality(Modality.APPLICATION_MODAL);
        m_window.setResizable(false);

        Label     label    = new Label();
        label.setText(note);
        Button    okButton = new Button(bundle.getString("ok"));
        okButton.setMinSize(50, 20);
        okButton.setOnAction(e -> m_window.close());

        VBox layOut   = new VBox(20);
        layOut.getChildren().addAll(label ,okButton);
        layOut.setAlignment(Pos.CENTER);
//        m_scene = new Scene(layOut , 350 , 150);
        m_scene = new Scene(layOut);
        m_window.setScene(m_scene);
        m_window.showAndWait();
    }

    public static boolean confirmMessage(String question ,  ResourceBundle bundle)
    {
        Stage   m_window = new Stage();
        m_window.initModality(Modality.APPLICATION_MODAL);
        m_window.setResizable(false);
        m_window.setOnCloseRequest(e->e.consume());// to not close the frame
        Label     label    = new Label();
        label.setText(question);

        Button    yesButton = new Button(bundle.getString("ok"));
        yesButton.setMinSize(50, 20);
        yesButton.setOnAction(e ->
        {
            m_answer = true;
            m_window.close();
        });

        Button    noButton = new Button(bundle.getString("no"));
        noButton.setMinSize(50, 20);
        noButton.setOnAction(e ->
        {
            m_answer = false;
            m_window.close();

        });
        HBox ButtonBox = new HBox(10);
        ButtonBox.getChildren().addAll(yesButton , noButton );
        ButtonBox.setAlignment(Pos.CENTER);

        VBox layOut   = new VBox(20);
        layOut.getChildren().addAll(label ,ButtonBox);
        layOut.setAlignment(Pos.CENTER);
        m_scene = new Scene(layOut);
        m_window.setScene(m_scene);
        m_window.showAndWait();

        return m_answer;
    }

    public static void alertMessage(String alter , ResourceBundle bundle)
    {
        Stage   m_window = new Stage();
        m_window.initModality(Modality.APPLICATION_MODAL);
        m_window.setResizable(false);

        Label     label    = new Label();
        label.setText(alter);
        label.setTextFill(Color.web("#ff0000", 0.8));
        Button    okButton;
        if ( bundle != null)
            okButton = new Button(bundle.getString("no"));
        else
           okButton = new Button("ok");

        okButton.setMinSize(50, 20);
        okButton.setOnAction(e -> m_window.close());

        VBox layOut   = new VBox(20);
        layOut.getChildren().addAll(label ,okButton);
        layOut.setAlignment(Pos.CENTER);
        m_scene = new Scene(layOut);
        m_window.setScene(m_scene);
        m_window.showAndWait();
    }

}
