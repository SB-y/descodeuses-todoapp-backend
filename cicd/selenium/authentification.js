const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');

(async function example() {
    let driver = await new Builder()
        .forBrowser('chrome')
        .setChromeOptions(new chrome.Options())
        .build();

    try {
        await driver.get("https://descodeuses-todolist-app.netlify.app");

        // Email
        const emailInput = await driver.wait(
            until.elementLocated(By.css('input[formcontrolname="username"]')),
            10000
        );
        await driver.wait(until.elementIsVisible(emailInput), 10000);
        await emailInput.sendKeys("admin@test.com");

        // Password
        const passwordInput = await driver.wait(
            until.elementLocated(By.css('input[formcontrolname="password"]')),
            10000
        );
        await driver.wait(until.elementIsVisible(passwordInput), 10000);
        await passwordInput.sendKeys("admin");

        // Bouton login
        const loginButton = await driver.wait(
            until.elementLocated(By.css('button[type="submit"]')),
            10000
        );
        await driver.wait(until.elementIsEnabled(loginButton), 10000);
        await loginButton.click();

        // Attendre la redirection
        await driver.wait(until.urlContains("dashboard"), 10000);
        console.log("Connexion réussie !");


    } finally {
          // Garder la fenêtre ouverte 10 secondes pour voir le dashboard 
          await driver.sleep(10000);
    }
})();