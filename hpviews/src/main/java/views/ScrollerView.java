package views;

import controllers.OnOverflowScroll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ScrollerView extends JPanel {

    int width = 10;
    int breadth = 250;
    int contentSize = 350;
    int moved = 0;
    int scrollBarLength = 0;
    int barArc = 20;
    int increment = 0;
    private ScrollPolicy policy = ScrollPolicy.VERTICAL;
    private BarHorizontal barHorizontal;
    private BarVertical barVertical;
    private BufferedImage image = null;

    public ScrollerView(int width, int breadth, int contentSize, ScrollPolicy policy) {
        this.width = width;
        this.breadth = breadth;
        this.contentSize = contentSize;
        this.policy = policy;
        common();
    }

    public void update(int contentSize){
        this.contentSize = contentSize;

        if(policy == ScrollPolicy.HORIZONTAL){
            scrollBarLength = getScrollBarLength(width, contentSize);
            image = null;
            barHorizontal.repaint();
        }else{
            scrollBarLength = getScrollBarLength(breadth, contentSize);
            image = null;
            barVertical.repaint();
        }
    }


    public ScrollerView() {
        common();
    }

    public ScrollerView(ScrollPolicy policy) {
        this.policy = policy;
        common();
    }

    private void common(){
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        if(policy == ScrollPolicy.HORIZONTAL){
            barHorizontal = new BarHorizontal();
            add(barHorizontal);
        }else{
            barVertical = new BarVertical();
            add(barVertical);
        }


    }


    private int getScrollBarLength(int viewLength, int contentLength){
        //return (int) (((double)viewLength / (double)contentLength) * (double) viewLength);
        double opened = 100 - ((double)(100 * viewLength) / (double)contentLength);
        double rem = (viewLength * opened) / 100;
        increment = (int) Math.round((double) (contentLength - viewLength) / rem);
        return (int) (viewLength - rem);
    }


    private void positionScrolls(Canvas canvas, int pos){
        moved = pos;
        canvas.repaint();
        if(onOverflowScroll != null)onOverflowScroll.scroll(pos*increment);
    }

    private class BarVertical extends Canvas{

        public BarVertical() {

            scrollBarLength = getScrollBarLength(breadth, contentSize);

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int y = e.getY();

                    if(breadth >= (scrollBarLength + y) && y >= 0){
                        positionScrolls(BarVertical.this, y);
                    }
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    positionScrolls(BarVertical.this, e.getY());
                }
            });
        }



        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, breadth);
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D graphics2D = (Graphics2D)g;
            if(image == null){
                image = new BufferedImage(width, scrollBarLength, BufferedImage.TYPE_INT_ARGB);
                Graphics2D imageGraphics2D = image.createGraphics();
                imageGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                imageGraphics2D.setColor(Color.GRAY);
                imageGraphics2D.fillRect(0, 0, width, scrollBarLength);
            }
            graphics2D.drawImage(image, 0, moved, null);


        }
    }

    private class BarHorizontal extends Canvas{

        public BarHorizontal() {
            scrollBarLength = getScrollBarLength(width, contentSize);

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int x =e.getX();
                    if(width >= (scrollBarLength + x) && x >= 0){
                        positionScrolls(BarHorizontal.this, x);
                    }
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    positionScrolls(BarHorizontal.this, e.getY());
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, breadth);
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D graphics2D = (Graphics2D)g;
            if(image == null){
                image = new BufferedImage(scrollBarLength, breadth, BufferedImage.TYPE_INT_ARGB);
                Graphics2D imageGraphics2D = image.createGraphics();
                imageGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                imageGraphics2D.setColor(Color.GRAY);
                imageGraphics2D.fillRect(0, 0, scrollBarLength, breadth);
            }
            graphics2D.drawImage(image, moved, 0, null);


        }
    }


    public enum ScrollPolicy{
        HORIZONTAL, VERTICAL
    }

    public OnOverflowScroll onOverflowScroll;

    public void onScroll(OnOverflowScroll onOverflowScroll){
        this.onOverflowScroll = onOverflowScroll;
    }
}
