package frc.team449.robot2023.subsystems.outreach.indexer

import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.team449.robot2022.indexer.IndexerConstants
import frc.team449.system.encoder.NEOEncoder
import frc.team449.system.motor.WrappedMotor
import frc.team449.system.motor.createSparkMax

class Indexer(
  private val indexMotor: WrappedMotor,
): SubsystemBase() {

  init {
    indexMotor.setVoltage(IndexerConstants.DESIRED_VOLTAGE)
  }

  companion object {
    fun createIndexer(): Indexer {
      val indexerMotor = createSparkMax(
        "Indexer",
        IndexerConstants.INDEXER_ID,
        NEOEncoder.creator(IndexerConstants.INDEXER_UPR, IndexerConstants.INDEXER_GEARING),
        inverted = true
      )

      return Indexer(indexerMotor)
    }
  }

}
