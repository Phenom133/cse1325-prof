package shapes;

import java.awt.Graphics;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Plot extends Shape {
    public Plot(FunctionToPlot f, int xOrigin, int yOrigin, double xFirst, double xLast, int count, double xScale, double yScale) {
        this(x -> x, f, xOrigin, yOrigin, xFirst, xLast, count, xScale, yScale);
    }
    public Plot(FunctionToPlot fx, FunctionToPlot fy, int xOrigin, int yOrigin, double xFirst, double xLast, int count, double xScale, double yScale) {
        super();
        
        double x = xFirst;
        double delta = (xLast - xFirst) / (double) count;
        while(count-- > 0) {
            int xNew = (int) ((double) xOrigin + fx.fn(x) * xScale);
            int yNew = (int) ((double) yOrigin + fy.fn(x) * yScale);
            addPoint(xNew, yNew);
            x += delta;
        }
    }


    // File I/O members
    public static String id() {return "Plot";}
    public Plot(BufferedReader br) throws IOException {
        super(br);
    }
    public void save(BufferedWriter bw) throws IOException {
        bw.write(id() + "\n");  // Identifies which object to instance
        super.save(bw);
    }

    // Spirograph equations courtesy of Linux Gazette, 
    //     https://linuxgazette.net/133/luana.html

    public static double spiroX(double R, double r, double p, double t) {
        return (double)((R-r)*Math.cos(t)+p*Math.cos((R-r)*t/r));
    }
    
    public static double spiroY(double R, double r, double p, double t) {
        return (double)((R-r)*Math.sin(t)+p*Math.sin((R-r)*t/r));
    }

}
