package GameMaking_1;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class EscapeRun extends JFrame {

	private Image screenImage;
	private Graphics screenGraphic;

	// 위에 바 나가기 버튼 이미지
	private ImageIcon Exit_entered_button = new ImageIcon(Main.class.getResource("../images/exit_entered_button.png"));
	private ImageIcon Exit_button = new ImageIcon(Main.class.getResource("../images/exit_button(Title).png"));
	private ImageIcon backButtonImage = new ImageIcon(Main.class.getResource("../images/back_button.png"));
	private ImageIcon backButtonImage2 = new ImageIcon(Main.class.getResource("../images/back_enter_button.png"));
	// 게임시작&&나가기버튼 이미지(사진 수정하기)
	private ImageIcon startButtonImage = new ImageIcon(Main.class.getResource("../images/start_button.png"));
	private ImageIcon startButtonImage2 = new ImageIcon(Main.class.getResource("../images/start_enter_button.png"));
	private ImageIcon quitButtonImage = new ImageIcon(Main.class.getResource("../images/quit_button.png"));
	private ImageIcon quitButtonImage2 = new ImageIcon(Main.class.getResource("../images/quit_enter_button.png"));
	// 인트로 배경화면 이미지
	private Image background = new ImageIcon(Main.class.getResource("../images/intro_background(Title)2.jpg"))
			.getImage();
	// 게임 배경화면 이미지
	//private Image play_background = new ImageIcon(Main.class.getResource("../images/intro_background.jpg")).getImage();

	// 위에 바&&버튼
	private JLabel intro_bar = new JLabel(new ImageIcon(Main.class.getResource("../images/intro_bar.jpg")));
	private JButton exitButton = new JButton(Exit_button);
	private JButton backButton = new JButton(backButtonImage);
	private JButton startButton = new JButton(startButtonImage);
	private JButton quitButton = new JButton(quitButtonImage);

	private int mouseX, mouseY;
	
	//*게임 소스*********************************
	
	
	//주인공 이미지
	ImageIcon charImgIcon[] = new ImageIcon[6];
	Image charImg[] = new Image[6];
	
	Char ch1;
	boolean ch1_Dead;
	
	//장애물 이미지
	ImageIcon obImgIcon[] = new ImageIcon[2];
	Image obImg[] = new Image[2];
	
	Obstacles ob1;
	
	public EscapeRun() {
		setUndecorated(true);
		setTitle("ESCAPE_RUN");
		setSize(Main.Screen_Width, Main.Screen_Height);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);
		
//		backButton.setBounds(1250,0,25,25);
//		backButton.setBorderPainted(false);
//		backButton.setContentAreaFilled(false);
//		backButton.setFocusPainted(false);
//		// 마우스 이벤트
//		backButton.addMouseListener(new MouseAdapter() {
//			public void mouseEntered(MouseEvent e) {
//				backButton.setIcon(backButtonImage2);
//				backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
//			}
//
//			public void mouseExited(MouseEvent e) {
//				backButton.setIcon(backButtonImage);
//				backButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//			}
//
//			public void mousePressed(MouseEvent e) {
//				System.(0);
//			}
//		});

		exitButton.setBounds(1250, 4, 25, 25);
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		// 마우스 이벤트
		exitButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(Exit_entered_button);
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(Exit_button);
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		add(exitButton);
	
		startButton.setBounds(485, 330, 300, 200);
		startButton.setBorderPainted(false);
		startButton.setContentAreaFilled(false);
		startButton.setFocusPainted(false);
		// 마우스 이벤트
		startButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				startButton.setIcon(startButtonImage2);
				startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				startButton.setIcon(startButtonImage);
				startButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void mousePressed(MouseEvent e) {
				// Music buttonEnteredMusic = new Music("url쓰기",false);
				// buttonEnteredMusi.start();
				// 게임 시작 이벤트
				startButton.setVisible(false);
				quitButton.setVisible(false);
				background = new ImageIcon(Main.class.getResource("../images/main_background.png")).getImage();
				//게임 플레이
//				game_Frame fms = new game_Frame();
				new game_Frame();
				
			}
		});
		add(startButton);

		quitButton.setBounds(485, 420, 300, 200);
		quitButton.setBorderPainted(false);
		quitButton.setContentAreaFilled(false);
		quitButton.setFocusPainted(false);
		// 마우스 이벤트
		quitButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				quitButton.setIcon(quitButtonImage2);
				quitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				quitButton.setIcon(quitButtonImage);
				quitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		add(quitButton);

		intro_bar.setBounds(0, 0, 1280, 30);// 크기 조정
		intro_bar.addMouseListener(new MouseAdapter() {
			// 오버라이드
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		intro_bar.addMouseMotionListener(new MouseMotionAdapter() {
			// 오버라이드
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY);
			}
		});
		add(intro_bar);

		Music intro_music = new Music("intro_music.mp3", true);
		intro_music.start();
		
		for(int i = 0; i < 6; i++) {
			charImgIcon[i] = new ImageIcon("hero0");
		}
	}

	public void paint(Graphics g) {
		screenImage = createImage(Main.Screen_Width, Main.Screen_Height);
		screenGraphic = screenImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
	}

	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintComponents(g);
		this.repaint();
	}

}
