package dev.ratas.mobcolors.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.ratas.mobcolors.config.abstraction.ResourceProvider;

public class ConfigTest {
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
    public void testConfigEntries() {
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
        if (clazz == EntityType.class) {
            return EntityType.AREA_EFFECT_CLOUD;
        }
        if (clazz == World.class) {
            return new NamedWorld("<World Name Placeholder>");
        }
        if (clazz == Object.class) {
            return EntityType.BEE;
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
    public void testConfigUsesPlaceholders() {
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

}
