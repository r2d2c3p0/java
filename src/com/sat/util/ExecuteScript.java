package com.sat.util;

import java.io.IOException;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

public class ExecuteScript {
	
    int returnCode;
    String command;

    public void runScript(String inputCommand){
        command = inputCommand;
        CommandLine CmdLine = CommandLine.parse(command);
        DefaultExecutor DExecutor = new DefaultExecutor();
        DExecutor.setExitValue(0);
        try {
            returnCode = DExecutor.execute(CmdLine);
        } catch (ExecuteException ee) {
            System.err.println("ExecuteException - Execution failed.");
            ee.printStackTrace();
        } catch (IOException ioe) {
            System.err.println("IOException - Permission denied.");
            ioe.printStackTrace();
        }
    }

    public static void main(String args[]){
        ExecuteScript testScript = new ExecuteScript();
        String finalCommand = args[0]+" "+args[1];
        testScript.runScript(finalCommand);
    }
}