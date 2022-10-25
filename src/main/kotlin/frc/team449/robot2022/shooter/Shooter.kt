package frc.team449.robot2022.shooter

import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.system.motor.WrappedMotor

class Shooter(
  val shooterMotor: WrappedMotor
) : SubsystemBase() {

  // Run shooter motor forward
  fun runShooter() {
    shooterMotor.set(ShooterConstants.shooterVoltage)
  }

  // Stop shooter motor
  fun stopShooter() {
    shooterMotor.set(0.0)
  }
}
