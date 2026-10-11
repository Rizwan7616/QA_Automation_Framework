# QA Automation Framework — UI + API

A portfolio-ready Java test automation project that demonstrates browser automation, API assertions, test organization, and repeatable execution.

## Stack
- Java 17
- Maven
- Selenium WebDriver
- TestNG
- REST Assured
- WireMock
- Chrome / ChromeDriver (Selenium Manager can resolve the driver)

## What is covered?
### UI tests
The tests use a small local login page in `src/test/resources/demo-site.html`. They check:
1. Valid credentials display a welcome message.
2. Empty fields show a validation message.
3. Incorrect credentials are rejected.

Demo-only credentials: `qa-demo` / `Passw0rd!`. This is not a real authentication system and must not be used for real accounts.

### API tests
WireMock starts a temporary local mock API for the test run. REST Assured checks:
1. A known user returns HTTP 200 and the expected JSON fields.
2. An unknown user returns HTTP 404 and an error body.
3. The response has a content-type header.

Using a mock API makes the suite repeatable and avoids depending on a third-party API or sending data over the internet.

## Prerequisites
- JDK 17 or newer
- Maven 3.9+
- Google Chrome installed
- Internet access for the first Maven dependency download and Selenium Manager driver setup

Check your tools:
```powershell
java -version
mvn -version
```

## Run the tests
From this directory:
```powershell
mvn clean test
```

Run with the browser visible:
```powershell
mvn clean test -Pheaded
```

The default profile runs Chrome headlessly, which is suitable for CI. Test reports are written under:
`target/surefire-reports/`

## CI
`.github/workflows/qa-tests.yml` runs the suite on pushes and pull requests. The workflow installs Java, installs Chrome, and runs Maven tests.

## Suggested next upgrades
- Add Page Object Model classes as the UI grows.
- Capture screenshots automatically when a UI test fails.
- Add JSON schema checks and more API edge cases.
- Add a real test environment base URL via a Maven property, once a real application is available.
- Publish test reports as CI artifacts.

## Important portfolio note
This project tests a deliberately small demo page and a mocked API. Describe it honestly as a test automation framework demonstration, not as testing a production client's system.
