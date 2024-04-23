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

    double xmin = -1.666, xmax = 1, ymin = -1, ymax = 1; // Parameter des aktuellen Ausschnitts
    double cr = -0.743643887035151, ci = 0.131825904205330; //c = cr (Realteil)+ ci*i (Imaginärteil)
    double zoomRate = 1.5; // Zoomfaktor mit welchem ein Ausschnitt vergrößert wird
    int xpix = 640, ypix = 480; // Größe des Bildes

    public void setModelAndView(ApfelModel m, ApfelView v) {
        this.m = m;
        this.v = v;
        v.setDim(xpix, ypix);
        m.setParameter(xpix, ypix);
    }

    /** Komplette Berechnung und Anzeige aller Bilder */
    void apfelVideo() {
        Color[][] bild;
        for(int i=0;i<65;i++){
            System.out.println(i); 
            bild= m.apfel_bild(xmin, xmax, ymin, ymax);
            v.update(bild);
            double xdim = xmax - xmin;
            double ydim = ymax - ymin;
            xmin = cr - xdim / 2 / zoomRate;
            xmax = cr + xdim / 2 / zoomRate;
            ymin = ci - ydim / 2 / zoomRate;
            ymax = ci + ydim / 2 / zoomRate;
        }
    }

    public void actionPerformed(ActionEvent e){
        apfelVideo();
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
        //ap.setBackground(Color.blue);
        sp.add(sb);
        sp.add(tf1);
        sp.add(tf2);

        f.add(ap, BorderLayout.CENTER);
        f.add(sp, BorderLayout.SOUTH);
        f.setSize(xpix,ypix+100);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public void update(Color[][] bild) {
        System.out.println("update image");
        for (int x = 0; x < xpix; x++) {
            for (int y = 0; y < ypix; y++) {
                if(bild[x][y]!=null)image.setRGB(x, y, bild[x][y].getRGB());
            }
        }
        ap.repaint();
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
        
        for (int x = 0; x < xpix; x++) {
            for (int y = 0; y < ypix; y++) {
                double cr = xmin + (xmax - xmin) * x / xpix;
                double ci = ymin + (ymax - ymin) * y / ypix;
                bild[x][y] = calc(cr, ci);
            }
        }

        return bild;
    }

    int MAX_ITER = 1000;
    /** Berechne die Farbe eines Punktes */
    Color calc(double cr, double ci) {
        double zr = 0, zi = 0;
        int n = 0;
        while (n < MAX_ITER && zr * zr + zi * zi < 4) {
            double zr1 = zr * zr - zi * zi + cr;
            zi = 2 * zr * zi + ci;
            zr = zr1;
            n++;
        }
        return getColor(n);
    }

    Color getColor(int n) {
        if (n == MAX_ITER) return Color.BLACK;
        return Color.getHSBColor((float) n / MAX_ITER, 1, 1); //Farbe abhängig von n: n=0 -> blau, n=100 -> schwarz
    }
}