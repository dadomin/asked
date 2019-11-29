package views;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

import domain.AnswerVO;
import domain.UserVO;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.MainApp;
import util.JDBCUtil;
import util.Util;

public class ProfileController extends MasterController {
	@FXML Label namelbl;
	@FXML Label infolbl;
	@FXML Canvas canvas;
	
	@FXML TextArea question;
	
	public UserVO user;
	
	@FXML Label doneq;
	@FXML Label newq;
	@FXML Label rejectq;
	
	@FXML VBox answerList;
	
	public void setData(String id, String name, String info, BufferedImage b) {
		user = new UserVO();
		user.setId(id);
		user.setName(name);
		user.setInfo(info);
		user.setDeck(b);
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
		question.setText("");
		setCount();
		setDone();
	}
	
	public void setCount() {
		donequestion();
		newquestion();
		rejectquestion();
	}
	
	public void showNew() {
		boolean check = MainApp.app.checklogin(user);
		if(check == false) {
			return;
		}
		int size = answerList.getChildren().size();
		answerList.getChildren().remove(0, size);
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM `ask_qa` WHERE id = ? AND answer IS NULL AND `reject` IS NULL";
		List<AnswerVO> list = new ArrayList<>();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getId());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				AnswerVO temp = new AnswerVO();
				temp.setUser(user);
				temp.setQuestion(rs.getString("question"));
				temp.setIdx(rs.getInt("idx"));
				String qdate = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(rs.getTimestamp("qdate"));
				temp.setQdate(qdate);
				list.add(temp);
			}
			makeDoneFXML(list);
		}catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 정보 가져오기 중 오류 발생", AlertType.ERROR);
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
	}
	
	private void makeDoneFXML(List<AnswerVO> list) throws Exception {
		for(AnswerVO item : list) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/views/NewAnswer.fxml"));
			
			AnchorPane root = loader.load();
			NewAnswerController ac = loader.getController();
			UserVO user = item.getUser();
			String question = item.getQuestion();
			String date = item.getQdate();
			int idx = item.getIdx();
			ac.setData(user, question, date, idx);
			answerList.getChildren().add(root);
		}
	}
	
	public void setDone() {
		int size = answerList.getChildren().size();
		answerList.getChildren().remove(0, size);
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM `ask_qa` WHERE id = ? AND answer IS NOT NULL AND `reject` IS NULL ORDER BY adate DESC";
		List<AnswerVO> list = new ArrayList<>();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getId());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				AnswerVO temp = new AnswerVO();
				temp.setUser(user);
				temp.setAnswer(rs.getString("answer"));
				temp.setQuestion(rs.getString("question"));
				String fdate = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(rs.getTimestamp("adate"));
				temp.setAdate(fdate);
				list.add(temp);
			}
			makeFXML(list);
		}catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 정보 가져오기 중 오류 발생", AlertType.ERROR);
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
	}
	
	private void makeFXML(List<AnswerVO> list) throws Exception {
		for(AnswerVO item : list) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/views/Answer.fxml"));	
			AnchorPane root = loader.load();
			AnswerController ac = loader.getController();
			UserVO user = item.getUser();
			String question = item.getQuestion();
			String answer = item.getAnswer();
			String date = item.getAdate();
			ac.setData(user, date, question, answer);
			answerList.getChildren().add(root);
		}
	}
	
	public void setReject() {
		int size = answerList.getChildren().size();
		answerList.getChildren().remove(0, size);
		
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM `ask_qa` WHERE `id` = ? AND `reject` IS NOT NULL ORDER BY qdate DESC";
		List<AnswerVO> list = new ArrayList<>();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getId());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				AnswerVO temp = new AnswerVO();
				temp.setUser(user);
				temp.setQuestion(rs.getString("question"));
				String fdate = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(rs.getTimestamp("qdate"));
				temp.setQdate(fdate);
				temp.setIdx(rs.getInt("idx"));
				list.add(temp);
			}
			makeRejectFXML(list);
		}catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 정보 가져오기 중 오류 발생", AlertType.ERROR);
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
	}
	
	private void makeRejectFXML(List<AnswerVO> list) throws Exception {
		for(AnswerVO item : list) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/views/RejectAnswer.fxml"));	
			AnchorPane root = loader.load();
			RejectAnswerController rac = loader.getController();
			UserVO user = item.getUser();
			String question = item.getQuestion();
			String date = item.getQdate();
			int id = item.getIdx();
			rac.setData(user, question, date, id);
			answerList.getChildren().add(root);
		}
	}
	
	public void donequestion() {
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(*) AS cnt FROM `ask_qa` WHERE id = ? AND answer IS NOT NULL AND reject IS NULL";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				doneq.setText(rs.getString("cnt"));
			}
		}catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 정보 가져오기 중 오류 발생", AlertType.ERROR);
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
	}
	
	public void newquestion() {
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(*) AS cnt FROM `ask_qa` WHERE id = ? AND answer IS NULL AND reject IS NULL";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				newq.setText(rs.getString("cnt"));
			}
		}catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 정보 가져오기 중 오류 발생", AlertType.ERROR);
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
	}
	
	public void rejectquestion() {
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(*) AS cnt FROM `ask_qa` WHERE id = ? AND reject IS NOT NULL";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				rejectq.setText(rs.getString("cnt"));
			}
		}catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 정보 가져오기 중 오류 발생", AlertType.ERROR);
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
	}
	
	public void goSearch() {
		MainApp.app.loadPane("searchName");
	}
	
	public void gohome() {
		MainApp.app.slideOut(getRoot());
	}
	
	public void question() {
		String qu = question.getText();
		
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		
		String sql = "INSERT INTO ask_qa (`id`, `question`, `qdate`) VALUES (?, ?, ?)";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getId());
			pstmt.setString(2, qu);
			Calendar cal = Calendar.getInstance();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());
			pstmt.setTimestamp(3, timestamp);
			
			if(pstmt.executeUpdate() != 1) {
				Util.showAlert("에러", "데이터베이스에 질문이 올바르게 입력되지 않았습니다. 다시 도전하세요.", AlertType.ERROR);
				return;
			}
			Util.showAlert("성공", "질문이 등록되었습니다.", AlertType.INFORMATION);
			newquestion();
			
		} catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 정보 가져오기 중 오류 발생", AlertType.ERROR);
		} finally {
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
		
	}
}
