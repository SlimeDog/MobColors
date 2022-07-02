package dev.ratas.mobcolors.commands.subcommands;

import java.util.Collections;
import java.util.List;

import dev.ratas.mobcolors.scheduling.SimpleTaskScheduler;
import dev.ratas.mobcolors.scheduling.TaskScheduler;
import dev.ratas.slimedogcore.api.commands.SDCCommandOptionSet;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import dev.ratas.slimedogcore.impl.commands.AbstractSubCommand;

public class SchedulerInfoSubCommand extends AbstractSubCommand {
    private static final String name = "schedulerinfo";
    private static final String usage = "/mobcolors schedulerinfo";
    private static final String perms = "mobcolors.schedulerinfo";
    private final SimpleTaskScheduler scheduler;

    public SchedulerInfoSubCommand(TaskScheduler scheduler, boolean onDebug) {
        super(name, perms, usage, false, onDebug);
        this.scheduler = (SimpleTaskScheduler) scheduler;
    }

    @Override
    public List<String> onTabComplete(SDCRecipient sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean onOptionedCommand(SDCRecipient sender, String[] args, SDCCommandOptionSet options) {
        sender.sendRawMessage("Tasks completed\tTicks worked on\tAtomic tasks completed\tWork time (ms)");
        sender.sendRawMessage(scheduler.toString());
        return true;
    }

}
