package frc.team449.robot2023.subsystems.outreach.intake

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.system.motor.WrappedMotor

class Intake(
  private val intakeMotor: WrappedMotor
) : SubsystemBase() {

  // Run intake motor forwards.
  fun runIntakeForward(): Command {
    return this.runOnce {intakeMotor.setVoltage(IntakeConstants.INTAKE_VOLTAGE)}
  }

  // Run intake motor in reverse.
  fun runIntakeReverse(): Command {
    return this.runOnce {intakeMotor.setVoltage(-IntakeConstants.INTAKE_VOLTAGE)}
  }

  // Stop intake motor.
  fun stopIntake(): Command {
    return this.runOnce {intakeMotor.stopMotor()}
  }
}
