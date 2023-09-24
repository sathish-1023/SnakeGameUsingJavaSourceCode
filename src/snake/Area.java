/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snake;

/**
 *
 * @author NDIAPPINK
 */
import cls.ClassDB;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;
//import sun.awt.im.InputMethodJFrame;
public class Area extends JPanel implements ActionListener{
InputStream in;
JFrame ex;
    private final int Height= 400;
    private final int Width = 400;
    private final int width_ball = 10;//urkarnabola
    private final int ALL_DOTS = 1000;
    private final int RAND_POS = 30;
    private static int DELAY = 300;
private int score_new,score_db,highscore = 0;
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private int dots;
    private int Random_x;
    private int Random_y;
    private boolean move_left = false;
    private boolean move_right = true;
    private boolean move_up = false;
    private boolean move_down = false;
    private boolean inGame = true;
    String Name="";
    String query="";
    private Timer timer;
    private Image ball;
    private Image food;
    private Image front_head;
    private int key;
   
    public Area() {
        
        name();
        addKeyListener(new dirAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(Width, Height));//Tingi-height,lebar-width
        loadImages();
        initGame();
    }

    private void name(){
        Name = JOptionPane.showInputDialog(this, "Please Input Your Name:");
        if (Name == null) {
        System.exit(0);
        }
        else{
            if(Name.equals("")){
        JOptionPane.showMessageDialog(this,"Name Already Exist!");
        name();
            }
            else{
                try{
              Connection c=ClassDB.dbConnect();
        Statement st=(Statement)c.createStatement();
        
             query="Select * from score where nama = '" + Name.toString()+"'";
            ResultSet r=st.executeQuery(query);
            if (r.next()){      
                    return;     
            }
            else{
                 try {           
            st.executeUpdate("Insert into score(nama) values('" + Name.toString() + "')");         
                 }   
                 catch(Exception e){
            System.out.println(e);
        }   
            }
        
         }catch(Exception e){
             System.out.println(e);
         }       
       }
         
    }
    
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/dot.png");
        ball = iid.getImage();

        ImageIcon iif= new ImageIcon("src/food.png");
        food= iif.getImage();

        ImageIcon iir = new ImageIcon("src/right.png");
        front_head = iir.getImage();
    }

