package com.holy.javer;

public class JavaException {
    public static void main(String[] args) {
        try{
            int i = 0;
            int j = 1;
            System.out.println(i / j);
        } catch (ArithmeticException ae){
            System.out.println("ArithmeticException ..........");
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Exception ..........");
        } finally {
            System.out.println("try catch finally block");
        }
    }
}
