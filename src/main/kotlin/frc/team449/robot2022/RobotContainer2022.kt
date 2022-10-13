package frc.team449.robot2022

import edu.wpi.first.math.filter.SlewRateLimiter
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.SerialPort
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import frc.team449.RobotContainerBase
import frc.team449.control.auto.AutoRoutine
import frc.team449.control.holonomic.MecanumDrive
import frc.team449.control.holonomic.OIHolonomic
import frc.team449.robot2022.auto.Example
import frc.team449.robot2022.drive.DriveConstants
import frc.team449.system.AHRS
import frc.team449.system.encoder.NEOEncoder
import frc.team449.system.motor.createSparkMax
import io.github.oblarg.oblog.annotations.Log
import kotlin.math.abs

class RobotContainer2022() : RobotContainerBase() {

  // Other CAN IDs
  private val PDP_CAN = 1
  private val PCM_MODULE = 0

  private val driveController = XboxController(0)

  private val ahrs = AHRS(SerialPort.Port.kMXP)

  // Instantiate/declare PDP and other stuff here

  override val powerDistribution: PowerDistribution = PowerDistribution(PDP_CAN, PowerDistribution.ModuleType.kCTRE)

  @Log.Include
  override val drive = MecanumDrive(
    createSparkMax("frontLeft", 0, NEOEncoder.creator(DriveConstants.DRIVE_UPR, DriveConstants.DRIVE_GEARING)),
    createSparkMax("frontRight", 1, NEOEncoder.creator(DriveConstants.DRIVE_UPR, DriveConstants.DRIVE_GEARING)),
    createSparkMax("backLeft", 2, NEOEncoder.creator(DriveConstants.DRIVE_UPR, DriveConstants.DRIVE_GEARING)),
    createSparkMax("backRight", 3, NEOEncoder.creator(DriveConstants.DRIVE_UPR, DriveConstants.DRIVE_GEARING)),
    Translation2d(10.500, -10.713),
    Translation2d(10.500, 10.713),
    Translation2d(-10.500, 10.713),
    Translation2d(-10.500, -10.713),
    Pose2d(),
    DriveConstants.MAX_LINEAR_SPEED,
    DriveConstants.MAX_ROT_SPEED
  )

  override val oi = OIHolonomic(
    drive,
    { if (abs(driveController.leftY) < .08) .0 else -driveController.leftY },
    { if (abs(driveController.leftX) < .08) .0 else -driveController.leftX },
    { if (abs(driveController.getRawAxis(4)) < .07) .0 else -driveController.getRawAxis(4) },
    SlewRateLimiter(10.5),
    4.5,
    { true }
  )

  override val autoChooser = addRoutines()

  private fun addRoutines(): SendableChooser<AutoRoutine> {
    val chooser = SendableChooser<AutoRoutine>()
    val exampleAuto = Example(this)
    chooser.setDefaultOption("Example Auto", exampleAuto.routine())
    return chooser
  }

  override fun teleopInit() {
    // todo Add button bindings here
  }

  override fun robotPeriodic() {
  }

  override fun simulationInit() {
  }

  override fun simulationPeriodic() {
    // Update simulated mechanisms on Mechanism2d widget and stuff
  }

  override fun disabledInit() {
    drive.stop()
  }
}
