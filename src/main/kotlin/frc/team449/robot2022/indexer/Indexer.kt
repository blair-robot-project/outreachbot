package frc.team449.robot2022.indexer

import frc.team449.system.motor.WrappedMotor

class Indexer(
  private val indexMotor: WrappedMotor,
) {

  fun forward() {
    indexMotor.setVoltage(IndexerConstants.DESIRED_VOLTAGE)
  }

  fun reverse() {
    indexMotor.setVoltage(-IndexerConstants.DESIRED_VOLTAGE)
  }

  fun stop() {
    indexMotor.setVoltage(0.0)
  }
}
