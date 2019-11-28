package views;

import java.awt.image.BufferedImage;

import domain.UserVO;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.MainApp;

public class ProfilItemController {
	@FXML Label idlbl;
	@FXML Label namelbl;
	@FXML Label infolbl;
	@FXML Canvas canvas;
	
	public BufferedImage bi;
	
	public void setData(String id, String name, String info,BufferedImage b) {
		this.bi = b;
		idlbl.setText(id);
		namelbl.setText(name);
		infolbl.setText(info);
		Image img = SwingFXUtils.toFXImage(b, null);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		double w = canvas.getWidth();
		double h = canvas.getHeight();
		gc.drawImage(img, 0, 0, w,h);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(30);
		gc.strokeOval(-15,-15,w+30,h+30);
	}
	
	public void goProfile() {
		String id = idlbl.getText();
		String name = namelbl.getText();
		String info = infolbl.getText();
		
		UserVO user = new UserVO();
		user.setId(id);
		user.setName(name);
		user.setInfo(info);
		user.setDeck(bi);
		
		MainApp.app.setProfile(user);
		MainApp.app.loadPane("profile");
	}
	
}
