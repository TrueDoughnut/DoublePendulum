package com.cfs.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    static final int WIDTH = 1600;
    static final int HEIGHT = 800;

    private final Timer timer = new Timer(80, this);

    void createAndShowGUI(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);

        Panel panel = new Panel();
        this.add(panel);

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

    private static int initialX, initialY;

    static {
        initialX = GUI.WIDTH / 2;
        initialY = GUI.HEIGHT / 2;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        int x1 = (int)(radius1 * Math.cos(angle1) + initialX);
        int y1 = (int)(radius1 * Math.sin(angle1) + initialY);
        int x2 = (int)(radius2 * Math.cos(angle2) + x1);
        int y2 = (int)(radius2 * Math.sin(angle2) + y1);

        g.setColor(Color.BLACK);
        g.drawLine(initialX, initialY, x1 - mass1 / 2, y1 - mass1 / 2);
        g.drawLine(x1 - mass1 / 2, y1 - mass1 / 2,
                x2 - mass2 / 2, y2 - mass2 / 2);

        g.setColor(Color.GREEN);
        g.fillOval(x1 - mass1, y1 - mass1, mass1, mass1);

        g.setColor(Color.RED);
        g.fillOval(x2 - mass2, y2 - mass2, mass2, mass2);

        run();
    }

    private double gravity = 1;
    private int mass1 = 20, mass2 = 20;
    private double angle1 = Math.PI / 2, angle2 = Math.PI / 2;
    private double angularVelocity1 = 0.0, angularVelocity2 = 0.0;
    private int radius1 = 100, radius2 = 200;

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
        double num4 = angularVelocity2 * angularVelocity2 * radius2 * mass2 * Math.cos(angle1 - angle2);
        double denominator = radius2 * (2 * mass1 + mass2 - mass2 * Math.cos(2 * angle1 - 2 * angle2));
        return (num1 * (num2 + num3 + num4)) / denominator;
    }

    private void run(){
        double angularAccel1 = calculateAccel1();
        double angularAccel2 = calculateAccel2();
        angle1 += angularVelocity1;
        angle2 += angularVelocity2;
        angularVelocity1 += angularAccel1;
        angularVelocity2 += angularAccel2;
        //angularVelocity1 *= 0.999;
        //angularVelocity2 *= 0.999;
    }
}
