package GameMaking_1;

public class Pilsal {
	int x;
	int y; 
	int speed; // �̻��� ���ǵ� ������ �߰�.
	
	Pilsal(int x, int y, int speed) {
	this.x = x; 
	this.y = y;
	this.speed = speed;
	// ��ü ������ �ӵ� ���� �߰��� �޽��ϴ�.
	}
	public void move(){
	x += speed; // �̻��� ���ǵ� �ӵ� ��ŭ �̵�
	}
}
