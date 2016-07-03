package com.shadyaardvark.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.shadyaardvark.GameBoard;
import com.shadyaardvark.Settings;

public class LocalGame implements Screen {
    private Game game;
    private OrthographicCamera camera;
    private GameBoard map;

    public LocalGame(Game game) {
        this.game = game;

        map = new GameBoard();
        //TODO: make number of players configurable
        map.createMap(Settings.MAP_WIDTH, Settings.MAP_HEIGHT, 5);

        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                map.getHexWidth() * (Settings.MAP_WIDTH + 1),
                map.getHexHeight() * (Settings.MAP_HEIGHT + 1));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.justTouched()) {
            map.createMap(Settings.MAP_WIDTH, Settings.MAP_HEIGHT, 5);
        }

        camera.update();
        map.render(camera);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
