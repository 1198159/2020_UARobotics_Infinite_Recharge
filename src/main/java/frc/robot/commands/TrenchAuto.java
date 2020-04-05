package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.AutoConstants;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class TrenchAuto extends SequentialCommandGroup {
	private final ShooterSubsystem m_shooter;
	private final DriveSubsystem m_robotDrive;
	private final IntakeSubsystem m_intake;
	private final ConveyorSubsystem m_conveyor;

	/**
	 * Creates a new TrenchAuto.
	 * 
	 * @param shooter
	 * @param robotDrive
	 * @param intake
	 * @param conveyor
	 */
	public TrenchAuto(ShooterSubsystem shooter, DriveSubsystem robotDrive, IntakeSubsystem intake,
			ConveyorSubsystem conveyor) {
		m_shooter = shooter;
		m_robotDrive = robotDrive;
		m_intake = intake;
		m_conveyor = conveyor;
		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(m_shooter, m_robotDrive, m_intake, m_conveyor);

		// Commands to be run in the order they should be run in
		addCommands(
				// placed to face trench
				// start shooter to speed we want
				new InstantCommand(() -> {
					m_shooter.setSetpoint(AutoConstants.kTrenchAutoShootRPM);
					m_shooter.enable();
				}, m_shooter),

				// lower intake and spin intake
				new InstantCommand(() -> {
					m_intake.toggleIntakePosition(true);
					m_intake.toggleIntakeWheels(true);
				}, m_intake),

				// drive forward distance of two balls (x feet)
				// new DriveStraight(AutoConstants.kTrenchAutoBallPickup, m_robotDrive),
				new InstantCommand(() -> m_robotDrive.driveTime(4, 0.5)),

				// Retract intake
				new InstantCommand(() -> {
					m_intake.toggleIntakePosition(true);
					m_intake.toggleIntakeWheels(true);
				}, m_intake),

				// turn around to face goal (-160)
				new TurnToAngle(AutoConstants.kTrenchAutoShootAngle, m_robotDrive),

				// Probably need to do a Limelight based AutoAim here but need to get it working
				// first

				// run conveyor when shooter is at speed (stop moving conveyor when not at
				// speed)
				new Shoot(AutoConstants.kAutoShootTimeSeconds, m_shooter, m_conveyor),

				// Stop shooter
				new InstantCommand(() -> {
					m_shooter.setSetpoint(0);
					m_shooter.disable();
				}, m_shooter),

				// turn (-45) to pick up more balls
				new TurnToAngle(AutoConstants.kTrenchAutoShootAngle, m_robotDrive),

				// Drive some more down field
				new DriveStraight(AutoConstants.kTrenchAutoDriveCenter, m_robotDrive));
	}
}