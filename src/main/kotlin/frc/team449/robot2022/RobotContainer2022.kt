package frc.team449.robot2022

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.RobotBase.isReal
import edu.wpi.first.wpilibj.SerialPort
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import frc.team449.RobotContainerBase
import frc.team449.control.auto.AutoRoutine
import frc.team449.control.holonomic.MecanumDrive.Companion.createMecanum
import frc.team449.control.holonomic.MecanumDrive.Companion.simDrive
import frc.team449.control.holonomic.OIHolonomic.Companion.createHolonomicOI
import frc.team449.robot2022.auto.Example
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
  val drive = if (isReal()) createMecanum(ahrs) else simDrive(ahrs)

  val oi = createHolonomicOI(drive, driveController)

  // create shooter here

  // create intake here

  // create indexer here

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
    NEOEncoder.creator(IndexerConstants.INDEXER_UPR, IndexerConstants.INDEXER_GEARING)
  )
  val intakeMotor = createSparkMax(
    "Intake",
    IntakeConstants.INTAKE_ID,
    NEOEncoder.creator(IntakeConstants.INTAKE_UPR, IntakeConstants.INTAKE_GEARING)
  )
  val shooterMotor = createSparkMax(
    "Shooter",
    ShooterConstants.SHOOTER_ID,
    NEOEncoder.creator(ShooterConstants.SHOOTER_UPR, ShooterConstants.SHOOTER_GEARING)
  )
  val feederMotor = createSparkMax(
    "Feeder",
    ShooterConstants.FEEDER_ID,
    NEOEncoder.creator(ShooterConstants.FEEDER_UPR, ShooterConstants.FEEDER_GEARING)
  )

  val indexer = Indexer(indexerMotor)
  val intake = Intake(intakeMotor)
  val shooter = Shooter(
    SimpleMotorFeedforward(
      ShooterConstants.SHOOTER_KS,
      ShooterConstants.SHOOTER_KV,
      ShooterConstants.SHOOTER_KA
    ),
    PIDController(0.0, 0.0, 0.0), shooterMotor, feederMotor
  )

  override fun teleopInit() {
    JoystickButton(driveController, XboxController.Button.kA.value).whenPressed(
      shooter::runShooter
    ).whenReleased(
      shooter::stopShooter
    )

    JoystickButton(driveController, XboxController.Button.kLeftBumper.value).whenPressed(
      indexer::forward
    ).whenReleased(
      indexer::stop
    )

    JoystickButton(driveController, XboxController.Button.kRightBumper.value).whenPressed(
      intake::runIntakeForward
    ).whenReleased(
      intake::stopIntake
    )
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
