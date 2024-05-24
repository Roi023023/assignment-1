package test;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler {
    private PrintWriter out;
    private Scanner in;

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        out = new PrintWriter(outToClient);
        in = new Scanner(inFromClient);
        String line = in.nextLine();
        String[] parts = line.split(",");
        String command = parts[0];
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);

        boolean result;
        if (command.equals("Q")) {
            result = DictionaryManager.get().query(args);
        } else if (command.equals("C")) {
            result = DictionaryManager.get().challenge(args);
        } else {
            result = false;
        }

        out.println(result ? "true" : "false");
        out.flush();
    }

    @Override
    public void close() {
        in.close();
        out.close();
    }
}