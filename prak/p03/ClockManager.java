import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;

class Clock //Model zur Bereitstellung der Daten
{
    private long startTime;

    public long getTime() {   
        long currTime = System.currentTimeMillis();
        return currTime-this.startTime;
    }

    public void reset() {
        this.startTime = System.currentTimeMillis();
    }
}

// ############################################################################
class UpdateRequest implements Runnable
{
    private Clock     clock;
    private ClockView view;

    public UpdateRequest(Clock clock, ClockView view) {
        this.clock = clock;
        this.view  = view;
    }

    public void run() {
        this.view.showTime(this.clock.getTime());
    }
}

class Ticker extends Thread
{
    private final static long UPDATE_INTERVAL = 10; // Milliseconds
    private UpdateRequest updateReq;

    public Ticker(Clock clock, ClockView view) {
        this.updateReq = new UpdateRequest(clock, view);
    }

    public void run() {
        try {
            while(!isInterrupted()) {
                EventQueue.invokeLater(updateReq);
                sleep(UPDATE_INTERVAL);
            }
        }
        catch(InterruptedException e) { }
    }



}

class ClockPresenter implements ActionListener
{
    protected Clock     clock;
    protected ClockView view;
    private Ticker ticker;

    public void setModelAndView(Clock clock, ClockView view)  {
        this.clock = clock;
        this.view  = view;
    }

    // this runs in the GUI task
    public void actionPerformed(ActionEvent e)  {
        String s = e.getActionCommand();
        if(s.equals("Start"))  {
            if(ticker==null){
              this.ticker = new Ticker(clock,view);
              clock.reset();
            }
            ticker.start();
        }        
        else if(s.equals("Stopp"))  {
          // TODO stop the clock (ticker) if running
            if(ticker!=null)ticker.interrupt();
            ticker=null;
        }
        else if(s.equals("Null")) {
            this.clock.reset();
            view.showTime(0);
        }
        else if(s.equals("Ende")) {
            System.exit(0);
        }
    }
}

// ############################################################################
class ClockView
{
    private ClockPresenter presenter;
    private JLabel  label;

    public ClockView( ClockPresenter presenter) {
        this.presenter = presenter;
        initView();
    }

    private void initView() {
        JFrame f = new JFrame("Uhr");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(0, 1));
        label = new JLabel("", SwingConstants.RIGHT);
        f.add(label);
        JButton b1 = new JButton("Start");
        f.add(b1);
        JButton b2 = new JButton("Stopp");
        f.add(b2);
        JButton b3 = new JButton("Null");
        f.add(b3);
        JButton b4 = new JButton("Ende");
        f.add(b4);
        b1.addActionListener(presenter);
        b2.addActionListener(presenter);
        b3.addActionListener(presenter);
        b4.addActionListener(presenter);
        f.setLocation(300, 50);
        f.setSize(150, 200);
        f.setVisible(true);
    }

    public void showTime(long elapsedTime) {
        String millis = String.format(": %d");
        String seconds = ":"+(elapsedTime/1000)%(60);
        String min = ":"+(elapsedTime/(1000*60))%60;
        String hour = ""+elapsedTime/(1000*60*60);
        this.label.setText(hour+min+seconds+millis);
    }
}

// ############################################################################
public class ClockManager
{
    public static void main(String[] args) {
        ClockPresenter h  = new ClockPresenter();
        ClockView    view = new ClockView( h );
        Clock clock       = new Clock();
        h.setModelAndView(clock, view);
        //h.reset();
    }
}