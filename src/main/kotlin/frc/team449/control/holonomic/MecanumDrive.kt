package frc.team449.control.holonomic

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.MecanumDriveKinematics
import edu.wpi.first.math.kinematics.MecanumDriveOdometry
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.robot2022.drive.DriveConstants
import frc.team449.system.AHRS
import frc.team449.system.encoder.NEOEncoder
import frc.team449.system.motor.WrappedMotor
import frc.team449.system.motor.createSparkMax
import io.github.oblarg.oblog.annotations.Log

/**
 * @param frontLeftMotor the front left motor
 * @param frontRightMotor the front right motor
 * @param backLeftMotor the back left motor
 * @param backRightMotor the back right motor
 * @param frontLeftLocation the offset of the front left wheel to the center of the robot
 * @param frontRightLocation the offset of the front right wheel to the center of the robot
 * @param backLeftLocation the offset of the back left wheel to the center of the robot
 * @param backRightLocation the offset of the back right wheel to the center of the robot
 * @param maxLinearSpeed the maximum translation speed of the chassis.
 * @param maxRotSpeed the maximum rotation speed of the chassis
 * @param feedForward the SimpleMotorFeedforward for mecanum
 * @param controller the PIDController for the robot
 */
class MecanumDrive(
  private val frontLeftMotor: WrappedMotor,
  private val frontRightMotor: WrappedMotor,
  private val backLeftMotor: WrappedMotor,
  private val backRightMotor: WrappedMotor,
  frontLeftLocation: Translation2d,
  frontRightLocation: Translation2d,
  backLeftLocation: Translation2d,
  backRightLocation: Translation2d,
  private val ahrs: AHRS,
  override val maxLinearSpeed: Double,
  override val maxRotSpeed: Double,
  private val feedForward: SimpleMotorFeedforward,
  private val controller: PIDController
) : HolonomicDrive, SubsystemBase() {
  init {
    controller.reset()
    ahrs.calibrate()
    ahrs.reset()
  }

  // 10.500 x, 10.713 y (outreach 2022) (in.) (top right) (y is horizontal axis)

  private val kinematics = MecanumDriveKinematics(
    frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation
  )

  private val odometry = MecanumDriveOdometry(kinematics, -ahrs.heading)

  override val heading: Rotation2d
    get() {
      return -ahrs.heading
    }

  override var pose: Pose2d
    @Log.ToString
    get() {
      return this.odometry.poseMeters
    }
    set(value) {
      this.odometry.resetPosition(value, heading)
    }

  private var desiredWheelSpeeds = MecanumDriveWheelSpeeds()
  private var prevTime = Double.NaN
  override fun set(desiredSpeeds: ChassisSpeeds) {
    desiredWheelSpeeds = kinematics.toWheelSpeeds(desiredSpeeds)
    desiredWheelSpeeds.desaturate(DriveConstants.MAX_ATTAINABLE_MODULE_SPEED)
  }

  override fun stop() {
    this.set(ChassisSpeeds(0.0, 0.0, 0.0))
  }

  override fun periodic() {
    val currTime = Timer.getFPGATimestamp()
    if (prevTime.isNaN()) {
      prevTime = currTime - 0.02
    }
    val dtSec = currTime - prevTime

    val frontLeftPID = controller.calculate(frontLeftMotor.velocity, desiredWheelSpeeds.frontLeftMetersPerSecond)
    val frontRightPID = controller.calculate(frontRightMotor.velocity, desiredWheelSpeeds.frontRightMetersPerSecond)
    val backLeftPID = controller.calculate(backLeftMotor.velocity, desiredWheelSpeeds.rearLeftMetersPerSecond)
    val backRightPID = controller.calculate(backRightMotor.velocity, desiredWheelSpeeds.rearRightMetersPerSecond)

    val frontLeftFF = feedForward.calculate(
      frontLeftMotor.velocity,
      desiredWheelSpeeds.frontLeftMetersPerSecond,
      dtSec
    )
    val frontRightFF = feedForward.calculate(
      frontRightMotor.velocity,
      desiredWheelSpeeds.frontRightMetersPerSecond,
      dtSec
    )
    val backLeftFF = feedForward.calculate(
      backLeftMotor.velocity,
      desiredWheelSpeeds.rearLeftMetersPerSecond,
      dtSec
    )
    val backRightFF = feedForward.calculate(
      backRightMotor.velocity,
      desiredWheelSpeeds.rearRightMetersPerSecond,
      dtSec
    )

    frontLeftMotor.setVoltage(frontLeftPID + frontLeftFF)
    frontRightMotor.setVoltage(frontRightPID + frontRightFF)
    backLeftMotor.setVoltage(backLeftPID + backLeftFF)
    backRightMotor.setVoltage(backRightPID + backRightFF)

    this.odometry.update(
      heading,
      MecanumDriveWheelSpeeds(
        frontLeftMotor.velocity,
        frontRightMotor.velocity,
        backLeftMotor.velocity,
        backRightMotor.velocity
      )
    )

    prevTime = currTime
  }

  companion object {
    /** Create a new Mecanum Drive from DriveConstants */
    fun createMecanum(ahrs: AHRS): MecanumDrive {
      return MecanumDrive(
        createSparkMax("frontLeft", DriveConstants.DRIVE_MOTOR_FL, NEOEncoder.creator(DriveConstants.DRIVE_UPR, DriveConstants.DRIVE_GEARING)),
        createSparkMax("frontRight", DriveConstants.DRIVE_MOTOR_FR, NEOEncoder.creator(DriveConstants.DRIVE_UPR, DriveConstants.DRIVE_GEARING)),
        createSparkMax("backLeft", DriveConstants.DRIVE_MOTOR_BL, NEOEncoder.creator(DriveConstants.DRIVE_UPR, DriveConstants.DRIVE_GEARING)),
        createSparkMax("backRight", DriveConstants.DRIVE_MOTOR_BR, NEOEncoder.creator(DriveConstants.DRIVE_UPR, DriveConstants.DRIVE_GEARING)),
        Translation2d(DriveConstants.WHEELBASE / 2, DriveConstants.TRACKWIDTH / 2),
        Translation2d(DriveConstants.WHEELBASE / 2, -DriveConstants.TRACKWIDTH / 2),
        Translation2d(-DriveConstants.WHEELBASE / 2, DriveConstants.TRACKWIDTH / 2),
        Translation2d(-DriveConstants.WHEELBASE / 2, -DriveConstants.TRACKWIDTH / 2),
        ahrs,
        DriveConstants.MAX_LINEAR_SPEED,
        DriveConstants.MAX_ROT_SPEED,
        SimpleMotorFeedforward(DriveConstants.DRIVE_KS, DriveConstants.DRIVE_KV, DriveConstants.DRIVE_KA),
        PIDController(DriveConstants.DRIVE_KP, DriveConstants.DRIVE_KI, DriveConstants.DRIVE_KD)
      )
    }
  }
}
