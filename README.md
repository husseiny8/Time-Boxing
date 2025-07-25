# ⏳ Time Boxing
## Socket Programing

A Java-based desktop application that helps users practice **Time Boxing**, a time management method shown to significantly improve productivity. This project includes both a **Server** (no GUI) and a **Client** (Swing GUI) application that communicate via sockets.

This is a good Project if you want to practice **Socket Programming** in Java.
Data storage in this project is done using a **MYSQL** database.
And a Beautiful GUI with **Java Swing**
> Developed as Project 4 for the Advanced Programming course.

---

## 📌 Features

- User authentication (Sign up / Login)
- GUI-based task planning using Java Swing
- Multi-stage time boxing workflow:
  1. Task Input
  2. Task Prioritization (select top 3 important tasks)
  3. Time Scheduling for each task
- Task schedule stored on the server (via sockets)

---

## 🏗️ Architecture

### 🖥 Server

- **Technology**: Java (no GUI)
- **Responsibilities**:
  - Store all data(per user)
  - Process client socket requests (task retrieval, update, delete, etc.)

### 💻 Client

- **Technology**: Java Swing
- **Responsibilities**:
  - GUI for user interaction
  - Sends task info to server via sockets

---



## ⚙️ How to Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/husseiny8/Time-Boxing.git
   cd Time-Boxing
