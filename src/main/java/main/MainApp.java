package main;

import java.util.HashMap;
import java.util.Map;

import domain.UserVO;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import views.LoginController;
import views.MainController;
import views.MasterController;
import views.ProfileController;
import views.RegisterController;
import views.SearchIdController;
import views.SearchNameController;

public class MainApp extends Application {
	public static MainApp app;
	
	private StackPane mainPage;
	
	public Map<String, MasterController> controllerMap = new HashMap<>();
	
	@Override
	public void start(Stage primaryStage) {
		app = this;
		try {
			FXMLLoader mainLoader = new FXMLLoader();
			mainLoader.setLocation(getClass().getResource("/views/MainLayout.fxml"));
			mainPage = mainLoader.load();
			
			MainController mc = mainLoader.getController();
			mc.setRoot(mainPage);
			controllerMap.put("main", mc);
			
			FXMLLoader loginLoader = new FXMLLoader();
			loginLoader.setLocation(getClass().getResource("/views/LoginLayout.fxml"));
			AnchorPane loginPage = loginLoader.load();
			
			LoginController lc = loginLoader.getController();
			lc.setRoot(loginPage);
			controllerMap.put("login", lc);
			
			FXMLLoader registerLoader = new FXMLLoader();
			registerLoader.setLocation(getClass().getResource("/views/RegisterLayout.fxml"));
			AnchorPane registerPage = registerLoader.load();
			
			RegisterController rc = registerLoader.getController();
			rc.setRoot(registerPage);
			controllerMap.put("register", rc);
			
			FXMLLoader searchNameLoader = new FXMLLoader();
			searchNameLoader.setLocation(getClass().getResource("/views/SearchName.fxml"));
			AnchorPane searchNamePage = searchNameLoader.load();
			
			SearchNameController snc = searchNameLoader.getController();
			snc.setRoot(searchNamePage);
			controllerMap.put("searchName", snc);
			
			FXMLLoader searchIdLoader = new FXMLLoader();
			searchIdLoader.setLocation(getClass().getResource("/views/SearchId.fxml"));
			AnchorPane searchIdPage = searchIdLoader.load();
			
			SearchIdController sic = searchIdLoader.getController();
			sic.setRoot(searchIdPage);
			controllerMap.put("searchId", sic);
			
			FXMLLoader profileLoader = new FXMLLoader();
			profileLoader.setLocation(getClass().getResource("/views/Profile.fxml"));
			AnchorPane profilePage = profileLoader.load();
			
			ProfileController pc = profileLoader.getController();
			pc.setRoot(profilePage);
			controllerMap.put("profile", pc);
			
			Scene scene = new Scene(mainPage);	
			scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
			
			mainPage.getChildren().add(loginPage);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void loadPane(String name) {
		MasterController c = controllerMap.get(name);
		Pane pane = c.getRoot();
		mainPage.getChildren().add(pane);
		
		pane.setOpacity(0);
		
		Timeline timeline = new Timeline();
		KeyValue fadeOut = new KeyValue(pane.opacityProperty(), 1);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(300), fadeOut);
		
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}
	
	public void slideOut(Pane pane) {
		Timeline timeline = new Timeline();
		KeyValue fadeOut = new KeyValue(pane.opacityProperty(), 0);
		
		KeyFrame keyFrame = new KeyFrame(Duration.millis(300), (e) -> {
			mainPage.getChildren().remove(pane);
		}, fadeOut);
		
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}
	
	public void setMain(UserVO user) {
		MainController mc = (MainController)controllerMap.get("main");
		mc.setLoginInfo(user);
		mc.setMain();
	}
	
	public void setProfile(UserVO user) {
		ProfileController pfc = (ProfileController)controllerMap.get("profile");
		pfc.setData(user.getId(), user.getName(), user.getInfo(), user.getDeck());
	}
	
	public boolean checklogin(UserVO u) {
		MainController mc = (MainController)controllerMap.get("main");
		return mc.checklogin(u);
	}
	
	public void doneNewAnswer() {
		ProfileController pcc = (ProfileController)controllerMap.get("profile");
		pcc.setCount();
		pcc.setDone();
	}
	
	public void doneRejectAnswer() {
		ProfileController pcc = (ProfileController)controllerMap.get("profile");
		pcc.setCount();
		pcc.setReject();
	}

}
