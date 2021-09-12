package dev.ratas.mobcolors.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.DyeColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.ratas.mobcolors.config.abstraction.ResourceProvider;
import dev.ratas.mobcolors.config.mob.MobType;
import dev.ratas.mobcolors.config.mock.DummyResourceProvider;
import dev.ratas.mobcolors.config.mock.FileResourceProvider;
import dev.ratas.mobcolors.config.variants.DyeVariant;
import dev.ratas.mobcolors.config.variants.TropicalFishPattern;
import dev.ratas.mobcolors.utils.WorldDescriptor;

public class MessagesTests {
    private static final Logger LOGGER = Logger.getLogger("[MobColors TEST]");

    @Test
    @DisplayName("Checking that all public methods within Messages class return strings")
    public void testMessagesAreStrings() {
        ResourceProvider fileProvider = new FileResourceProvider(LOGGER);
        Messages messages;
        try {
            messages = new Messages(fileProvider);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }
        for (Method method : Messages.class.getDeclaredMethods()) {
            if (!method.canAccess(messages)) { // only test public methods
                continue;
            }
            Object res = invokeMethod(messages, method);
            Assertions.assertTrue(res instanceof String, "Messages need to be strings");
        }
    }

    @Test
    @DisplayName("Checking that messages are the same in code and in messages.yml")
    public void testParityOfMessageEntries() {
        ResourceProvider fileProvider = new FileResourceProvider(LOGGER);
        ResourceProvider dummyProvider = new DummyResourceProvider(LOGGER);
        Messages fileMessages;
        Messages codeMessages;
        try {
            fileMessages = new Messages(fileProvider);
            codeMessages = new Messages(dummyProvider);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }
        for (Method method : Messages.class.getDeclaredMethods()) {
            if (!method.canAccess(fileMessages)) { // only test public methods
                continue;
            }
            checkMethod(fileMessages, codeMessages, method);
        }
    }

    private void checkMethod(Messages file, Messages code, Method method) {
        String inFile = getString(file, method);
        String inCode = getString(code, method);
        Assertions.assertEquals(inFile, inCode, "Issue with " + method.getName() + " method");
    }

    // Can be used after the first test makes sure they are all strings
    public String getString(Messages messages, Method method) {
        return (String) invokeMethod(messages, method);
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
        ResourceProvider fileProvider = new FileResourceProvider(LOGGER);
        Messages messages;
        try {
            messages = new Messages(fileProvider);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }
        for (Method method : Messages.class.getDeclaredMethods()) {
            if (!method.canAccess(messages)) { // only test public methods
                continue;
            }
            checkMethodResult(messages, method);
        }
    }

    private void checkMethodResult(Messages messages, Method method) {
        String res = getString(messages, method);
        Assertions.assertFalse(res.contains("{"), "Message has unfilled placeholder for method: " + method.getName());
        Assertions.assertFalse(res.contains("}"), "Message has unfilled placeholder for method: " + method.getName());
    }

    @Test
    @DisplayName("Checking that item conversion works for all correct types")
    public void testItemMessageObjectConversion() {
        ResourceProvider fileProvider = new FileResourceProvider(LOGGER);
        Messages messages;
        try {
            messages = new Messages(fileProvider);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }
        Object color = DyeColor.CYAN;
        messages.getDoneScanningItemMessage(color, 10);
        color = Axolotl.Variant.BLUE;
        messages.getDoneScanningItemMessage(color, 10);
        color = Cat.Type.ALL_BLACK;
        messages.getDoneScanningItemMessage(color, 10);
        color = Fox.Type.RED;
        messages.getDoneScanningItemMessage(color, 10);
        color = HorseVariant.getVariant(Horse.Color.BLACK, Horse.Style.BLACK_DOTS);
        messages.getDoneScanningItemMessage(color, 10);
        color = Llama.Color.GRAY;
        messages.getDoneScanningItemMessage(color, 10);
        color = MushroomCow.Variant.BROWN;
        messages.getDoneScanningItemMessage(color, 10);
        color = Parrot.Variant.GRAY;
        messages.getDoneScanningItemMessage(color, 10);
        color = Rabbit.Type.BROWN;
        messages.getDoneScanningItemMessage(color, 10);
        color = TropicalFishVariant.getVariant(TropicalFishPattern.snooper, DyeVariant.brown, DyeVariant.light_gray);
        messages.getDoneScanningItemMessage(color, 10);
        Assertions.assertThrows(IllegalArgumentException.class, () -> messages.getDoneScanningItemMessage("color", 10));
    }

}
