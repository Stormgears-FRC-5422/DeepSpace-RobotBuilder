package org.usfirst.frc5422.Minimec.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DelayCommand extends Command {
    private double m_duration;
    private Timer m_timer;
    public DelayCommand(double duration){
        m_timer = new Timer();
        m_duration = duration;
    }

    @Override
    protected void initialize(){
        m_timer.reset();
        m_timer.start();
    }

    @Override
    protected boolean isFinished() {
        return (m_timer.get() > m_duration);
    }
}
