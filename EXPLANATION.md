# Explain the QA Automation Framework (study guide)

This document explains the project in plain language. The Java source files intentionally stay focused; the explanations live here rather than in long code comments.

## 1. The overall idea

A QA automation framework is reusable test code. Instead of manually opening a page and repeating the same checks, the tests drive a browser or send HTTP requests and compare the actual results with expected results.

This project has two test layers:

- **UI (user interface):** Selenium controls Chrome the way a person would.
- **API (application programming interface):** REST Assured sends HTTP requests and checks status codes, headers, and JSON data.

TestNG organizes the tests and reports pass/fail results. Maven downloads dependencies, compiles the project, and runs the test suite.

## 2. How the UI tests work

### `demo-site.html`
This is a small local test page, not a production login service. It has username and password fields, a Sign in button, and a message area. Its JavaScript checks three cases: missing values, the one accepted demo account, and incorrect values.

The page is intentionally local so the tests don't break when a public demo website changes or becomes unavailable.

### `BaseUiTest.java`
This is a shared setup class. `@BeforeMethod` runs before each UI test:
1. It configures Chrome.
2. It enables headless mode by default, meaning Chrome runs without opening a visible window.
3. Selenium opens the local HTML file.

`@AfterMethod(alwaysRun = true)` closes the browser after the test, including when a test fails. This reduces the chance of leftover Chrome processes affecting later tests.

`ChromeDriver` starts the browser session. Modern Selenium includes Selenium Manager, which can help obtain a compatible driver automatically. The first run may need internet access.

### `LoginPageTests.java`
Each `@Test` method represents one scenario:
- `validCredentialsShowWelcomeMessage` fills in the demo username and password and expects a welcome message.
- `emptyCredentialsShowValidationMessage` clicks Sign in without entering values and expects validation text.
- `invalidCredentialsAreRejected` enters incorrect values and expects a rejection message.

`By.id(...)` locates elements by their HTML `id`. `sendKeys(...)` types into an input. `click()` presses the button. `getText()` reads visible text. `getAttribute(...)` reads an HTML attribute. `Assert.assertEquals(...)` fails the test if actual and expected values differ.

## 3. How the API tests work

### Why WireMock is included
There is no real backend included in this QA project. WireMock creates a temporary local HTTP server and defines sample responses. This avoids relying on a public API and makes test outcomes repeatable.

`UsersApiTests.java` starts WireMock once before the API test class. It configures two sample endpoints:
- `GET /api/users/42` returns a JSON user and HTTP 200.
- `GET /api/users/999` returns an error JSON and HTTP 404.

REST Assured calls those endpoints. The assertions check the response status, content type, JSON fields, and error body. The mock server is stopped after the tests.

In a real client project, the same testing pattern could target a staging API using a configured base URL and approved test credentials. Never commit real credentials or customer data.

## 4. What Maven and TestNG do

`pom.xml` declares Java and library versions. Maven downloads these dependencies and runs the configured plugins.

`testng.xml` lists the test classes that belong to the suite. The Maven Surefire plugin reads that suite file and produces reports in `target/surefire-reports/`.

The `headless` profile is the default. `mvn clean test -Pheaded` runs the browser visibly, which is useful when learning or debugging.

## 5. How CI works

The GitHub Actions workflow checks out the repository, sets up Java and Chrome, and runs `mvn clean test`. Every push or pull request can therefore rerun the same checks automatically.

A green workflow means the automated checks passed in that CI environment. It does not prove the application has no defects; it means only that the covered scenarios passed.

## 6. How to explain it in an interview

**30-second version:**
“I built a Java-based QA automation framework with Selenium and TestNG for UI regression checks and REST Assured for API assertions. I used WireMock to mock API responses so the tests are repeatable and don't rely on external services. Maven runs the suite, and GitHub Actions executes it automatically on pushes and pull requests.”

**Be ready to answer:**
- Why use Selenium? To automate browser interactions and verify user-visible behavior.
- Why use REST Assured? It simplifies HTTP requests and assertions in Java tests.
- Why use WireMock? It simulates API responses in a controlled, isolated test environment.
- What is a test assertion? A check comparing actual behavior with the expected result.
- Why headless mode? CI runners usually don't need a visible browser window.
- What would you improve next? Add Page Objects, failure screenshots, data-driven tests, and a real staging API configuration.

## 7. Honest limitations

The login page is a local demonstration, not secure authentication. The API is mocked, not a deployed backend. Before claiming client experience, use the framework on an actual project with permission, add meaningful test cases, and keep a record of what you personally changed and verified.
