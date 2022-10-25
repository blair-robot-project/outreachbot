package frc.team449.robot2022

import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.SerialPort
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import frc.team449.RobotContainerBase
import frc.team449.control.auto.AutoRoutine
import frc.team449.robot2022.auto.Example
import frc.team449.robot2022.drive.DriveConstants
import frc.team449.robot2022.intake.Intake
import frc.team449.robot2022.shooter.Shooter
import frc.team449.system.AHRS
import frc.team449.system.encoder.AbsoluteEncoder
import frc.team449.system.encoder.NEOEncoder
import frc.team449.system.motor.createSparkMax
import io.github.oblarg.oblog.annotations.Log

class RobotContainer2022() : RobotContainerBase() {

  // Other CAN IDs
  private val PDP_CAN = 1
  private val PCM_MODULE = 0

  private val driveController = XboxController(0)

  private val ahrs = AHRS(SerialPort.Port.kMXP)

  // Instantiate/declare PDP and other stuff here

  override val powerDistribution: PowerDistribution = PowerDistribution(PDP_CAN, PowerDistribution.ModuleType.kCTRE)

  /**
   * Converts the drive to a SwerveSim if the robot is in simulation
   */
  @Log.Include

  override val autoChooser = addRoutines()

  // INTAKE VALUES
  val intakeName: String = "intakeMotor"

  val intakeMotor = createSparkMax("intakeMotor", 13, NEOEncoder.creator(1.0, 1.0)) // TODO - Find actual ID
  val intakeObj = Intake(intakeMotor)

  // SHOOTER VALUES
  val shooterName: String = "shooterMotor"

  val shooterMotor = createSparkMax("shooterMotor", 13, NEOEncoder.creator(1.0, 1.0)) // TODO - Find actual ID
  val shooterObj = Shooter(shooterMotor)

  /** Helper to make turning motors for swerve */

  /** Helper to make turning motors for swerve */
  private fun makeTurningMotor(
    name: String,
    motorId: Int,
    inverted: Boolean,
    sensorPhase: Boolean,
    encoderChannel: Int,
    offset: Double
  ) =
    createSparkMax(
      name = name + "Turn",
      id = motorId,
      enableBrakeMode = true,
      inverted = inverted,
      encCreator = AbsoluteEncoder.creator(
        encoderChannel,
        offset,
        DriveConstants.TURN_UPR,
        sensorPhase
      )
    )
  //

  private fun addRoutines(): SendableChooser<AutoRoutine> {
    val chooser = SendableChooser<AutoRoutine>()
    val exampleAuto = Example(this)
    chooser.setDefaultOption("Example Auto", exampleAuto.routine())
    return chooser
  }

  override fun teleopInit() {
    // --------- INTAKE CONTROLS ---------
    // Y Button - Intake Forward
    JoystickButton(driveController, XboxController.Button.kY.value).whileHeld(
      InstantCommand(intakeObj::runIntakeForward)
    )
    // A Button - Intake Reverse
    JoystickButton(driveController, XboxController.Button.kA.value).whenPressed(
      InstantCommand(intakeObj::runIntakeReverse)
    )
    // X Button - Stop Intake
    JoystickButton(driveController, XboxController.Button.kX.value).whenPressed(
      InstantCommand(intakeObj::stopIntake)
    )

    // --------- SHOOTER CONTROLS ---------
    // Right Bumper - Hold to run shooter, Release to stop shooter
    JoystickButton(
      driveController,
      XboxController.Button.kRightBumper.value
    ).whenPressed(
      InstantCommand(shooterObj::runShooter)
    ).whenReleased(
      InstantCommand(shooterObj::stopShooter)
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
  }

  override fun robotInit() {
  }
}
