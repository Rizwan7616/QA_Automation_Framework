# QA Automation Framework — UI + API

A Java test automation project demonstrating browser checks, API contract assertions, isolated mock services, and CI execution.

## Stack
- Java 17+ and Maven
- Selenium WebDriver + Chrome
- TestNG
- REST Assured
- WireMock
- GitHub Actions

## Current coverage
The suite contains **42 test invocations defined by 42 independent `@Test` methods**: 26 UI checks and 16 API checks. The actual pass/fail count must be confirmed by running the suite in your environment and in GitHub Actions.

### UI checks (26)
`src/test/resources/demo-site.html` is a small local demo login page. Tests cover page metadata and content, field visibility and attributes, label associations, accessible status messaging, blank-field validation, valid and invalid credentials, case sensitivity, username trimming, password whitespace, and changing from failed to successful login (and vice versa).

Demo-only credentials: `qa-demo` / `Passw0rd!`. This is **not** a real authentication system and must not be used for real accounts.

### API checks (15)
WireMock starts a temporary local mock API. REST Assured verifies a known user response, status code, JSON content type, individual JSON fields, expected fields, unknown-user errors, unknown routes, unsupported methods, and a trailing-slash route mismatch. These tests validate the defined mock contract and test-client assertions; they do **not** verify a production or deployed backend.

## Prerequisites
- JDK 17 or newer
- Maven 3.9+
- Google Chrome installed (or Chromium configured for your environment)
- Internet access for the first Maven dependency download and Selenium Manager setup

Check tools:
```powershell
java -version
mvn -version
```

## Run
From this directory:
```powershell
mvn clean test
```
Run with a visible browser:
```powershell
mvn clean test -Pheaded
```
Reports are written to `target/surefire-reports/`.

## CI
`.github/workflows/qa-tests.yml` runs the suite on pushes and pull requests, and uploads Surefire reports as a workflow artifact. A green workflow confirms only that these covered scenarios passed in that CI environment; it does not prove the application has no defects.

## Honest portfolio description
Describe this as a self-contained UI/API automation demonstration. The browser tests exercise a local demo page and the API tests exercise a WireMock mock server, not a client production system.
