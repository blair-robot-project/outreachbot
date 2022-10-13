package frc.team449.control.holonomic

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.MecanumDriveKinematics
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.system.motor.WrappedMotor

class MecanumDrive(
  private val frontLeftMotor: WrappedMotor,
  private val frontRightMotor: WrappedMotor,
  private val backLeftMotor: WrappedMotor,
  private val backRightMotor: WrappedMotor,
  private val frontLeftLocation: Translation2d,
  private val frontRightLocation: Translation2d,
  private val backLeftLocation: Translation2d,
  private val backRightLocation: Translation2d,
  val ahrs: AHRS,
  override var pose: Pose2d,
  override val maxLinearSpeed: Double,
  override val maxRotSpeed: Double,
  private val feedForward: SimpleMotorFeedforward,
  private val controller: PIDController
) : HolonomicDrive, SubsystemBase() {
  // 10.500 x, 10.713 y (outreach 2022) (in)

  private val kinematics = MecanumDriveKinematics(
    frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation
  )

  private var desiredWheelSpeeds = MecanumDriveWheelSpeeds()
  private var disabledChassisSpeeds = ChassisSpeeds(0.0, 0.0, 0.0)

  override fun set(desiredSpeeds: ChassisSpeeds) {
    desiredWheelSpeeds = kinematics.toWheelSpeeds(desiredSpeeds)
    desiredWheelSpeeds.desaturate(this.maxLinearSpeed)
  }

  override fun stop() {
    this.set(disabledChassisSpeeds)
  }

  override fun periodic() {
    val FLPID = controller.calculate(frontLeftMotor.velocity)
    val FRPID = controller.calculate(frontRightMotor.velocity)
    val BLPID = controller.calculate(backLeftMotor.velocity)
    val BRPID = controller.calculate(backRightMotor.velocity)

    val FLFF = feedForward.calculate(frontLeftMotor.velocity, desiredWheelSpeeds.frontLeftMetersPerSecond, 0.02)
    val FRFF = feedForward.calculate(frontRightMotor.velocity, desiredWheelSpeeds.frontRightMetersPerSecond, 0.02)
    val BLFF = feedForward.calculate(backLeftMotor.velocity, desiredWheelSpeeds.rearLeftMetersPerSecond, 0.02)
    val BRFF = feedForward.calculate(backRightMotor.velocity, desiredWheelSpeeds.rearRightMetersPerSecond, 0.02)

    frontLeftMotor.setVoltage(FLPID + FLFF)
    frontRightMotor.setVoltage(FRPID + FRFF)
    backLeftMotor.setVoltage(BLPID + BLFF)
    backRightMotor.setVoltage(BRPID + BRFF)
  }
}
