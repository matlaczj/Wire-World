# Wire World

<img src="https://th.bing.com/th/id/OIG.czlkGFZfN8SaPRioW8hw?pid=ImgGn" width="300" height="300" style="display: block; margin: auto;">

## Table of Contents
- [Description](#description)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [File Structure](#file-structure)
- [Data Format](#data-format)
- [Extensions](#extensions)
- [Testing](#testing)
- [Contributing](#contributing)
- [Documentation](#documentation)

## Description

The Wire World is a real-time simulation program that models the behavior of selected cellular automata. It provides a graphical interface for both technical and non-technical users, suitable for recreational or scientific purposes.

## Features

- Real-time simulation of cellular automata
- Dynamic display of the current state
- Save and load simulation states
- User-friendly interface with options to customize the board size
- Manual modification of cell states
- Support for two default automata: WireWorld and Game of Life
- Extension possibilities for additional automata

## Getting Started

### Prerequisites

- Java Runtime Environment (JRE)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/matlaczj/WireWorld
   ```

2. Navigate to the project directory:
   ```bash
   cd WireWorld
   ```

3. Run the executable JAR file:
   ```bash
   java -jar main.jar
   ```

## Usage

1. Upon launching the program, select the type of automaton.
2. A default-sized empty board is generated, which can be modified.
3. Click on cells to change their state or load an entire board from a file.
4. Set the number of generations and observe the simulation.
5. Save the current generation to a file at any point.

## File Structure

Inside the directory containing the executable JAR file, you'll find two subdirectories:

- `output_states`: Default location for saving automaton generations.
- `structures`: Contains default structures for the automaton, with the option to extend.

## Definitions

- **Generation:** Collection of cell states comprising the board at a specific moment.
- **Board:** Collection of cells that can have various states.
- **Simulation:** Reproduction of cellular automaton behavior.

## Extensions

The program, by default, supports WireWorld and Game of Life automata. To add new automata, follow the provided extension guidelines.

## Testing

Program was tested. 

## Contributing

Feel free to contribute.

## Documentation

Please read the reports.