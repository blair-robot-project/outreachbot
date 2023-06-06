package frc.team449.robot2023.subsystems

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj2.command.button.JoystickButton

class ControllerBindings(
  private val controller: XboxController,
  private val robot: frc.team449.robot2023.Robot
) {

  fun bindButtons() {
    JoystickButton(controller, XboxController.Button.kA.value).onTrue(
      robot.shooter.runShooter()
    ).onFalse(
      robot.shooter.stopShooter()
    )
  }
}
