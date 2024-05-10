

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;




public class Game extends JFrame implements KeyListener,MouseListener{
	Board board = new Board();
	ArrayList<Enemy> enemyList = new ArrayList<>();
	ArrayList<Friend> friendList = new ArrayList<>();
	ArrayList<String> npcPosition = new ArrayList<>();//10+""+200
	ArrayList<Projectile> projectiles = new ArrayList<>();
	ArrayList<Integer> coordinates = new ArrayList<>();
	int tempX;
	int tempY;
	Random r = new Random();
	public static boolean isAlive;
	TempThread t;

	public Game() {
		setSize(525, 550);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(board);
		addKeyListener(this);
		addMouseListener(this);
		for(int i=0;i<500;i += 10) {
			coordinates.add(i);
		}
		isAlive = true;
		
	}
	
	class Enemy extends Thread{//rengi siyah
		int x;
		int y;
		Color color;
		String strColor;
		boolean isAlive;
		static boolean enemyExists;//pop-up condition için
		
		public Enemy() {
			strColor = "black";
			color = Color.black;
			Randomizer();
			x = tempX;
			y = tempY;
			enemyList.add(this);
			isAlive = true;
			enemyExists = true;
			ThreatCheck t = new ThreatCheck();
		}
		public void isInFriend() {
			for(int i=0;i<friendList.size();i++) {
				if(friendList.get(i).x == this.x && friendList.get(i).y == this.y) {
					enemyList.remove(this);
					friendList.remove(i);
					board.repaint();
				}
				else if(this.x == AirCraft.x && this.y == AirCraft.y) {
					enemyList.remove(this);
					Game.isAlive = false;
					board.repaint();
				}
			}
			
		}
		public String getRandomDirection() {
			Random r = new Random();
			int d = r.nextInt(1,5);
			if(d == 1) return "up";
			else if(d == 2) return "left";
			else if(d == 3) return "down";
			else return "right";
		}
		
		public void moveTo(String direction) {
			int min_x = 0;
			int min_y = 0;
			int max_x = 500;
			int max_y = 500;
			
			switch(direction) {
			case "up":
				y -= 10;
				break;
			case "left":
				x -= 10;
				break;
			case "down":
				y += 10;
				break;
			case "right":
				x += 10;
				break;
			}
			
			if(x > max_x)
				x = 500;
			else if(x < min_x)
				x = 0;
			else if(y > max_y)
				y = 500;
			else if(y < min_x)
				y = 0;
			
		}
		
		@Override
		public void run() {
			while(this.isAlive) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				moveTo(getRandomDirection());
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				moveTo(getRandomDirection());
				Projectile right = new Projectile(x+10,y,"blue","right");
				Projectile left = new Projectile(x-10,y,"blue","left");
				right.setColor(Color.blue);
				left.setColor(Color.blue);
				
	
				projectiles.add(right);
				projectiles.add(left);
				right.start();
				left.start();
				
				isAlive = false;
				for(int i=0;i<enemyList.size();i++) {
					if(enemyList.get(i).x == this.x && enemyList.get(i).y == this.y) 
						this.isAlive = true;
				}
			}
			
			
			
			board.repaint();
	
		}
		
