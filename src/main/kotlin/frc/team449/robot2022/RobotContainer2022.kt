package frc.team449.robot2022

import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.SerialPort
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import frc.team449.RobotContainerBase
import frc.team449.control.auto.AutoRoutine
import frc.team449.control.holonomic.MecanumDrive.Companion.createMecanum
import frc.team449.control.holonomic.OIHolonomic.Companion.createHolonomicOI
import frc.team449.robot2022.auto.Example
import frc.team449.robot2022.drive.DriveConstants
import frc.team449.robot2022.indexer.Indexer
import frc.team449.robot2022.indexer.IndexerConstants
import frc.team449.robot2022.intake.Intake
import frc.team449.robot2022.intake.IntakeConstants
import frc.team449.robot2022.shooter.Shooter
import frc.team449.robot2022.shooter.ShooterConstants
import frc.team449.system.AHRS
import frc.team449.system.encoder.NEOEncoder
import frc.team449.system.motor.createSparkMax
import io.github.oblarg.oblog.annotations.Log

class RobotContainer2022 : RobotContainerBase() {

  // Other CAN IDs
  private val PDP_CAN = 1
  private val PCM_MODULE = 0

  private val driveController = XboxController(0)

  private val ahrs = AHRS(SerialPort.Port.kMXP)

  // Instantiate/declare PDP and other stuff here

  override val powerDistribution: PowerDistribution =
    PowerDistribution(PDP_CAN, PowerDistribution.ModuleType.kCTRE)

  @Log.Include
  val drive = createMecanum(ahrs)

  val oi = createHolonomicOI(drive, driveController)

  override val autoChooser = addRoutines()

  private fun addRoutines(): SendableChooser<AutoRoutine> {
    val chooser = SendableChooser<AutoRoutine>()
    val exampleAuto = Example(this)
    chooser.setDefaultOption("Example Auto", exampleAuto.routine())
    return chooser
  }

  val indexerMotor = createSparkMax(
    "Indexer",
    IndexerConstants.INDEXER_ID,
    NEOEncoder.creator(IndexerConstants.INDEXER_UPR, IndexerConstants.INDEXER_GEARING),
    inverted = true
  )
  val intakeMotor = createSparkMax(
    "Intake",
    IntakeConstants.INTAKE_ID,
    NEOEncoder.creator(IntakeConstants.INTAKE_UPR, IntakeConstants.INTAKE_GEARING)
  )
  val shooterMotor = createSparkMax(
    "Shooter",
    ShooterConstants.SHOOTER_ID,
    NEOEncoder.creator(ShooterConstants.SHOOTER_UPR, ShooterConstants.SHOOTER_GEARING),
    inverted = true
  )
  val feederMotor = createSparkMax(
    "Feeder",
    ShooterConstants.FEEDER_ID,
    NEOEncoder.creator(ShooterConstants.FEEDER_UPR, ShooterConstants.FEEDER_GEARING)
  )

  val indexer = Indexer(indexerMotor)
  val intake = Intake(intakeMotor)
  val shooter = Shooter(
    shooterMotor,
    feederMotor
  )

  override fun teleopInit() {
    JoystickButton(driveController, XboxController.Button.kA.value).onTrue(
      InstantCommand(shooter::runShooter)
    ).onFalse(
      InstantCommand(shooter::stopShooter)
    )

//     JoystickButton(driveController, XboxController.Button.kX.value).onTrue(
//        PoseAlign(
//          this,
//          Pose2d(2.75, 0.0, Rotation2d(0.0)),
//          PIDController(0.75, 0.0, 0.0),
//          PIDController(1.25, 0.0, 0.0),
//          PIDController(0.75, 0.0, 0.0)
//        ).generateCommand()
//     )

//    JoystickButton(driveController, XboxController.Button.kB.value).whenPressed(
//      indexer::toggleForward
//    )

    JoystickButton(driveController, XboxController.Button.kRightBumper.value).onTrue(
      InstantCommand(intake::runIntakeForward)
    ).onFalse(
      InstantCommand(intake::stopIntake)
    )
  }

  override fun robotPeriodic() {
  }

  override fun robotInit() {
    ahrs.calibrate()
    ahrs.reset()
    ahrs.heading = DriveConstants.GYRO_OFFSET
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
