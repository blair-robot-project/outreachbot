package frc.team449.robot2022.indexer

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.system.motor.WrappedMotor

class Indexer(
  private val indexMotor: WrappedMotor,
): SubsystemBase() {

  fun runIndexer(): Command {
    return this.runOnce { indexMotor.setVoltage(IndexerConstants.DESIRED_VOLTAGE) }
  }

}
