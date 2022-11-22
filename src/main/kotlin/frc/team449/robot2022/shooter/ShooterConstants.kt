package frc.team449.robot2022.shooter

object ShooterConstants {

  /** Shooter (gray) values. */
  const val SHOOTER_ID = 12
  const val SHOOTER_UPR = 1.0
  const val SHOOTER_GEARING = 1.0

  /** Shooter feed forward values. */
  const val SHOOTER_KS = 0.14967
  const val SHOOTER_KV = 0.12303
  const val SHOOTER_KA = 0.0071119

  /** Shooter PID controller values. */
  const val SHOOTER_KP = 0.0
  const val SHOOTER_KI = 0.0
  const val SHOOTER_KD = 0.0

  /** Feeder (green) values. */
  const val FEEDER_VOLTAGE = 5.0 // TODO - test for the values
  const val FEEDER_ID = 13
  const val FEEDER_UPR = 1.0
  const val FEEDER_GEARING = 1.0

  const val SHOOTER_VEL = 10.0 // TODO - test for the values
  const val DELTA_TIME = 0.02 // TODO - test for the values
  const val TOLERANCE = 1.0 // TODO - test for the values
}
