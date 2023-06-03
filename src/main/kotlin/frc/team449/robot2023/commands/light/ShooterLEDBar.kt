package frc.team449.robot2023.commands.light

import edu.wpi.first.wpilibj.util.Color
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.team449.robot2023.subsystems.outreach.light.Light
import frc.team449.robot2023.subsystems.outreach.shooter.Shooter
import frc.team449.robot2023.subsystems.outreach.shooter.ShooterConstants

class ShooterLEDBar(
  private val light: Light,
  private val shooter: Shooter
): CommandBase() {

  init {
    addRequirements(light)
  }

  private val desiredShooterSpeedPct = (ShooterConstants.SHOOTER_VEL - ShooterConstants.TOLERANCE) / ShooterConstants.SHOOTER_VEL

  override fun execute() {
    val shooterSpeedPct = shooter.getShooterVelocity() / ShooterConstants.SHOOTER_VEL

    if (shooterSpeedPct >= desiredShooterSpeedPct) {
      for (i in 0 until light.buffer.length) {
        light.buffer.setLED(i, Color.kLightGreen)
      }
    }
    else if (shooterSpeedPct == 0.0) {
      for (i in 0 until light.buffer.length) {
        light.buffer.setLED(i, Color.kBlack)
      }
    }
    else {
      for (i in 0 until light.buffer.length) {
        if (i / light.buffer.length <= shooterSpeedPct) {
          light.buffer.setLED(i, Color.kRed)
        }
      }
    }
  }

}