package org.gooseapple.core.collision;

import com.github.davidmoten.rtree2.Entry;
import com.github.davidmoten.rtree2.RTree;
import com.github.davidmoten.rtree2.geometry.Rectangle;
import org.gooseapple.core.event.EventHandler;
import org.gooseapple.core.event.EventListener;
import org.gooseapple.core.event.EventManager;
import org.gooseapple.core.event.events.CollisionEvent;
import org.gooseapple.core.event.events.TickEvent;
import org.gooseapple.core.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles the 2D physics part of the system
 */
public class PhysicsService implements EventListener {
    private static PhysicsService physicsService = new PhysicsService();
    private RTree<UUID, Rectangle> physicsEntities = RTree.create(); //RTree allow easy and computationally cheap base collision checking, before the AABB checks below
    private ConcurrentHashMap<UUID, PhysicsBody> activePhysicsBodiesMap = new ConcurrentHashMap<>();
    private double physicsSpeed = 70;

    public PhysicsService() {
        EventManager.getInstance().addListener(this);
    }

    public static PhysicsService get() {
        return physicsService;
    }

    public void add(PhysicsBody body) {
        activePhysicsBodiesMap.put(body.getID(), body);
    }

    public void remove(PhysicsBody body) {
        activePhysicsBodiesMap.remove(body.getID());
    }

    private boolean isActive(PhysicsBody body) {
        return body.getVelocity().length() > 0;
    }

    @EventHandler
    public void handleTick(TickEvent event) {
        double dt = event.getDeltaTime();

        for (var entry : activePhysicsBodiesMap.keySet()) {
            var physicsBody = activePhysicsBodiesMap.get(entry);

            if (physicsBody.isAffectedByGravity()) {
                physicsBody.getVelocity().add(new Vector2(0, physicsSpeed * (1/16d) * dt));
            }

            Vector2 delta = physicsBody.getVelocity().clone().multiply(dt * physicsSpeed);
            physicsBody.updateGeometry(delta);

        }

        RTree<UUID, Rectangle> newTree = RTree.create();

        for (var entry : activePhysicsBodiesMap.entrySet()) {
            UUID id = entry.getKey();
            PhysicsBody body = entry.getValue();
            newTree = newTree.add(id, body.toGeometry());
        }

        physicsEntities = newTree;

        runCollisionChecks(event);
    }

    private void runCollisionChecks(TickEvent event) {
        for (var entry : activePhysicsBodiesMap.keySet()) {

            var body =  activePhysicsBodiesMap.get(entry);

            if (body == null)
                continue; //fix case where deleted ones could still exist in this frame

            Rectangle geom = body.toGeometry();
            Iterator<Entry<UUID, Rectangle>> it = physicsEntities.search(geom).iterator();

            while (it.hasNext()) {
                Entry<UUID, Rectangle> otherEntry = it.next();
                UUID id = otherEntry.value();
                PhysicsBody other = activePhysicsBodiesMap.get(id);

                if (other != null && other != body &&
                        other.isCollisionEnabled() && body.isCollisionEnabled()) {
                    handleCollision(other, body);
                }
            }
        }
    }

    /**
     * CHeck if two AABB bound objects collide
     * @param a first physics body
     * @param b second physics body
     */
    private void handleCollision(PhysicsBody a, PhysicsBody b) {
        Vector2 aPos = a.getPosition();
        Vector2 aSize = a.getSize();
        Vector2 bPos = b.getPosition();
        Vector2 bSize = b.getSize();

        boolean overlapX = aPos.getX() + aSize.getX() > bPos.getX() && aPos.getX() < bPos.getX() + bSize.getX();
        boolean overlapY = aPos.getY() + aSize.getY() > bPos.getY() && aPos.getY() < bPos.getY() + bSize.getY();

        if (overlapX && overlapY) {
            CollisionEvent event = new CollisionEvent(a, b);
            event.dispatch();
        }
    }
}
