/*
 * Java port of Bullet (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * Bullet Continuous Collision Detection and Physics Library
 * Copyright (c) 2003-2008 Erwin Coumans  http://www.bulletphysics.com/
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose, 
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package com.bulletphysics.dynamics.vehicle;

import javax.vecmath.Vector3d;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

/**
 * WheelInfo contains information per wheel about friction and suspension.
 * 
 * @author jezek2
 */
public class WheelInfo {

	//protected final BulletStack stack = BulletStack.get();
	
	public final RaycastInfo raycastInfo = new RaycastInfo();

	public final Transform worldTransform = new Transform();
	
	public final Vector3d chassisConnectionPointCS = new Vector3d(); // const
	public final Vector3d wheelDirectionCS = new Vector3d(); // const
	public final Vector3d wheelAxleCS = new Vector3d(); // const or modified by steering
	public double suspensionRestLength1; // const
	public double maxSuspensionTravelCm;
	public double wheelsRadius; // const
	public double suspensionStiffness; // const
	public double wheelsDampingCompression; // const
	public double wheelsDampingRelaxation; // const
	public double frictionSlip;
	public double steering;
	public double rotation;
	public double deltaRotation;
	public double rollInfluence;

	public double engineForce;

	public double brake;
	
	public boolean bIsFrontWheel;
	
	public Object clientInfo; // can be used to store pointer to sync transforms...

	public double clippedInvContactDotSuspension;
	public double suspensionRelativeVelocity;
	// calculated by suspension
	public double wheelsSuspensionForce;
	public double skidInfo;
	
	public WheelInfo(WheelInfoConstructionInfo ci) {
		suspensionRestLength1 = ci.suspensionRestLength;
		maxSuspensionTravelCm = ci.maxSuspensionTravelCm;

		wheelsRadius = ci.wheelRadius;
		suspensionStiffness = ci.suspensionStiffness;
		wheelsDampingCompression = ci.wheelsDampingCompression;
		wheelsDampingRelaxation = ci.wheelsDampingRelaxation;
		chassisConnectionPointCS.set(ci.chassisConnectionCS);
		wheelDirectionCS.set(ci.wheelDirectionCS);
		wheelAxleCS.set(ci.wheelAxleCS);
		frictionSlip = ci.frictionSlip;
		steering = 0f;
		engineForce = 0f;
		rotation = 0f;
		deltaRotation = 0f;
		brake = 0f;
		rollInfluence = 0.1f;
		bIsFrontWheel = ci.bIsFrontWheel;
	}
	
	public double getSuspensionRestLength() {
		return suspensionRestLength1;
	}

	public void updateWheel(RigidBody chassis, RaycastInfo raycastInfo) {
		if (raycastInfo.isInContact) {
			double project = raycastInfo.contactNormalWS.dot(raycastInfo.wheelDirectionWS);
			Vector3d chassis_velocity_at_contactPoint = new Vector3d();
			Vector3d relpos = new Vector3d();
			relpos.sub(raycastInfo.contactPointWS, chassis.getCenterOfMassPosition(new Vector3d()));
			chassis.getVelocityInLocalPoint(relpos, chassis_velocity_at_contactPoint);
			double projVel = raycastInfo.contactNormalWS.dot(chassis_velocity_at_contactPoint);
			if (project >= -0.1f) {
				suspensionRelativeVelocity = 0f;
				clippedInvContactDotSuspension = 1f / 0.1f;
			}
			else {
				double inv = -1f / project;
				suspensionRelativeVelocity = projVel * inv;
				clippedInvContactDotSuspension = inv;
			}
		}
		else {
			// Not in contact : position wheel in a nice (rest length) position
			raycastInfo.suspensionLength = getSuspensionRestLength();
			suspensionRelativeVelocity = 0f;
			raycastInfo.contactNormalWS.negate(raycastInfo.wheelDirectionWS);
			clippedInvContactDotSuspension = 1f;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	public static class RaycastInfo {
		// set by raycaster
		public final Vector3d contactNormalWS = new Vector3d(); // contactnormal
		public final Vector3d contactPointWS = new Vector3d(); // raycast hitpoint
		public double suspensionLength;
		public final Vector3d hardPointWS = new Vector3d(); // raycast starting point
		public final Vector3d wheelDirectionWS = new Vector3d(); // direction in worldspace
		public final Vector3d wheelAxleWS = new Vector3d(); // axle in worldspace
		public boolean isInContact;
		public Object groundObject; // could be general void* ptr
	}
	
}
