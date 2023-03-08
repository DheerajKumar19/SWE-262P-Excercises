package org.example;


import java.util.concurrent.ArrayBlockingQueue;

interface Actor<T> extends Runnable {
    ArrayBlockingQueue<T> getOutputChannel();
}
