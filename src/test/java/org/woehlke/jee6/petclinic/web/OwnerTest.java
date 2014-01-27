package org.woehlke.jee6.petclinic.web;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.logging.Logger;

import static org.jboss.arquillian.graphene.Graphene.goTo;


/**
 * Created with IntelliJ IDEA.
 * User: tw
 * Date: 22.01.14
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
 */
@RunWith(Arquillian.class)
public class OwnerTest {

    private static Logger log = Logger.getLogger(OwnerTest.class.getName());

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return Deployments.createOwnerDeployment();
    }

    @Drone
    WebDriver driver;

    @ArquillianResource
    URL deploymentUrl;

    @Page
    private HelloPage helloPage;

    @Page
    private FindOwnersPage findOwnersPage;

    @Page
    private OwnersPage ownersPage;

    @Page
    private NewOwnerPage newOwnerPage;

    @Test
    @InSequence(1)
    @RunAsClient
    public void testOpeningHomePage() {
        goTo(HelloPage.class);
        helloPage.assertTitle();
    }

    @Test
    @InSequence(2)
    @RunAsClient
    public void testOpenFindOwnersPage() {
        goTo(FindOwnersPage.class);
        findOwnersPage.assertPageIsLoaded();
    }

    @Test
    @InSequence(3)
    @RunAsClient
    public void testOpenOwnersPage() {
        goTo(FindOwnersPage.class);
        findOwnersPage.assertPageIsLoaded();
        findOwnersPage.clickSearch();
        ownersPage.assertPageIsLoaded();
    }

    @Test
    @InSequence(4)
    @RunAsClient
    public void testOpenNewOwnerPage() {
        goTo(FindOwnersPage.class);
        findOwnersPage.assertPageIsLoaded();
        findOwnersPage.clickNewOwner();
        newOwnerPage.assertPageIsLoaded();
    }

    @Test
    @InSequence(5)
    @RunAsClient
    public void testOpenNewOwnerPageFromOwnersList() {
        goTo(FindOwnersPage.class);
        findOwnersPage.assertPageIsLoaded();
        findOwnersPage.clickSearch();
        ownersPage.assertPageIsLoaded();
        ownersPage.clickNewOwner();
        newOwnerPage.assertPageIsLoaded();
    }

    @Test
    @InSequence(6)
    @RunAsClient
    public void testAddNewOwner() {
        goTo(FindOwnersPage.class);
        findOwnersPage.assertPageIsLoaded();
        findOwnersPage.clickSearch();
        ownersPage.assertPageIsLoaded();
        ownersPage.clickNewOwner();
        newOwnerPage.assertPageIsLoaded();
        newOwnerPage.addNewContent("Thomas","Woehlke","Schoenhauser Allee 42","Berlin","03012345678");
        ownersPage.assertPageIsLoaded();
        ownersPage.assertNewContentFound("Thomas","Woehlke","Schoenhauser Allee 42","Berlin","03012345678");
    }

    @Test
    @InSequence(7)
    @RunAsClient
    public void testEditOwner() {
        String url = deploymentUrl.toExternalForm() + "findOwners.jsf";
        log.info("url: " + url);
        driver.get(url);
        Assert.assertTrue(driver.findElement(By.id("findOwners")).isDisplayed());
        driver.findElement(By.id("findOwnersForm:search")).click();
        Assert.assertTrue(driver.findElement(By.id("owners")).isDisplayed());
        driver.findElement(By.id("ownersForm:ownersTable:0:showOwner")).click();
        Assert.assertTrue(driver.findElement(By.id("showOwnerForm")).isDisplayed());
        driver.findElement(By.id("showOwnerForm:edit")).click();
        Assert.assertTrue(driver.findElement(By.id("editOwnerForm")).isDisplayed());
        driver.findElement(By.id("editOwnerForm:firstName")).clear();
        driver.findElement(By.id("editOwnerForm:lastName")).clear();
        driver.findElement(By.id("editOwnerForm:address")).clear();
        driver.findElement(By.id("editOwnerForm:city")).clear();
        driver.findElement(By.id("editOwnerForm:telephone")).clear();
        driver.findElement(By.id("editOwnerForm:firstName")).sendKeys("Willy");
        driver.findElement(By.id("editOwnerForm:lastName")).sendKeys("Wombel");
        driver.findElement(By.id("editOwnerForm:address")).sendKeys("Elbchaussee 242");
        driver.findElement(By.id("editOwnerForm:city")).sendKeys("Hamburg");
        driver.findElement(By.id("editOwnerForm:telephone")).sendKeys("04012345678");
        driver.findElement(By.id("editOwnerForm:save")).click();
        Assert.assertTrue(driver.findElement(By.id("showOwnerForm")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(text(), 'Willy')]")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(text(), 'Wombel')]")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(text(), 'Elbchaussee 242')]")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(text(), 'Hamburg')]")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(text(), '04012345678')]")).isDisplayed());
    }

}
