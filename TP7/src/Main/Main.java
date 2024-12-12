package Main;

import Controller.StaffController;
import View.StaffView;

public class Main {
    public static void main(String[] args) {
        StaffView view = new StaffView();
        new StaffController(view);
        view.setVisible(true);
    }
}