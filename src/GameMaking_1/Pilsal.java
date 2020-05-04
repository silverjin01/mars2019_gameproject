package GameMaking_1;

public class Pilsal {
	int x;
	int y; 
	int speed; // 미사일 스피드 변수를 추가.
	
	Pilsal(int x, int y, int speed) {
	this.x = x; 
	this.y = y;
	this.speed = speed;
	// 객체 생성시 속도 값을 추가로 받습니다.
	}
	public void move(){
	x += speed; // 미사일 스피드 속도 만큼 이동
	}
}
