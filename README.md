# Multi-threaded Factorial Calculation Program

This project is a **console-based, multi-threaded Java program** that calculates the factorial for each number read from an input file. The program uses multiple threads to optimize processing and output, with specific rules for calculating factorials in parallel.

## Features
- **Threaded Input & Output**:
    - One thread reads numbers from the input file (`input.txt`).
    - One thread writes the results to an output file (`output.txt`).
    - A pool of threads calculates factorials for the numbers (1-100).

- **Thread Pool for Factorial Calculation**:
    - The size of the thread pool for factorial computation is defined by the user at runtime.
    - Factorial calculations are spread across multiple threads for efficient computation.

- **Rate Limiting**:
    - The program ensures that no more than **100 factorials are calculated per second**.

## Input & Output Format

1. **Input File (`input.txt`)**:
    - Contains a list of integers, each on a new line.
    - Example:
      ```
      1
      4
      6
      ```

2. **Output File (`output.txt`)**:
    - The results of factorial calculations are written in the format:
      ```
      ix (number from the input file) = ir (factorial of the number)
      ```
    - Example:
      ```
      1 = 1
      4 = 24
      6 = 720
      ```

## Program Flow

1. **Input Thread**:
    - Reads data from `input.txt` line-by-line.
    - Passes each number to the pool of threads for factorial computation.

2. **Thread Pool**:
    - The pool size is specified by the user at runtime.
    - Each thread calculates the factorial for a given number and ensures that no more than **100 numbers are processed in 1 second**.

3. **Output Thread**:
    - After the factorial is calculated, the results are passed to an output thread.
    - The output thread writes the results to `output.txt` in the same order as the numbers appeared in `input.txt`.

4. **Rate Limiting**:
    - Rate limit of 100 factorials per second.

