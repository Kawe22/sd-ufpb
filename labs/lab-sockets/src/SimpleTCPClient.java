import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SimpleTCPClient {
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;

    public void start(String serverIp, int serverPort) throws IOException {
        System.out.println("[C1] Conectando com servidor " + serverIp + ":" + serverPort);
        socket = new Socket(serverIp, serverPort);
        output = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("[C2] Conexão estabelecida, eu sou o cliente: " + socket.getLocalSocketAddress());

        Thread sendMessage = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Digite uma mensagem: ");
                String msg = scanner.nextLine();
                output.println(msg);
                System.out.println("[C3] Mensagem enviada: " + msg);
            }
        });

        Thread readMessage = new Thread(() -> {
            String msg;
            try {
                while ((msg = input.readLine()) != null) {
                    System.out.println("[C5] Mensagem recebida: " + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        sendMessage.start();
        readMessage.start();
    }

    public void stop() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverIp = "localhost";
        int serverPort = 6666;
        try {
            SimpleTCPClient client = new SimpleTCPClient();
            client.start(serverIp, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
