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

	int[] cx = { 0, 0, 0 }; // ��� ��ũ�� �ӵ� ����� ����
	int bx = 0; // ��ü ��� ��ũ�� �� ����

	boolean KeyUp = false;
	boolean KeyDown = false;
	boolean KeySpace = false;

	int cnt;

	int player_Speed; // ������ ĳ���Ͱ� �����̴� �ӵ��� ������ ����
	int missile_Speed; // �̻����� ���󰡴� �ӵ� ������ ����
	int fire_Speed; // �̻��� ���� �ӵ� ���� ����
	int enemy_speed; // �� �̵� �ӵ� ����
	int player_Status = 0;

	// ���� ĳ���� ���� üũ ���� 0 : ����, 1: �̻��Ϲ߻�, 2: �浹
	int game_Score; // ���� ���� ���
	int player_Hitpoint; // �÷��̾� ĳ������ ü��

	// int e_w, e_h; //�ҽ� �������� ���� �ش� ������ �ʿ���������ϴ�
	// int m_w, m_h;

	Thread th;
	Toolkit tk = Toolkit.getDefaultToolkit();
	Image[] Player_img;
	// �÷��̾� �ִϸ��̼� ǥ���� ���� �̹����� �迭�� ����
	Image BackGround_img; // ���ȭ�� �̹���
	Image[] Cloud_img; // �����̴� ���� �̹����迭
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
		setTitle("���� ���� �����");
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

		// �÷��̾� �ִϸ��̼� ǥ���� ���� �����̸���
		// �ѹ����� ���� �迭�� ��´�.
		BackGround_img = new ImageIcon("main_background.png").getImage();

		// ��ü ���ȭ�� �̹����� �޽��ϴ�.
		Cloud_img = new Image[3];
		for (int i = 0; i < Cloud_img.length; ++i) {
			Cloud_img[i] = new ImageIcon("item0" + i + ".png").getImage();
		}

		// �̹��� ũ�� ��� �޼ҵ带 �����մϴ�.
		// Swing���� �����Ǵ� ImageIcon����
		// �̹��� ũ�Ⱚ�� ���� ������ �ֱ���.
		game_Score = 0;// ���� ���ھ� �ʱ�ȭ
		player_Hitpoint = 3;// ���� �÷��̾� ü��
		player_Speed = 10; // ���� ĳ���� �����̴� �ӵ� ����
		missile_Speed = 11; // �̻��� ������ �ӵ� ����
		fire_Speed = 15; // �̻��� ���� �ӵ� ����
		enemy_speed = 7;// ���� ������� �ӵ� ����
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
			// �̻����� �߻��ϸ� �÷��̾� ĳ���� ���¸� 1�� ����.
			if ((cnt % fire_Speed) == 0) {
				// �÷��̾��� �̻��� ����ӵ��� �����Ѵ�.
				ms = new Pilsal(x + 150, y + 30, missile_Speed);
				// �̻��� �̵� �ӵ� ���� �߰��� �޴´�
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
					// �̻����� ��ǥ �� �̹�������,
					// ���� ��ǥ�� �̹��� ������ �޾�
					// �浹���� �޼ҵ�� �ѱ�� true,false����
					// ���� �޾� true�� �Ʒ��� �����մϴ�.
					Missile_List.remove(i);
					Enemy_List.remove(j);
					game_Score += 10; // ���� ������ +10��.
					// ���� ��ġ���ִ� ���� �߽� ��ǥ x,y ����
					// ���� ������ ���� �� ( 0 �Ǵ� 1 )�� �޽��ϴ�.
					// ���� ���� �� - 0 : ���� , 1 : �ܼ� �ǰ�

					// �浹�������� ����� ���� ��ġ��
					// ����Ʈ�� �߰��Ѵ�.
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
				// �÷��̾�� ���� �浹�� �����Ͽ�
				// boolean���� ���� �޾� true�� �Ʒ��� �����մϴ�.
				player_Hitpoint--; // �÷��̾� ü���� 1����ϴ�.
				Enemy_List.remove(i); // ���� �����մϴ�.
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
			// �� ������ �ӵ��� �߰��� �޾� ���� �����Ѵ�.
		}
	}
	
	
	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2) {
		// ���� �浹 ���� �ҽ��� �����մϴ�.
		// ���� �̹��� ������ �ٷ� �޾� �ش� �̹����� ����, ���̰���
		// �ٷ� ����մϴ�.

		boolean check = false;
		if (Math.abs((x1 + img1.getWidth(null) / 2) - (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2
				+ img1.getWidth(null) / 2)
				&& Math.abs((y1 + img1.getHeight(null) / 2)
						- (y2 + img2.getHeight(null) / 2)) < (img2.getHeight(null) / 2 + img1.getHeight(null) / 2)) {
			// �̹��� ����, ���̰��� �ٷ� �޾� ����մϴ�.
			check = true;// �� ���� true�� check�� true�� �����մϴ�.
		} else {
			check = false;
		}
		return check; // check�� ���� �޼ҵ忡 ���� ��ŵ�ϴ�.
	}

	public void paint(Graphics g) {
		buffImage = createImage(f_width, f_height);
		buffg = buffImage.getGraphics();
		update(g);
	}

	public void update(Graphics g) {
		Draw_Background(); // ��� �̹��� �׸��� �޼ҵ� ����
		Draw_Player(); // �÷��̾ �׸��� �޼ҵ� �̸� ����
		Draw_Enemy();
		Draw_Missile();
		Draw_StatusText();// ���� ǥ�� �ؽ�Ʈ�� �׸��� �޼ҵ� ����
		g.drawImage(buffImage, 0, 0, this);
	}

	public void Draw_Background() {
		// ��� �̹����� �׸��� �κ��Դϴ�.
		buffg.clearRect(0, 0, f_width, f_height);
		buffg.drawImage(BackGround_img, 0, 0, this);
		// ȭ�� ����� ����� ���� ���⼭ �����մϴ�.
//		if (bx > -3500) {
//			// �⺻ ���� 0�� bx�� -3500 ���� ũ�� ����
//			buffg.drawImage(BackGround_img, bx, 0, this);
//			bx -= 1;
//			// bx�� 0���� -1��ŭ ��� ���̹Ƿ� ����̹����� x��ǥ��
//			// ��� �������� �̵��Ѵ�. �׷��Ƿ� ��ü ����� õõ��
//			// �������� �����̰� �ȴ�.
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
			// 3���� ���� �̹����� ���� �ٸ� �ӵ� ������ �������� ������.
		}
	}

	public void Draw_Player() {
		switch (player_Status) {
		case 0: // ����
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(Player_img[1], x, y, this);
			} else {
				buffg.drawImage(Player_img[2], x, y, this);
			}
			// �����ʿ��� ���� �մ� �̹����� ������ �׷��ش�.
			break;
		case 1: // �̻��Ϲ߻�
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(Player_img[3], x, y, this);
			} else {
				buffg.drawImage(Player_img[4], x, y, this);
			}
			// �̻����� ��µ��� �̹����� ������ �׷��ش�.
			player_Status = 0;
			// �̻��� ��Ⱑ ������ �÷��̾� ���¸� 0���� ������.
			break;
		case 2: // �浹
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
		//��ź ������
	}

	public void Draw_StatusText() { // ���� üũ�� �ؽ�Ʈ�� �׸��ϴ�.
		buffg.setFont(new Font("Defualt", Font.ITALIC, 20));
		buffg.setColor(Color.RED);
		// ��Ʈ ������ �մϴ�. �⺻��Ʈ, ����, ������ 20
		buffg.drawString("SCORE : " + game_Score, 1000, 70);
		// ��ǥ x : 1000, y : 70�� ���ھ ǥ���մϴ�.
		buffg.drawString("HitPoint : " + player_Hitpoint, 1000, 90);
		// ��ǥ x : 1000, y : 90�� �÷��̾� ü���� ǥ���մϴ�.
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
			// ĳ���Ͱ� �������� ȭ�� ���� �� �Ѿ�� �մϴ�.
			player_Status = 0;
			// �̵�Ű�� �������� �÷��̾� ���¸� 0���� �����ϴ�.
		}

		if (KeyDown == true) {
			if (y + Player_img[0].getHeight(null) < f_height)
				y += 5;
			// ĳ���Ͱ� �������� ȭ�� �Ʒ��� �� �Ѿ�� �մϴ�.
			player_Status = 0;
			// �̵�Ű�� �������� �÷��̾� ���¸� 0���� �����ϴ�.
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
