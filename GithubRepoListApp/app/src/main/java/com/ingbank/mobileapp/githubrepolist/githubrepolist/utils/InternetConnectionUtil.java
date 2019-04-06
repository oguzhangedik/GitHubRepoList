package com.ingbank.mobileapp.githubrepolist.githubrepolist.utils;

import java.io.IOException;

public class InternetConnectionUtil {

    public static boolean isConnected() {
        try {
            return InternetConnectionUtil.isConnectedToInternet();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isConnectedToInternet() throws InterruptedException, IOException {
        final String command = "ping -i 5 -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
}
