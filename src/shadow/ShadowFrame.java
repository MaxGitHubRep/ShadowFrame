package shadow;

import exception.InvalidPanelSizeException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Max Carter
 */
public class ShadowFrame extends JFrame {
    
    protected Timer timer;
    protected boolean isRising;
    protected static final int
            frameWidth = 1920,
            frameHeight = 1080;
    protected static int
            xCoord, yCoord,
            shadowSize,
            shadowedMargin,
            duration,
            position,
            animationGap;
    protected static JPanel panel;
    
    /**
    * Creates a new undecorated frame with a shadow background.
    * 
    * @param frameTitle The title of the frame.
    * @param panel The panel that will be applied to the frame.
    * @param panelPosition The location of the frame on the screen. (Uses 1920x1080 res)
    * @param shadowSize Size of the shadow.
    * @param durationInSeconds How long the panel will stay on screen for.
    */
    public ShadowFrame(String frameTitle, JPanel panel, int panelPosition, int shadowSize, int durationInSeconds) throws InvalidPanelSizeException {
        if (panelPosition > 4 || panelPosition < 0) {
            throw new IllegalArgumentException("Panel position must be between 0 and 4.");
            
        } else if (shadowSize < 0) {
            throw new IllegalArgumentException("The size of the shadow must be greater than 0.");
            
        } else if (panel.getWidth() > frameWidth - (shadowedMargin * 2) || panel.getHeight() > frameHeight - (shadowedMargin * 2)) {
            throw new InvalidPanelSizeException();
        }
        
        this.shadowSize = shadowSize;
        this.panel = panel;
        this.duration = durationInSeconds * 1000;
        this.position = panelPosition;
        shadowedMargin = 5 + shadowSize;
        animationGap = 20;
        isRising = true;
        
        // Creates the frame
        setUndecorated(true);
        setSize(panel.getWidth() + (shadowSize * 2), panel.getHeight() + (shadowSize * 2));
        setBackground(new Color(0, 0, 0, 0));
        setTitle(frameTitle);
        this.add(new ShadowPanel());
        
        setLocationInFrame(panelPosition);
        
        if (durationInSeconds > 0) {
            initTimer(isRising);
        }
    }
    
    /**
    * Defines the x and y coordinates for the frame.
    * 
    * @param panelPosition The location of the panel on the screen.
    */
    private final void setLocationInFrame(int panelPosition) {
        switch (panelPosition) {
            case (Position.CENTRE):
                xCoord = ((frameWidth - getWidth())/2) - shadowedMargin;
                yCoord = ((frameHeight - getHeight())/2) - shadowedMargin;
                break;

            case (Position.BOTTOM_LEFT):
                xCoord = shadowedMargin;
                yCoord = frameHeight - getHeight() - shadowedMargin;
                break;
                
            case (Position.BOTTOM_RIGHT):
                xCoord = frameWidth - getWidth() - shadowedMargin;
                yCoord = frameHeight - getHeight() - shadowedMargin;
                break;
                
            case (Position.TOP_RIGHT):
                xCoord = frameWidth - getWidth() - shadowedMargin;
                yCoord = shadowedMargin;
                break;
                
            default:
            case (Position.TOP_LEFT):
                xCoord = shadowedMargin;
                yCoord = shadowedMargin;
                break;
        }
    }
    
    /**
    * Closes the frame.
    */
    private void close() {
        this.dispose();
    }
    
    /**
    * Moves the frame if an animation is required.
    */
    private void exec() {
        timer.stop();
        if (animationGap > 0) {
            animationGap--;
            setLocation(xCoord, yCoord + animationGap);
            if (!isVisible()) setVisible(true);
        } else {
            isRising = false;
        }
        
        initTimer(isRising);
    }
    
    /**
    * Initiates the timer to move the frame.
    */
    private void initTimer(boolean rise) {
        timer = new Timer(rise ? 50 : duration, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rise) {
                    exec();
                } else {
                    close();
                }
            }
        }); 
        timer.start();
    }
    
    /**
    * Shadow panel class.
    */
    class ShadowPanel extends JPanel {

        /**
        * Creates border and defines panel size.
        */
        public ShadowPanel() {
            setSize(panel.getWidth() + (shadowSize * 2), panel.getHeight() + (shadowSize * 2));
            add(panel);
            panel.setLocation(shadowSize, shadowSize);

            setBorder(BorderFactory.createCompoundBorder(this.getBorder(), BorderFactory.createEmptyBorder(
                    shadowSize, shadowSize, shadowSize, shadowSize)));
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            int shade = 0;
            int topOpacity = 80;
            for (int i = 0; i < shadowSize; i++) {
                g.setColor(new Color(shade, shade, shade, ((topOpacity / shadowSize) * i)));
                g.drawRect(i, i, this.getWidth() - ((i * 2) + 1), this.getHeight() - ((i * 2) + 1));
            }
        }
        
    }
    
}