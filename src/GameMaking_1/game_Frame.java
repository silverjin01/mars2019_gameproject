package GameMaking_1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

class game_Frame extends JFrame implements KeyListener, Runnable {
	Pilsal ms;
	Enemy en;

	boolean heroDead;
	boolean gameOver;

	int f_width;
	int f_height;
	int x, y;

	int[] cx = { 0, 0, 0 }; // 배경 스크롤 속도 제어용 변수
	int bx = 0; // 전체 배경 스크롤 용 변수

	boolean KeyUp = false;
	boolean KeyDown = false;
	boolean KeySpace = false;

	int cnt;

	int player_Speed; // 유저의 캐릭터가 움직이는 속도를 조절할 변수
	int missile_Speed; // 미사일이 날라가는 속도 조절할 변수
	int fire_Speed; // 미사일 연사 속도 조절 변수
	int enemy_speed; // 적 이동 속도 설정
	int player_Status = 0;

	// 유저 캐릭터 상태 체크 변수 0 : 평상시, 1: 미사일발사, 2: 충돌
	int game_Score; // 게임 점수 계산
	int player_Hitpoint; // 플레이어 캐릭터의 체력

	// int e_w, e_h; //소스 변경으로 인해 해당 변수가 필요없어졌습니다
	// int m_w, m_h;

	Thread th;
	Toolkit tk = Toolkit.getDefaultToolkit();
	Image[] Player_img;
	// 플레이어 애니메이션 표현을 위해 이미지를 배열로 받음
	Image BackGround_img; // 배경화면 이미지
	Image[] Cloud_img; // 움직이는 배경용 이미지배열
	Image Missile_img;
	Image Enemy_img;

	ArrayList Missile_List = new ArrayList();
	ArrayList Enemy_List = new ArrayList();
	ArrayList Player_List = new ArrayList();

	Image buffImage;
	Graphics buffg;

	public game_Frame() {
		init();
		start();
		setTitle("슈팅 게임 만들기");
		setSize(f_width, f_height);
		Dimension screen = tk.getScreenSize();
		int f_xpos = (int) (screen.getWidth() / 2 - f_width / 2);
		int f_ypos = (int) (screen.getHeight() / 2 - f_height / 2);
		setLocation(f_xpos, f_ypos);
		setResizable(false);
		setVisible(true);
	}

	public void init() {
		x = 100;
		y = 480;
		f_width = 1280;
		f_height = 760;

		Missile_img = new ImageIcon("Missile.png").getImage();

		Enemy_img = new ImageIcon("enemy1.png").getImage();

		Player_img = new Image[5];
		for (int i = 0; i < Player_img.length; ++i) {
			Player_img[i] = new ImageIcon("hero0" + i + ".png").getImage();
		}

		// 플레이어 애니메이션 표현을 위해 파일이름을
		// 넘버마다 나눠 배열로 담는다.
		BackGround_img = new ImageIcon("main_background.png").getImage();

		// 전체 배경화면 이미지를 받습니다.
		Cloud_img = new Image[3];
		for (int i = 0; i < Cloud_img.length; ++i) {
			Cloud_img[i] = new ImageIcon("item0" + i + ".png").getImage();
		}

		// 이미지 크기 계산 메소드를 삭제합니다.
		// Swing에서 지원되는 ImageIcon으로
		// 이미지 크기값을 쉽게 얻을수 있군요.
		game_Score = 0;// 게임 스코어 초기화
		player_Hitpoint = 3;// 최초 플레이어 체력
		player_Speed = 10; // 유저 캐릭터 움직이는 속도 설정
		missile_Speed = 11; // 미사일 움직임 속도 설정
		fire_Speed = 15; // 미사일 연사 속도 설정
		enemy_speed = 7;// 적이 날라오는 속도 설정
	}