		class ThreatCheck extends Thread{
			public ThreatCheck() {
				this.start();
			}
			@Override
			public void run() {
				while(isAlive) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					isInFriend();
					
				}
			}
		}

	}
	
	class Friend extends Thread{//rengi yeşil
		int x;
		int y;
		Color color;
		String strColor;
		boolean isAlive;
		
		public Friend() {
			color = Color.green;
			strColor = "green";
			Randomizer();
			x = tempX;
			y = tempY;
			friendList.add(this);
			isAlive = true;
		}
		
		public String getRandomDirection() {
			Random r = new Random();
			int d = r.nextInt(1,5);
			if(d == 1) return "up";
			else if(d == 2) return "left";
			else if(d == 3) return "down";
			else return "right";
		}
		
		public void moveTo(String direction) {
			int min_x = 0;
			int min_y = 0;
			int max_x = 500;
			int max_y = 500;
			int cout = 0;
			
			switch(direction) {
			case "up":
				if(this.x == AirCraft.x && this.y-10 == AirCraft.y)
					cout += 1;
				if(cout == 0)
					y -= 10;
				break;
			case "left":
				if(this.x-10 == AirCraft.x && this.y == AirCraft.y)
					cout += 1;
				if(cout == 0)
					x -= 10;
				break;
			case "down":
				if(this.x == AirCraft.x && this.y+10 == AirCraft.y)
					cout += 1;
				if(cout == 0)
					y += 10;
				break;
			case "right":
				if(this.x+10 == AirCraft.x && this.y == AirCraft.y)
					cout += 1;
				if(cout == 0)
					x += 10;
				break;
			}
			
			if(x > max_x)
				x = 500;
			else if(x < min_x)
				x = 0;
			else if(y > max_y)
				y = 500;
			else if(y < min_x)
				y = 0;
		}
		
		@Override
		public void run() {
			while(isAlive) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				moveTo(getRandomDirection());
				board.repaint();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				moveTo(getRandomDirection());
				board.repaint();
				Projectile right = new Projectile(x+10,y,"magenta","right");
				Projectile left = new Projectile(x-10,y,"magenta","left");
				right.setColor(Color.magenta);
				left.setColor(Color.magenta);
				
	
				projectiles.add(right);
				projectiles.add(left);
				right.start();
				left.start();
				
				isAlive = false;
				for(int i=0;i<friendList.size();i++) {
					if(friendList.get(i).x == this.x && friendList.get(i).y == this.y) 
						this.isAlive = true;
				}
			}
			
			
			board.repaint();
			
			
		}
	}
	
	class AirCraft extends Thread{
		static int x;
		static int y;
		static boolean isFinished;
		public AirCraft() {
			x = 250;
			y = 250;
			TempThread t = new TempThread();
			t.start();
			isFinished = false;
		}
	}
	
	class Board extends JPanel{
		public Board() {
			setSize(500, 500);
			setVisible(true);
			setOpaque(true);
		}
		
		public void paint(Graphics g) {
			super.paint(g);
			
			if(Game.isAlive) {
				g.setColor(Color.red);
				g.fillRect(AirCraft.x, AirCraft.y, 10, 10);
			}
			
			
			for(int i=0;i<projectiles.size();i++) {
				if(projectiles.get(i) != null) {
					g.setColor(projectiles.get(i).getColor());
					g.fillRect(projectiles.get(i).x, projectiles.get(i).y, 5, 5);
				}
			}
			
			
			for(int i=0;i<friendList.size();i++) {
				g.setColor(friendList.get(i).color);
				g.fillRect(friendList.get(i).x, friendList.get(i).y, 10, 10);
			}
			
			
			for(int i=0;i<enemyList.size();i++) {
				g.setColor(enemyList.get(i).color);
				g.fillRect(enemyList.get(i).x, enemyList.get(i).y, 10, 10);
			}
			
		}

	}
	
	class Projectile extends Thread{
		int x;
		int y;
		Color color;
		String strColor;
		String direction;
		
		public Projectile(int x, int y,String strColor, String direction) {
			this.x = x;
			this.y = y;
			this.strColor = strColor;
			this.direction = direction;
		}
		
		@Override
		public void run() {
			while(x<=520 && x>=0) {
				if(direction.equals("right")) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.x += 10;
				}
				else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.x -= 10;
				}
				
				if(x>=500 || x<=0)
					projectiles.remove(this);
				
				if(strColor.equals("blue")) {
					if(x == AirCraft.x && y == AirCraft.y) {
						Game.isAlive = false;
						projectiles.remove(this);
					}
					else if(x == AirCraft.x+5 && y == AirCraft.y) {
						Game.isAlive = false;
						projectiles.remove(this);
					}
					else if(x == AirCraft.x && y == AirCraft.y+5) {
						Game.isAlive = false;
						projectiles.remove(this);
					}
					else if(x == AirCraft.x+5 && y == AirCraft.y+5) {
						Game.isAlive = false;
						projectiles.remove(this);
					}

					
					
					for(int i=0;i<friendList.size();i++) {
						if(x == friendList.get(i).x && y == friendList.get(i).y) {
							projectiles.remove(this);
							friendList.remove(i);
						}
						else if(x == friendList.get(i).x+5 && y == friendList.get(i).y) {
							projectiles.remove(this);
							friendList.remove(i);
						}
						else if(x == friendList.get(i).x && y == friendList.get(i).y+5) {
							projectiles.remove(this);
							friendList.remove(i);
						}
						else if(x == friendList.get(i).x+5 && y == friendList.get(i).y+5) {
							projectiles.remove(this);
							friendList.remove(i);
						}
					}
					board.repaint();
				}
				
				else if(strColor.equals("magenta") || strColor.equals("orange")){
					for(int i=0;i<enemyList.size();i++) {
						if(x == enemyList.get(i).x && y == enemyList.get(i).y) {
							projectiles.remove(this);
							enemyList.remove(i);
						}
						else if(x == enemyList.get(i).x+5 && y == enemyList.get(i).y) {
							projectiles.remove(this);
							enemyList.remove(i);
						}
						else if(x == enemyList.get(i).x && y == enemyList.get(i).y+5) {
							projectiles.remove(this);
							enemyList.remove(i);
						}
						else if(x == enemyList.get(i).x+5 && y == enemyList.get(i).y+5) {
							projectiles.remove(this);
							enemyList.remove(i);
						}
					}
				}
				board.repaint();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			board.repaint();
			
			
		}
		public void setColor(Color c) {
			color = c;
		}
		
		public Color getColor() {
			return color;
		}
		
	
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int cout = 0;
		if(e.getKeyCode() == KeyEvent.VK_W) {
			for(int i=0;i<friendList.size();i++) {
				if(AirCraft.y-10 == friendList.get(i).y && AirCraft.x == friendList.get(i).x)
					cout += 1;
			}
			if(cout == 0) {
				AirCraft.y -= 10;
				board.repaint();
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_A) {
			for(int i=0;i<friendList.size();i++) {
				if(AirCraft.y == friendList.get(i).y && AirCraft.x-10 == friendList.get(i).x)
					cout += 1;
			}
			if(cout == 0) {
				AirCraft.x -= 10;
				board.repaint();
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_S) {
			for(int i=0;i<friendList.size();i++) {
				if(AirCraft.y+10 == friendList.get(i).y && AirCraft.x == friendList.get(i).x)
					cout += 1;
			}
			if(cout == 0) {
				AirCraft.y += 10;
				board.repaint();
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_D) {
			for(int i=0;i<friendList.size();i++) {
				if(AirCraft.y == friendList.get(i).y && AirCraft.x+10 == friendList.get(i).x)
					cout += 1;
			}
			if(cout == 0) {
				AirCraft.x += 10;
				board.repaint();
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(Game.isAlive) {
			Projectile right = new Projectile(AirCraft.x+10,AirCraft.y,"orange","right");
			Projectile left = new Projectile(AirCraft.x-5,AirCraft.y,"orange","left");
			right.setColor(Color.orange);
			left.setColor(Color.orange);
			right.start();
			left.start();
			
			projectiles.add(left);
			projectiles.add(right);
			
			
			board.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	void Randomizer() {
		tempX = coordinates.get(r.nextInt(0,50));
		tempY = coordinates.get(r.nextInt(0,50));
		while(npcPosition.size() > 0 && npcPosition.contains(tempX+""+tempY)) {
			tempX = coordinates.get(r.nextInt(0,50));
			tempY = coordinates.get(r.nextInt(0,50));
		}
	}
	
	
	//-------------------------------------------------------------------------- POP-UP EKRANI --------------------------------------------------
	
	public class ExitWindow extends JFrame{
		static int counter = 0;
		public ExitWindow() {
			setSize(300, 300);
			setResizable(false);
			setLayout(new GridLayout(2,1));
			if(Game.isAlive == true && enemyList.size() == 0) {
				JLabel label = new JLabel("Oyunu kazandınız");
				add(label);
			}
			else if(Game.isAlive == false){
				JLabel label = new JLabel("Oyunu Kaybettiniz");
				add(label);
			}
			
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
			counter = 1;
		}
	}
	
	class TempThread extends Thread{

		@Override
		public void run() {
			while(true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				boolean alive = Game.isAlive;
				if((alive == true && enemyList.size() == 0) || (alive == false)) {
					if(ExitWindow.counter == 0)
						new ExitWindow();
				}
			}
			
		}
	}


}

