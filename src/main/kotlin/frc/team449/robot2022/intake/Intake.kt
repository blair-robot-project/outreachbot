package frc.team449.robot2022.intake

import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.system.motor.WrappedMotor

class Intake(
  private val intakeMotor: WrappedMotor
) : SubsystemBase() {

  // Run intake motor forwards
  fun runIntakeForward() {
    intakeMotor.set(IntakeConstants.intakeVoltage)
  }

  // Run intake motor reverse
  fun runIntakeReverse() {
    intakeMotor.set(-IntakeConstants.intakeVoltage)
  }

  // Stop intake motor
  fun stopIntake() {
    intakeMotor.set(0.0)
  }
}
