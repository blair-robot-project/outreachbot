package frc.team449.robot2023

import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.SerialPort
import edu.wpi.first.wpilibj.XboxController
import frc.team449.RobotBase
import frc.team449.control.holonomic.MecanumDrive.Companion.createMecanum
import frc.team449.control.holonomic.OIHolonomic.Companion.createHolonomicOI
import frc.team449.robot2023.constants.RobotConstants
import frc.team449.robot2023.subsystems.outreach.light.Light
import frc.team449.robot2023.subsystems.outreach.light.Light.Companion.createLight
import frc.team449.system.AHRS
import io.github.oblarg.oblog.annotations.Log

class Robot : RobotBase() {

  val driveController = XboxController(0)

  // Instantiate/declare PDP and other stuff here

  override val powerDistribution: PowerDistribution = PowerDistribution(RobotConstants.PDP_CAN, PowerDistribution.ModuleType.kCTRE)

  val light = createLight()

}
