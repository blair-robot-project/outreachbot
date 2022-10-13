package frc.team449.control.holonomic

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.geometry.Translation2d
import frc.team449.system.motor.WrappedMotor

class MecanumMotor (
  motor: WrappedMotor,
  location: Translation2d,
  feedforward: SimpleMotorFeedforward,
  pid: PIDController
  ) {}