# RogueEscape - Dungeon Escape Game

**RogueEscape** is an intelligent dungeon escape game where the player, as a rogue, uses strategic algorithms to evade a pursuing monster and escape the dungeon. The game leverages algorithms like **Breadth-First Search (BFS)** to calculate optimal paths and incorporates game theory to ensure the rogue's survival.

## Project Overview

**RogueEscape** combines intelligent pathfinding and strategy to create a dynamic and engaging gameplay experience. The rogue uses BFS to find the safest escape routes, while the monster employs the same algorithm to pursue the rogue. The game also implements **game theory** to model decision-making and ensure the rogue's best chance of survival.

### **Key Features**:
- **Monster Movement Algorithm**: The monster uses BFS to calculate the shortest path to the rogue and adjust its movement strategy to block escape routes.
- **Rogue Escape Algorithm**: The rogue uses BFS to find safe corridors, avoid the monster, and attempt to reach a "winning circle" for survival.
- **Real-time Simulation**: The rogue and monster interact in real-time, allowing for dynamic decision-making and gameplay.

## Installation

### Prerequisites:
- **Java 8+**
- **IDE**: IntelliJ IDEA or any Java IDE for development.

### Setup Instructions:
1. Clone the repository:
   ```bash
   git clone https://github.com/yyy1029/RogueEscape.git
````

2. Navigate to the project directory:

   ```bash
   cd RogueEscape
   ```

3. Compile and run the game:

   * Open the project in your Java IDE and run the `Game.java` class to start the game.

## Code Structure

### Main Classes:

* **Dungeon.java**: Manages the dungeon grid and game logic.
* **Rogue.java**: Defines the rogue character and implements its movement strategy to escape the monster.
* **Monster.java**: Defines the monster character and implements its movement strategy to pursue the rogue using BFS.
* **Player.java**: Contains common functionality for the rogue and player classes.

### Algorithms:

* **Monster Algorithm**: The monster uses BFS to calculate the shortest path to catch the rogue and block escape routes.
* **Rogue Algorithm**: The rogue uses BFS to find escape paths, avoid the monster, and reach a safe "winning circle."

## Technologies Used

* **Java**: Used for core game logic and implementation.
* **Breadth-First Search (BFS)**: Utilized for pathfinding and movement calculations for both the rogue and monster.
* **Game Theory**: Applied to model optimal strategies for the rogue’s escape and the monster’s pursuit.

## Contributing

We welcome contributions! If you'd like to improve the game or add new features, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request.

## License

This project is licensed under the MIT License.
