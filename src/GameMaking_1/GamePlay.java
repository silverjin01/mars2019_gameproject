package GameMaking_1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GamePlay extends JFrame implements Runnable, KeyListener{
	public static final int SCREEN_WIDTH=1280;
	public static final int SCREEN_HEIGHT=720;
	
	boolean gameOver;

	ImageIcon heroImgIcon[] = new ImageIcon[6];
	Image heroImg[] = new Image[6];
	Char hero;
	boolean heroDead;

	ImageIcon batImgIcon[] = new ImageIcon[2];
	Image batImg[] = new Image[2];
	Obstacles zombie1;
	
	Image background;
	Graphics background_img;
	Image off;

	public GamePlay() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1270, 720);
		setVisible(true);

		// Hero 객체 생성
		for (int i = 0; i < 6; i++) {
			heroImgIcon[i] = new ImageIcon("hero0" + (i + 1) + ".png");
			heroImg[i] = heroImgIcon[i].getImage();
		}
		
		hero = new Char(10, 200, heroImg); 
		
		hero.dir = 1;
		// 박쥐 객체 생성
		for (int i = 0; i < 2; i++) {
			batImgIcon[i] = new ImageIcon("enemy" + (i + 1) + ".png");
			batImg[i] = batImgIcon[i].getImage();
		}
		zombie1 = new Obstacles(1200, 200, batImg);

		// repaint();
		addKeyListener(this);
		// 게임 동작 (스레드 동작)
		new Thread(this).start();
	}
	
	public void init() {
		//가상화면 만들기
		background = createImage(1280,760);
		background_img = background.getGraphics();
		//이미지 로드
		MediaTracker myTracker = new MediaTracker(this);
		
		off = Toolkit.getDefaultToolkit().getImage("main_background.png");
		myTracker.addImage(off, 0);
	}

	public void run() {
		while (true) {
			if (heroDead == false) {
				gameOver = hero.charUpdate();
				if (gameOver == true) {
					repaint();
					break;
				}
				zombie1.randomMove();
				heroDead = zombie1.crush(hero);
				if (heroDead)
					System.out.println("while");
				repaint();
			}

			try {
				Thread.sleep(200);
			} catch (Exception e) {
				System.out.println(e);
			}
		}repaint();
	}

	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, this);
		hero.paint(g);
		zombie1.paint(g);
		if (heroDead) {
			g.setColor(Color.RED);
			g.drawString("충돌!!! 게임끝", 150, 100);
			System.out.println("hero");
		}
		if (gameOver) {
			g.setColor(Color.RED);
			g.drawString("목적지에 도착했습니다. 게임오버!!!", 150, 100);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if ((heroDead == false) && (gameOver == false)) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				hero.dir = 2;
				hero.moveLeft();
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				hero.dir = 1;
				hero.moveRight();
			} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				hero.jump = true;
			}
			heroDead = hero.crush(zombie1);
			if (heroDead)
				System.out.println("me");
			repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GamePlay().setTitle("좀비 게임");
	}


}
