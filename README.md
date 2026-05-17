<div align="center">

# 🏨 Hotel Reservation System

**A full-featured hotel management desktop application built with Java Swing**

[![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.java.com)
[![UI](https://img.shields.io/badge/UI-Java%20Swing-5C5CFF?style=flat-square)](https://docs.oracle.com/javase/8/docs/api/javax/swing/package-summary.html)
[![Storage](https://img.shields.io/badge/Storage-Flat%20File%20CSV-4CAF50?style=flat-square)](/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)

</div>

---

## 📋 Table of Contents

- [About](#-about)
- [Features](#-features)
- [Screenshots](#-screenshots)
- [Project Structure](#-project-structure)
- [Room Types & Pricing](#️-room-types--pricing)
- [Getting Started](#-getting-started)
- [Default Credentials](#-default-credentials)
- [Data Storage](#-data-storage)
- [License](#-license)

---

## 🧾 About

Hotel Reservation System is a desktop application for managing day-to-day hotel operations. Built entirely in Java with a Swing GUI, it requires no external database — all data is persisted in plain CSV text files. The system supports two user roles (admin and reception) with role-based access to different modules.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔐 Role-based Login | `admin` and `reception` accounts with different access levels |
| 🛏️ Room Management | Add/delete rooms by type with auto-calculated prices and availability tracking |
| 👥 Customer Management | Register and manage guest records |
| 📅 Reservations | Assign customers to rooms with check-in/check-out dates and services |
| 🧾 Invoice Generation | Auto-calculate and display invoices per reservation |
| 🛎️ Services | Define and attach extra hotel services to rooms and reservations |
| 👨‍💼 Employee Management | Manage staff records — visible to `admin` only |
| ⏰ Near Checkout Alert | Filter rooms with guests checking out within the next 48 hours |
| 💾 File Persistence | All data saved in plain `.txt` CSV files — no database setup required |

---

## 🖼️ Screenshots

> _Add screenshots of your application here after running it._

```
📸 Login Dialog        →  /screenshots/login.png
📸 Rooms Panel         →  /screenshots/rooms.png
📸 Reservations Panel  →  /screenshots/reservations.png
📸 Invoice View        →  /screenshots/invoice.png
```

---

## 📁 Project Structure

```
HotelReservationSystem/
│
├── src/
│   │
│   ├── ── Entry Point ──
│   ├── HotelApp.java               # Main entry — launches login & tabbed window
│   │
│   ├── ── UI Panels ──
│   ├── LoginDialog.java            # Login dialog with role-based access control
│   ├── RoomsPanel.java             # Room management (add, delete, filter, assign)
│   ├── CustomersPanel.java         # Customer registration and management
│   ├── ReservationsPanel.java      # Reservations list, invoice generation
│   ├── ServicesPanel.java          # Hotel services management
│   ├── EmployeesPanel.java         # Employee management (admin only)
│   ├── AssignRoomDialog.java       # Dialog to assign a room to a customer
│   │
│   ├── ── Models ──
│   ├── Room.java                   # Room entity with CSV serialization
│   ├── Customer.java               # Customer entity
│   ├── Reservation.java            # Reservation entity with services list
│   ├── Employee.java               # Employee entity
│   ├── ServiceItem.java            # Hotel service entity
│   ├── User.java                   # Auth user entity
│   │
│   └── ── File I/O Managers ──
│       ├── RoomManager.java        # Read/write rooms.txt
│       ├── CustomerManager.java    # Read/write customers.txt
│       ├── ReservationManager.java # Read/write reservations.txt
│       ├── EmployeeManager.java    # Read/write employees.txt
│       ├── ServiceManager.java     # Read/write services.txt
│       ├── FileManager.java        # Generic file utilities
│       └── FileUtils.java          # CSV read/write helpers
│
├── rooms.txt                       # Rooms data
├── customers.txt                   # Customers data
├── reservations.txt                # Reservations data
├── employees.txt                   # Employees data
├── services.txt                    # Services data
└── users.txt                       # Login credentials
```

---

## 🛏️ Room Types & Pricing

| Type | Price / Night |
|---|---:|
| 🛏️ Single | $100 |
| 🛏️🛏️ Double | $150 |
| 🛏️🛏️ Twin | $180 |
| 🏰 Suite | $300 |
| ✨ Deluxe | $400 |
| 👨‍👩‍👧‍👦 Family | $800 |

> Prices are fixed and auto-assigned when selecting a room type from the UI.

---

## 🚀 Getting Started

### Prerequisites

- Java JDK **8 or higher** installed
- Any terminal / command prompt

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/HotelReservationSystem.git
cd HotelReservationSystem
```

### 2. Compile

```bash
cd src
javac *.java
```

### 3. Run

> ⚠️ **Important:** Run from the **project root directory** (not inside `src/`), so the `.txt` data files are found correctly.

```bash
cd ..
java -cp src HotelApp
```

---

## 🔑 Default Credentials

On the first run, if `users.txt` is not found, the system auto-creates it with these default accounts:

| Username | Password | Role | Access |
|---|---|---|---|
| `admin` | `1234` | Administrator | All tabs including Employees |
| `reception` | `5678` | Receptionist | Customers, Rooms, Services, Reservations |

> Credentials are stored in `users.txt` as comma-separated values and loaded at login.

---

## 💾 Data Storage

All data is stored locally in plain-text CSV files — no database setup required.

| File | Contents |
|---|---|
| `users.txt` | Login credentials (`username,password`) |
| `rooms.txt` | Room records (`number,type,price,busy,services`) |
| `customers.txt` | Customer records |
| `reservations.txt` | Reservation records with service lists |
| `employees.txt` | Employee records |
| `services.txt` | Available hotel services |

---

## 🛠️ Tech Stack

- **Language:** Java (JDK 8+)
- **GUI Framework:** Java Swing
- **Data Persistence:** Flat-file CSV (no external DB)
- **Build:** Manual `javac` — no Maven or Gradle needed

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

<div align="center">
  Made with ☕ Java &nbsp;·&nbsp; No frameworks. No databases. Just code.
</div>
