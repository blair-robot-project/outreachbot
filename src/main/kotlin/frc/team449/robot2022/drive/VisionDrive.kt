package frc.team449.robot2022.drive

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import frc.team449.control.auto.HolonomicFollower
import frc.team449.robot2022.RobotContainer2022
import frc.team449.robot2022.auto.AutoConstants
import frc.team449.robot2022.auto.Paths

class VisionDrive(
  private val robot: RobotContainer2022
) {

  fun routine(): Command {
    val traj = Paths.ALIGN

    val cmd = SequentialCommandGroup(
      HolonomicFollower(
        robot.drive,
        traj, false,
        AutoConstants.MAX_ROTVEL,
        AutoConstants.MAX_ROTACC,
        timeout = 8.75,
        translation_kP = 0.5,
        rotation_kP = 1.0
      ),
      InstantCommand(robot.shooter::runShooter),
      WaitCommand(7.5),
      InstantCommand(robot.shooter::stopShooter)
    )
    cmd.addRequirements(robot.drive)

    return cmd
  }
}
