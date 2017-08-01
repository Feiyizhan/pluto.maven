package demo.pluto.maven.thread;

public class ThreadNeverStop {
    
    private static volatile boolean stopRequested;
    
    public static void main(String[] args) throws InterruptedException {
        stopRequested = false;
        Thread backgroundThread = new Thread(new Runnable() {
            
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                while (!stopRequested) {
                    //whatever
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.print(".");
                }
                long end = System.currentTimeMillis();
                System.out.println("it takes " + (end - start) + " miliseconds.");
            }
        });
        
        backgroundThread.start();
        
        Thread.sleep(1000L);
        
        stopRequested = true;
    }
}
