package com.irr310.server.engine.system;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.system.CelestialObject;
import com.irr310.common.world.system.Ship;

public interface SystemEngineObserver {
    void onDeployShip(Ship ship, TransformMatrix transform);

    void onDeployCelestialObject(CelestialObject celestialObject, TransformMatrix transform);
}
