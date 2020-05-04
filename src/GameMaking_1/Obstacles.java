package GameMaking_1;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

public class Obstacles {
	final int MoveStep = 5;
	final int MaxX = 1280;
	final int MaxY = 720;

	int x;
	int y;
	int dir;
	Image img[];
	int imgWidth = 20;
	int imgHeight = 20;
	int count;
	boolean toggle;

	Random r = new Random();

	// ������
	public Obstacles(int x, int y, Image img[]) {
		this.x = x;
		this.y = y;
		this.img = img;
	}

	// �޼ҵ�
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

	public void moveDown() {
		y = y + MoveStep;
		if (y > MaxY - imgHeight)
			y = MaxX - imgHeight;
	}

	// ����&�ڵ����� �����̱� ���� �޼ҵ�
	public void randomMove() {
		dir = r.nextInt(3);
		if (dir == 0)
			moveUp();
		else if (dir == 1)
			moveLeft();
		else if (dir == 2)
			moveRight();
//		else if (dir == 3)
//			moveDown();

		toggle = !toggle;
	}

	public boolean crush(Char ob) {
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

	// ȭ�鿡 �׸���
	public void paint(Graphics g) {
		// ĳ������ ���¿� ���� �ٸ� �̹����� �׷���
		if (toggle)
			g.drawImage(img[0], x, y, null);
		else
			g.drawImage(img[1], x, y, null);
	}

}
