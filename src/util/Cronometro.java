package util;
public final class Cronometro {  
  
 private static long startValue;  
 private static long stopValue;  
 private static long timeDiff;  

 public static void start() {  
  startValue = System.currentTimeMillis();  
  stopValue = 0;  
  timeDiff = 0;  
 }  
  

 public static void stop() {  
  stopValue = System.currentTimeMillis();  
  timeDiff = stopValue - startValue;  
 }  
  
   
 public static long elapsedTime() {  
  return timeDiff;  
 }  
}