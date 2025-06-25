# Chess Game

A Chess Game Engine implemented in Java with a graphical user interface (GUI) using Swing.

## Project Structure

```
.
├── images/               # Contains all image assets (pieces, board)
├── src/                  # Contains all Java source code
│   ├── Engine/           # Implements the chess engine and AI logic
│   │   └── Scorer.java   # (AI uses Minimax with alpha-beta pruning & move ordering)
│   ├── board/            # Manages the chessboard, pieces, and GUI
│   ├── promotion/        # Handles UI for pawn promotion
│   ├── runner/           # Handles game initialization and execution
│   ├── timer/            # Contains helper classes, potentially for timing or AI recursion depth
│   └── win/              # Manages UI elements for win/loss/draw messages
├── .classpath            # Eclipse project build path
├── .gitignore            # Specifies intentionally untracked files for Git
├── .project              # Eclipse project configuration
├── module-info.java      # (If using Java modules) Defines module dependencies
└── README.md             # This file
```

**Key Packages:**

-   `src/Engine`: Implements the chess engine. The AI uses the Minimax algorithm with alpha-beta pruning and move ordering heuristics (evident from `Helper.recursiveBoardRunner` and its usage in `BoardFrame` for the "Play Best" feature, and `Scorer.java` for board evaluation).
-   `src/board`: Contains the core logic for the chessboard, piece representation, move validation, and the main graphical user interface (`BoardFrame.java`).
-   `src/promotion`: Handles the user interface for pawn promotion.
-   `src/runner`: Includes the main application class (`App.java`) to start the game and the `Game.java` class for game setup.
-   `src/timer`: The `Helper.java` class in this package seems to be crucial for the AI's recursive move calculation and depth management.
-   `src/win`: Manages pop-up dialogs for displaying game end states like checkmate or stalemate.
-   `images/`: Stores all graphical assets like piece sprites and the board background.

## How to Build and Run

This project is built using Java and can be run in a few ways:

**1. Using an IDE (Recommended):**

   - **Eclipse:**
     - Import the project into your Eclipse workspace (`File > Import > General > Existing Projects into Workspace`).
     - Locate the `src/runner/App.java` file.
     - Right-click on `App.java` and select `Run As > Java Application`.
   - **IntelliJ IDEA:**
     - Open the project directory with IntelliJ IDEA (`File > Open`).
     - IntelliJ should automatically detect it as a Java project.
     - Locate the `src/runner/App.java` file.
     - Right-click on `App.java` and select `Run 'App.main()'`.

**2. Using Command Line:**

   - **Prerequisites:** Make sure you have the Java Development Kit (JDK) installed and configured on your system.
   - **Compile:**
     ```bash
     # Navigate to the project's root directory
     cd path/to/your/chess-game-project

     # Create a directory for compiled classes (e.g., 'bin') if it doesn't exist
     mkdir -p bin

     # Compile the Java source files
     javac -d bin -sourcepath src src/runner/App.java src/module-info.java
     # If you are not using modules (i.e., no module-info.java), you might compile like this:
     # find src -name "*.java" > sources.txt
     # javac -d bin @sources.txt
     # rm sources.txt
     ```
   - **Run:**
     ```bash
     # Run the application
     java -cp bin runner.App
     ```
     *(Note: If you used `module-info.java`, the run command might be slightly different, e.g., `java --module-path bin -m chessgame/runner.App` where `chessgame` is your module name defined in `module-info.java`)*

## Features

- **Graphical User Interface (GUI):** A visual chessboard where players can interact with pieces.
- **Player vs. AI:** Play against a computer opponent.
- **Move Validation:** The game enforces legal chess moves.
- **Special Moves:**
    - **Pawn Promotion:** Pawns are automatically promoted to Queens upon reaching the opposite end of the board. (Functionality for choosing other pieces might be present via `PopupMenuPromotion.java`).
    - **Castling:** Kingside and Queenside castling are implemented.
    - **En Passant:** Capturing "in passing" is supported for pawns.
- **Game State Detection:**
    - **Check:** The game indicates when a King is in check.
    - **Checkmate:** Detects and declares a winner when a King is checkmated.
    - **Stalemate:** Detects and declares a draw in stalemate situations.
- **Undo Move:** An "Undo" button is available to revert the last move.
- **AI Best Move Suggestion:** A "Play Best" button allows the AI to suggest and play its best calculated move (based on a certain search depth).
- **Visual Feedback:**
    - Selected pieces are highlighted.
    - Possible moves for a selected piece are indicated.
    - Kings in check are visually marked.

## Prerequisites

- **Java Development Kit (JDK):** Version 8 or higher is recommended. You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase-downloads.html) or use an open-source distribution like OpenJDK.

## Future Improvements / Known Issues

- **AI Difficulty Levels:** Implement different difficulty settings for the AI.
- **Customizable Pawn Promotion:** Allow players to choose which piece to promote a pawn to (Queen, Rook, Bishop, or Knight). Currently, it defaults to Queen, though `PopupMenuPromotion.java` suggests the UI for this might be partially implemented.
- **Network Play:** Add functionality for two human players to play over a network.
- **Save/Load Game:** Allow users to save the current game state and load it later.
- **More Robust Error Handling:** Improve error handling and user feedback for invalid operations.
- **Code Refinements:**
    - The `BoardFrame.java` class is quite large and could be refactored for better separation of concerns (e.g., move logic, UI updates, game state management).
    - Some parts of the code, especially within `BoardFrame.java` for move generation and validation, could be further optimized or clarified.
- **Testing:** Add unit tests and integration tests to ensure code quality and prevent regressions.
