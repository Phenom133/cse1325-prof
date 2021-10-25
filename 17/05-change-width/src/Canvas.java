import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;

import shapes.Shape;
import shapes.Line;

public class Canvas extends JPanel {
    private final static int border = 25;

    public Canvas() {
        super();
        
        isDirty = false; // No data to start
        rubberBand = null;  // No rubber band yet
        color = Color.RED;  // Start with red color
        stroke = new BasicStroke();  // Default stroke
        
        // White background to our paintings by default
        setBackground(new java.awt.Color(255, 255, 255));
        
        // Call onMouseClicked when the mouse is clicked
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                onMouseClicked(event);
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent event) {
                onMouseMoved(event);
            }
        });
        
        clickInProgress = false;
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(1024,768);
    }

    // /////////////////////////////////////////////////////////////////
    // Listeners
    
    public void onMouseClicked(MouseEvent event) {
        // Single click with primary button
        if ((event.getButton() == MouseEvent.BUTTON1) &&
            (event.getClickCount() == 1)) {
            if(!clickInProgress) { // first click
                x1 = event.getX() - xOffset;
                y1 = event.getY() - yOffset;
                clickInProgress = true;
            } else { // second click
                shapes.add(new Shape(
                    new Line(x1, y1, 
                             event.getX() - xOffset,
                             event.getY() - yOffset,
                             color, stroke)
                ));
                repaint(); // request call to paintComponent
                isDirty = true;  // unsaved data now present
                rubberBand = null; // No more rubber banding
                clickInProgress = false;
            }
        }
    }

    public void onMouseMoved(MouseEvent event) {
        // Single click with primary button
        if(clickInProgress) { // second click pending
            rubberBand = new Shape(
                    new Line(x1, y1, 
                             event.getX() - xOffset,
                             event.getY() - yOffset,
                             color, stroke)
            );
            repaint(); // request call to paintComponent
        }
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
             

        // Move origin to center
        java.awt.Rectangle size = getBounds();
        xOffset = size.width / 2;
        yOffset = size.height/2;
        g.translate(xOffset, yOffset);
        
        // Paint all shapes in the ArrayList
        for(Shape shape : shapes)
            shape.paintComponent(g);
            
        // Paint the temporary shape that "rubber bands" a pending line
        if(rubberBand != null) rubberBand.paintComponent(g);
    }
    
    // ////////////////////////////////////////////////////////////////
    // Attributes, Getters and Setters
    public boolean isDirty() {
        return isDirty;
    }
    public Color color() {return color;}
    public void color(Color newColor) {color = newColor;}
    
    public float width() {return stroke.getLineWidth();}
    public void width(float newWidth) {
        stroke = new BasicStroke(newWidth, 
            stroke.getEndCap(), stroke.getLineJoin(), stroke.getMiterLimit(), 
            stroke.getDashArray(), stroke.getDashPhase());
    }

    private int x1, y1; // Location of previous click
    private int xOffset, yOffset; // Amount of translation to center the origin
    private Color color; // Color for upcoming lines to be drawn
    private BasicStroke stroke; // width and dash pattern
    private boolean clickInProgress; // true after first click
     
    protected ArrayList<Shape> shapes = new ArrayList<>();
    protected Shape rubberBand;
    private boolean isDirty;
}
