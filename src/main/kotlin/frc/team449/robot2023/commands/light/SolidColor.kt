package frc.team449.robot2023.commands.light

import edu.wpi.first.wpilibj.util.Color
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj2.command.WrapperCommand
import frc.team449.robot2023.subsystems.outreach.light.Light

class SolidColor(
  private val light: Light,
  private val color: Color
): CommandBase() {

  private var finished = false;

  init {
    addRequirements(light)
  }

  override fun runsWhenDisabled(): Boolean {
    return true
  }

  override fun execute() {
    for (i in 0 until light.buffer.length) {
      light.buffer.setLED(i, color)
    }
    finished = true
  }

  override fun isFinished(): Boolean {
    return finished
  }
}