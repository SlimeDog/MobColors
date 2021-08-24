package dev.ratas.mobcolors.commands.subcommands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import dev.ratas.mobcolors.commands.SimpleSubCommand;
import dev.ratas.mobcolors.scheduling.SimpleTaskScheduler;
import dev.ratas.mobcolors.scheduling.TaskScheduler;

public class SchedulerInfoSubCommand extends SimpleSubCommand {
    private static final String name = "schedulerinfo";
    private static final String usage = "/mobcolors schedulerinfo";
    private static final String perms = "mobcolors.schedulerinfo";
    private final SimpleTaskScheduler scheduler;

    public SchedulerInfoSubCommand(TaskScheduler scheduler, boolean onDebug) {
        super(name, usage, perms, false, onDebug);
        this.scheduler = (SimpleTaskScheduler) scheduler;
    }

    @Override
    public List<String> getTabComletions(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        sender.sendMessage("Tasks completed\tTicks worked on\tAtomic tasks completed\tWork time (ms)");
        sender.sendMessage(scheduler.toString());
        return true;
    }

}
