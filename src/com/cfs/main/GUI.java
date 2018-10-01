package com.cfs.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    private final Timer timer = new Timer(80, this);

    void createAndShowGUI(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);

        Panel panel = new Panel(WIDTH / 2, HEIGHT / 2);
        this.add(panel);

        panel.getGraphics().setColor(Color.green);

        timer.start();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.revalidate();
        this.repaint();
    }
}

class Panel extends JPanel {

    private static int x, y;

    private static int initialX, initialY;

    static {
        initialX = WIDTH / 2;
        initialY = HEIGHT / 20; 
    }

    Panel(int x, int y){
        Panel.x = x;
        Panel.y = y;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawOval(x, y, mass1, mass1);

    }

    private double gravity = 9.8;
    private int mass1 = 20, mass2 = 20;
    private double angle1 = Math.PI / 3, angle2 = Math.PI / 3;
    private double angularVelocity1 = 0.0, angularVelocity2 = 0.0;
    private int radius1 = 20, radius2 = 20;

    private double calculateAccel1(){
        double num1 = -gravity * (2 * mass1 + mass2) * Math.sin(angle1);
        double num2 = -mass2 * gravity * Math.sin(angle1 - 2 * angle2);
        double num3 =  -2 * Math.sin(angle1 - angle2) * mass2 *
                    (angularVelocity2 * angularVelocity2 * radius2 +
                    angularVelocity1 * angularVelocity1 * radius1 * Math.cos(angle1 - angle2));
        double denominator = radius1 * (2 * mass1 + mass2 - mass2 * Math.cos(2 * angle1 - 2 * angle2));
        return (num1 + num2 + num3) / denominator;
    }
    private double calculateAccel2(){
        double num1 = 2 * Math.sin(angle1 - angle2);
        double num2 = angle1 * angle1 * radius1 * (mass1 + mass2);
        double num3 = gravity * (mass1 + mass2) * Math.cos(angle1);
        double num4 = angle2 * angle2 * radius2 * mass2 * Math.cos(angle1 - angle2);
        double denominator = radius2 * (2 * mass1 + mass2 - mass2 * Math.cos(2 * angle1 - 2 * angle2));
        return (num1 * (num2 + num3 + num4)) / denominator;
    }
}
