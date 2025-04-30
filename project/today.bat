@echo off

REM Set the lib directory containing all required JAR files
set LIB_DIR=c:\Users\temek\ohjelmoinnin-syventavat\project\

REM Compile the Java files if not already compiled
if not exist "c:\Users\temek\ohjelmoinnin-syventavat\project\target\classes" mkdir "c:\Users\temek\ohjelmoinnin-syventavat\project\target\classes"
javac -d c:\Users\temek\ohjelmoinnin-syventavat\project\target\classes -cp "%LIB_DIR%\*" c:\Users\temek\ohjelmoinnin-syventavat\project\src\main\java\tamk\ohsyte\*.java c:\Users\temek\ohjelmoinnin-syventavat\project\src\main\java\tamk\ohsyte\commands\*.java

REM Run the program with the provided subcommands
java -cp "c:\Users\temek\ohjelmoinnin-syventavat\project\target\classes;%LIB_DIR%\*" tamk.ohsyte.Today %*