package frc.team449.robot2023.subsystems.outreach.light

import edu.wpi.first.wpilibj.AddressableLED
import edu.wpi.first.wpilibj.AddressableLEDBuffer
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.util.Color
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase

class Light(
  port: Int,
  length: Int
): SubsystemBase() {

  private var strip = AddressableLED(port)
  var buffer = AddressableLEDBuffer(length)
  val colorChooser: SendableChooser<Color> = SendableChooser()

  init {
    colorChooser.setDefaultOption("Red", Color.kRed)
    setColorOptions(listOf(Color.kOrange, Color.kYellow, Color.kGreen, Color.kBlue, Color.kIndigo, Color.kViolet))
    SmartDashboard.putData("LED Colors", colorChooser)
    strip.setLength(buffer.length)
    strip.start()
  }


  private fun setColorOptions(colors: List<Color>) {
    for (color in colors) {
      colorChooser.addOption(color.toString().substring(1), color)
    }
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