	public void start() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		th = new Thread(this);
		th.start();
	}

	public void run() {
		try {
			while (true) {
				KeyProcess();
				EnemyProcess();
				MissileProcess();
				repaint();
				Thread.sleep(20);
				cnt++;

				if(player_Hitpoint == 0) {
					break;
				}else if(game_Score == 100)
					break;
			}

		} catch (Exception e) {
		}
	}

	public void MissileProcess() {
		if (KeySpace) {
			player_Status = 1;
			// 미사일을 발사하면 플레이어 캐릭터 상태를 1로 변경.
			if ((cnt % fire_Speed) == 0) {
				// 플레이어의 미사일 연사속도를 조절한다.
				ms = new Pilsal(x + 150, y + 30, missile_Speed);
				// 미사일 이동 속도 값을 추가로 받는다
				Missile_List.add(ms);
			}
		}

		for (int i = 0; i < Missile_List.size(); ++i) {
			ms = (Pilsal) Missile_List.get(i);
			ms.move();
			if (ms.x > f_width - 20) {
				Missile_List.remove(i);
			}
			for (int j = 0; j < Enemy_List.size(); ++j) {
				en = (Enemy) Enemy_List.get(j);
				if (Crash(ms.x, ms.y, en.x, en.y, Missile_img, Enemy_img)) {
					// 미사일의 좌표 및 이미지파일,
					// 적의 좌표및 이미지 파일을 받아
					// 충돌판정 메소드로 넘기고 true,false값을
					// 리턴 받아 true면 아래를 실행합니다.
					Missile_List.remove(i);
					Enemy_List.remove(j);
					game_Score += 10; // 게임 점수를 +10점.
					// 적이 위치해있는 곳의 중심 좌표 x,y 값과
					// 폭발 설정을 받은 값 ( 0 또는 1 )을 받습니다.
					// 폭발 설정 값 - 0 : 폭발 , 1 : 단순 피격

					// 충돌판정으로 사라진 적의 위치에
					// 이펙트를 추가한다.
				}
//				if(Crash(x,y,))
			}
		}
	}

	public void EnemyProcess() {
		for (int i = 0; i < Enemy_List.size(); ++i) {
			en = (Enemy) (Enemy_List.get(i));
			en.move();
			if (en.x < -200) {
				Enemy_List.remove(i);
			}

			if (Crash(x, y, en.x, en.y, Player_img[0], Enemy_img)) {
				// 플레이어와 적의 충돌을 판정하여
				// boolean값을 리턴 받아 true면 아래를 실행합니다.
				player_Hitpoint--; // 플레이어 체력을 1깍습니다.
				Enemy_List.remove(i); // 적을 제거합니다.
				game_Score += 10;
			}
		}

		if (cnt % 200 == 0) {
			en = new Enemy(f_width + 100, 100, enemy_speed);
			Enemy_List.add(en);
			en = new Enemy(f_width + 100, 200, enemy_speed);
			Enemy_List.add(en);
			en = new Enemy(f_width + 100, 300, enemy_speed);
			Enemy_List.add(en);
			en = new Enemy(f_width + 100, 400, enemy_speed);
			Enemy_List.add(en);
			en = new Enemy(f_width + 100, 500, enemy_speed);
			Enemy_List.add(en);
			// 적 움직임 속도를 추가로 받아 적을 생성한다.
		}
	}
	
	
	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2) {
		// 기존 충돌 판정 소스를 변경합니다.
		// 이제 이미지 변수를 바로 받아 해당 이미지의 넓이, 높이값을
		// 바로 계산합니다.

		boolean check = false;
		if (Math.abs((x1 + img1.getWidth(null) / 2) - (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2
				+ img1.getWidth(null) / 2)
				&& Math.abs((y1 + img1.getHeight(null) / 2)
						- (y2 + img2.getHeight(null) / 2)) < (img2.getHeight(null) / 2 + img1.getHeight(null) / 2)) {
			// 이미지 넓이, 높이값을 바로 받아 계산합니다.
			check = true;// 위 값이 true면 check에 true를 전달합니다.
		} else {
			check = false;
		}
		return check; // check의 값을 메소드에 리턴 시킵니다.
	}

	public void paint(Graphics g) {
		buffImage = createImage(f_width, f_height);
		buffg = buffImage.getGraphics();
		update(g);
	}

	public void update(Graphics g) {
		Draw_Background(); // 배경 이미지 그리기 메소드 실행
		Draw_Player(); // 플레이어를 그리는 메소드 이름 변경
		Draw_Enemy();
		Draw_Missile();
		Draw_StatusText();// 상태 표시 텍스트를 그리는 메소드 실행
		g.drawImage(buffImage, 0, 0, this);
	}

	public void Draw_Background() {
		// 배경 이미지를 그리는 부분입니다.
		buffg.clearRect(0, 0, f_width, f_height);
		buffg.drawImage(BackGround_img, 0, 0, this);
		// 화면 지우기 명령은 이제 여기서 실행합니다.
//		if (bx > -3500) {
//			// 기본 값이 0인 bx가 -3500 보다 크면 실행
//			buffg.drawImage(BackGround_img, bx, 0, this);
//			bx -= 1;
//			// bx를 0에서 -1만큼 계속 줄이므로 배경이미지의 x좌표는
//			// 계속 좌측으로 이동한다. 그러므로 전체 배경은 천천히
//			// 좌측으로 움직이게 된다.
//		} else {
//			bx = 0;
//		}

		for (int i = 0; i < cx.length; ++i) {
			if (cx[i] < 1400) {
				cx[i] += 5 + i * 3;
			} else {
				cx[i] = 0;
			}
			buffg.drawImage(Cloud_img[i], 1200 - cx[i], 50 + i * 200, this);
			// 3개의 구름 이미지를 각기 다른 속도 값으로 좌측으로 움직임.
		}
	}

	public void Draw_Player() {
		switch (player_Status) {
		case 0: // 평상시
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(Player_img[1], x, y, this);
			} else {
				buffg.drawImage(Player_img[2], x, y, this);
			}
			// 엔진쪽에서 불을 뿜는 이미지를 번갈아 그려준다.
			break;
		case 1: // 미사일발사
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(Player_img[3], x, y, this);
			} else {
				buffg.drawImage(Player_img[4], x, y, this);
			}
			// 미사일을 쏘는듯한 이미지를 번갈아 그려준다.
			player_Status = 0;
			// 미사일 쏘기가 끝나면 플레이어 상태를 0으로 돌린다.
			break;
		case 2: // 충돌
			break;
		}
	}

	public void Draw_Missile() {
		for (int i = 0; i < Missile_List.size(); ++i) {
			ms = (Pilsal) (Missile_List.get(i));
			buffg.drawImage(Missile_img, ms.x, ms.y, this);
		}
	}

	public void Draw_Enemy() {
		for (int i = 0; i < Enemy_List.size(); ++i) {
			en = (Enemy) (Enemy_List.get(i));
			buffg.drawImage(Enemy_img, en.x, en.y, this);			
		}
	}
	
	public void Draw_Obstacles() {
		//폭탄 구성중
	}

	public void Draw_StatusText() { // 상태 체크용 텍스트를 그립니다.
		buffg.setFont(new Font("Defualt", Font.ITALIC, 20));
		buffg.setColor(Color.RED);
		// 폰트 설정을 합니다. 기본폰트, 굵게, 사이즈 20
		buffg.drawString("SCORE : " + game_Score, 1000, 70);
		// 좌표 x : 1000, y : 70에 스코어를 표시합니다.
		buffg.drawString("HitPoint : " + player_Hitpoint, 1000, 90);
		// 좌표 x : 1000, y : 90에 플레이어 체력을 표시합니다.
		if(player_Hitpoint == 0) {
			buffg.setFont(new Font("Defualt",Font.BOLD,100));
			buffg.setColor(Color.white);
			buffg.drawString("Game Over", 640, 300);
		}
		if(game_Score == 100) {
			buffg.setFont(new Font("Defualt",Font.BOLD,100));
			buffg.setColor(Color.white);
			buffg.drawString("Success!!", 640, 300);
		}
	}

	public void KeyProcess() {
		if (KeyUp == true) {
			if (y > 20)
				y -= 5;
			// 캐릭터가 보여지는 화면 위로 못 넘어가게 합니다.
			player_Status = 0;
			// 이동키가 눌려지면 플레이어 상태를 0으로 돌립니다.
		}

		if (KeyDown == true) {
			if (y + Player_img[0].getHeight(null) < f_height)
				y += 5;
			// 캐릭터가 보여지는 화면 아래로 못 넘어가게 합니다.
			player_Status = 0;
			// 이동키가 눌려지면 플레이어 상태를 0으로 돌립니다.
		}
	}

	public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				KeyUp = true;
				break;
			case KeyEvent.VK_DOWN:
				KeyDown = true;
				break;
			case KeyEvent.VK_SPACE:
				KeySpace = true;
				break;
			}
//		if ((heroDead == false) && (gameOver == false)) {
//			if (e.getKeyCode() == KeyEvent.VK_UP) {
//				hero.jump = true;
//			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//				hero.slide = true;
//			}
//			heroDead = hero.crush(en);
//			if (heroDead)
//				System.out.println("me");
//			repaint();
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			KeyUp = false;
			break;
		case KeyEvent.VK_DOWN:
			KeyDown = false;
			break;
		case KeyEvent.VK_SPACE:
			KeySpace = false;
			break;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

}
