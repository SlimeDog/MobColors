package dev.ratas.mobcolors.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.ratas.mobcolors.config.Messages.IllegalColorException;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.config.variants.DyeVariant;
import dev.ratas.mobcolors.config.variants.HorseColor;
import dev.ratas.mobcolors.config.variants.HorseStyle;
import dev.ratas.mobcolors.config.variants.TropicalFishPattern;
import dev.ratas.mobcolors.config.variants.TropicalFishVariant;
import dev.ratas.mobcolors.mock.MockSlimeDogPlugin;
import dev.ratas.mobcolors.utils.WorldDescriptor;
import dev.ratas.slimedogcore.api.SlimeDogPlugin;
import dev.ratas.slimedogcore.api.messaging.SDCMessage;
import dev.ratas.slimedogcore.api.messaging.context.SDCContext;
import dev.ratas.slimedogcore.api.messaging.context.factory.SDCContextFactory;
import dev.ratas.slimedogcore.api.messaging.context.factory.SDCDoubleContextFactory;
import dev.ratas.slimedogcore.api.messaging.context.factory.SDCQuadrupleContextFactory;
import dev.ratas.slimedogcore.api.messaging.context.factory.SDCSingleContextFactory;
import dev.ratas.slimedogcore.api.messaging.context.factory.SDCTripleContextFactory;
import dev.ratas.slimedogcore.api.messaging.context.factory.SDCVoidContextFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCDoubleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCMessageFactory;

public class MessagesTests {
    // private static final Logger LOGGER = Logger.getLogger("[MobColors TEST]");
    private static final Object[] CONTEXT_FILLER_OPTIONS = new Object[] {
            new WorldDescriptor(UUID.randomUUID(), "<RandomWorld>"), 6, 8, MobType.horse,
            new WorldDescriptor(UUID.randomUUID(), "<RandomWorld2>"), 6.8D, MobType.cat, "emtpy",
            Arrays.asList("w1", "w2"), 2, 4L, 10L, MobType.sheep, 12L, 13L, 2.3, new String[] { "abc", "cde" },
            MobType.cat, new Messages.ProgressInfo(5L, 10L) };

    @Test
    @DisplayName("Checking that all public methods within Messages class return message factories")
    public void testMessagesAreMessageFacotories() {
        SlimeDogPlugin mockPlugin = new MockSlimeDogPlugin();
        Messages messages;
        try {
            messages = new Messages(mockPlugin);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }
        for (Method method : Messages.class.getDeclaredMethods()) {
            if (!shouldTestMethod(method, messages)) { // only test public non-static methods
                continue;
            }
            Object res = invokeMethod(messages, method);
            Assertions.assertTrue(res instanceof SDCMessageFactory, "Messages need to be message factories");
        }
    }

    @Test
    @DisplayName("Checking that messages are the same in code and in messages.yml")
    public void testParityOfMessageEntries() {
        Messages fileMessages;
        Messages codeMessages;
        SlimeDogPlugin filePlugin = new MockSlimeDogPlugin(true);
        SlimeDogPlugin codePlugin = new MockSlimeDogPlugin();
        try {
            fileMessages = new Messages(filePlugin);
            codeMessages = new Messages(codePlugin);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }
        for (Method method : Messages.class.getDeclaredMethods()) {
            if (!shouldTestMethod(method, fileMessages)) { // only test public non-static methods
                continue;
            }
            checkMethod(fileMessages, codeMessages, method);
        }
    }

    private boolean shouldTestMethod(Method method, Object obj) {
        return !Modifier.isStatic(method.getModifiers()) && method.canAccess(obj)
                && !method.getName().equals("reloadConfig") && !method.getName().equals("fill");
    }

    private void checkMethod(Messages file, Messages code, Method method) {
        SDCMessageFactory<?> res1 = getMessage(file, method);
        SDCMessage<?>[] fileMsgs = fillMessages(res1);
        SDCMessageFactory<?> res2 = getMessage(code, method);
        SDCMessage<?>[] codeMsgs = fillMessages(res2);
        if (fileMsgs.length == 0 || codeMsgs.length == 0) {
            throw new IllegalStateException("Unable to find suitable context for message: " + method);
        }
        Assertions.assertEquals(fileMsgs.length, codeMsgs.length);
        int timesSuccessful = 0;
        for (int i = 0; i < fileMsgs.length; i++) {
            SDCMessage<?> fileMsg = fileMsgs[i];
            SDCMessage<?> codeMsg = codeMsgs[i];
            String inFile;
            String inCode;
            try {
                inFile = fileMsg.getFilled();
                inCode = codeMsg.getFilled();
            } catch (ClassCastException | IllegalColorException e) {
                continue;
            }
            Assertions.assertEquals(inFile, inCode, "Issue with " + method.getName() + " method");
            timesSuccessful++;
        }
        Assertions.assertTrue(timesSuccessful > 0,
                "Could not find a suitable set of arguments for method " + method.getName());
    }

