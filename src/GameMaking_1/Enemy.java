package GameMaking_1;

public class Enemy {
	int x;
	int y;
	int speed; // �� �̵� �ӵ� ������ �߰�
	public int imgWidth;
	public int imgHeight;
	
	Enemy(int x, int y, int speed ) {
	this.x = x;
	this.y = y;
	this.speed = speed;
	// ��ü ������ �ӵ� ���� �߰��� �޽��ϴ�.
	}

	public void move(){ 
	x -= speed;// ���̵��ӵ���ŭ �̵�
	}
}
