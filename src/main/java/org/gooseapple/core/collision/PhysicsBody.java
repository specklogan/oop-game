package org.gooseapple.core.collision;

import com.github.davidmoten.rtree2.geometry.Geometries;
import org.gooseapple.core.math.Vector2;
import org.gooseapple.core.render.Rectangle;

import java.util.UUID;

public class PhysicsBody {
    private Rectangle parent;
    private com.github.davidmoten.rtree2.geometry.Rectangle geometry;
    private Vector2 velocity;
    private Vector2 position;
    private Vector2 size;
    private boolean collisionEnabled = false;
    private UUID bodyID;
    private boolean isAffectedByGravity = false;

    public PhysicsBody(Rectangle parent, Vector2 position) {
        this.parent = parent;
        this.bodyID = UUID.randomUUID();
        this.size = parent.getSize().clone();
        this.position = position;
        this.velocity = new Vector2(0,0);
        updateGeometry();
        PhysicsService.get().add(this);
    }

    public com.github.davidmoten.rtree2.geometry.Rectangle getOldGeometry() {
        return geometry;
    }

    public void updateGeometry(Vector2 deltaChange) {
        geometry = Geometries.rectangle(
                position.getX(),
                position.getY(),
                position.getX() + size.getX(),
                position.getY() + size.getY()
        );
        position.add(deltaChange);
    }

    public void updateGeometry() {
        geometry = Geometries.rectangle(
                position.getX(),
                position.getY(),
                position.getX() + size.getX(),
                position.getY() + size.getY()
        );
    }

    public void onRemove() {
        PhysicsService.get().remove(this);
    }

    public Rectangle getParent() {
        return parent;
    }

    public boolean isCollisionEnabled() {
        return collisionEnabled;
    }

    public void setCollisionEnabled(boolean collisionEnabled) {
        this.collisionEnabled = collisionEnabled;
    }

    public boolean isAffectedByGravity() {
        return isAffectedByGravity;
    }

    public void setAffectedByGravity(boolean affectedByGravity) {
        isAffectedByGravity = affectedByGravity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public com.github.davidmoten.rtree2.geometry.Rectangle toGeometry() {
        return geometry;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        updateGeometry();
    }

    public Vector2 getSize() {
        return size;
    }

    public void setCollisionSize(Vector2 size) {
        this.size = size;
        updateGeometry();
    }

    public UUID getID() {
        return bodyID;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}
