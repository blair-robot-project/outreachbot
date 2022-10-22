package frc.team449.robot2022

import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.RobotBase.isReal
import edu.wpi.first.wpilibj.SerialPort
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import frc.team449.RobotContainerBase
import frc.team449.control.auto.AutoRoutine
import frc.team449.control.holonomic.HolonomicDrive
import frc.team449.control.holonomic.MecanumDrive
import frc.team449.control.holonomic.MecanumDrive.Companion.createMecanum
import frc.team449.control.holonomic.MecanumDrive.Companion.simOf
import frc.team449.control.holonomic.OIHolonomic.Companion.createHolonomicOI
import frc.team449.robot2022.auto.Example
import frc.team449.system.AHRS
import io.github.oblarg.oblog.annotations.Log

class RobotContainer2022() : RobotContainerBase() {

  // Other CAN IDs
  private val PDP_CAN = 1
  private val PCM_MODULE = 0

  private val driveController = XboxController(0)

  private val ahrs = AHRS(SerialPort.Port.kMXP)

  // Instantiate/declare PDP and other stuff here

  override val powerDistribution: PowerDistribution = PowerDistribution(PDP_CAN, PowerDistribution.ModuleType.kCTRE)

  @Log.Include
  override val drive = if (isReal()) createMecanum(this.ahrs) else simOf(createMecanum(this.ahrs))

  override val oi = createHolonomicOI(this, driveController)

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
