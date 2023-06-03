package frc.team449.robot2023.subsystems.outreach.light

import edu.wpi.first.wpilibj.AddressableLED
import edu.wpi.first.wpilibj.AddressableLEDBuffer
import edu.wpi.first.wpilibj.util.Color
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.robot2023.subsystems.outreach.shooter.Shooter

class Light(
  port: Int,
  length: Int
): SubsystemBase() {

  private var strip = AddressableLED(port)
  var buffer = AddressableLEDBuffer(length)

  init {
    strip.setLength(buffer.length)
    strip.setData(buffer)
    strip.start()
  }

  override fun periodic() {
    strip.setData(buffer)
  }

  companion object {
    fun createLight(): Light {
      return Light(
        LightConstants.LED_PORT,
        LightConstants.LED_LENGTH,
      )
    }
  }

}