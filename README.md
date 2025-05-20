# Rush Hour Game Solver 🚗⌚
![image](https://github.com/user-attachments/assets/6467464d-d989-441f-a940-a19d42566c2f)

## Overview
This program is an automated solver for the classic Rush Hour puzzle game, where the goal is to move the red car out of a congested (commonly) 6x6 grid by sliding other vehicles out of its way. This solver uses a pathfinding algorithm to efficiently find a sequence of moves that leads the red car to the exit. Specifically, it treats each game state as a state and explores possible moves using uninformed and informed search strategies. 

## Project Structure
```
├── .vscode/
├── bin/
├── doc/
├── lib/
│   └── javafx-sdk-24.0.1/
├── src/
│   ├── algorithms/
│   │   ├── Algorithm.java
│   │   ├── AStarSolver.java
│   │   ├── BeamSearchSolver.java
│   │   ├── GreedyBFSSolver.java
│   │   ├── Heuristic.java
│   │   └── UCSSolver.java
│   ├── gui/
│   │   ├── bg.png
│   │   ├── Block.java
│   │   ├── LandingPage.java
│   │   ├── MainApp.java
│   │   ├── MainBoardPage.java
│   │   └── style.css
│   ├── utils/
│   │   ├── Position.java
│   │   ├── RushHourGame.java
│   │   ├── SaveLoad.java
│   │   ├── State.java
│   │   ├── Vehicle.java
│   │   └── Main.java
├── test/

```
## Algorithms
This section explains the algorithm used in this program, including Uniform Cost Search, Greedy Best First Search, A*, and Beam Search in a brief. Please refer to our [full report](./doc/) for more complete explaination and analysis.

### Uniform Cost Search
The solver implements UCS algorithm using a priority queue to explore possible game states based on their estimated cost to reach the solution. Starting from the initial board configuration, it repeatedly selects the state with the lowest cost, checks if it is the goal, and if not, generates all valid neighboring states by moving vehicles. It keeps track of explored states to avoid revisiting them, ensuring efficiency. This process continues until the goal state—where the red car can exit—is found, or all possibilities are exhausted. The algorithm then reconstructs the sequence of moves leading to the solution, providing an optimal or near-optimal path to solve the puzzle.

### Greedy Best First Search
This solver also uses a greedy best-first search algorithm guided by a heuristic that estimates how close each state is to the goal. It maintains a priority queue of unexplored states, always expanding the state with the lowest heuristic value first. For each explored state, it generates all possible next moves (neighboring states), sorts them by their heuristic values, and adds them back into the queue. To avoid redundant work, it keeps track of already explored states. The search continues until the goal state—where the red car can exit—is found or all options are exhausted. This approach efficiently prioritizes promising moves, often reaching a solution faster than uninformed searches.

### A*
This solver employs the A* pathfinding algorithm to efficiently find the optimal sequence of moves to solve the Rush Hour puzzle. It uses a priority queue ordered by the combined cost f(n) = g(n) + h(n), where g(n) is the exact cost from the start to the current state, and h(n) is a heuristic estimate of the remaining cost to reach the goal. The algorithm keeps track of explored states to avoid revisiting them and maintains the best known cost to reach each state. For every explored state, it generates neighboring states, updates their costs if a better path is found, and adds them to the queue for further exploration. The search terminates once the goal state—where the red car can exit—is reached, returning the shortest path solution.

## How to Compile and Run the Program
1. Install [Java](https://www.oracle.com/java/technologies/downloads/?er=221886) in your computer.
2. Clone this repository from terminal with this command:
```
$ git clone https://github.com/najwakahanifatima/Tucil3_13523043_13523080.git
```
### Run the application (CLI)
Run the program on CLI by running the following command:
```
$ java -cp bin Main
```
### Run the application (GUI)
Run the program on JavaFX (GUI) by running the following command:
```
$ java --module-path lib/javafx-sdk-24.0.1/lib --add-modules javafx.controls -cp bin gui.MainApp  
```
or re-compile and auto run by using
```
$ ./run.bat
```

### Run after updates
To run the program after doing updates, you can add a compile 
```
$ ./run.bat
```

## Available Scripts
In the project directory, you can run:
```
./run.bat
```
This will compile and runs program in GUI. Please make sure to adjust run.bat if there is any changes in file structure.

## How to use the program
After running the program, upload/write your input file (refer to /test folder for dummy) and choose the algorithm (and) the heuristic.

## Contributors 
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/najwakahanifatima">
        <img src="https://avatars.githubusercontent.com/najwakahanifatima" width="80" style="border-radius: 50%;" /><br />
        <span><b>Najwa Kahani Fatima </br> 13523043</b></span>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/DiyahSusan">
        <img src="https://avatars.githubusercontent.com/DiyahSusan" width="80" style="border-radius: 50%;" /><br />
        <span><b> Diyah Susan Nugrahani </br> 13523080 </b></span>
      </a>
    </td>
  </tr>
</table>

