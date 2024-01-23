package frc.team449

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.team449.control.DriveCommand
import frc.team449.robot2023.Robot
import frc.team449.robot2023.subsystems.ControllerBindings
import io.github.oblarg.oblog.Logger

/** The main class of the robot, constructs all the subsystems and initializes default commands. */
class RobotLoop : TimedRobot() {

  private val robot = Robot()
  private var autoCommand: Command? = null

  override fun robotInit() {
    // Yes this should be a print statement, it's useful to know that robotInit started.
    println("Started robotInit.")

    if (isSimulation()) {
      // Don't complain about joysticks if there aren't going to be any
      DriverStation.silenceJoystickConnectionWarning(true)
//      val instance = NetworkTableInstance.getDefault()
//      instance.stopServer()
//      instance.startClient4("localhost")
    }

    Logger.configureLoggingAndConfig(robot, false)
    SmartDashboard.putData("Field", robot.field)

    ControllerBindings(robot.driveController, robot).bindButtons()
  }

  override fun robotPeriodic() {
    CommandScheduler.getInstance().run()

    Logger.updateEntries()

    robot.field.robotPose = robot.drive.pose
  }

  override fun autonomousInit() {
  }

  override fun autonomousPeriodic() {}

  override fun teleopInit() {
    if (autoCommand != null) {
      CommandScheduler.getInstance().cancel(autoCommand)
    }
    robot.drive.defaultCommand = DriveCommand(robot.drive, robot.oi)
  }

  override fun teleopPeriodic() {
  }

  override fun disabledInit() {
    robot.drive.stop()
  }

  override fun disabledPeriodic() {}

  override fun testInit() {
    if (autoCommand != null) {
      CommandScheduler.getInstance().cancel(autoCommand)
    }
  }

  override fun testPeriodic() {}

  override fun simulationInit() {}

  override fun simulationPeriodic() {}
}
