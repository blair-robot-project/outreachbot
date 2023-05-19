package frc.team449.robot2023.commands.light

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.team449.robot2023.subsystems.outreach.light.Light

class Rainbow(
  private val light: Light
): CommandBase() {
  init {
    addRequirements(light)
  }

  override fun runsWhenDisabled(): Boolean {
    return true
  }

  private var firstHue = 100

  override fun execute() {
    for (i in 0 until light.buffer.length) {
      val hue = (firstHue + i * 180 / light.buffer.length) % 180
      light.buffer.setHSV(i, hue, 255, 255)
    }
    firstHue += 1
    firstHue %= 180
  }
}