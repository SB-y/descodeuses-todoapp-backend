const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

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




        //AJOUT D'UNE TACHE

        // Attendre qu'on soit sur la bonne page todolist
        await driver.wait(until.urlContains("todolist"), 10000);


        // Attendre que l'input pour la tâche soit visible
        const tacheInput = await driver.wait(
            until.elementLocated(By.css('input[formcontrolname="title"]')),
            10000
        );
        await driver.wait(until.elementIsVisible(tacheInput), 10000);

        // Maintenant on peut saisir la tâche
        await tacheInput.click();
        await tacheInput.sendKeys("Acheter du fromage pour Peihsin");
        await sleep(2000);

        // Bouton ajouter
        const ajouterButton = await driver.wait(
            until.elementLocated(By.css('button[type="submit"]')),
            10000
        );
        await driver.wait(until.elementIsEnabled(ajouterButton), 10000);
        await ajouterButton.click();
        console.log("Tâche : Acheter du fromage pour Peihsin ajoutée ✅");
        await sleep(2000);



        //SUPPRESSION TACHE QUI VIENT D'ETRE AJOUTEE

        // Attendre la redirection vers la page détail
        await driver.wait(until.urlContains("tododetails/"), 10000);
        console.log("Redirection vers la page détail détectée");

        // Attendre que le bouton supprimer soit visible
        const boutonSupp = await driver.wait(
            until.elementLocated(
                By.xpath("//button//span[contains(text(), 'Supprimer')]")
            ),
            10000
        );

        await sleep(2000);
        await boutonSupp.click();



        //VERIFICATION SUPPRESSION TACHE

        // Attendre la redirection vers la liste (todolist)
        await driver.wait(until.urlContains("todolist"), 10000);
        console.log("✅ Retour sur la page todolist après suppression");

        // Vérifier que la tâche n'existe plus
        await sleep(2000); // petit délai pour laisser Angular recharger la liste
        const taches = await driver.findElements(
            By.xpath("//span[normalize-space(text())='Acheter du fromage pour Peihsin']")
        );

        if (taches.length === 0) {
            console.log("Tâche : Acheter du fromage pour Peihsin bien supprimée ✅");
        } else {
            console.log("❌ Tâche non supprimée !");
        }


        //LISTE DES TACHES ET VERIF NBRE
        // Récupérer toutes les tâches de la liste
        await driver.wait(until.urlContains("todolist"), 10000);
        const listeTachesApresSupp = await driver.wait(
            until.elementsLocated(By.css("span.flex-1.cursor-pointer")),
            10000
        );


        // Afficher chaque tâche
        for (let i = 0; i < listeTachesApresSupp.length; i++) {
            let tacheTexte = await listeTachesApresSupp[i].getText();
            console.log(`Tâche ${i + 1} : ${tacheTexte}`);
        }


    } finally {
        // Garder la fenêtre ouverte 10 secondes pour voir le résultat
        await driver.sleep(10000);
        // await driver.quit();
    }
})();