    // Can be used after the first test makes sure they are all strings
    public SDCMessageFactory<?> getMessage(Messages messages, Method method) {
        return (SDCMessageFactory<?>) invokeMethod(messages, method);
    }

    public SDCMessage<?>[] fillMessages(SDCMessageFactory<?> factory) {
        SDCContextFactory<?> cfact = factory.getContextFactory();
        SDCContext[] contexts = getContexts(cfact);
        return _getMessages(factory, contexts);
    }

    @SuppressWarnings("unchecked")
    public SDCMessage<?>[] _getMessages(SDCMessageFactory<?> msgFactory, SDCContext[] contexts) {
        List<SDCMessage<?>> msgs = new ArrayList<>();
        for (int i = 0; i < contexts.length; i++) {
            msgs.add(((SDCMessageFactory<SDCContext>) msgFactory).getMessage(contexts[i]));
        }
        return msgs.toArray(new SDCMessage<?>[msgs.size()]);
    }

    @SuppressWarnings("unchecked")
    public SDCContext[] getContexts(SDCContextFactory<?> cfact) {
        if (cfact instanceof SDCVoidContextFactory) {
            return new SDCContext[] { ((SDCVoidContextFactory) cfact).getContext() };
        } else if (cfact instanceof SDCSingleContextFactory) {
            // unchecked
            return tryGetContext1((SDCSingleContextFactory<Object>) cfact, CONTEXT_FILLER_OPTIONS);
        } else if (cfact instanceof SDCDoubleContextFactory) {
            // unchecked
            return tryGetContext2((SDCDoubleContextFactory<Object, Object>) cfact, CONTEXT_FILLER_OPTIONS);
        } else if (cfact instanceof SDCTripleContextFactory) {
            // unchecked
            return tryGetContext3((SDCTripleContextFactory<Object, Object, Object>) cfact, CONTEXT_FILLER_OPTIONS);
        } else if (cfact instanceof SDCQuadrupleContextFactory) {
            // unchecked
            return tryGetContext4((SDCQuadrupleContextFactory<Object, Object, Object, Object>) cfact,
                    CONTEXT_FILLER_OPTIONS);
        }
        throw new IllegalArgumentException("Unknown context factory: " + cfact);
    }

    private SDCContext[] tryGetContext1(SDCSingleContextFactory<Object> cfact, Object... options) {
        SDCContext[] out = new SDCContext[options.length];
        for (int i = 0; i < options.length; i++) {
            Object opt = options[i];
            out[i] = cfact.getContext(opt);
        }
        return out;
    }

    private Object[][] get2DOptArr(int atATime, Object... options) {
        Object[][] out = new Object[options.length - atATime][atATime];
        for (int i = 0; i + atATime < options.length; i++) {
            for (int j = 0; j < atATime; j++) {
                out[i][j] = options[i + j];
            }
        }
        return out;
    }

    private SDCContext[] tryGetContext2(SDCDoubleContextFactory<Object, Object> cfact, Object... options) {
        return tryGetContext2ArrArr(cfact, get2DOptArr(2, options));
    }

    private SDCContext[] tryGetContext2ArrArr(SDCDoubleContextFactory<Object, Object> cfact, Object[][] options) {
        SDCContext[] out = new SDCContext[options.length];
        for (int i = 0; i < options.length; i++) {
            out[i] = cfact.getContext(options[i][0], options[i][1]);
        }
        return out;
    }

    private SDCContext[] tryGetContext3(SDCTripleContextFactory<Object, Object, Object> cfact, Object... options) {
        return tryGetContext3ArrArr(cfact, get2DOptArr(3, options));
    }

    private SDCContext[] tryGetContext3ArrArr(SDCTripleContextFactory<Object, Object, Object> cfact,
            Object[][] options) {
        SDCContext[] out = new SDCContext[options.length];
        for (int i = 0; i < options.length; i++) {
            out[i] = cfact.getContext(options[i][0], options[i][1], options[i][2]);
        }
        return out;
    }

    private SDCContext[] tryGetContext4(SDCQuadrupleContextFactory<Object, Object, Object, Object> cfact,
            Object... options) {
        return tryGetContext4ArrArr(cfact, get2DOptArr(4, options));
    }

