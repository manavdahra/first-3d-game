package com.vagumlabs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.UBJsonReader;
import com.vagumlabs.character.Character;
import com.vagumlabs.character.CharacterAnimation;

import java.util.ArrayList;
import java.util.List;

public class Application extends ApplicationAdapter implements InputProcessor {

    private ChaseCamera camera;

    private ModelBatch modelBatch;

    private UBJsonReader jsonReader = new UBJsonReader();
    private G3dModelLoader loader = new G3dModelLoader(jsonReader);

    private Model character;
    private Model tiles;

    private Character characterInstance;
    private List<ModelInstance> tileInstances;

    private Environment environment;

    private AnimationController idleController;
    private CharacterAnimation runningController;
    private AnimationController leftTurningController;
    private AnimationController rightTurningController;

    private boolean isKeyPressed;
    private int keyPressed;

    @Override
    public void create() {
        // environment carries info of lighting, shadows and other details except the main objects
        environment = new Environment();
        // ambient light is the light reflected from everywhere
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1));

        // draw in batches for faster rendering
        modelBatch = new ModelBatch();

        // to load idle from assets
        character = loader.loadModel(Gdx.files.getFileHandle("character/character.g3db", Files.FileType.Internal));
        // model instance is an implementation of archetype -> model
        characterInstance = new Character(character, new Vector3(0f, 0f, 0f));
        float characterHeight = characterInstance.getBoundingBox().getHeight();
        Vector3 characterPosition = new Vector3(characterInstance.getPosition());

        createFloor();

        // 3d camera that shows things far away smaller
        // field of view tells how wide angle the view will be
        camera = new ChaseCamera(75f, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());

        camera.position.set(0f, characterHeight, -characterHeight);
        camera.lookAt(characterPosition.add(0f, (3 * characterInstance.getBoundingBox().getHeight())/4, 0f));
        camera.near = 0.1f;
        camera.far = 600f;

        idleController = new CharacterAnimation(characterInstance, camera, CharacterAnimation.AnimationType.CharacterIdle);
        runningController = new CharacterAnimation(characterInstance, camera, CharacterAnimation.AnimationType.CharacterSlowRun);
        leftTurningController = new CharacterAnimation(characterInstance, camera, CharacterAnimation.AnimationType.CharacterTurnLeft);
        rightTurningController = new CharacterAnimation(characterInstance, camera, CharacterAnimation.AnimationType.CharacterTurnRight);

        Gdx.input.setInputProcessor(this);
    }

    public void createFloor() {
        tiles = loader.loadModel(Gdx.files.getFileHandle("world/tiles.g3db", Files.FileType.Internal));
        BoundingBox boundingBox = new BoundingBox();
        boundingBox = tiles.calculateBoundingBox(boundingBox);
        float tileLength = boundingBox.getWidth();
        for (float x = -tileLength; x <= tileLength; x = x + tileLength) {
            for (float z = -tileLength; z <= tileLength; z = z + tileLength) {
                if (tileInstances == null) {
                    tileInstances = new ArrayList<>();
                }
                Vector3 centre = new Vector3(x, 0, z);
                Gdx.app.log("INFO", "creating tiles at location: " + centre);
                tileInstances.add(new ModelInstance(tiles, centre));
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (isKeyPressed) {
            resolveAnimations();
        } else {
            idleController.update(Gdx.graphics.getDeltaTime());
        }
        camera.update();

        modelBatch.begin(camera);
        modelBatch.render(characterInstance, environment);
        modelBatch.render(tileInstances, environment);
        modelBatch.end();
    }

    private void resolveAnimations() {
        if (keyPressed == Input.Keys.LEFT) {
            leftTurningController.update(Gdx.graphics.getDeltaTime());
        } else if (keyPressed == Input.Keys.RIGHT) {
            rightTurningController.update(Gdx.graphics.getDeltaTime());
        } else if (keyPressed == Input.Keys.UP) {
            runningController.update(Gdx.graphics.getDeltaTime());
        } else if (keyPressed == Input.Keys.DOWN) {
            idleController.update(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        character.dispose();
        tiles.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        isKeyPressed = true;
        keyPressed = keycode;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        isKeyPressed = false;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
