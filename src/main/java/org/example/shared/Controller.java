package org.example.shared;

import java.util.Scanner;

public class Controller {

    public static String getString(){
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static int get_int(){
        int input_int;
        do {
            try {
                input_int = Integer.parseInt(getString());
                break;
            }
            catch (NumberFormatException e) {
                System.out.println(("Неправильный ввод! Введите заново: "));
            }
        }while (true);
        return input_int;
    }
}
