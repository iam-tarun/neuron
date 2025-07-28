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

## 🧠 Architecture

Neuron (Lanterna UI)
│
├── Axon (Sends Impulses)
├── ImpulseMemory (Stores chat history)
├── SynapseConnector (Outgoing socket)
└── SynapseAcceptor  (Incoming socket)
     └── SynapseHandler (Read/write wrapper per socket)

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
