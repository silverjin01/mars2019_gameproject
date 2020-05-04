package GameMaking_1;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.JFrame;

public class Char extends JFrame {
	final int MoveStep = 10;// ĳ���Ͱ� �����̴� ����
	final int MaxX = 1280;// �� ũ�� x
	final int MaxY = 720;// �� ũ�� y

	int x;// ĳ���� x��
	int y;// ĳ���� y��
	int dir;// ĳ���� ����
	Image img[];// ĳ���� �̹����� ��� ���� �迭
	int imgWidth = 80;// ĳ���� �̹��� ũ��
	int imgHeight = 240;// ĳ���� �̹��� ũ��
	boolean jump;// ĳ���� ����
	int jumpCount = 1;

	int count;

	int hp;
	int maxdamage;
	int pilsal;

	Random r = new Random();

	// ������(������)
	public Char(int x, int y, Image img[]) {
		this.x = x;
		this.y = y;
		this.img = img;
	}

	// ������(Ư¡)
	public Char(int hp, int maxdamage, int pilsal) {
		this.hp = hp;
		this.maxdamage = maxdamage;
		this.pilsal = pilsal;
	}

	// ĳ���� Ư¡ �޼ҵ�
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

	// ĳ���� ������ �޼ҵ�
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
		// ������ ���� (ȭ�� �� üũ)
		if (x >= 1280 - imgWidth)
			return true;
		else
			return false;
	}

	// ȭ�鿡 �׸���
	public void paint(Graphics g) {
		// ĳ������ ���¿� ���� �ٸ� �̹����� �׷���
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
