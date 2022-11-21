package frc.team449.robot2022.shooter

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.system.motor.WrappedMotor

class Shooter(
  private val feedForward: SimpleMotorFeedforward,
  private val controller: PIDController,
  private val shooterMotor: WrappedMotor,
  private val feederMotor: WrappedMotor
) : SubsystemBase() {

  private var runShoot = false

  // Starts the shooter by changing runShoot to true.
  fun runShooter() {
    runShoot = true
  }

  // Stops the shooter by changing runShoot to false.
  fun stopShooter() {
    runShoot = false
  }

  // Uses PID and FF to calculate motor voltage.
  override fun periodic() {
    if (runShoot) {
      val pid = controller.calculate(shooterMotor.velocity, ShooterConstants.SHOOTER_VEL)
      val ff = feedForward.calculate(ShooterConstants.SHOOTER_VEL)

      shooterMotor.setVoltage(-(pid + ff))
      feederMotor.setVoltage(-(pid + ff))
    } else {
      shooterMotor.set(0.0)
    }
  }
}
