package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.DriveSubsystem;
import io.github.oblarg.oblog.annotations.Log;

/**
 * A command that will turn the robot to the specified angle.
 */
public class TurnToRelativeAngle extends PIDCommand {
	private final PIDController controller;

	/**
	 * Turns to robot to the specified angle.
	 *
	 * @param targetAngleDegrees The angle to turn to
	 * @param drive              The drive subsystem to use
	 */
	public TurnToRelativeAngle(double targetRelativeAngleDegrees, DriveSubsystem drive) {
		super(new PIDController(DriveConstants.kRelTurnP, DriveConstants.kRelTurnI, DriveConstants.kRelTurnD),
				// Close loop on heading
				() -> -drive.getHeading(),
				// Set reference to target
				-drive.getHeading() - targetRelativeAngleDegrees,
				// Pipe output to turn robot
				output -> {
					if (output > 0) {
						drive.arcadeDrive(0, output + DriveConstants.kRelTurnFriction);
					} else if (output < 0) {
						drive.arcadeDrive(0, output - DriveConstants.kRelTurnFriction);
					} else {
						drive.arcadeDrive(0, output);
					}
				},
				// Require the drive
				drive);
		this.controller = getController();
		// Set the controller to be continuous (because it is an angle controller)
		controller.enableContinuousInput(-180, 180);
		// Set the controller tolerance - the delta tolerance ensures the robot is
		// stationary at the
		// setpoint before it is considered as having reached the reference
		controller.setTolerance(DriveConstants.kRelTurnToleranceDeg, DriveConstants.kRelTurnRateToleranceDegPerS);
	}

	@Log
	@Override
	public boolean isFinished() {
		// End when the controller is at the reference.
		return controller.atSetpoint();
	}

	@Log
	public double getPositionError() {
		return controller.getPositionError();
	}

	@Log
	public double getVelocityError() {
		return controller.getVelocityError();
	}
}