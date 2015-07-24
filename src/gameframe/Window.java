package gameframe;

import static gameframe.GameFrame.HEIGHT;
import static gameframe.GameFrame.WIDTH;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author Hallgeir
 */
public class Window {

    private final JFrame frame;
    private final Canvas canvas;
    
    private final BufferedImage imageBuffer;
    
    private final Graphics2D gfx;
    
    private boolean disposed = false;
    
    public Window(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setIgnoreRepaint(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Dimension size = new Dimension(WIDTH, HEIGHT);

        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setPreferredSize(size);
        canvas.setBackground(Color.BLACK);

        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        setupFocusListener(frame, canvas);
        
        imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        gfx = imageBuffer.createGraphics();
    }
    
    public synchronized void render() {
        if(!disposed) {
            BufferStrategy bs = canvas.getBufferStrategy();
            if(bs == null) {
                canvas.createBufferStrategy(1);
                return;
            }

            Graphics2D g = (Graphics2D)bs.getDrawGraphics();

            if(g != null) {
                g.drawImage(imageBuffer, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
             
                g.dispose();
            }

            bs.show();
            Toolkit.getDefaultToolkit().sync();
        }
    }
    
    public Graphics2D getDrawGraphics() {
        return gfx;
    }
    
    public Component getFocusedComponent() {
        return canvas;
    }
    
    public synchronized void dispose() {
        disposed = true;
        gfx.dispose();
        frame.dispose();
    }
    
    public boolean isDisposed() {
        return disposed;
    }
    
    private void setupFocusListener(JFrame frame, Canvas canvas) {
        canvas.setFocusable(true);
        canvas.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                if(e.getOppositeComponent() != null) {
                    canvas.requestFocus();
                }
            }
        });

        frame.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                canvas.requestFocus();
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });
        
        frame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
            
        });
    }
}