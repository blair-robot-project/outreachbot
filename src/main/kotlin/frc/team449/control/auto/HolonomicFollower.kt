package frc.team449.control.auto

import com.pathplanner.lib.PathPlannerTrajectory
import com.pathplanner.lib.controllers.PPHolonomicDriveController
import com.pathplanner.lib.server.PathPlannerServer
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.team449.control.holonomic.HolonomicDrive
import frc.team449.robot2023.auto.AutoConstants
import kotlin.math.PI

/**
 * @param drivetrain Holonomic Drivetrain used
 * @param trajectory Path Planner trajectory to follow
 * @param xController The PIDController you want to use for correcting translational X error
 * @param yController The PIDController you want to use for correcting translational Y error
 * @param thetaController The PIDController you want to use for correcting rotational error
 * @param resetPose Whether to reset the drivetrain pose to the inital trajectory pose
 * @param poseTol Tolerance for the robot to be in by the end of the command
 * @param timeout Time to wait after trajectory is finished for the robot to correct its end pose; overrides poseTol
 */
class HolonomicFollower(
  private val drivetrain: HolonomicDrive,
  trajectory: PathPlannerTrajectory,
  private val xController: PIDController = PIDController(AutoConstants.DEFAULT_X_KP, 0.0, 0.0),
  private val yController: PIDController = PIDController(AutoConstants.DEFAULT_Y_KP, 0.0, 0.0),
  private val thetaController: PIDController = PIDController(AutoConstants.DEFAULT_ROTATION_KP, 0.0, 0.0),
  poseTol: Pose2d = Pose2d(0.05, 0.05, Rotation2d(0.05)),
  private val timeout: Double = 1.0,
  private val resetPose: Boolean = false
) : CommandBase() {

  private val timer = Timer()
  private var prevTime = 0.0

  private val controller = PPHolonomicDriveController(
    xController, yController, thetaController
  )

  // MAKE SURE YOUR ORIGINAL PATH IS FOR THE BLUE ALLIANCE
  private val transformedTrajectory = PathPlannerTrajectory.transformTrajectoryForAlliance(
    trajectory,
    DriverStation.getAlliance()
  )

  init {
    // require the drivetrain to interrupt
    addRequirements(drivetrain)

    if (resetPose) {
      drivetrain.pose = trajectory.initialHolonomicPose
    }

    // controlling heading which is circular, [0, 2*PI)
    thetaController.enableContinuousInput(.0, 2 * PI)

    controller.setTolerance(poseTol)
  }

  override fun initialize() {

    // reset the controllers so that the error from last run doesn't transfer
    xController.reset()
    yController.reset()
    thetaController.reset()

    PathPlannerServer.sendActivePath(transformedTrajectory.states)

    // reset timer from last run and restart for this run
    timer.reset()
    timer.start()
  }

  override fun execute() {
    val currTime = timer.get()

    val reference = transformedTrajectory.sample(currTime) as PathPlannerTrajectory.PathPlannerState

    val currentPose = drivetrain.pose

    PathPlannerServer.sendPathFollowingData(
      Pose2d(
        reference.poseMeters.translation,
        reference.holonomicRotation
      ),
      currentPose
    )

    drivetrain.set(
      controller.calculate(
        currentPose,
        reference
      )
    )

    prevTime = currTime
  }

  /**
   * @return if the robot reached ending position by estimated end time
   */
  override fun isFinished(): Boolean {
    return (timer.hasElapsed(transformedTrajectory.totalTimeSeconds) && controller.atReference()) ||
      timer.hasElapsed(transformedTrajectory.totalTimeSeconds + timeout)
  }

  override fun end(interrupted: Boolean) {
    timer.stop()
    timer.reset()
    drivetrain.stop()
  }
}
