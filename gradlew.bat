@rem Gradle bootstrap for Windows. Fetches gradle-wrapper.jar if missing.
@echo off
set APP_HOME=%~dp0
set JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
if not exist "%JAR%" (
  echo Downloading gradle-wrapper.jar ...
  powershell -Command "Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/v8.9.0/gradle/wrapper/gradle-wrapper.jar' -OutFile '%JAR%'"
  if errorlevel 1 (
    echo Could not download gradle-wrapper.jar. Open the project in Android Studio instead.
    exit /b 1
  )
)
java -classpath "%JAR%" org.gradle.wrapper.GradleWrapperMain %*
