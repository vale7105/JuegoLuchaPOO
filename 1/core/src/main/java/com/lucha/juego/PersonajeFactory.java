package com.lucha.juego;

import com.badlogic.gdx.Input;

public class PersonajeFactory {

    public static Personaje crearPersonaje(String tipo, Personaje.Dificultad dificultad, float posicionX) {
        switch (tipo.toLowerCase()) {
            case "ninja":
                return new Personaje(
                        "Heidy",
                        dificultad,
                        posicionX,
                        Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.F,
                        "robot_ninja.png" // ← Nuevo sprite sheet con los 4 frames
                );

            case "vaquera":
                return new Personaje(
                        "Stefany",
                        dificultad,
                        posicionX,
                        Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SPACE,
                        "vaquera_atacar.png"
                );

            default:
                throw new IllegalArgumentException("Tipo de personaje no válido: " + tipo);
        }
    }
}
