package frc.team449.robot2022.drive

import edu.wpi.first.math.util.Units

object DriveConstants {

  /** Drive motor ports */
  const val DRIVE_MOTOR_FL = 1
  const val DRIVE_MOTOR_FR = 2
  const val DRIVE_MOTOR_BL = 3
  const val DRIVE_MOTOR_BR = 4

  /** Feed forward values for driving each module */
  const val DRIVE_KS = 0.15121
  const val DRIVE_KV = 2.3301
  const val DRIVE_KA = 0.37064

  /** PID gains for driving each module*/
  const val DRIVE_KP = 0.25
  const val DRIVE_KI = 0.0
  const val DRIVE_KD = 0.0

  /** Drive configuration */
  const val DRIVE_GEARING = 1 / 8.0
  val DRIVE_UPR = Math.PI * Units.inchesToMeters(6.0)
  const val MAX_LINEAR_SPEED = .5
  const val MAX_ROT_SPEED = 2.5
  const val MAX_ATTAINABLE_MODULE_SPEED = 1.0
  const val MAX_ACCEL = 5.0

  /** Controller Configurations */
  const val RATE_LIMIT = 1.5
  const val TRANSLATION_DEADBAND = .125
  const val ROTATION_DEADBAND = .125

  val CAM_NAME = "PhotonCam"

  val WHEELBASE = Units.inchesToMeters(21.426)
  val TRACKWIDTH = Units.inchesToMeters(21.000)
}
