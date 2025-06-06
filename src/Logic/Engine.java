package Logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Engine
{
    private Process engineProcess;
    private BufferedReader processReader;
    private BufferedWriter processWriter;

    public boolean startEngine(String pathToEngine)
    {
        try
        {
            engineProcess = new ProcessBuilder(pathToEngine).redirectErrorStream(true).start();
            processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
            processWriter = new BufferedWriter(new OutputStreamWriter(engineProcess.getOutputStream()));
            return true;
        }
        catch (IOException e)
        {
            System.err.println("Error starting Stockfish: " + e.getMessage());
            return false;
        }
    }

    public void sendCommand(String command) throws IOException
    {
        processWriter.write(command + "\n");
        processWriter.flush();
    }

    public String getOutput(long waitTimeMillis) throws IOException
    {
        StringBuilder output = new StringBuilder();
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < waitTimeMillis)
        {
            if (processReader.ready()) {
                String line = processReader.readLine();
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }
    public String getBestMove(String fen, int depth) throws IOException
    {
        sendCommand("uci");
        sendCommand("ucinewgame");
        sendCommand("position fen " + fen);
        sendCommand("go depth " + depth);
        String output = getOutput(2000);
        for (String line : output.split("\n"))
        {
            if (line.startsWith("bestmove"))
            {
                return line.split(" ")[1];
            }
        }
        return null;
    }
    public void stopEngine() throws IOException
    {
        sendCommand("quit");
        engineProcess.destroy();
    }

}
