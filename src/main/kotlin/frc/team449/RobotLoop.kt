package frc.team449

import com.pathplanner.lib.server.PathPlannerServer
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.util.Color
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.Commands
import frc.team449.control.DriveCommand
import frc.team449.robot2023.Robot
import frc.team449.robot2023.auto.AutoChooser
import frc.team449.robot2023.auto.Paths
import frc.team449.robot2023.commands.light.Rainbow
import frc.team449.robot2023.commands.light.SolidColor
import frc.team449.robot2023.subsystems.ControllerBindings
import io.github.oblarg.oblog.Logger

/** The main class of the robot, constructs all the subsystems and initializes default commands. */
class RobotLoop : TimedRobot() {

  private val robot = Robot()
  private var autoChooser: AutoChooser = AutoChooser(robot)
  private var autoCommand: Command? = null

  override fun robotInit() {
    // Yes this should be a print statement, it's useful to know that robotInit started.
    println("Started robotInit.")

    if (RobotBase.isSimulation()) {
      // Don't complain about joysticks if there aren't going to be any
      DriverStation.silenceJoystickConnectionWarning(true)
//      val instance = NetworkTableInstance.getDefault()
//      instance.stopServer()
//      instance.startClient4("localhost")
    }

    PathPlannerServer.startServer(5811)

    Logger.configureLoggingAndConfig(robot, false)
    Logger.configureLoggingAndConfig(Paths, false)
    SmartDashboard.putData("Field", robot.field)
    SmartDashboard.putData("Auto Chooser", autoChooser)

    ControllerBindings(robot.driveController, robot).bindButtons()
    SmartDashboard.putData("LED Colors", robot.light.colorChooser)
  }

  override fun robotPeriodic() {
    CommandScheduler.getInstance().run()

    Logger.updateEntries()
  }

  override fun autonomousInit() {
  }

  override fun autonomousPeriodic() {}

  override fun teleopInit() {
  }

  override fun teleopPeriodic() {
    CommandScheduler.getInstance().schedule(SolidColor(robot.light, robot.light.colorChooser.selected))
  }

  override fun disabledInit() {
  }

  override fun disabledPeriodic() {}

  override fun testInit() {
  }

  override fun testPeriodic() {}

  override fun simulationInit() {}

  override fun simulationPeriodic() {}
}
