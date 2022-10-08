package frc.team449.control.holonomic

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds
import edu.wpi.first.math.kinematics.MecanumDriveKinematics
import edu.wpi.first.math.kinematics.MecanumDriveOdometry
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.system.AHRS
import frc.team449.system.motor.WrappedMotor

class MecanumDrive(
  private val frontLeftMotor: WrappedMotor,
  private val frontRightMotor: WrappedMotor,
  private val backLeftMotor: WrappedMotor,
  private val backRightMotor: WrappedMotor,
  frontLeftLocation: Translation2d,
  frontRightLocation: Translation2d,
  backLeftLocation: Translation2d,
  backRightLocation: Translation2d,
  val ahrs: AHRS,
  override var pose: Pose2d,
  override val maxLinearSpeed: Double,
  override val maxRotSpeed: Double
): HolonomicDrive, SubsystemBase() {

  init {
    ahrs.calibrate()
    ahrs.reset()
  }

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

  private val odometry = MecanumDriveOdometry(this.kinematics, -ahrs.heading)

  private var desiredSpeeds = ChassisSpeeds()

  override fun set(desiredSpeeds: ChassisSpeeds) {
    this.desiredSpeeds = desiredSpeeds
  }

  override fun stop() {
    this.desiredSpeeds = ChassisSpeeds(0.0, 0.0, 0.0)
    this.set(desiredSpeeds)
  }

  override fun periodic() {
    val mecanumDriveModuleStates: MecanumDriveWheelSpeeds = this.kinematics.toWheelSpeeds(this.desiredSpeeds)
  }

}