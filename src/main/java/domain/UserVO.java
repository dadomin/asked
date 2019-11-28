package domain;

import java.awt.image.BufferedImage;

import com.mysql.cj.jdbc.Blob;

import javafx.scene.image.Image;

public class UserVO {
	private String id;
	private String name;
	private String info;
	private BufferedImage deck;
	
	public BufferedImage getDeck() {
		return deck;
	}
	public void setDeck(BufferedImage deck) {
		this.deck = deck;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}	
}
