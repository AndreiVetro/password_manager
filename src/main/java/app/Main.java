package app;

import controller.AppStartController;
import domain.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Main extends Application
{
    private static Stage primaryStage;
    private static Scene selectScene;
    private static ApplicationContext applicationContext;
    public static User user;

    public static void goBackButtonOnClick()
    {
        primaryStage.setScene(selectScene);
        primaryStage.setFullScreen(true);
    }

    public static void openNewScreen(String fxmlName, Initializable sourceController, Class<?> targetController) throws IOException
    {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(sourceController.getClass().getResource("/fxml/" + fxmlName + ".fxml"));
        loader.setController(applicationContext.getBean(targetController));

        Parent content = loader.load();

        Scene selectScene = new Scene(content);
        primaryStage.setScene(selectScene);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Main.primaryStage = primaryStage;
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("configuration");
        Main.applicationContext = context;
        AppStartController appStartController = context.getBean(AppStartController.class);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/appStart.fxml"));
        loader.setController(appStartController);

        Parent content = loader.load();

        Scene selectScene = new Scene(content);
        Main.selectScene = selectScene;
        primaryStage.setScene(selectScene);
        primaryStage.setTitle("Password manager");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
        primaryStage.show();

        primaryStage.setOnCloseRequest(t ->
        {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
