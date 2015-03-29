package com.shutterbug.spacearcade;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class MainActivity {
    public static void main (String[] args) {
        new LwjglApplication(new MyGdxGame(), "Game", 1080, 1920);
    }
}