package com.messiuw.system;

import javax.swing.*;

public class SystemAPI {

    public static void showInfoDialog(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfoDialog(String message, Class callingClass) {
        showInfoDialog(message,callingClass.getSimpleName());
    }

    public static void showErrorDialog(String message, Class callingClass) {
        showErrorDialog(message,callingClass.getSimpleName());
    }

}

