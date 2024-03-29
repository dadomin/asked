package views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import domain.AnswerVO;
import domain.UserVO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import main.MainApp;
import util.JDBCUtil;
import util.Util;

public class NewAnswerController {
	@FXML Label question;
	@FXML Label qdate;
	@FXML TextArea answer;
	
	public UserVO user;
	public int idx;
	
	public void setData(UserVO u, String q, String qd, int id) {
		this.user = u;
		this.idx = id;
		question.setText(q);
		qdate.setText(qd);
	}
	
	public void goAnswer() {
		String an = answer.getText();
		
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "UPDATE `ask_qa` SET `answer` = ?, `adate`=? WHERE idx = ?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, an);
			Calendar cal = Calendar.getInstance();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());
			pstmt.setTimestamp(2, timestamp);
			pstmt.setInt(3, this.idx);
			if(pstmt.executeUpdate() != 1) {
				Util.showAlert("에러", "데이터베이스에 답변이 올바르게 입력되지 않았습니다. 다시 도전하세요.", AlertType.ERROR);
				return;
			}
			Util.showAlert("성공", "답변이 등록되었습니다.", AlertType.INFORMATION);
			MainApp.app.doneNewAnswer();
		}catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 정보 가져오기 중 오류 발생", AlertType.ERROR);
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
	}
	
	public void goreject() {

		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "UPDATE `ask_qa` SET `reject` = ? WHERE idx = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, 1);
			pstmt.setInt(2, idx);
			if(pstmt.executeUpdate() != 1) {
				Util.showAlert("에러", "데이터베이스에 오류가 생겼습니다.. 다시 도전하세요.", AlertType.ERROR);
				return;
			}
			Util.showAlert("성공", "해당 질문을 성공적으로 거절하였습니다.", AlertType.INFORMATION);
			MainApp.app.doneRejectAnswer();
		}catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 정보 가져오기 중 오류 발생", AlertType.ERROR);
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
		
	}
	
}
