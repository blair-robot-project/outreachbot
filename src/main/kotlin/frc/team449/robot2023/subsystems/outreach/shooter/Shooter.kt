package frc.team449.robot2023.subsystems.outreach.shooter

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.robot2023.commands.light.ShooterLEDBar
import frc.team449.robot2023.subsystems.outreach.light.Light
import frc.team449.system.encoder.NEOEncoder
import frc.team449.system.motor.WrappedMotor
import frc.team449.system.motor.createSparkMax
import io.github.oblarg.oblog.Loggable
import io.github.oblarg.oblog.annotations.Log
import kotlin.math.abs

class Shooter(
  private val shooterMotor: WrappedMotor,
  private val feederMotor: WrappedMotor,
  private val shooterController: PIDController,
  private val shooterFF: SimpleMotorFeedforward,
  private val light: Light
) : SubsystemBase(), Loggable {

  private var runShoot = false
  @Log.Graph
  private var shooterSpeed = 0.0

  fun runShooter(): Command {
    return this.runOnce {runShoot = true}
  }


  fun stopShooter(): Command {
    return this.runOnce {runShoot = false}
  }


  fun runShooterReverse(): Command {
    return this.runOnce {shooterMotor.setVoltage(-7.0)}
  }


  override fun periodic() {
    if (runShoot) {
      val shooterPID = shooterController.calculate(shooterMotor.velocity, ShooterConstants.SHOOTER_VEL)
      val shooterFF = shooterFF.calculate(ShooterConstants.SHOOTER_VEL)

      shooterMotor.setVoltage(shooterPID + shooterFF)

      ShooterLEDBar(this.light, shooterMotor.velocity / ShooterConstants.SHOOTER_VEL)

      if (shooterController.atSetpoint()) {
        feederMotor.setVoltage(ShooterConstants.FEEDER_VOLTAGE)
      }
    } else {
      shooterMotor.stopMotor()
      feederMotor.stopMotor()
    }
  }


  companion object {
    fun createShooter(light: Light): Shooter {
      val shooterMotor = createSparkMax(
        "Shooter",
        ShooterConstants.SHOOTER_ID,
        NEOEncoder.creator(ShooterConstants.SHOOTER_UPR, ShooterConstants.SHOOTER_GEARING),
        inverted = true
      )

      val feederMotor = createSparkMax(
        "Feeder",
        ShooterConstants.FEEDER_ID,
        NEOEncoder.creator(ShooterConstants.FEEDER_UPR, ShooterConstants.FEEDER_GEARING)
      )

      val shooterPID = PIDController(
        ShooterConstants.SHOOTER_KP,
        ShooterConstants.SHOOTER_KI,
        ShooterConstants.SHOOTER_KD
      )

      val shooterFF = SimpleMotorFeedforward(
        ShooterConstants.SHOOTER_KS,
        ShooterConstants.SHOOTER_KV,
        ShooterConstants.SHOOTER_KA
      )

      return Shooter(
        shooterMotor,
        feederMotor,
        shooterPID,
        shooterFF,
        light
      )
    }
  }
}
