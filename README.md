# Neuron P2P Messaging – Phase 1

A terminal-based peer-to-peer messaging system built in Java using Lanterna for UI and Java Sockets for bidirectional communication.

---

## 🚀 Features (Phase 1)

- ✅ **Lanterna UI**: Clean, responsive terminal interface using [Lanterna](https://github.com/mabe02/lanterna)
- ✅ **Bidirectional Communication**: Java sockets enable real-time message exchange between peers
- ✅ **Unique Neuron Identity**: Each peer generates a UUID on startup for consistent identification
- ✅ **Impulse System**: Messages are modeled as `Impulse` objects, supporting flexible message types
- ✅ **Synapse Layer**: Handles socket connections (both accepting and connecting) with automatic peer registration
- ✅ **UI Integration**: Messages sent/received through sockets are instantly reflected in the terminal interface

---

## Phase 2 Features

### ✅ Core Goals

- [ ] **Event-Driven Architecture**
  - Define core events: `PeerConnectedEvent`, `PeerDisconnectedEvent`, `MessageReceivedEvent`, etc.
  - Implement a central `EventBus` or `EventDispatcher` with pub/sub capabilities.
  - Decouple message handling logic from socket threads.

- [ ] **Event Loop System**
  - Design a central `EventLoop` using a `BlockingQueue` to handle events sequentially.
  - Move toward a JavaScript-like single-threaded architecture.
  - Prepare for potential NIO-based non-blocking socket upgrade.

- [ ] **Upgrade UI Framework**
  - Replace Lanterna with a more advanced but easy-to-use UI framework.
    - **JavaFX** (FXML-based, event-driven, CSS-styled).
  - Design UI components for:
    - Peer status (online/offline)
    - Message feed
    - Input box with send action
    - Notification popups

## 🔧 Additional Enhancements

- [ ] **Centralized Logging**
  - Use SLF4J + Logback or another logging framework.
  - Include timestamps, event types, and error logs.

- [ ] **Configuration Management**
  - Externalize config (YAML or JSON) for:
    - Port number
    - Peer ID/Username
    - Logging level
    - UI preferences

- [ ] **Local Message Persistence**
  - Save messages to file or lightweight DB (like SQLite).
  - Option to load recent chat history on startup.

- [ ] **Robust Error Handling**
  - Graceful handling of peer disconnects and socket failures.
  - Optional retry mechanism for transient errors.

- [ ] **Plugin-Friendly Design**
  - Begin modularizing code for future extensions like:
    - File sharing
    - Encryption
    - Peer discovery

---

## 📦 Modules Overview

| Module    | Description                                                    |
| --------- | -------------------------------------------------------------- |
| `neuron`  | Entry point + Lanterna UI                                      |
| `synapse` | Socket connection manager (`acceptor`, `connector`, `handler`) |
| `impulse` | Data model for messages and in-memory storage                  |
| `axon`    | Sends messages to connected peers                              |

---

## 🔧 How to Run

1. Clone the repo
2. Build with Maven or your preferred IDE
3. Run two instances (on different ports):

   ```bash
   java -jar neuron.jar --port=9001
   java -jar neuron.jar --port=9000
   ```
4. Connect them from the UI using IP + Port
5. Start chatting!

---

## 📌 Notes

* A unique Neuron ID (UUID) is generated at startup and used to identify each peer
* Connections are registered using a `REGISTER` impulse
* UI shows message history by peer ID
* This version does **not** support usernames or persistence (planned in later phases)

---

## 🧭 Roadmap

### Phase 2 (Planned):

* Add support for usernames + persistent IDs
* Message timestamps and system messages
* Message encryption
* File transfer support
* Improved UI layout (split view, notifications)

---

## 📜 License

MIT License

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first.
```
