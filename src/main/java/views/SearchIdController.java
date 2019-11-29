package views;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import domain.UserVO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.MainApp;
import util.JDBCUtil;
import util.Util;

public class SearchIdController extends MasterController {
	@FXML TextField id;
	@FXML VBox profList;
	
	public List<UserVO> list;
	
	public void search() {
		list = new ArrayList<>();
		int size = profList.getChildren().size();
		profList.getChildren().remove(0, size);
		String sid = id.getText();
		
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM `ask_user` WHERE id LIKE ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, "%" + sid + "%");
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				UserVO temp = new UserVO();
				temp.setId(rs.getString("id"));
				temp.setName(rs.getString("name"));
				temp.setInfo(rs.getString("info"));
				InputStream in = rs.getBinaryStream("img");
				BufferedImage bimg = ImageIO.read(in);
				temp.setDeck(bimg);
				list.add(temp);
			}
			makeFXML(list);
		} catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 정보 가져오기 중 오류 발생", AlertType.ERROR);
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
	}
	
	private void makeFXML(List<UserVO> list) throws Exception {
		for(UserVO item : list) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/views/ProfileItem.fxml"));
			
			AnchorPane root = loader.load();
			ProfilItemController pc = loader.getController();
			String id = item.getId();
			String name = item.getName();
			String info = item.getInfo();
			BufferedImage deck = item.getDeck();
			pc.setData(id, name, info, deck);
			profList.getChildren().add(root);
		}
	}
	
	public void searchName() {
		MainApp.app.slideOut(getRoot());
	}
	
	public void gohome() {
		MainApp.app.slideOut(getRoot());
	}
}
