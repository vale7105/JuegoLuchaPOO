package com.lucha.juego.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.lucha.juego.MainGame; // Ensure this is the correct package for MainGame

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        @SuppressWarnings("unused")
        Lwjgl3Application app = new Lwjgl3Application(new MainGame(), config);
    }
}
