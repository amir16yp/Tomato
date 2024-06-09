package game;

import game.Utils;
import game.ui.LogWindow;
import java.util.Arrays;

public class Logger {
    private String className;
    private String prefix = "";
    public static LogWindow logWindow = new LogWindow();

    public Logger(String className) {
        this.className = "[" + className + "] ";
    }

    public void addPrefix(String prefix) {
        this.prefix += "[" + prefix + "] ";
    }

    public void Log(String msg) {
        String o = Utils.ConsoleColors.WHITE + this.className + this.prefix + msg + Utils.ConsoleColors.RESET;
        System.out.println(o);
        logWindow.appendLog(o);
    }

    private void popOnError()
    {
        logWindow.setLocationRelativeTo(null);
        logWindow.setVisible(true);
    }

    public void Error(String msg) {
        String o = Utils.ConsoleColors.RED + this.className + this.prefix + " [ERROR] " + msg;
        System.out.println(o);
        logWindow.appendLog(o);
        popOnError();
    }

    public void Error(Exception err) {
        String o = Utils.ConsoleColors.RED + this.className + this.prefix + " [ERROR]\n";

        // Concatenate stack trace elements into a multiline string
        StringBuilder stackTraceBuilder = new StringBuilder();
        for (StackTraceElement element : err.getStackTrace()) {
            stackTraceBuilder.append(element.toString()).append("\n");
        }
        String stackTraceString = stackTraceBuilder.toString();

        System.out.println(o + stackTraceString);
        logWindow.appendLog(o + stackTraceString);
        popOnError();
    }
}
