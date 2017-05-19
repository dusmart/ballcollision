import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;

/**
 * Created by dusmart on 17-5-3.
 */


public class CollisionSystem
{
    private MinPQ<Event> pq; // the priority queue
    private double t = 0.0; // simulation clock time
    private Particle[] particles; // the array of particles
    public CollisionSystem(Particle[] particles) {
        this.particles = particles;
    }
    private void predict(Particle a) {
        if (a == null) return;
        double dt;
        for (int i = 0; i < particles.length; i++)
        {
            dt = a.timeToHit(particles[i]);
            if(dt!=Double.POSITIVE_INFINITY)
                pq.insert(new Event(t + dt, a, particles[i]));
        }
        dt = a.timeToHitVerticalWall();
        if(dt!=Double.POSITIVE_INFINITY)
            pq.insert(new Event(t + dt , a, null));
        dt = a.timeToHitHorizontalWall();
        if(dt!=Double.POSITIVE_INFINITY)
            pq.insert(new Event(t + dt, null, a));
    }
    private void redraw() {
        for (Particle p : particles)
            p.draw();
    }
    public void simulate() {
        StdDraw.enableDoubleBuffering();
        double timepease = 0.02;
        pq = new MinPQ<Event>();
        for(int i = 0; i < particles.length; i++) predict(particles[i]);
        pq.insert(new Event(0, null, null));
        while(!pq.isEmpty())
        {
            Event event = pq.delMin();
            if(!event.isValid()) continue;
            Particle a = event.a;
            Particle b = event.b;
            while(event.time - t > timepease) {
                StdDraw.clear();
                for(int i = 0; i < particles.length; i++)
                    particles[i].move(timepease);
                t += timepease;
                StdDraw.show();
            }
            StdDraw.clear();
            for(int i = 0; i < particles.length; i++)
                particles[i].move(event.time - t);
            StdDraw.show();
            t = event.time;
            if (a != null && b != null) a.bounceOff(b);
            else if (a != null && b == null) a.bounceOffVerticalWall();
            else if (a == null && b != null) b.bounceOffHorizontalWall();
            else if (a == null && b == null) redraw();
            predict(a);
            predict(b);
        }
    }







    private class Event implements Comparable<Event> {
        private double time; // time of event
        private Particle a, b; // particles involved in event
        private int countA, countB; // collision counts for a and b
        public Event(double t, Particle a, Particle b) {
            time = t;
            this.a = a;
            this.b = b;
            this.countA = a==null?0:a.getCount();
            this.countB = b==null?0:b.getCount();
        }
        public int compareTo(Event that)
        { return Double.compare(this.time,that.time); }
        public boolean isValid() {

            return (a==null||countA == a.getCount()) && (b==null||countB == b.getCount()) && time!=Double.POSITIVE_INFINITY;
        }
    }

    public static void main(String[] args) {
        Particle[] particles = new Particle[81];
        Color[] colors = new Color[]{
                new Color(0x56, 0x77, 0xfc),
                new Color(0x00, 0x96, 0x88),
                new Color(0xcd, 0xdc, 0x39),
                new Color(0xff, 0x98, 0x00),
                new Color(0x9e, 0x9e, 0x9e)
        };


        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                double vx = StdRandom.uniform(-0.1,0.1);
                double vy = StdRandom.uniform(-0.1,0.1);
                double r = StdRandom.uniform(0.01,0.03);
                double mass = r*r;
                Color color = colors[StdRandom.uniform(0,colors.length)];
                particles[(i-1)*9+j-1] = new Particle(i*0.1,j*0.1, vx, vy, r, mass, color);
            }
        }
        CollisionSystem test = new CollisionSystem(particles);
        test.simulate();
    }
}