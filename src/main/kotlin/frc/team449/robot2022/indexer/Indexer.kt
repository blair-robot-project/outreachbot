package frc.team449.robot2022.indexer

import frc.team449.system.motor.WrappedMotor

class Indexer(
  private val indexMotor: WrappedMotor,
) {

  private var forward = false

  init {
    indexMotor.setVoltage(IndexerConstants.DESIRED_VOLTAGE)
  }

//  fun toggleForward() {
//    forward = if (forward) {
//      stop()
//      false
//    }
//    else {
//      indexMotor.setVoltage(IndexerConstants.DESIRED_VOLTAGE)
//      true
//    }
//  }
//ermumgrei
  fun stop() {
    indexMotor.setVoltage(0.0)
  }
}
