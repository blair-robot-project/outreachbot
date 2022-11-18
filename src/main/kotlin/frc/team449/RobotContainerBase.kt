package frc.team449

import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.smartdashboard.Field2d
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import frc.team449.control.auto.AutoRoutine
import frc.team449.system.SimBattery

abstract class RobotContainerBase {

  val field = Field2d()

  abstract val autoChooser: SendableChooser<AutoRoutine>

  abstract val powerDistribution: PowerDistribution

  private val simBattery: SimBattery = SimBattery()

  open fun robotInit() {
  }

  open fun robotPeriodic() {}

  open fun teleopInit() {}

  open fun teleopPeriodic() {}

  open fun autonomousInit() {}

  open fun simulationInit() {}

  open fun simulationPeriodic() {
    simBattery.update()
  }

  open fun disabledInit() {
  }
}