    private void initGame() {

        dots = 5;

        for (int z = 0; z < dots; z++) {
            x[z] = Width/20;
            y[z] = Height/2;
        }

        RandomPosition();

        timer = new Timer(DELAY, this);
        timer.start();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    private void updatescore(){
         try {            
            Connection c=ClassDB.dbConnect();
           Statement s=(Statement)c.createStatement();
        query="Select * from score where nama = '" + Name.toString() +"'";
            ResultSet r=s.executeQuery(query);
            if (r.next()){
             score_db = Integer.parseInt(r.getString("score"));
             if (score_new <= score_db){
                  return;  
             }
             else{
                  query = "UPDATE score Set score ='" + score_new +"' where nama = '" + Name.toString()+ "'";     
            s.executeUpdate(query); 
             }
                      
            }            
        }catch(Exception e) {
            System.out.println(e);
        }
    }
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(food, Random_x, Random_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(front_head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
             String msg = "Score = "+score_new;
             
        Font small = new Font("Helvetica", Font.BOLD, 10);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, 5, Height - (Height-10));
        
        try {            
            
            Connection c=ClassDB.dbConnect();
            Statement s= c.createStatement();
            query="Select * from score where score = (select max(score) from score)";
            ResultSet r=s.executeQuery(query);
            if (r.next()){
                highscore = Integer.parseInt(r.getString("score"));
                String scr = "Score width "+r.getString("nama")+" = "+highscore;
     
               g.drawString(scr, (Width - metr.stringWidth(scr)) -10, Height -5);
            }
            else{
                String scr = "Score width = 0";
     
               g.drawString(scr, (Width - metr.stringWidth(scr)) -10, Height -5);
            }
            
            r.close();
            s.close();
           
        }catch(Exception e) {
            System.out.println(e);
        }

        } else {
            gameOver(g);         
        }        
    }
    private void gameOver(Graphics g) {
        updatescore();
        if (score_new <= highscore){
            String msg = "Your Score: = "+ score_new;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (Width - metr.stringWidth(msg)) / 2, Height / 2);
       
        }
        else{
            String msgg = "Congratulation High Score = "+ score_new;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.blue);
        g.setFont(small);
        g.drawString(msgg, (Width- metr.stringWidth(msgg)) / 2, Height/ 2);
       
        }
       
           
    }
    
       
    private void setPositionFood() {//cekM

        if ((x[0] == Random_x) && (y[0] == Random_y)) {

            dots++;
            score_new= score_new+ 3;
            try        
    {            
    //in = new FileInputStream(new File("src\\slurp.wav"));            
    //AudioStream audios = new AudioStream(in);
    //AudioPlayer.player.start(audios);  
      SimpleAudioPlayer audioplayer=new SimpleAudioPlayer("C:\\Users\\alich\\Downloads\\mixkit-liquid-bubble-3000.wav");
       //SimpleAudioPlayer audioplayer=new SimpleAudioPlayer("src\\.wav");
      audioplayer.play();
    // Thread.sleep(100);
      //audioplayer.gotoChoice(4);
    }        
    catch(Exception e)                
    {                    
        System.out.println(e);          
    }
            RandomPosition();
        }
        DELAY/=2;
    }

    private void sizeSnake() {//pindah

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (move_left) {
            x[0] -= width_ball;
        }

        if (move_right) {
            x[0] += width_ball;
        }

        if (move_up) {
            y[0] -= width_ball;
        }

        if (move_down) {
            y[0] += width_ball;
        }
    }

    private void Exceptions() {//cekT

        for (int z = dots; z > 0; z--) {

            if (((z > 5) && (x[0] == x[z]) && (y[0] == y[z])) ||
                    y[0] >= Height || x[0] >= Width || x[0]<0 || y[0] <0) {
                inGame = false;
                try        
    {            
    //in = new FileInputStream(new File("src\\beep.wav"));            
   // AudioStream audios = new AudioStream(in);            
   // AudioPlayer.player.start(audios); 
      SimpleAudioPlayer audioplayer=new SimpleAudioPlayer("src\\beep.wav");
      audioplayer.play();
    }        
    catch(Exception e)                
    {                    
                
    }
            
            }

        }
        
        if(!inGame) {
            timer.stop();
        }
    }

    private void RandomPosition() {

        int r = (int) (Math.random() * RAND_POS);
        Random_x = ((r * width_ball));

        r = (int) (Math.random() * RAND_POS);
        Random_y = ((r * width_ball));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            setPositionFood();//cekM
            sizeSnake();
            Exceptions();
        }

        repaint();
    }

    private class dirAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!move_right)) {
                move_left = true;
                move_up= false;
                move_down = false;
                 ImageIcon left = new ImageIcon("src/left.png");
        front_head= left.getImage();
            }

            if ((key == KeyEvent.VK_RIGHT) && (!move_left)) {
                move_right = true;
                move_up = false;
                move_down = false;
                 ImageIcon right = new ImageIcon("src/right.png");
        front_head = right.getImage();
            }

            if ((key == KeyEvent.VK_UP) && (!move_down)) {
                move_up = true;
                move_right = false;
                move_left = false;
               ImageIcon up = new ImageIcon("src/up.png");
        front_head = up.getImage();
            }

            if ((key == KeyEvent.VK_DOWN) && (!move_up)) {
                move_down= true;
                move_left = false;
                move_right = false;
                 ImageIcon down = new ImageIcon("src/down.png");
        front_head = down.getImage();
            }
            if ((key == KeyEvent.VK_P) ) {
               
               if(timer.isRunning()){
                   timer.stop();                 
               }  
               else{
                   timer.start();     
               }    
            }    
        }
    }
}
