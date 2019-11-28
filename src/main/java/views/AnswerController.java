package views;

import domain.UserVO;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class AnswerController {
	@FXML Label namelbl;
	@FXML Label datelbl;
	@FXML Label questionlbl;
	@FXML Label answerlbl;
	@FXML Canvas canvas;

	public UserVO user;
	
	public void setData(UserVO u, String date, String question, String answer) {
		this.user = u;
		namelbl.setText(u.getName());
		datelbl.setText(date);
		questionlbl.setText(question);
		answerlbl.setText(answer);
		Image img = SwingFXUtils.toFXImage(user.getDeck(), null);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		double w = canvas.getWidth();
		double h = canvas.getHeight();
		gc.drawImage(img, 0, 0, w,h);
		gc.setStroke(Color.rgb(250, 250, 250));
		gc.setLineWidth(30);
		gc.strokeOval(-15,-15,w+30,h+30);
	}
}
