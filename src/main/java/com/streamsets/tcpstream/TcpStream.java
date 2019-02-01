package com.streamsets.tcpstream;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Scanner;

public class TcpStream {

  private static final long MAX_MESSAGES = Long.MAX_VALUE - 1;
  private static final String HOSTNAME = "localhost";
  private static final int PORT = 55_555;

  public static void main(String[] args) {

    int option = printOptions();
    while (option != 3) {
      if (option == 1) {
        System.out.println(
            "Enter the number of records to send. If the number entered is <= 0, the number of messages that will be " +
                "sent is 'Long.MAX_VALUE - 1'");
        long input = getNextLong();
        if (input <= 0) {
          loadTesting(MAX_MESSAGES);
        } else {
          loadTesting(input);
        }
      } else if (option == 2) {
        System.out.println(
            "Enter the number of seconds to wait for the timeout before trying to send or receive data. Enter a " +
                "number >= 1");
        long input = getNextLong();
        if (input < 1) {
          System.out.println("Wrong number. Timeout number must be >= 1");
          input = getNextLong();
        }
        readTimeoutTesting(input);
      }
      option = printOptions();
    }
  }

  private static int getNextInt() {
    Scanner sc = new Scanner(System.in);
    if (sc.hasNextInt()) {
      return sc.nextInt();
    }

    return -1;
  }

  private static long getNextLong() {
    Scanner sc = new Scanner(System.in);
    if (sc.hasNextLong()) {
      return sc.nextLong();
    }

    return -1;
  }

  private static int printOptions() {
    while (true) {
      System.out.println("Choose a test:");
      System.out.println("1: Load Testing");
      System.out.println("2: Read Timeout Testing");
      System.out.println("3: End Program");

      int input = getNextInt();
      if (input <= 0 || input > 3) {
        System.out.println("Incorrect option!");
      } else {
        return input;
      }
    }
  }

  private static void loadTesting(long nummMessagesToSend) {
    final int RECORD_COUNT = 100_000;
    long globalRecordCount = 0;
    while (globalRecordCount < nummMessagesToSend) {
      final long startTime = System.currentTimeMillis();
      while (globalRecordCount < nummMessagesToSend) {
        long batchTime = System.currentTimeMillis();
        try (Socket clientSocket = new Socket(HOSTNAME, PORT)) {
          try (DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {
            for (int i = 0; i < RECORD_COUNT && globalRecordCount < nummMessagesToSend; i++) {
              String cheese = "hello there. record " + globalRecordCount + "!\n";
              output.writeBytes(cheese);
              globalRecordCount++;
            }
            output.flush();
            Thread.sleep(2000);
          } catch (InterruptedException ex) {
            System.err.println("exception " + ex.getMessage());
          }
        } catch (IOException ex) {
          System.err.println("exception " + ex.getMessage());
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            System.out.println("InterruptedException " + e.getMessage());
            e.printStackTrace();
            System.exit(27);
          }
          break;
        }

        long curr = System.currentTimeMillis() - batchTime;

        try {
          Thread.sleep(50);
        } catch (InterruptedException ex) {
          System.out.println("Exception: " + ex.getMessage());
          System.exit(27);
        }
        long elapsed = (System.currentTimeMillis() - startTime)/1000;
        System.out.print(String.format("%s  %d:%02d:%02d  batch elapsed %d\n",
            new Date(System.currentTimeMillis()),
            elapsed /3600,
            elapsed % 3600 / 60,
            elapsed % 60,
            curr));
      }
    }
  }

  private static void readTimeoutTesting(long timeout) {
    final long startTime = System.currentTimeMillis();
    long endTime;
    try (Socket clientSocket = new Socket(HOSTNAME, PORT)) {
      DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
      System.out.println("Writing message before timeout");
      output.writeBytes("Timeout test record\n");
      output.flush();
      Thread.sleep(timeout*1000);
      try {
        System.out.println("Writing message after timeout");
        output.writeBytes("Trying to send message after sleeping for: " + timeout + " seconds\n");
        output.flush();
        // sleep 10 seconds and try another write as buffer delays may affect when detecting whether channel is closed
        Thread.sleep(10000);
        System.out.println("Writing message after timeout + 10 seconds as buffers delay affects the time when " +
            "channel closed is detected");
        output.writeBytes("Trying to send message after sleeping for: " + timeout +
                " seconds and waiting for 10 additional seconds\n");
        output.flush();
      } catch (Exception e) {
        System.out.println("Got next exception when trying to send message after waiting for timeout: " + e);
      }

    } catch (Exception e) {
      System.out.println("Got exception in Read Timeout Test: " + e.getMessage());
      e.printStackTrace();
    }
    endTime = System.currentTimeMillis();
    System.out.println("Elapsed time (seconds) = " + (endTime - startTime)/1000.0 + " seconds");
  }
}
