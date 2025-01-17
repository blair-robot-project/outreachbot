package frc.team449.robot2023

import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.SerialPort
import edu.wpi.first.wpilibj.XboxController
import frc.team449.RobotBase
import frc.team449.control.holonomic.MecanumDrive.Companion.createMecanum
import frc.team449.control.holonomic.OIHolonomic.Companion.createHolonomicOI
import frc.team449.robot2023.subsystems.outreach.indexer.Indexer
import frc.team449.robot2022.indexer.IndexerConstants
import frc.team449.robot2023.constants.RobotConstants
import frc.team449.robot2023.subsystems.outreach.indexer.Indexer.Companion.createIndexer
import frc.team449.robot2023.subsystems.outreach.intake.Intake
import frc.team449.robot2023.subsystems.outreach.intake.Intake.Companion.createIntake
import frc.team449.robot2023.subsystems.outreach.intake.IntakeConstants
import frc.team449.robot2023.subsystems.outreach.shooter.Shooter
import frc.team449.robot2023.subsystems.outreach.shooter.Shooter.Companion.createShooter
import frc.team449.robot2023.subsystems.outreach.shooter.ShooterConstants
import frc.team449.system.AHRS
import frc.team449.system.encoder.NEOEncoder
import frc.team449.system.motor.createSparkMax
import io.github.oblarg.oblog.annotations.Log

class Robot : RobotBase() {

  val driveController = XboxController(0)

  private val ahrs = AHRS(SerialPort.Port.kMXP)

  // Instantiate/declare PDP and other stuff here

  override val powerDistribution: PowerDistribution = PowerDistribution(RobotConstants.PDP_CAN, PowerDistribution.ModuleType.kCTRE)

  override val drive = createMecanum(ahrs)

  @Log(name = "Joystick Input")
  override val oi = createHolonomicOI(drive, driveController)

  val indexer = createIndexer()
  val intake = createIntake()
  val shooter = createShooter()
}
