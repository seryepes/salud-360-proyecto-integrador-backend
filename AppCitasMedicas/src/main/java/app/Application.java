package app;

import app.configuration.AppConfig;

public class Application {

    public static void main(String[] args) {
        new AppConfig().createUserInterface().start();
    }
}
