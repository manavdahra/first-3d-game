package com.vagumlabs.geometry;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class Util {
    /**
     * Idea is to first find projection of Vector on normal of the plane then using vector arithmetic
     * @param v Vector for which we want projection on plane
     * @param n Vector projection on normal of the plane
     * @return v - n
     */
    public static Vector3 prjOnPlane(Vector3 v, Vector3 n) {
        float dot = v.dot(n);
        Vector3 vPerp = n.scl(dot);
        Vector3 vParl = new Vector3(v);
        return vParl.sub(vPerp);
    }

    public static Vector3 intersectionRayPlane(Vector3 origin, Vector3 direction, Vector3 normal) {
        Vector3 intersection = new Vector3();
        if (Intersector.intersectRayPlane(new Ray(origin, direction), new Plane(normal, 0), intersection)) {
            return intersection;
        } else {
            return null;
        }
    }
}
