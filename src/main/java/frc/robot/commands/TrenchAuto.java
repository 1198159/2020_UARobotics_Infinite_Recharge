package frc.robot.commands;

import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import io.github.oblarg.oblog.annotations.Log;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.controller.PIDController;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

import frc.robot.Constants.AutoConstants;

public class TrenchAuto extends SequentialCommandGroup implements Loggable{
  private final ShooterSubsystem m_shooter;
  private final DriveSubsystem m_robotDrive;
  private final IntakeSubsystem m_intake;
  private final ConveyorSubsystem m_conveyor;
  /**
   * Creates a new TrenchAuto.
   * @param shooter
   * @param robotDrive
   * @param intake
   * @param conveyor
   */
  public TrenchAuto(ShooterSubsystem shooter, DriveSubsystem robotDrive, IntakeSubsystem intake, ConveyorSubsystem conveyor) {
    m_shooter = shooter;
    m_robotDrive = robotDrive;
    m_intake = intake;
    m_conveyor = conveyor;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_shooter, m_robotDrive, m_intake, m_conveyor);

    // Commands to be run in the order they should be run in
    addCommands(
      //placed to face trench
      //start shooter to speed we want
      new InstantCommand(() -> {
        m_shooter.setSetpoint(AutoConstants.kTrenchAutoShootRPM);
        m_shooter.enable();
      }, m_shooter),
      //lower intake and spin intake
      new InstantCommand(() -> m_intake.toggleIntakePosition(true), m_intake)
      //drive forward distance of two balls (x feet)
      
      //turn around to face goal (-160), 
      //run conveyor when shooter is at speed (stop moving conveyor when not at speed)
      //turn (-45) to pick up more balls

      /* new FunctionalCommand(() -> {
        m_shooter.setSetpoint(AutoConstants.kTrenchAutoShootRPM);
        m_shooter.enable();},
        , m_shooter::wait
        , m_shooter::atSetpoint).,
 */
    );
  }
}