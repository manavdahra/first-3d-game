package com.vagumlabs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

public class Stats extends Stage {

    private Viewport viewport;
    private Stage stage;
    private Label playerPosX;
    private Label playerPosY;
    private Label playerPosZ;
    private Vector3 trans = new Vector3();

    public Stats(Batch batch) {

        float vpW = Gdx.graphics.getWidth();
        float vpH = Gdx.graphics.getHeight();

        OrthographicCamera orthographicCamera = new OrthographicCamera(vpW, vpH);

        viewport = new FitViewport(vpW, vpH, orthographicCamera);
        stage = new Stage(viewport, batch);

        BitmapFont font = new BitmapFont();
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.fontColor = Color.BLACK;
        textStyle.font = font;

        playerPosX = new Label("PosX: ", textStyle);
        playerPosX.setFontScale(1f, 1f);

        playerPosY = new Label("PosY: ", textStyle);
        playerPosY.setFontScale(1f, 1f);

        playerPosZ = new Label("PosZ: ", textStyle);
        playerPosZ.setFontScale(1f, 1f);

        final Table statsTable = new Table();
        statsTable.setDebug(true);
        statsTable.top().right();

        // player stats
        statsTable.row().pad(5, 5, 5, 5);
        statsTable.setFillParent(true);

        statsTable.add(playerPosX).left();
        statsTable.add(playerPosY).center();
        statsTable.add(playerPosZ).right();

        stage.addActor(statsTable);
    }

    public void draw(ModelInstance player) {
        player.transform.getTranslation(trans);
        stage.act(Gdx.graphics.getDeltaTime());
        playerPosX.setText(String.format(Locale.US,"%.2f", trans.x));
        playerPosY.setText(String.format(Locale.US,"%.2f", trans.y));
        playerPosY.setText(String.format(Locale.US,"%.2f", trans.z));

        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    public void dispose() {
        stage.dispose();
    }
}
