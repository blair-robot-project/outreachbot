package frc.team449.robot2022.indexer

import frc.team449.system.motor.WrappedMotor

class Indexer(
  private val indexMotor: WrappedMotor,
  private val desiredVoltage: Double
) {

  fun forward() {
    indexMotor.setVoltage(desiredVoltage)
  }

  fun reverse() {
    indexMotor.setVoltage(-desiredVoltage)
  }

  fun stop() {
    indexMotor.setVoltage(0.0)
  }
}
