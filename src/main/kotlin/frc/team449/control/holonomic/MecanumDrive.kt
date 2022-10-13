package frc.team449.control.holonomic

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds
import edu.wpi.first.math.kinematics.MecanumDriveKinematics
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
  override var pose: Pose2d,
  override val maxLinearSpeed: Double,
  override val maxRotSpeed: Double
): HolonomicDrive, SubsystemBase() {
  // 10.500 x, 10.713 y (outreach 2022)
  /*
    speed = sqrt(joystickX^2+joystickY^2) * maxSpeed
    theta = arctan(joystickY/joystickX)
    motors are numbers clockwise from top left
    motors fl,br = speed * sin(theta-45)
    motors fr,bl = speed * cos(theta-45)
   */

  private val kinematics = MecanumDriveKinematics(
    frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation
  )

  private var wheelSpeeds = MecanumDriveWheelSpeeds()

  override fun set(desiredSpeeds: ChassisSpeeds) {
    wheelSpeeds = kinematics.toWheelSpeeds(desiredSpeeds)
    wheelSpeeds.desaturate(this.maxLinearSpeed)
    // could be set instead of setVoltage
    frontLeftMotor.setVoltage(wheelSpeeds.frontLeftMetersPerSecond)
    frontRightMotor.setVoltage(wheelSpeeds.frontRightMetersPerSecond)
    backLeftMotor.setVoltage(wheelSpeeds.rearLeftMetersPerSecond)
    backRightMotor.setVoltage(wheelSpeeds.rearRightMetersPerSecond)
  }

  override fun stop() {
    this.set(ChassisSpeeds(0.0, 0.0, 0.0))
  }

  override fun periodic() {

  }

}