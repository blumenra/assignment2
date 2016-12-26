package bgu.spl.a2;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by blumenra on 12/12/16.
 */
public class VersionMonitorTest {
    @Test
    public void getVersion() throws Exception {
        VersionMonitor vm = new VersionMonitor();
        assertEquals("version monitor's version is initialized as 0", 0, vm.getVersion());
    }

    @Test
    public void inc() throws Exception {

        VersionMonitor vm = new VersionMonitor();
        vm.inc();
        vm.inc();
        vm.inc();
        vm.inc();

        assertEquals("version monitor's version was incremented 4 times", 4, vm.getVersion());

    }

    @Test
    public void await() throws Exception {

        VersionMonitor vm = new VersionMonitor();

        Runnable r1 = new Runnable() {
            @Override
            public void run() {

                try {
                    vm.await(0);

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        };

        Thread t1 = new Thread(r1);

        Runnable r2 = new Runnable() {
            @Override
            public void run() {

                vm.inc();
            }
        };

        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }

}