# RogueEscape - Dungeon Escape Game

**RogueEscape** is an AI-driven dungeon escape game where the player, as a rogue, uses intelligent algorithms to evade a pursuing monster and escape the dungeon. The game implements pathfinding algorithms and strategy games, allowing the rogue to navigate safely through the dungeon.

## Project Overview

The primary goal of **RogueEscape** is to implement an intelligent movement strategy for both the rogue and the monster using algorithms like **Breadth-First Search (BFS)**. The game uses game theory to find the optimal path for the rogue and monster to enhance decision-making during pursuit and escape.

### **Key Features**:
- **Monster Movement**: The monster uses BFS to find the shortest path to the rogue and attempts to block escape routes.
- **Rogue Escape Strategy**: The rogue uses BFS to find the safest path to avoid the monster and reach the winning circle.
- **Real-time Simulation**: The rogue and monster interact in real-time, creating a dynamic gameplay environment.

## Installation

### Prerequisites:
- **Java 8+**
- **IDE**: IntelliJ IDEA or any Java IDE for development.

### Setup Instructions:
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/RogueEscape.git
````

2. Navigate to the project directory:

   ```bash
   cd RogueEscape
   ```

3. Install dependencies (if applicable, for any external libraries).

4. Run the project:

   * Open the project in your Java IDE and run the `Game.java` class to start the game.

## Code Structure

### Main Classes:

* **Dungeon.java**: Manages the dungeon grid and game logic.
* **Rogue.java**: Defines the rogue character and implements its escape logic.
* **Monster.java**: Defines the monster character and implements its pursuit logic using BFS.
* **Player.java**: Common functionality for player and rogue classes.

### Algorithms:

* **Monster Algorithm**: The monster uses BFS to calculate the shortest path to the rogue and attempts to block the escape route.
* **Rogue Algorithm**: The rogue uses BFS to navigate through safe corridors, avoiding the monster and seeking the winning circle.

## Technologies Used

* **Java** for core game logic and object-oriented design.
* **Breadth-First Search (BFS)** for pathfinding and movement algorithms.
* **Game Theory** to model optimal strategies for the rogue and monster.

## Contributing

We welcome contributions! If you'd like to help improve the game or add new features, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request.

## License

This project is licensed under the MIT License.

```

### **Explanation**:
- **Project Overview**: This section highlights the core mechanics of the game, focusing on the rogue's escape strategy and monster's pursuit using BFS and game theory.
- **Installation**: Instructions for setting up and running the game locally.
- **Code Structure**: Describes the main classes and algorithms, helping others understand the architecture.
- **Technologies Used**: Lists the main technologies and algorithms employed.
- **Contributing**: Provides instructions for others who want to contribute to the project.
- **License**: You can change the license type (currently MIT) based on your preference.
