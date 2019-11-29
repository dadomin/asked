package views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import domain.UserVO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import main.MainApp;
import util.JDBCUtil;
import util.Util;

public class RejectAnswerController {
	@FXML Label question;
	@FXML Label qdate;
	
	public UserVO user;
	public int idx;
	
	public void setData(UserVO u, String qu, String qd, int id) {
		this.user = u;
		this.idx = id;
		question.setText(qu);
		qdate.setText(qd);
	}
	
	public void delete() {
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "DELETE FROM `ask_qa` WHERE idx = ?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			if(pstmt.executeUpdate() != 1) {
				Util.showAlert("에러", "데이터베이스에서 오류가 발생하였습니다. 다시 도전하세요.", AlertType.ERROR);
				return;
			}
			Util.showAlert("성공", "질문이 삭제되었습니다.", AlertType.INFORMATION);
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
	
	public void restore() {
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "UPDATE `ask_qa` SET reject = NULL WHERE idx = ?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			if(pstmt.executeUpdate() != 1) {
				Util.showAlert("에러", "데이터베이스에서 오류가 발생하였습니다. 다시 도전하세요.", AlertType.ERROR);
				return;
			}
			Util.showAlert("성공", "질문이 다시 등록되었습니다.", AlertType.INFORMATION);
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
}
