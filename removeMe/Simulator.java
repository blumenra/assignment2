import java.util.List;
import java.util.LinkedList;


interface TimePassedEvent
{
    public void callBack();
}
 
class TimeNotifier implements Runnable
{
    private List<TimePassedEvent> subscribers;
    
    public TimeNotifier ()
    {
        subscribers = new LinkedList<TimePassedEvent>();
    } 
    
    public void subscribeToNotifier(TimePassedEvent ie) {
        subscribers.add(ie);
    }
 
    @Override
    public void run() {
            try {
               Thread.sleep(3000);
            } catch (InterruptedException e1) {}
 
        for (TimePassedEvent e : subscribers) {
                e.callBack();
        }
    } 
}
 
class Worker {
 
    private int id;
    private  String[] sarr = {"alon"};
    
    public Worker(int id) {
        this.id = id;
    }
    
    public void subscribeToNotifier(TimeNotifier notifier) {
        long threadID = Thread.currentThread().getId();
        String[] sarr2 = sarr;
        //threadID++; //(1)
        notifier.subscribeToNotifier(() -> 
            {
                System.out.println(sarr2[0]);
                sarr2[0] = "ofer";

                //int id = 5; //(2)
                System.out.println("{ Worker "+ id +" was notified that three seconds passed." +
                        "This callback function was defined in Thread: "+threadID +
                        "and activated on Thread: "+Thread.currentThread().getId() +" }");
                //threadID++; //(3)
            });
    }

    public String[] getSarr() {

        return sarr;
    }
 
}
 
class Simulator {
    
    public static void main(String[] args) {
        
        TimeNotifier notifier = new TimeNotifier();
        
        Worker w2;

        // for (int i=0;i<3;i++) {
        //     Worker w = new Worker(i);
        //     if(i == 0) {

        //         w2 = w; 
        //     }
        //     w.subscribeToNotifier(notifier);
        // }
        
        Worker w = new Worker(0);
        System.out.println("w:" + w.getSarr()[0]);
        w2 = w;
        System.out.println("w2:" + w2.getSarr()[0]);
        
        w.subscribeToNotifier(notifier);

        Thread t = new Thread(notifier);
        t.start();

        try {
            
            t.join();
        } catch(Exception e) {
            System.out.println(e);
        }

        System.out.println("w:" + w.getSarr()[0]);
        System.out.println("w2:" + w2.getSarr()[0]);
    }
}