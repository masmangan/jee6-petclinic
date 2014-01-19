package org.woehlke.jee6.petclinic.web;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.woehlke.jee6.petclinic.dao.SpecialtyDao;
import org.woehlke.jee6.petclinic.dao.SpecialtyDaoImpl;
import org.woehlke.jee6.petclinic.entities.*;

import java.net.URL;


/**
 * Created with IntelliJ IDEA.
 * User: tw
 * Date: 19.01.14
 * Time: 16:28
 * To change this template use File | Settings | File Templates.
 */
@RunWith(Arquillian.class)
public class FirstTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "specialties.war")
                .addClasses(SpecialtyController.class,
                        SpecialtyDao.class, SpecialtyDaoImpl.class,
                        Owner.class, Pet.class, PetType.class,
                        Specialty.class, Vet.class, Visit.class)
                .merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                        "/", Filters.include(".*\\.xhtml$"))
                .addAsResource("META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Drone
    WebDriver driver;

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void testOpeningHomePage() {

        driver.get("http://www.google.com");

        String pageTitle = driver.getTitle();

        Assert.assertEquals(pageTitle, "Google");
    }
}
