package frc.team449.robot2022.shooter

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.system.motor.WrappedMotor

class Shooter(
  private val feedForward: SimpleMotorFeedforward,
  private val controller: PIDController,

  val shooterMotor: WrappedMotor

) : SubsystemBase() {

  // Run shooter motor forward
  fun runShooter() {
    shooterMotor.setVoltage(controller.calculate(shooterMotor.velocity, ShooterConstants.desiredShooterVoltage) + feedForward.calculate(shooterMotor.velocity, ShooterConstants.desiredShooterVoltage, ShooterConstants.deltaTime))
  }

  // Stop shooter motor
  fun stopShooter() {
    shooterMotor.set(0.0)
  }
}
