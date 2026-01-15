# Pinapp Challenge Workspace

This repo contains two modules:
- `JNotifier`: the notification library.
- `jnotifier-client`: a small client app that exercises the library (including the mock Slack integration).

## How to run

### 1) Build and test the client (recommended)
```bash
cd jnotifier-client
mvn test
```

### 2) Run the client demo
```bash
cd jnotifier-client
mvn -q -DskipTests exec:java -Dexec.mainClass=com.pinapp.App
```

### 3) Build the library (optional)
```bash
cd JNotifier
mvn test
```

## Docker (build and run the client demo)

Build the image:
```bash
docker build -t jnotifier-client .
```

Run the container:
```bash
docker run --rm jnotifier-client
```
