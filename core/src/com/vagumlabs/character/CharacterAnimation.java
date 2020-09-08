package com.vagumlabs.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.vagumlabs.ChaseCamera;
import com.vagumlabs.geometry.Util;

public class CharacterAnimation extends AnimationController {

    private ChaseCamera camera;
    private Character target;
    private Vector3 translate = new Vector3();
    private Vector3 normal = new Vector3();
    private AnimationType animationType;
    private Vector3 yAxis = new Vector3(0, 1, 0);

    public enum AnimationType {
        CharacterIdle("Character|idle"),
        CharacterSlowRun("Character|slow-run"),
        CharacterSpeedRun("Character|speed-right"),
        CharacterTurnLeft("Character|turn-left"),
        CharacterTurnRight("Character|turn-right");

        private String animationType;

        AnimationType(String animationType) {
            this.animationType = animationType;
        }

        public String getAnimationType() {
            return animationType;
        }

        public void setAnimationType(String animationType) {
            this.animationType = animationType;
        }
    }

    /**
     * Construct a new AnimationController.
     *
     * @param target The {@link Character} on which the animations will be performed.
     */
    public CharacterAnimation(Character target, ChaseCamera camera, AnimationType animationType) {
        super(target);
        this.camera = camera;
        this.target = target;
        this.animationType = animationType;
        this.setAnimation(animationType.getAnimationType(), -1);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        switch (this.animationType) {
            case CharacterIdle:
                // do nothing
                break;
            case CharacterSlowRun:
                normal.set(0f, 1f, 0f);
                Vector3 direction = Util.prjOnPlane(camera.direction, normal);
                translate.set(direction.nor()).scl(5);
                target.translate(translate);
                camera.position.add(translate);
                break;
            case CharacterTurnLeft:
                rotateAnimate(1f);
                break;
            case CharacterTurnRight:
                rotateAnimate(-1f);
                break;
        }
    }

    private void rotateAnimate(float deg) {
        target.rotate(new Quaternion(yAxis, deg));
        camera.rotateAround(target.getPosition(), yAxis, deg);
    }

    @Override
    public AnimationDesc setAnimation(String id, int loopCount) {
        return super.setAnimation(id, loopCount);
    }
}
