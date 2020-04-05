package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import io.github.oblarg.oblog.annotations.Log;

public class Shoot extends CommandBase {
	private final ShooterSubsystem m_shooter;
	private final ConveyorSubsystem m_conveyor;

	private final Timer m_timer = new Timer();
	private final double timeout;

	public Shoot(double timeoutIn, ShooterSubsystem shooter, ConveyorSubsystem conveyor) {
		m_shooter = shooter;
		m_conveyor = conveyor;
		timeout = timeoutIn;

		addRequirements(m_shooter, m_conveyor);
	}

	@Override
	public void initialize() {
		m_timer.reset();
		m_timer.start();
	}

	@Log
	@Override
	public boolean isFinished() {
		// End when the controller is at the reference.
		return m_timer.get() > timeout;
	}

	@Override
	public void execute() {
		m_conveyor.turnOn();
		/*
		 * if(m_shooter.atSetpoint()) { m_conveyor.turnOn(); } else{
		 * m_conveyor.turnOff(); }
		 */
	}

	@Override
	public void end(boolean interrupted) {
		m_conveyor.turnOff();
	}
}