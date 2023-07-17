# Java Checkers Program

A program made as a continous project in the class SER216 using Java. Purpose of program is to practice using a software development lifecycle. Project developed over the course of 3-4 months during the 2023 Spring term.

## Features

- Console based Checkers Game
- Visual GUI Checkers
- Computer checkers player

## Notes
`app/build.gradle` needs to contain:
```
run {
    standardInput = System.in
}
```
in order to read console input from terminal line. Additonally, program needs to be executed using `./gradlew --console plain run` in project root directory since task progress bar will be present in the execution runtime
(if using gradle wrapper Java application to execute).