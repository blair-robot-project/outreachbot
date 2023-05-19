package frc.team449.robot2023.subsystems

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.WaitCommand
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import java.time.Instant

class ControllerBindings(
  private val controller: XboxController,
  private val robot: frc.team449.robot2023.Robot
) {

  fun bindButtons() {
//    JoystickButton(controller, XboxController.Button.kA.value).onTrue(
//      robot.intake.runIntakeForward().andThen(
//        robot.shooter.runShooter()
//      )).onFalse(
//        robot.intake.stopIntake().andThen(
//          robot.shooter.stopShooter()
//        )
//      )
//
//    JoystickButton(controller, XboxController.Button.kX.value).onTrue(
//      InstantCommand(robot.intake::runIntakeForward)
//    ).onFalse(
//      InstantCommand(robot.intake::stopIntake)
//    )
  }
}
