package GameMaking_1;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.JFrame;

public class Char extends JFrame {
	final int MoveStep = 10;// 캐릭터가 움직이는 간격
	final int MaxX = 1280;// 맵 크기 x
	final int MaxY = 720;// 맵 크기 y

	int x;// 캐릭터 x값
	int y;// 캐릭터 y값
	int dir;// 캐릭터 방향
	Image img[];// 캐릭터 이미지를 담기 위한 배열
	int imgWidth = 80;// 캐릭터 이미지 크기
	int imgHeight = 240;// 캐릭터 이미지 크기
	boolean jump;// 캐릭터 점프
	int jumpCount = 1;

	int count;

	int hp;
	int maxdamage;
	int pilsal;

	Random r = new Random();

	// 생성자(움직임)
	public Char(int x, int y, Image img[]) {
		this.x = x;
		this.y = y;
		this.img = img;
	}

	// 생성자(특징)
	public Char(int hp, int maxdamage, int pilsal) {
		this.hp = hp;
		this.maxdamage = maxdamage;
		this.pilsal = pilsal;
	}

	// 캐릭터 특징 메소드
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getmaxDamage() {
		return maxdamage;
	}

	public void setmaxDamage(int maxdamage) {
		this.maxdamage = maxdamage;
	}

	// 캐릭터 움직임 메소드
	public void moveLeft() {
		x = x - MoveStep;
		if (x < 0)
			x = 0;
	}

	public void moveRight() {
		x = x + MoveStep;
		if (x > MaxX - imgWidth)
			x = MaxX - imgWidth;
	}

	public void moveUp() {
		y = y - MoveStep;
		if (y < 0)
			y = 0;
	}

	public void moveSlide() {
		x = x - (MoveStep * 30);
		y = 0;
		if (x > MaxX - imgWidth)
			x = MaxX - imgWidth;
	}

	public void attack(Char c) {
		if (c instanceof Char) {
			System.out.println();
			int damage = r.nextInt(this.maxdamage) + 1;
			if (c.pilsal() == 1) {
				damage = 0;
			}
			c.setHp(c.getHp() - damage);
		}
	}

	public int attack() {
		int damage = r.nextInt(this.maxdamage) + 1;
		return damage;
	}

	private int pilsal() {
		int Random = r.nextInt(5) + 1;
		int Defense = 0;
		if (Random > 4)
			return Defense;
		else
			Defense = 0;
		return Defense;
	}

	public boolean crush(Obstacles ob) {
		if (((ob.x < x) && (x < ob.x + ob.imgWidth)) && ((ob.y < y) && (y < ob.y + ob.imgHeight)))
			return true;
		if (((ob.x < x + imgWidth) && (x + imgWidth < ob.x + ob.imgWidth)) && ((ob.y < y) && (y < ob.y + ob.imgHeight)))
			return true;
		if (((ob.x < x) && (x < ob.x + ob.imgWidth))
				&& ((ob.y < y + imgHeight) && (y + imgHeight < ob.y + ob.imgHeight)))
			return true;
		if (((ob.x < x + imgWidth) && (x + imgWidth < ob.x + ob.imgWidth))
				&& ((ob.y < y + imgHeight) && (y + imgHeight < ob.y + ob.imgHeight)))
			return true;

		return false;
	}

	public boolean charUpdate() {
		count++;

		if (jump == true) {
			if (jumpCount <= 5)
				y = y - 10;
			else if (jumpCount <= 10)
				y = y + 10;
			if (jumpCount == 10) {
				jump = false;
				jumpCount = 1;
			} else
				jumpCount++;
		}
		// 목적지 도착 (화면 끝 체크)
		if (x >= 1280 - imgWidth)
			return true;
		else
			return false;
	}

	// 화면에 그리기
	public void paint(Graphics g) {
		// 캐릭터의 상태에 따라 다른 이미지를 그려줌
		if (dir == 1) {
			if (jump == true)
				g.drawImage(img[2], x, y, this);
			else
				g.drawImage(img[count % 2], x, y, this);
		} else if (dir == 2) {
			if (jump == true)
				g.drawImage(img[5], x, y, this);
			else
				g.drawImage(img[count % 2 + 3], x, y, this);
		}

	}

}
