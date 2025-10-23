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

        // Garder la fenêtre ouverte 10 secondes pour voir le dashboard
        await driver.sleep(2000);




        // Trouver lien todolist et click
        const todolistLink = await driver.wait(
            until.elementLocated(By.linkText("Ma gestion de la to-do list")),
            10000
        );
        await todolistLink.click();

        // Attendre qu'on soit sur la bonne page todolist
        await driver.wait(until.urlContains("todolist"), 10000);


        // Attendre que l'input pour la tâche soit visible
        const tacheInput = await driver.wait(
            until.elementLocated(By.css('input[formcontrolname="title"]')),
            10000
        );
        await driver.wait(until.elementIsVisible(tacheInput), 10000);

        // Maintenant on peut saisir la tâche
        await tacheInput.sendKeys("Acheter du fromage pour Peihsin");

        // Bouton ajouter
        const ajouterButton = await driver.wait(
            until.elementLocated(By.css('button[type="submit"]')),
            10000
        );
        await driver.wait(until.elementIsEnabled(ajouterButton), 10000);
        await ajouterButton.click();


        // Attendre que l'élément avec le texte exact apparaisse
        const tacheAjoutee = await driver.wait(
            until.elementLocated(By.xpath("//span[contains(text(), 'Acheter du fromage pour Peihsin')]")),
            5000
        );

        if (tacheAjoutee) {
            console.log("Tache ajoutée !");
        } else {
            console.log("Tache non trouvée.");
        }

    } finally {
        // Garder la fenêtre ouverte 10 secondes pour voir le résultat
        await driver.sleep(10000);
        // await driver.quit();
    }
})();