package com.lucha.juego;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Personaje {

    private final String nombre;
    private int puntosDeVida;
    private final Random rand = new Random();

    private final Texture spriteSheet;
    private final TextureRegion[] frames;

    private TextureRegion frameActual;

    private float posicionX;
    private float posicionY = 100f;

    private final int teclaArriba;
    private final int teclaAbajo;
    private final int teclaIzquierda;
    private final int teclaDerecha;
    private final int teclaAtaque;

    private final float velocidad = 150f;

    @SuppressWarnings("unused")
    private float tiempoAnimacion = 0f;

    private static final int MAX_DANO = 30;
    private static final int MIN_DANO = 10;

    public Personaje(String nombre, Dificultad dificultad, float posicionX,
                     int teclaArriba, int teclaAbajo, int teclaIzquierda, int teclaDerecha,
                     int teclaAtaque, String rutaSprite) {
        this.nombre = nombre;
        this.puntosDeVida = 100;
        this.posicionX = posicionX;
        this.teclaArriba = teclaArriba;
        this.teclaAbajo = teclaAbajo;
        this.teclaIzquierda = teclaIzquierda;
        this.teclaDerecha = teclaDerecha;
        this.teclaAtaque = teclaAtaque;

        spriteSheet = new Texture(rutaSprite);
        TextureRegion[][] temp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 4, spriteSheet.getHeight());

        frames = new TextureRegion[4];
        System.arraycopy(temp[0], 0, frames, 0, 4);

        frameActual = frames[0]; // Quieto por defecto
    }

    public void update(float delta) {
        tiempoAnimacion += delta;

        boolean movido = false;

        if (Gdx.input.isKeyPressed(teclaIzquierda)) {
            posicionX -= velocidad * delta;
            frameActual = frames[1]; // correr
            movido = true;
        } else if (Gdx.input.isKeyPressed(teclaDerecha)) {
            posicionX += velocidad * delta;
            frameActual = frames[1]; // correr
            movido = true;
        }

        if (Gdx.input.isKeyPressed(teclaArriba)) {
            posicionY += velocidad * delta;
            frameActual = frames[2]; // saltar
            movido = true;
        } else if (Gdx.input.isKeyPressed(teclaAbajo)) {
            posicionY -= velocidad * delta;
            frameActual = frames[3]; // agacharse
            movido = true;
        }

        if (!movido) {
            frameActual = frames[0]; // quieto
        }

        posicionX = Math.max(0, Math.min(posicionX, Gdx.graphics.getWidth() - spriteSheet.getWidth() / 4));
        float piso = 100f;
        posicionY = Math.max(piso, Math.min(posicionY, Gdx.graphics.getHeight() - spriteSheet.getHeight()));
    }

    public void render(SpriteBatch batch, float delta) {
        batch.draw(frameActual, posicionX, posicionY);
    }

    public boolean atacarSiColisiona(Personaje oponente) {
        if (Math.abs(this.posicionX - oponente.posicionX) < 50 &&
            Math.abs(this.posicionY - oponente.posicionY) < 50) {
            int danio = rand.nextInt(MAX_DANO - MIN_DANO + 1) + MIN_DANO;
            oponente.puntosDeVida = Math.max(0, oponente.puntosDeVida - danio);
            return true;
        }
        return false;
    }

    public boolean estaVivo() {
        return puntosDeVida > 0;
    }

    public int getPuntosDeVida() {
        return puntosDeVida;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTeclaAtaque() {
        return teclaAtaque;
    }

    public float getPosicionX() {
        return posicionX;
    }

    public float getPosicionY() {
        return posicionY;
    }

    public void dispose() {
        spriteSheet.dispose();
    }

    public enum Dificultad {
        FACIL, MEDIO, DIFICIL
    }
}
