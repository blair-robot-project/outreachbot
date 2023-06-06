package frc.team449.robot2023.subsystems.outreach.light

import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.AddressableLED
import edu.wpi.first.wpilibj.AddressableLEDBuffer
import edu.wpi.first.wpilibj.util.Color
import frc.team449.robot2023.Robot
import frc.team449.robot2023.subsystems.outreach.shooter.ShooterConstants
import kotlin.math.abs

object Light {

  private val strip = AddressableLED(LightConstants.LED_PORT)
  private val buffer = AddressableLEDBuffer(LightConstants.LED_LENGTH)

  private var firstHue = 100
  private var firstColor = 0.0
  private var firstIntensity = 175.0

  init {
    strip.setLength(buffer.length)
    strip.setData(buffer)
    strip.start()
  }

  fun update(robot: Robot) {
    redAndWhite(Sections.ALL)

    if (robot.intake.isRunning) {
      breatheHue(Sections.DRIVE, 60)
    } else if (robot.intake.isEjecting) {
      breatheHue(Sections.DRIVE, 0)
    }

    if (robot.shooter.runShoot) {
      shooterBar(Sections.SHOOTER, robot)
    }

    strip.setData(buffer)
  }

  private fun redAndWhite(section: Sections) {
    for (i in section.beginning() until section.end()) {
      val transition = (firstColor + i * 50 / buffer.length) % 255
      buffer.setRGB(i, 255, transition.toInt(), transition.toInt())
    }
    firstColor += 0.5
    firstColor %= 255
  }

  fun setSolidColor(section: Sections, color: Color) {
    for (i in section.beginning() until section.end()) {
      buffer.setLED(i, color)
    }
  }

  fun breatheHue(
    section: Sections,
    hue: Int,
    minIntensity: Double = 175.0,
    maxIntensity: Double = 255.0,
    period: Double = 37.5,
    updateIntensity: Double = 0.075,
    saturation: Int = 255
  ) {
    for (i in section.beginning() until section.end()) {
      // This number is related to how many lights will show up between the high and low intensity
      val intensity = MathUtil.inputModulus(firstIntensity + i * period / buffer.length, minIntensity, maxIntensity)
      buffer.setHSV(i, hue, saturation, intensity.toInt())

      // The i * 255.0 relates to how fast it will cycle in between the high and low intensity
      firstIntensity += updateIntensity
      firstIntensity = MathUtil.inputModulus(firstIntensity, minIntensity, maxIntensity)
    }
  }

  fun rainbow(section: Sections) {
    for (i in section.beginning() until section.end()) {
      val hue = (firstHue + i * 180 / buffer.length) % 180
      buffer.setHSV(i, hue, 255, 255)
    }
    firstHue += 1
    firstHue %= 180
  }

  fun shooterBar(section: Sections, robot: Robot) {
    if (robot.shooter.currVel >= ShooterConstants.SHOOTER_VEL - ShooterConstants.TOLERANCE) {
      for (i in section.beginning() until section.end()) {
        buffer.setLED(i, Color.kLightGreen)
      }
    } else {
      for (i in section.beginning() until section.end()) {
        if (i.toDouble() / buffer.length <= abs(robot.shooter.currVel - ShooterConstants.SHOOTER_VEL)) {
          buffer.setLED(i, Color.kLightBlue)
        }
      }
    }
  }

  enum class Sections {
    DRIVE,
    SHOOTER,
    ALL;

    fun beginning(): Int {
      return when (this) {
        ALL -> LightConstants.DRIVE_START
        DRIVE -> LightConstants.DRIVE_START
        SHOOTER -> LightConstants.SHOOTER_START
      }
    }

    fun end(): Int {
      return when (this) {
        ALL -> LightConstants.SHOOTER_END
        DRIVE -> LightConstants.DRIVE_END
        SHOOTER -> LightConstants.SHOOTER_END
      }
    }
  }
}
