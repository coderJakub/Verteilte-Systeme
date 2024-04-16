import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class MandelbrotLoesung {
  public static void main(String[] args) {
    ApfelPresenter p = new ApfelPresenter();
    ApfelView v = new ApfelView(p);
    ApfelModel m = new ApfelModel(v);
    p.setModelAndView(m, v);
    p.apfel();
  }
}

/* ************************** Presenter ********************** */
class ApfelPresenter {
  protected ApfelModel m;
  protected ApfelView v;

  double xmin = -1.666, xmax = 1, ymin = -1, ymax = 1; // Parameter des Ausschnitts
  // static double  cr = -0.3735,  ci = 0.655;
  double cr = -0.743643887036151, ci = 0.131825904205330;
  double zoomRate = 1.5;
  int xpix = 640, ypix = 480;

  public void setModelAndView(ApfelModel m, ApfelView v) {
    this.m = m;
    this.v = v;
    v.setDim(xpix, ypix);
    m.setParameter(xpix, ypix);
  }

  /** Komplette Berechnung aller Bilder */
  void apfel() {
    Color[][] c = new Color[xpix][ypix];
    for (int i = 1; i < 65; i++) { // Iterationen bis zum Endpunkt
      System.out.println(i + " Vergrößerung: " + 2.6 / (xmax - xmin) + " xmin: " + xmin + " xmax: " + xmax);
      c = m.apfel_bild(xmin, xmax, ymin, ymax);
      v.update(c);
      double xdim = xmax - xmin;
      double ydim = ymax - ymin;
      xmin = cr - xdim / 2 / zoomRate;
      xmax = cr + xdim / 2 / zoomRate;
      ymin = ci - ydim / 2 / zoomRate;
      ymax = ci + ydim / 2 / zoomRate;
    }
  }

}

/* ************************* View *************************** */
class ApfelView {
  private ApfelPresenter p;
  private ApfelPanel ap = new ApfelPanel();
  public  JTextField tfi;
  public  JTextField tfr;
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
    JPanel  sp = new JPanel( new FlowLayout());
    JButton sb = new JButton("Start");

    tfr = new JTextField("-0.743643887037151");
    tfi = new JTextField("0.131825904205330");
    sp.add(tfr);
    sp.add(tfi);
    sp.add(sb);

    //f.setLayout( new BorderLayout() );
    f.add(ap, BorderLayout.CENTER);
    f.add(sp, BorderLayout.SOUTH);
    f.setSize(xpix, ypix+100);
    f.setVisible(true);
  }

  public void update(Color[][] c) {
    for (int y = 0; y < ypix; y++) {
      for (int x = 0; x < xpix; x++) {
        if (c[x][y] != null) image.setRGB(x, y, c[x][y].getRGB());
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
class ApfelModel {
  ApfelView v;
  boolean farbe = false;
  final int max_iter = 5000;
  final double max_betrag2 = 4;
  int xpix, ypix;
  double xmin, xmax, ymin, ymax;
  int[][] bildIter; // Matrix der Iterationszahl, t.b.d.
  Color[][] bild;


  public ApfelModel(ApfelView v) {
    this.v = v;
  }

  public void setParameter(int xpix, int ypix) {
    this.xpix = xpix;
    this.ypix = ypix;
    bildIter = new int[xpix][ypix]; // Matrix der Iterationszahl, t.b.d.
    bild = new Color[xpix][ypix];
  }

  /** Erzeuge ein komplettes Bild mittles Threads */
  Color[][] apfel_bild(double xmin, double xmax, double ymin, double ymax) {
    this.xmin = xmin;
    this.xmax = xmax;
    this.ymin = ymin;
    this.ymax = ymax;

    ApfelWorker worker = new ApfelWorker(0, ypix);
    worker.work();
    return bild;
  }

  // Threads and writing to arrays
  // http://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html#jls-17.6

  /** @author jvogt lokale Klasse zum Thread-Handling */
  class ApfelWorker  {
    int y_sta, y_sto;

    public ApfelWorker(int y_start, int y_stopp) {
      this.y_sta = y_start;
      this.y_sto = y_stopp;
    }

    public void work() {
      double c_re, c_im;
      for (int y = y_sta; y < y_sto; y++) {
        c_im = ymin + (ymax - ymin) * y / ypix;

        for (int x = 0; x < xpix; x++) {
          c_re = xmin + (xmax - xmin) * x / xpix;
          int iter = calc(c_re, c_im);
          bildIter[x][y] = iter;
          Color pix = farbwert(iter); // Farbberechnung
          // if (iter == max_iter) pix = Color.RED; else pix = Color.WHITE;
          // v.image.setRGB(x, y, pix.getRGB()); // rgb
          bild[x][y] = pix;
        }
      }
    }

    /**
     * @param cr Realteil
     * @param ci Imaginärteil
     * @return Iterationen
     */
    public int calc(double cr, double ci) {
      int iter;
      double zr, zi, zr2 = 0, zi2 = 0, zri = 0, betrag2 = 0;
      //  z_{n+1} = z²_n + c
      //  z²  = x² - y² + i(2xy)
      // |z|² = x² + y²
      for (iter = 0; iter < max_iter && betrag2 <= max_betrag2; iter++) {
        zr = zr2 - zi2 + cr;
        zi = zri + zri + ci;

        zr2 = zr * zr;
        zi2 = zi * zi;
        zri = zr * zi;
        betrag2 = zr2 + zi2;
      }
      return iter;
    }

    /**
     * @param iter Iterationszahl
     * @return Farbwert nsmooth = n + 1 - Math.log(Math.log(zn.abs()))/Math.log(2)
     *     Color.HSBtoRGB(0.95f + 10 * smoothcolor ,0.6f,1.0f);
     */
    Color farbwert(int iter) {
      if (!farbe) {
        if (iter == max_iter) return Color.BLACK;
        else return Color.RED;
      }
      return Color.BLACK;
    }
  } // ApfelThread
}
