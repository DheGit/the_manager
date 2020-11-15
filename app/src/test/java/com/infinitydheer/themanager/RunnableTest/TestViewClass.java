package com.infinitydheer.themanager.RunnableTest;

import org.junit.Test;

public class TestViewClass{
    @Test
    public void dataReceiver(){
        TestListener listener=new TestListener() {
            @Override
            public void testChange(int data) {
                System.out.println("The received data is : "+data);
            }
        };
        Thread newThread=new Thread(new TestRunnable(listener));
        newThread.start();
        System.out.println("The thread has started running");
        try {
            newThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
