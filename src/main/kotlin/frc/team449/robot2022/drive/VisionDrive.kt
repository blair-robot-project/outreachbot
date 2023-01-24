package frc.team449.robot2022.drive

import com.pathplanner.lib.PathConstraints
import com.pathplanner.lib.PathPlanner
import com.pathplanner.lib.PathPoint
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj2.command.Command
import frc.team449.control.auto.HolonomicFollower
import frc.team449.robot2022.RobotContainer2022

class PoseAlign(
  private val robot: RobotContainer2022,
  private val targetPose: Pose2d,
  private val xController: PIDController,
  private val yController: PIDController,
  private val thetaController: PIDController,
  private val poseTolerance: Pose2d = Pose2d(0.05, 0.05, Rotation2d(0.05)),
  private val timeout: Double = 0.75,
  private val maxSpeeds: PathConstraints = PathConstraints(3.0, 1.5)
) {

  fun generateCommand(): Command {
    val startPointRotation = targetPose.translation.minus(robot.drive.pose.translation).angle
    val endPointRotation = robot.drive.pose.translation.minus(targetPose.translation).angle

    val traj = PathPlanner.generatePath(
      maxSpeeds,
      PathPoint(robot.drive.pose.translation, startPointRotation, robot.drive.pose.rotation),
      PathPoint(targetPose.translation, endPointRotation, targetPose.rotation)
    )

    val cmd = HolonomicFollower(
      robot.drive,
      traj,
      xController,
      yController,
      thetaController,
      poseTolerance,
      timeout,
      resetPose = false
    )

    cmd.addRequirements(robot.drive)

    return cmd
  }
}
