# Prática 1 (Extra) - Chat com Sockets TCP

## Descrição

Nesta atividade, modifiquei o código do cliente e servidor TCP da Prática 1 para desenvolver um bate-papo em linha de comando, onde múltiplos clientes podem se comunicar através de um servidor central.

## Modificações Realizadas

### Servidor (SimpleTCPServer.java)

1. **Gerenciamento de Múltiplos Clientes:**
   - Adicionei um `Set` sincronizado para armazenar os manipuladores dos clientes (`ClientHandler`).
   - Para cada nova conexão de cliente, criei uma nova thread para gerenciar a comunicação com esse cliente.

2. **Retransmissão de Mensagens:**
   - Implementei a função `broadcastMessage` que retransmite as mensagens recebidas para todos os clientes conectados, exceto o remetente.

### Cliente (SimpleTCPClient.java)

1. **Envio e Recebimento de Mensagens:**
   - Modifiquei o cliente para permitir o envio e recebimento de mensagens contínuo utilizando threads.
   - Criei uma thread para enviar mensagens ao servidor e outra para ler mensagens do servidor.
