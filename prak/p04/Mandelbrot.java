import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Mandelbrot {
    public static void main(String[] args) {
        ApfelPresenter p = new ApfelPresenter();
        ApfelView v = new ApfelView(p);
        ApfelModel m = new ApfelModel(v);
        p.setModelAndView(m, v);
        p.apfelVideo();
    }
}

/* ************************** Presenter ********************** */
class ApfelPresenter implements ActionListener {
    protected ApfelModel m;
    protected ApfelView v;

    double xmin = -1.666, xmax = 1, ymin = -1, ymax = 1; // Parameter des Ausschnitts
    double cr = -0.743643887035151, ci = 0.131825904205330;
    double zoomRate = 1.5;
    int xpix = 640, ypix = 480;

    public void setModelAndView(ApfelModel m, ApfelView v) {
        this.m = m;
        this.v = v;
        v.setDim(xpix, ypix);
        m.setParameter(xpix, ypix);
    }

    /** Komplette Berechnung und Anzeige aller Bilder */
    void apfelVideo() {
        // TODO
    }

    public void actionPerformed(ActionEvent e){
        System.out.println("click");
    } 
}

/* ************************* View *************************** */
class ApfelView {
    private ApfelPresenter p;
    private ApfelPanel ap = new ApfelPanel();
    int xpix, ypix;
    BufferedImage image;

    public ApfelView(ApfelPresenter p) {
        this.p = p;
    }

    public void setDim(int xpix, int ypix) {
        this.xpix = xpix;
        this.ypix = ypix;
        image = new BufferedImage(xpix, ypix, BufferedImage.TYPE_INT_RGB);
        initView();
    }

    private void initView() {
        JFrame f = new JFrame();
        JPanel sp = new JPanel(new FlowLayout());
        JButton sb = new JButton("Start");
        JTextField tf1 = new JTextField("1.1");
        JTextField tf2 = new JTextField("1.1");

        sb.addActionListener(p);
        //sp.setBackground(Color.green);
        ap.setBackground(Color.blue);
        sp.add(sb);
        sp.add(tf1);
        sp.add(tf2);

        f.add(ap, BorderLayout.CENTER);
        f.add(sp, BorderLayout.SOUTH);
        f.setSize(xpix,ypix+100);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    class ApfelPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null); // see javadoc
        }
    }
}

/* *********** Model **************************** */
  // Threads and writing to arrays
  // http://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html#jls-17.6
class ApfelModel {
    ApfelView v;
    int xpix, ypix;
    double xmin, xmax, ymin, ymax;
    Color[][] bild;

    public ApfelModel(ApfelView v) {
        this.v = v;
    }

    public void setParameter(int xpix, int ypix) {
        this.xpix = xpix;
        this.ypix = ypix;
        bild = new Color[xpix][ypix];
    }

    /** Erzeuge ein komplettes Bild mit oder ohne Threads */
    Color[][] apfel_bild(double xmin, double xmax, double ymin, double ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        
        for(int y=ymin; y<ymax; y++){
            double c_im = ymin+(ymax-ymin)*y/ypi;
            for(int x=xmin; x<xmax; x++){
                
            }
        }
        return bild;
    }
}