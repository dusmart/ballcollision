import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.security.spec.ECField;

import static java.lang.Thread.sleep;

/**
 * Created by dusmart on 17-5-3.
 */
public class Particle
{
    private double rx, ry; // position
    private double vx, vy; // velocity
    private final double radius; // radius
    private final double mass; // mass
    private static final double INFINITY = Double.POSITIVE_INFINITY;
    private int count; // number of collisions
    private Color color;
    public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color) {
        this.rx = rx;
        this.ry = ry;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.mass = mass;
        this.color = color;
    }
    public void move(double dt)  {
        //StdDraw.setPenColor(new Color(255,255,255));
        //StdDraw.filledCircle(rx, ry, radius*1.1);
        rx += vx * dt;
        ry += vy * dt;
        //StdDraw.setPenColor(new Color(0,0,0));
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(rx, ry, radius);
        try{
        sleep((long) dt);}catch (Exception e){}
        assert rx+radius<=1.0 && rx-radius>=0;
        assert ry+radius<=1.0 && ry-radius>=0;
    }
    public void draw() {
        StdDraw.filledCircle(rx, ry, radius);
    }
    public double timeToHit(Particle that) {
        if (this == that) return INFINITY;
        double dx = that.rx - this.rx, dy = that.ry - this.ry;
        double dvx = that.vx - this.vx, dvy = that.vy - this.vy;
        double dvdr = dx*dvx + dy*dvy;
        if( dvdr > 0) return INFINITY;
        double dvdv = dvx*dvx + dvy*dvy;
        double drdr = dx*dx + dy*dy;
        double sigma = this.radius + that.radius;
        double d = (dvdr*dvdr) - dvdv * (drdr - sigma*sigma);
        if (d < 0) return INFINITY;
        return -(dvdr + Math.sqrt(d)) / dvdv;
    }
    public double timeToHitHorizontalWall() {
        assert ry+radius<=1.0 && ry-radius>=0;
        if (vy > 0) return (1.0-ry-radius) / vy;
        else if(vy < 0) return (ry-radius) / -vy;
        else return INFINITY;
    }
    public double timeToHitVerticalWall() {
        assert rx+radius<=1.0 && rx-radius>=0;
        if (vx > 0) return (1.0-rx-radius) / vx;
        else if(vx<0) return (rx-radius) / -vx;
        else return INFINITY;
    }
    public void bounceOff(Particle that) {
        double dx = that.rx - this.rx, dy = that.ry - this.ry;
        double dvx = that.vx - this.vx, dvy = that.vy - this.vy;
        double dvdr = dx*dvx + dy*dvy;
        double dist = this.radius + that.radius;
        double J = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);
        double Jx = J * dx / dist;
        double Jy = J * dy / dist;
        this.vx += Jx / this.mass;
        this.vy += Jy / this.mass;
        that.vx -= Jx / that.mass;
        that.vy -= Jy / that.mass;
        this.count++;
        that.count++;
    }
    public void bounceOffHorizontalWall() {
        vy = -vy;
        this.count++;
    }
    public void bounceOffVerticalWall() {
        vx = -vx;
        this.count++;
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) {
        System.out.println("test");
    }
}