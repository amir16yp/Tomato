package game;

import game.ui.LogWindow;

import java.util.Arrays;

public class Logger
{
    private String className;
    private String prefix = "";

    public Logger(String className)
    {
        this.className = "[" + className + "] ";
    }

    public void addPrefix(String prefix)
    {
        this.prefix += "[" + prefix + "] ";
    }

    public void Log(String msg)
    {
        String o = this.className + this.prefix + msg;
        System.out.println(o);
        LogWindow.appendLog(o);
    }

    public void Error(String msg)
    {
        String o = this.className + this.prefix + " [ERROR] " + msg;
        System.out.println(o);
        LogWindow.appendLog(o);
    }

    public void Error(Exception err)
    {
        String o = this.className + this.prefix + " [ERROR]\n" + Arrays.toString(err.getStackTrace());
    }
}