    private SDCContext[] tryGetContext4ArrArr(SDCQuadrupleContextFactory<Object, Object, Object, Object> cfact,
            Object[][] options) {
        SDCContext[] out = new SDCContext[options.length];
        for (int i = 0; i < options.length; i++) {
            out[i] = cfact.getContext(options[i][0], options[i][1], options[i][2], options[i][3]);
        }
        return out;
    }

    private Object invokeMethod(Messages messages, Method method) {
        Class<?>[] argTypes = method.getParameterTypes();
        Object[] args = new Object[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            args[i] = getArgOfType(argTypes[i]);
        }

        try {
            return method.invoke(messages, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private Object getArgOfType(Class<?> clazz) {
        if (clazz == String.class) {
            return "<String Placeholder>";
        }
        if (clazz == String[].class) {
            return new String[] { "<String Array Placeholder>" };
        }
        if (clazz == int.class) {
            return -1;
        }
        if (clazz == long.class) {
            return -2;
        }
        if (clazz == double.class) {
            return -0.5D;
        }
        if (clazz == MobType.class) {
            return MobType.mooshroom;
        }
        if (clazz == WorldDescriptor.class) {
            return new WorldDescriptor(UUID.randomUUID(), "<World Name Placeholder>");
        }
        if (clazz == Object.class) { // expects enum or custom variant
            return DyeColor.GREEN;
        }
        if (clazz == List.class) {
            List<String> list = new ArrayList<>();
            list.add("<ArrayList Placeholder>");
            return list;
        }
        throw new IllegalArgumentException("Do not know how to create an instance of " + clazz + " at test time");
    }

    @Test
    @DisplayName("Checking that no placeholders are left unfilled within messages")
    public void testMesagesUsesPlaceholders() {
        SlimeDogPlugin mockPlugin = new MockSlimeDogPlugin();
        Messages messages;
        try {
            messages = new Messages(mockPlugin);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }
        for (Method method : Messages.class.getDeclaredMethods()) {
            if (!shouldTestMethod(method, messages)) { // only test public methods
                continue;
            }
            checkMethodResult(messages, method);
        }
    }

    private void checkMethodResult(Messages messages, Method method) {
        SDCMessageFactory<?> res = getMessage(messages, method);
        SDCMessage<?>[] msgs = fillMessages(res);
        int success = 0;
        for (SDCMessage<?> msg : msgs) {
            String raw;
            try {
                raw = msg.getFilled();
            } catch (ClassCastException | IllegalColorException e) {
                continue;
            }
            Assertions.assertFalse(raw.contains("{"),
                    "Message has unfilled placeholder for method: " + method.getName());
            Assertions.assertFalse(raw.contains("}"),
                    "Message has unfilled placeholder for method: " + method.getName());
            success++;
        }
        Assertions.assertTrue(success > 0, "Unable to find a suitable set of context arguments for method " + method
                + ". Had " + msgs.length + " tries");
    }

    @Test
    @DisplayName("Checking that item conversion works for all correct types")
    public void testItemMessageObjectConversion() {
        SlimeDogPlugin mockPlugin = new MockSlimeDogPlugin();
        Messages messages;
        try {
            messages = new Messages(mockPlugin);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }
        Object color = DyeColor.CYAN;
        SDCDoubleContextMessageFactory<Integer, Object> msg = messages.getDoneScanningItemMessage();
        msg.getMessage(msg.getContextFactory().getContext(10, color));
        color = Axolotl.Variant.BLUE;
        msg.getMessage(msg.getContextFactory().getContext(10, color));
        color = Cat.Type.ALL_BLACK;
        msg.getMessage(msg.getContextFactory().getContext(10, color));
        color = Fox.Type.RED;
        msg.getMessage(msg.getContextFactory().getContext(10, color));
        color = HorseVariant.getVariant(HorseColor.black, HorseStyle.black_dots);
        msg.getMessage(msg.getContextFactory().getContext(10, color));
        color = Llama.Color.GRAY;
        msg.getMessage(msg.getContextFactory().getContext(10, color));
        color = MushroomCow.Variant.BROWN;
        msg.getMessage(msg.getContextFactory().getContext(10, color));
        color = Parrot.Variant.GRAY;
        msg.getMessage(msg.getContextFactory().getContext(10, color));
        color = Rabbit.Type.BROWN;
        msg.getMessage(msg.getContextFactory().getContext(10, color));
        color = TropicalFishVariant.getVariant(TropicalFishPattern.snooper, DyeVariant.brown, DyeVariant.light_gray);
        msg.getMessage(msg.getContextFactory().getContext(10, color));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> msg.getMessage(msg.getContextFactory().getContext(10, "color")).getFilled());
    }

}
