package frc.team449.robot2022.arm

import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.system.encoder.NEOEncoder
import frc.team449.system.motor.WrappedMotor
import frc.team449.system.motor.createSparkMax

class Arm(
  val armMotor: WrappedMotor,
  val pid: ProfiledPIDController
) : SubsystemBase() {
  var desiredAngle = 0.0
  fun get() = desiredAngle
  fun set(a: Double) { desiredAngle = a }

  override fun periodic() {
    val armPID = pid.calculate(armMotor.position, desiredAngle)
    armMotor.set(armPID)
  }

  companion object {
    fun createArm(): Arm {
      return Arm(
        createSparkMax("armMotor", ArmConstants.ARM_ID, NEOEncoder.creator(ArmConstants.ARM_UPR, ArmConstants.ARM_GEARING)),
        ProfiledPIDController(ArmConstants.ARM_KP, ArmConstants.ARM_KI, ArmConstants.ARM_KD, TrapezoidProfile.Constraints(ArmConstants.MAX_SPD, ArmConstants.MAX_ACC))
      )
    }
  }
}
