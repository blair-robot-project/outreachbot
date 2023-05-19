package frc.team449.robot2023.subsystems.outreach.light

import edu.wpi.first.wpilibj.AddressableLED
import edu.wpi.first.wpilibj.AddressableLEDBuffer
import edu.wpi.first.wpilibj.util.Color
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase

class Light(
  private val port: Int,
  private val length: Int
): SubsystemBase() {

  private var strip = AddressableLED(port)
  var buffer = AddressableLEDBuffer(length)

  init {
    strip.setLength(buffer.length)
    strip.setData(buffer)
    strip.start()
  }

  fun setSolidColor(color: Color): Command {
    return this.runOnce {
      for (i in 0 until this.buffer.length) {
        buffer.setRGB(i, color.red.toInt(), color.green.toInt(), color.blue.toInt())
      }
    }
  }

  override fun periodic() {
    strip.setData(buffer)
  }

  companion object {
    fun createLight(): Light {
      return Light(
        LightConstants.LED_PORT,
        LightConstants.LED_LENGTH
      )
    }
  }

}