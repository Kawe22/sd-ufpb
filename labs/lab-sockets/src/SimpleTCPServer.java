import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleTCPServer {

    private static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        int serverPort = 6666;
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("[S1] Servidor de chat iniciado na porta " + serverPort);

            while (true) {
                System.out.println("[S2] Aguardando conexão...");
                Socket socket = serverSocket.accept();
                System.out.println("[S3] Conexão estabelecida com cliente: " + socket.getRemoteSocketAddress());
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message, ClientHandler excludeClient) {
        synchronized (clientHandlers) {
            System.out.println("[S6] Retransmitindo mensagem: " + message);
            for (ClientHandler client : clientHandlers) {
                if (client != excludeClient) {
                    client.sendMessage(message);
                }
            }
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.out = new PrintWriter(socket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println("[S4] Mensagem recebida: " + message);
                    broadcastMessage(message, this);
                }
            } catch (IOException e) {
                System.out.println("[S5] Conexão com cliente perdida: " + socket.getRemoteSocketAddress());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clientHandlers.remove(this);
            }
        }

        public void sendMessage(String message) {
            out.println(message);
            System.out.println("[S7] Mensagem enviada: " + message);
        }
    }
}
