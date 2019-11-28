package views;

import domain.UserVO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class NewAnswerController {
	@FXML Label question;
	@FXML Label qdate;
	@FXML TextArea answer;
	
	public UserVO user;
	
	public void setData(UserVO u, String q, String qd) {
		this.user = u;
		question.setText(q);
		qdate.setText(qd);
	}
}
