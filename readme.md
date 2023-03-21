build.gradle needs to contain:
    run {
        standardInput = System.in
    }
in order to read console input from terminal line

Note: needs to be executed using "./gradlew --console plain run" in project root directory 
since task progress bar will be present in the execution runtime
(if using gradle wrapper Java application to execute)