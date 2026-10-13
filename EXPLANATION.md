# Explain the QA Automation Framework

## Overview
This project demonstrates two test layers: Selenium drives Chrome against a local HTML demo page, while REST Assured sends HTTP requests to a temporary WireMock server. TestNG organizes test methods, Maven runs the suite, and GitHub Actions repeats the checks on pushes and pull requests.

## UI test target
`src/test/resources/demo-site.html` is intentionally local. It contains username and password inputs, a submit button, and a status message. The JavaScript accepts the demo-only credentials `qa-demo` / `Passw0rd!`, trims whitespace from the username, does not trim the password, and reports missing values or invalid credentials. It is not a secure authentication implementation.

`BaseUiTest.java` launches Chrome before each UI test, opens the local file, and closes the browser afterward. Headless mode is enabled by default for CI. `LoginPageTests.java` checks page metadata, form structure, accessibility attributes, validation, successful and failed login messages, input sensitivity, and state changes after subsequent submissions.

## API test target
`UsersApiTests.java` starts a local WireMock server on an available port. `/api/users/42` returns a known sample JSON user; unknown numeric user IDs return a JSON 404 error. Other routes and unsupported methods are not mapped and return 404. REST Assured asserts status codes, content type, and JSON fields. These are repeatable mock-contract tests, not integration tests against a real service.

## Run and read reports
Run `mvn clean test` from this project directory. Surefire reports are written to `target/surefire-reports/`. The final number of passing tests must be taken from the actual Maven or GitHub Actions result, not assumed from the number of test methods.

## Interview explanation
“I built a Java UI and API automation demo using Selenium, TestNG, REST Assured, and WireMock. The UI tests verify the behavior and accessibility attributes of a local login page; the API tests check a controlled mock contract. Maven runs the tests, and GitHub Actions executes them automatically on repository changes.”

## Limitations and next steps
The login page is a deliberately simple demo, and the API is mocked. Future improvements could include Page Object Model classes, failure screenshots, data-driven test inputs, schema validation, and testing an approved staging API with configurable environment settings.
