import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        String str = "capsule" + "\t" + "viên thuốc con ";
        System.out.println(str);
        String[] arrOfString = str.split("\t");
        for (String i : arrOfString){
            System.out.println(i);
        }
        System.out.println();
    }
}
