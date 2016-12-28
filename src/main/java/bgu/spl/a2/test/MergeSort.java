/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

    private final int[] array;

    public MergeSort(int[] array) {
        this.array = array;
    }

    @Override
    protected void start() { //TODO: change back to this original line instead of the one below!!!!!
//    public void start() {//TODO: REMOVE MEEEEEEEEEEEEEEEEEEEE

        if(array.length ==1){

            complete(array);
        }
        else {

            List<Task<int[]>> tasks = new ArrayList<>();
            int[][] arrays = split(array);

            MergeSort task0 = new MergeSort(arrays[0]);
            MergeSort task1 = new MergeSort(arrays[1]);

            tasks.add(task0);
            tasks.add(task1);

            spawn(task0, task1);

            whenResolved(tasks, () -> {

                int[] resultOfTask0 = tasks.get(0).getResult().get();
                int[] resultOfTask1 = tasks.get(1).getResult().get();
                int[] mergedArray = merge(resultOfTask0, resultOfTask1);

                complete(mergedArray);
            });

        }
//
////        MergeSort task3 = new MergeSort(ints);
////        MergeSort task4 = new MergeSort(ints);
//
//        spawn(task2);


//        System.out.println(Arrays.toString(arrays[0]));
//        System.out.println(Arrays.toString(arrays[1]));
//        System.out.println(Arrays.toString(merge(arrays[0], arrays[1])));

    }

    private int[] merge(int[] arr1, int[] arr2) {

        int arr1Length = arr1.length;
        int arr2Length = arr2.length;

        int[] mergedArr = new int[arr1Length + arr2Length];
        int indexOfArr1 = 0;
        int indexOfArr2 = 0;
        int indexOfMerged = 0;

        while((indexOfArr1 < arr1Length) || (indexOfArr2 < arr2Length)) {

            if((indexOfArr1 < arr1Length) && (indexOfArr2 < arr2Length)) {

                if(arr1[indexOfArr1] < arr2[indexOfArr2]) {

                    mergedArr[indexOfMerged] = arr1[indexOfArr1];
                    indexOfArr1++;
                }
                else {

                    mergedArr[indexOfMerged] = arr2[indexOfArr2];
                    indexOfArr2++;
                }

                indexOfMerged++;
            }
            else if((indexOfArr1 < arr1Length)) {

                mergedArr[indexOfMerged] = arr1[indexOfArr1];
                indexOfArr1++;
                indexOfMerged++;
            }
            else {

                mergedArr[indexOfMerged] = arr2[indexOfArr2];
                indexOfArr2++;
                indexOfMerged++;
            }
        }

        return mergedArr;
    }

    private int[][] split(int[] array) {

        int[][] arrays = new int[2][];
        int arrayLength = array.length;
        int[] arr1;
        int[] arr2;
        int arrayIndex = 0;

        if((arrayLength%2) == 0){

            arr1 = new int[arrayLength/2];
            arr2 = new int[arrayLength/2];
        }
        else {

            arr1 = new int[arrayLength/2 +1];
            arr2 = new int[arrayLength/2];
        }

        for(int i=0; i < arr1.length; i++){

            arr1[i] = array[arrayIndex];
            arrayIndex++;
        }

        for(int i=0; i < arr2.length; i++){

            arr2[i] = array[arrayIndex];
            arrayIndex++;
        }

        arrays[0] = arr1;
        arrays[1] = arr2;

        return arrays;
    }

    public static void main(String[] args) throws InterruptedException {

        // TODO: REMOVE FROM HERE
        long  start = System.currentTimeMillis();
        WorkStealingThreadPool pool = new WorkStealingThreadPool(13);
        int n = 1000000;
        // TODO: REMOVE TO HERE AND UNCOMMENT THE TWO LINES BELOW

//        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
//        int n = 1000000; //you may check on different number of elements if you like

        int[] array = new Random().ints(n).toArray();

        MergeSort task = new MergeSort(array);

        CountDownLatch l = new CountDownLatch(1);
        pool.start();
        pool.submit(task);
        task.getResult().whenResolved(() -> {
            //warning - a large print!! - you can remove this line if you wish
//            System.out.println(Arrays.toString(task.getResult().get()));//TODO: UNCOMMENT IT!!!!

            ////////////////TODO: REMOVE FROM HERE

            int[] ans = task.getResult().get();

            System.out.println("size of final merged array: " + ans.length);

            boolean isSorted = true;

            for(int i = 1; i<ans.length; i++) {
                if(ans[i-1] > ans[i]){
                    isSorted = false;
                }
            }

            System.out.println("sorted: " + isSorted);

            long  time = System.currentTimeMillis() - start;
            System.out.println("Execution time(mills): " + time);
            Toolkit.getDefaultToolkit().beep();
            ////////////////TODO: REMOVE TO HERE
            l.countDown();
        });

        l.await();
        pool.shutdown();

        System.out.println("End of main");// TODO: REMOVE MEEEEEEEEEE
    }

}
