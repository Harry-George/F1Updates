package com.f1updates;

class CarMotionData {
    static public int GetSize() {
        return
                4 + //            float         m_worldPositionX;           // World space X position
                        4 + //            float         m_worldPositionY;           // World space Y position
                        4 + //            float         m_worldPositionZ;           // World space Z position
                        4 + //            float         m_worldVelocityX;           // Velocity in world space X
                        4 + //            float         m_worldVelocityY;           // Velocity in world space Y
                        4 + //            float         m_worldVelocityZ;           // Velocity in world space Z
                        2 + //            int16         m_worldForwardDirX;         // World space forward X direction (normalised)
                        2 + //            int16         m_worldForwardDirY;         // World space forward Y direction (normalised)
                        2 + //            int16         m_worldForwardDirZ;         // World space forward Z direction (normalised)
                        2 + //            int16         m_worldRightDirX;           // World space right X direction (normalised)
                        2 + //            int16         m_worldRightDirY;           // World space right Y direction (normalised)
                        2 + //            int16         m_worldRightDirZ;           // World space right Z direction (normalised)
                        4 + //            float         m_gForceLateral;            // Lateral G-Force component
                        4 + //            float         m_gForceLongitudinal;       // Longitudinal G-Force component
                        4 + //            float         m_gForceVertical;           // Vertical G-Force component
                        4 + //            float         m_yaw;                      // Yaw angle in radians
                        4 + //            float         m_pitch;                    // Pitch angle in radians
                        4; //            float         m_roll;                     // Roll angle in radians

    }
}

public class Motion {

    public int GetSize() {

        return CarMotionData.GetSize() * 22 +
                //            CarMotionData   m_carMotionData[22];    	// Data for all cars on track
//
//            // Extra player car ONLY data
                4 +//            float         m_suspensionPosition[4];      // Note: All wheel arrays have the following order:
                4 +//            float         m_suspensionVelocity[4];      // RL, RR, FL, FR
                4 +//            float         m_suspensionAcceleration[4];	// RL, RR, FL, FR
                4 +//            float         m_wheelSpeed[4];           	// Speed of each wheel
                4 +//            float         m_wheelSlip[4];               // Slip ratio for each wheel
                4 +//            float         m_localVelocityX;         	// Velocity in local space
                4 +//            float         m_localVelocityY;         	// Velocity in local space
                4 +//            float         m_localVelocityZ;         	// Velocity in local space
                4 +//            float         m_angularVelocityX;		    // Angular velocity x-component
                4 +//            float         m_angularVelocityY;           // Angular velocity y-component
                4 +//            float         m_angularVelocityZ;           // Angular velocity z-component
                4 +//            float         m_angularAccelerationX;       // Angular velocity x-component
                4 +//            float         m_angularAccelerationY;	    // Angular velocity y-component
                4 +//            float         m_angularAccelerationZ;       // Angular velocity z-component
                4;//            float         m_frontWheelsAngle;           // Current front wheels angle in radians

    }
}
