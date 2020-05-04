package GameMaking_1;

public class Enemy {
	int x;
	int y;
	int speed; // 적 이동 속도 변수를 추가
	public int imgWidth;
	public int imgHeight;
	
	Enemy(int x, int y, int speed ) {
	this.x = x;
	this.y = y;
	this.speed = speed;
	// 객체 생성시 속도 값을 추가로 받습니다.
	}

	public void move(){ 
	x -= speed;// 적이동속도만큼 이동
	}
}
