package frc.team449.control.holonomic

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.MecanumDriveKinematics
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.system.motor.WrappedMotor

/**
 * @param frontLeftMotor the front left motor
 * @param frontRightMotor the front right motor
 * @param backLeftMotor the back left motor
 * @param backRightMotor the back right motor
 * @param frontLeftLocation the offset of the front left wheel to the center of the robot
 * @param frontRightLocation the offset of the front right wheel to the center of the robot
 * @param backLeftLocation the offset of the back left wheel to the center of the robot
 * @param backRightLocation the offset of the back right wheel to the center of the robot
 * @param pose the (x,y) position of the robot (unused i think)
 * @param maxLinearSpeed the maximum translation speed of the chassis.
 * @param maxRotSpeed the maximum rotation speed of the chassis
 * @param feedForward the SimpleMotorFeedforward for the robot
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
  override var pose: Pose2d,
  override val maxLinearSpeed: Double,
  override val maxRotSpeed: Double,
  private val feedForward: SimpleMotorFeedforward,
  private val controller: PIDController
) : HolonomicDrive, SubsystemBase() {
  init {
    controller.reset()
  }

  // 10.500 x, 10.713 y (outreach 2022) (in.) (top right) (y is horizontal axis)

  private val kinematics = MecanumDriveKinematics(
    frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation
  )

  private var desiredWheelSpeeds = MecanumDriveWheelSpeeds()
  private var disabledChassisSpeeds = ChassisSpeeds(0.0, 0.0, 0.0)
  private var prevTime = Double.NaN
  override fun set(desiredSpeeds: ChassisSpeeds) {
    desiredWheelSpeeds = kinematics.toWheelSpeeds(desiredSpeeds)
    desiredWheelSpeeds.desaturate(this.maxLinearSpeed)
  }

  override fun stop() {
    this.set(disabledChassisSpeeds)
  }

  override fun periodic() {
    val currTime = Timer.getFPGATimestamp()
    if (prevTime.isNaN()) {
      prevTime = currTime - 0.02
    }
    val dtSec = currTime - prevTime

    val FLPID = controller.calculate(frontLeftMotor.velocity)
    val FRPID = controller.calculate(frontRightMotor.velocity)
    val BLPID = controller.calculate(backLeftMotor.velocity)
    val BRPID = controller.calculate(backRightMotor.velocity)

    val FLFF = feedForward.calculate(
      frontLeftMotor.velocity,
      desiredWheelSpeeds.frontLeftMetersPerSecond,
      dtSec
    )
    val FRFF = feedForward.calculate(
      frontRightMotor.velocity,
      desiredWheelSpeeds.frontRightMetersPerSecond,
      dtSec
    )
    val BLFF = feedForward.calculate(
      backLeftMotor.velocity,
      desiredWheelSpeeds.rearLeftMetersPerSecond,
      dtSec
    )
    val BRFF = feedForward.calculate(
      backRightMotor.velocity,
      desiredWheelSpeeds.rearRightMetersPerSecond,
      dtSec
    )

    frontLeftMotor.setVoltage(FLPID + FLFF)
    frontRightMotor.setVoltage(FRPID + FRFF)
    backLeftMotor.setVoltage(BLPID + BLFF)
    backRightMotor.setVoltage(BRPID + BRFF)

    prevTime = currTime

  }

  companion object {
    /**
     *
     * Create a new Mecanum Drive
     *
     * @param frontLeftMotor the front left motor
     * @param frontRightMotor the front right motor
     * @param backLeftMotor the back left motor
     * @param backRightMotor the back right motor
     * @param trackwidth the distance from a back wheel to a front wheel
     * @param length the distance form a left wheel to the right
     * @param pose the (x,y) position of the robot (unused i think)
     * @param maxLinearSpeed the maximum translation speed of the chassis.
     * @param maxRotSpeed the maximum rotation speed of the chassis
     * @param feedForward the SimpleMotorFeedforward for the robot
     * @param controller the PIDController for the robot
     */
    fun createMecanum(
      frontLeftMotor: WrappedMotor,
      frontRightMotor: WrappedMotor,
      backLeftMotor: WrappedMotor,
      backRightMotor: WrappedMotor,
      trackwidth: Double,
      length: Double,
      pose: Pose2d,
      maxLinearSpeed: Double,
      maxRotSpeed: Double,
      feedForward: SimpleMotorFeedforward,
      controller: PIDController
    ): MecanumDrive {
      return MecanumDrive(
        frontLeftMotor,
        frontRightMotor,
        backLeftMotor,
        backRightMotor,
        Translation2d(-length/2, trackwidth/2),
        Translation2d(length/2, trackwidth/2),
        Translation2d(length/2, -trackwidth/2),
        Translation2d(-length/2, -trackwidth/2),
        pose,
        maxLinearSpeed,
        maxRotSpeed,
        feedForward,
        controller
      )
    }
  }
}
