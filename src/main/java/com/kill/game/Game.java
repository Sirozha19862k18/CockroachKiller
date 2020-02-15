package com.kill.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;



public class Game extends JFrame {

    private  static Game game_window;
    private static Image background;
    private static Image tarakan;
    private static Image gameOver;
    private static Image cursorImage;
    private static Image backgroundEndGame;
    private static Image ammo;
    private static Image killedCounterLogo;
    private static float tarakanLeft=200;
    private static float tarakanTop=-200;
    private static long lastFrameTime;
    private static float tarakanVelocity=200;
    private static int countTarakanKilled=0;
    private static int ammoCounter=10;


    public static void main(String[] args) throws IOException {
        game_window=new Game();
        game_window.initResources(); //подгрузка графических файлов
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(100, 200);
        game_window.setSize(800, 631);
        game_window.setResizable(false);
        game_window.setTitle("Тараканья бойня");
        game_window.setAlwaysOnTop(true);

        lastFrameTime=System.nanoTime();
        final GameField gameField = new GameField();
        game_window.changeCursor(gameField); // смена курсора на прицел

        gameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               int mouseCoordinateX = e.getX();
               int mouseCoordinateY=e.getY();
               if(ammoCounter>0) {
                   ammoCounter--;
                   float tarakanRight = tarakanLeft + tarakan.getWidth(null);
                   float tarakanBottom = tarakanTop + tarakan.getHeight(null);
                   boolean isTarakanKill = mouseCoordinateX >= tarakanLeft && mouseCoordinateX <= tarakanRight && mouseCoordinateY >= tarakanTop && mouseCoordinateY <= tarakanBottom;
                   if (isTarakanKill) {
                       countTarakanKilled++;
                       ammoCounter += 3;
                       tarakanTop = -100;
                       tarakanLeft = (int) (Math.random() * gameField.getWidth() - tarakan.getWidth(null));
                       tarakanVelocity++;
                   }
               }

            }
        });
        game_window.add(gameField);
        game_window.setVisible(true);
    }

    private static void onRepaint(Graphics g){
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime-lastFrameTime)*0.000000001f;
        lastFrameTime=currentTime;
        g.drawImage(background, 0, 0, null);
        g.drawImage(ammo, 570, 0, null);
        if (ammoCounter==0){game_window.changeFontEndAmmo(g);}
        game_window.changeFontScoreCounter(g);  //выбор шрифта
        g.drawImage(killedCounterLogo, 0, 10, null);
        g.drawString(String.valueOf(countTarakanKilled), 50, 50);
        g.drawString(String.valueOf(ammoCounter), 650, 50);
        tarakanTop= tarakanTop+tarakanVelocity*deltaTime;
        tarakanLeft= (float) (tarakanLeft+(-260+Math.random()*520)*deltaTime);
        g.drawImage(tarakan, (int)tarakanLeft, (int)tarakanTop, null);
        if(tarakanTop>game_window.getHeight()){
            g.drawImage(backgroundEndGame, 0, 0, null);
            g.drawImage(gameOver, 230, 150, null);
            game_window.changeFontEndGame(g);
            g.drawString("Жэстачайшэ замочана: "+String.valueOf(countTarakanKilled)+ " усач", 100, 500);
        }
        if(tarakanLeft>780){
            tarakanTop=-200;
            tarakanLeft=-10;
        }
        if(tarakanLeft<-30){
            tarakanTop=-200;
            tarakanLeft=600;
        }

    }

    private static class GameField extends JPanel{
        protected void paintComponent (Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }

    }
    void changeCursor(GameField gameField){
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "blank cursor");
        gameField.setCursor(blankCursor);
    }
    void changeFontScoreCounter(Graphics g){
        Font myFont = new Font ("Courier New", 1, 55);
        g.setColor(Color.blue);
        g.setFont(myFont);
    }
    void changeFontEndGame(Graphics g){
        Font myFont = new Font ("Courier New", 1, 30);
        g.setColor(Color.black);
        g.setFont(myFont);
    }
    void changeFontEndAmmo(Graphics g){
        Font myFont = new Font ("Courier New", 1, 60);
        g.setColor(Color.red);
        g.setFont(myFont);
        g.drawString("Кончились патроны!!!", 60, 590);
    }

    void initResources() throws IOException {
        background = ImageIO.read(Game.class.getResourceAsStream("/fon.png"));
        tarakan = ImageIO.read(Game.class.getResourceAsStream("/tarakan.png"));
        gameOver = ImageIO.read(Game.class.getResourceAsStream("/gameover.png"));
        backgroundEndGame = ImageIO.read(Game.class.getResourceAsStream("/fonEnd.png"));
        cursorImage = ImageIO.read(Game.class.getResourceAsStream("/ceil.png"));
        ammo = ImageIO.read(Game.class.getResourceAsStream("/ammo.png"));
        killedCounterLogo = ImageIO.read(Game.class.getResourceAsStream("/sword.png"));
    }




}
