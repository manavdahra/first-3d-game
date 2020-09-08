package com.vagumlabs.character;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Character extends ModelInstance {

    private Vector3 scale;
    private BoundingBox boundingBox = new BoundingBox();

    public Character(Model model, Vector3 position) {
        super(model, position);
        this.scale = new Vector3();
        boundingBox = this.calculateBoundingBox(boundingBox);
    }

    public Vector3 getPosition() {
        Vector3 position = new Vector3();
        this.transform.getTranslation(position);
        return position;
    }

    public void translate(Vector3 trn) {
        this.transform.trn(trn);
    }

    public Quaternion getRotation() {
        Quaternion rot = new Quaternion();
        this.transform.getRotation(rot);
        return rot;
    }

    public void rotate(Quaternion rotation) {
        this.transform.rotate(rotation);
    }

    public Vector3 getScale() {
        return scale;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
}
