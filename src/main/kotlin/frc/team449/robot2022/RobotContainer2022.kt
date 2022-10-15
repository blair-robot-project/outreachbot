package frc.team449.robot2022

import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.SerialPort
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.motorcontrol.MotorController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import frc.team449.RobotContainerBase
import frc.team449.control.auto.AutoRoutine
import frc.team449.robot2022.auto.Example
import frc.team449.robot2022.drive.DriveConstants
import frc.team449.robot2022.intake.Intake
import frc.team449.system.AHRS
import frc.team449.system.encoder.AbsoluteEncoder
import frc.team449.system.encoder.Encoder
import frc.team449.system.motor.WrappedMotor
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

  val name: String = "intakeMotor"
  val motorController: MotorController = TODO()
  val encoder: Encoder = TODO()

  val intakeMotor = WrappedMotor("intakeMotor", motorController, encoder)



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
    // todo Add button bindings here
    JoystickButton(
      driveController,
      XboxController.Button.kStart.value
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
