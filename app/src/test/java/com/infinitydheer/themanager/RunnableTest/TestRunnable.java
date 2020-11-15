package com.infinitydheer.themanager.RunnableTest;

public class TestRunnable implements Runnable {
    private TestListener listener;

    public TestRunnable(TestListener t){
        this.listener=t;
    }

    @Override
    public void run() {
        int x=5;
        try{
            Thread.sleep(1000);
            x=x*x;
            Thread.sleep(1000);
            x=x+x;
            Thread.sleep(1000);
            x=x+10958;
            Thread.sleep(10000);

        }catch (InterruptedException e){
            e.printStackTrace();
        }
        listener.testChange(x);
    }
}
