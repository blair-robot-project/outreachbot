package frc.team449.robot2023.subsystems.outreach.intake

import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.system.encoder.NEOEncoder
import frc.team449.system.motor.WrappedMotor
import frc.team449.system.motor.createSparkMax

class Intake(
  private val intakeMotor: WrappedMotor
) : SubsystemBase() {

  var isRunning = false
  var isEjecting = false

  // Run intake motor forwards.
  fun runIntakeForward() {
    intakeMotor.setVoltage(IntakeConstants.INTAKE_VOLTAGE)
    isRunning = true
  }

  // Run intake motor in reverse.
  fun runIntakeReverse() {
    intakeMotor.setVoltage(-IntakeConstants.INTAKE_VOLTAGE)
    isEjecting = true
  }

  // Stop intake motor.
  fun stopIntake() {
    intakeMotor.stopMotor()
    isRunning = false
    isEjecting = false
  }

  companion object {
    fun createIntake(): Intake {
      val intakeMotor = createSparkMax(
        "Intake",
        IntakeConstants.INTAKE_ID,
        NEOEncoder.creator(IntakeConstants.INTAKE_UPR, IntakeConstants.INTAKE_GEARING)
      )

      return Intake(intakeMotor)
    }
  }
}
