package com.cfs.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener {

    static final int WIDTH = 900;
    static final int HEIGHT = 600;

    private final Timer timer = new Timer(20, this);

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
    private int prevX = -1, prevY = -1;
    private ArrayList<int[]> lines = new ArrayList<>();

    static {
        initialX = GUI.WIDTH / 2;
        initialY = GUI.HEIGHT / 2;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        int x1 = (int)(radius1 * Math.sin(angle1) + initialX);
        int y1 = (int)(radius1 * Math.cos(angle1) + initialY);
        int x2 = (int)(radius2 * Math.sin(angle2) + x1);
        int y2 = (int)(radius2 * Math.cos(angle2) + y1);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));

        g2d.setColor(Color.BLACK);
        g2d.drawLine(initialX, initialY, x1 - mass1 / 2, y1 - mass1 / 2);
        g2d.drawLine(x1 - mass1 / 2, y1 - mass1 / 2,
                x2 - mass2 / 2, y2 - mass2 / 2);
        if(prevX != -1 && prevY != -1){
            int[] arr = {x2 - mass1 / 2, y2 - mass2 / 2 , prevX - mass1 / 2 , prevY - mass1 / 2};
            lines.add(arr);
        }
        g2d.setColor(Color.GRAY);
        for(int[] arr : lines){
            g2d.drawLine(arr[0], arr[1], arr[2], arr[3]);
        }

        g2d.setColor(Color.GREEN);
        g2d.fillOval(x1 - mass1, y1 - mass1, mass1, mass1);

        g2d.setColor(Color.RED);
        g2d.fillOval(x2 - mass2, y2 - mass2, mass2, mass2);

        this.prevX = x2;
        this.prevY = y2;

        run();
    }

    private double gravity = 1;
    private int mass1 = 20, mass2 = 20;
    private double angle1 = Math.PI / 2, angle2 = Math.PI / 2;
    private double angularVelocity1 = 0.0, angularVelocity2 = 0.0;
    private int radius1 = 100, radius2 = 100;

    private double calculateAccel1(){
        double num1 = -gravity * (2 * mass1 + mass2) * Math.sin(angle1);
        double num2 = -mass2 * gravity * Math.sin(angle1-2*angle2);
        double num3 = -2*Math.sin(angle1-angle2)*mass2;
        double num4 = angularVelocity2*angularVelocity2*radius2+angularVelocity1*angularVelocity1*radius1*Math.cos(angle1-angle2);
        double den = radius1 * (2*mass1+mass2-mass2*Math.cos(2*angle1-2*angle2));
        return (num1 + num2 + num3 * num4) / den;
    }
    private double calculateAccel2(){
        double num1 = 2 * Math.sin(angle1-angle2);
        double num2 = (angularVelocity1*angularVelocity1*radius1*(mass1+mass2));
        double num3 = gravity * (mass1 + mass2) * Math.cos(angle1);
        double num4 = angularVelocity2*angularVelocity2*radius2*mass2*Math.cos(angle1-angle2);
        double den = radius2 * (2*mass1+mass2-mass2*Math.cos(2*angle1-2*angle2));
        return (num1 * (num2 + num3 + num4)) / den;
    }

    private boolean dampen = false;

    private void run(){
        double angularAccel1 = calculateAccel1();
        double angularAccel2 = calculateAccel2();
        angularVelocity1 += angularAccel1;
        angularVelocity2 += angularAccel2;
        angle1 += angularVelocity1;
        angle2 += angularVelocity2;
        if(dampen) {
            double dampening = 0.999;
            angularVelocity1 *= dampening;
            angularVelocity2 *= dampening;
        }
    }
}
