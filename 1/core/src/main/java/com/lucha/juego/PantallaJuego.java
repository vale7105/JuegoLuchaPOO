package com.lucha.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaJuego implements Screen {

    private final MainGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private Music musicaFondo;
    private Sound sonidoAtaque;

    private Texture fondo;
    private Animation<TextureRegion> animAvatar1, animAvatar2;
    private float tiempoAnimAvatar = 0f;

    private Personaje jugador1, jugador2;
    private float tiempoDesdeGolpe1 = 0;
    private float tiempoDesdeGolpe2 = 0;
    private final float duracionSonidoGolpe = 2f;
    private long idGolpe1 = -1, idGolpe2 = -1;

    private boolean juegoTerminado = false;

    private Stage stage;
    private Table tabla;
    private TextButton botonReiniciar;

    private OrthographicCamera camara;
    private Viewport viewport;
    private final int VIRTUAL_WIDTH = 800;
    private final int VIRTUAL_HEIGHT = 480;

    public PantallaJuego(MainGame game) {
        this.game = game;
        iniciarJuego();
    }

    private void iniciarJuego() {
        camara = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camara);
        camara.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camara.update();

        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();

        fondo = new Texture("fondo_juego.png");

        Texture avatarSheet1 = new Texture("avatar1.png");
        Texture avatarSheet2 = new Texture("avatar2.png");

        animAvatar1 = new Animation<>(0.3f, new TextureRegion(avatarSheet1));
        animAvatar2 = new Animation<>(0.3f, new TextureRegion(avatarSheet2));

        jugador1 = new Personaje("Heidy", Personaje.Dificultad.FACIL, 100f,
                Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.F,
                "ninja_atacar.png");

        jugador2 = new Personaje("Stefany", Personaje.Dificultad.FACIL, 300f,
                Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SPACE,
                "vaquera_atacar.png");

        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musica_fondo.mp3"));
        sonidoAtaque = Gdx.audio.newSound(Gdx.files.internal("sonido_ataque.wav"));
        musicaFondo.setLooping(true);
        musicaFondo.play();
        musicaFondo.setPosition(100f);

        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        tabla = new Table();
        tabla.top().left();
        tabla.setFillParent(true);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        botonReiniciar = new TextButton("Reiniciar", skin);
        botonReiniciar.addListener(event -> {
            if (botonReiniciar.isPressed()) {
                stage.clear();
                musicaFondo.stop();
                game.setScreen(new PantallaJuego(game));
            }
            return true;
        });

        tabla.add(botonReiniciar).padTop(150).padLeft(50);
        stage.addActor(tabla);
    }

    private void dibujarBarraEstilo(Personaje jugador, float x, float y, float anchoMax, boolean izquierda, Animation<TextureRegion> animAvatar) {
        float porcentaje = jugador.getPuntosDeVida() / 100f;
        float anchoVida = anchoMax * porcentaje;

        float avatarDrawWidth = 64f;
        float avatarDrawHeight = 64f;
        float espacio = 15f;
        float avatarX = izquierda ? x - avatarDrawWidth - espacio : x + anchoMax + espacio;
        float avatarY = y - 10;

        TextureRegion frameAvatar = animAvatar.getKeyFrame(tiempoAnimAvatar, true);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x - 2, y - 2, anchoMax + 4, 24);
        shapeRenderer.setColor(Color.SKY);
        shapeRenderer.rect(x, y, anchoMax, 20);
        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.rect(x, y, anchoVida, 20);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(avatarX, avatarY, avatarDrawWidth, avatarDrawHeight);
        shapeRenderer.end();

        batch.begin();
        batch.draw(frameAvatar, avatarX, avatarY, avatarDrawWidth, avatarDrawHeight);
        font.setColor(Color.WHITE);
        font.getData().setScale(1.2f);
        font.draw(batch, jugador.getNombre(),
                avatarX + (avatarDrawWidth / 2) - (jugador.getNombre().length() * 3),
                avatarY - 5);
        batch.end();
    }

    @Override
    public void render(float delta) {
        tiempoAnimAvatar += delta;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camara.update();

        batch.setProjectionMatrix(camara.combined);
        shapeRenderer.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(fondo, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        batch.end();

        float margen = 100f;
        float barraY = VIRTUAL_HEIGHT - 60;
        float barraAncho = 200f;

        dibujarBarraEstilo(jugador1, margen, barraY, barraAncho, true, animAvatar1);
        dibujarBarraEstilo(jugador2, VIRTUAL_WIDTH - margen - barraAncho, barraY, barraAncho, false, animAvatar2);

        batch.begin();
        if (!juegoTerminado) {
            jugador1.update(delta);
            jugador2.update(delta);

            if (Gdx.input.isKeyJustPressed(jugador1.getTeclaAtaque()) && jugador1.atacarSiColisiona(jugador2)) {
                if (tiempoDesdeGolpe1 <= 0) {
                    idGolpe1 = sonidoAtaque.play();
                    tiempoDesdeGolpe1 = duracionSonidoGolpe;
                }
            }

            if (Gdx.input.isKeyJustPressed(jugador2.getTeclaAtaque()) && jugador2.atacarSiColisiona(jugador1)) {
                if (tiempoDesdeGolpe2 <= 0) {
                    idGolpe2 = sonidoAtaque.play();
                    tiempoDesdeGolpe2 = duracionSonidoGolpe;
                }
            }

            if (!jugador1.estaVivo() || !jugador2.estaVivo()) {
                juegoTerminado = true;
            }
        } else {
            font.getData().setScale(2.5f);
            font.setColor(Color.RED);
            font.draw(batch, "¡KO!", VIRTUAL_WIDTH / 2f - 50, VIRTUAL_HEIGHT / 2f + 70);

            font.getData().setScale(1.4f);
            font.setColor(Color.WHITE);
            String ganador = jugador1.estaVivo() ? jugador1.getNombre() : jugador2.getNombre();
            font.draw(batch, ganador + " ha ganado la pelea.\nPresiona ENTER para reiniciar.", 50, 380);

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                stage.clear();
                musicaFondo.stop();
                game.setScreen(new PantallaJuego(game));
            }
        }

        tiempoDesdeGolpe1 = Math.max(0, tiempoDesdeGolpe1 - delta);
        tiempoDesdeGolpe2 = Math.max(0, tiempoDesdeGolpe2 - delta);

        if (tiempoDesdeGolpe1 == 0 && idGolpe1 != -1) {
            sonidoAtaque.stop(idGolpe1);
            idGolpe1 = -1;
        }
        if (tiempoDesdeGolpe2 == 0 && idGolpe2 != -1) {
            sonidoAtaque.stop(idGolpe2);
            idGolpe2 = -1;
        }

        jugador1.render(batch, delta);
        jugador2.render(batch, delta);

        font.setColor(Color.RED);
        if (tiempoDesdeGolpe1 > 0)
            font.draw(batch, "¡GOLPE!", jugador2.getPosicionX() + 10, jugador2.getPosicionY() + 80);
        if (tiempoDesdeGolpe2 > 0)
            font.draw(batch, "¡GOLPE!", jugador1.getPosicionX() + 10, jugador1.getPosicionY() + 80);

        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 0.4f);
        if (tiempoDesdeGolpe1 > 0)
            shapeRenderer.rect(jugador2.getPosicionX(), jugador2.getPosicionY(), 64, 64);
        if (tiempoDesdeGolpe2 > 0)
            shapeRenderer.rect(jugador1.getPosicionX(), jugador1.getPosicionY(), 64, 64);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void show() {}

    @Override public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
        musicaFondo.dispose();
        sonidoAtaque.dispose();
        jugador1.dispose();
        jugador2.dispose();
        fondo.dispose();
    }
}
