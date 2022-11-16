package frc.team449.control

import edu.wpi.first.wpilibj2.command.CommandBase

class DriveCommand(
  private val oi: OI,
  private val drive: DriveSubsystem
) : CommandBase() {
  init {
    addRequirements(drive)
  }

  override fun execute() {
    drive.set(oi.get())
  }

  override fun end(interrupted: Boolean) {
    drive.stop()
  }
}
