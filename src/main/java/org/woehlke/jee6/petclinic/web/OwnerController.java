package org.woehlke.jee6.petclinic.web;

import org.woehlke.jee6.petclinic.dao.OwnerDao;
import org.woehlke.jee6.petclinic.dao.PetDao;
import org.woehlke.jee6.petclinic.dao.PetTypeDao;
import org.woehlke.jee6.petclinic.dao.VisitDao;
import org.woehlke.jee6.petclinic.entities.Owner;
import org.woehlke.jee6.petclinic.entities.Pet;
import org.woehlke.jee6.petclinic.entities.PetType;
import org.woehlke.jee6.petclinic.entities.Visit;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tw
 * Date: 06.01.14
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@SessionScoped
public class OwnerController implements Serializable {

    @EJB
    private OwnerDao ownerDao;

    @EJB
    private PetDao petDao;

    @EJB
    private PetTypeDao petTypeDao;

    @EJB
    private VisitDao visitDao;

    private String searchterm;

    private List<Owner> ownerList;

    private Owner owner;

    private Pet pet;

    private long petTypeId;

    private Visit visit;

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public long getPetTypeId() {
        return petTypeId;
    }

    public void setPetTypeId(long petTypeId) {
        this.petTypeId = petTypeId;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Owner> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(List<Owner> ownerList) {
        this.ownerList = ownerList;
    }

    public String getSearchterm() {
        return searchterm;
    }

    public void setSearchterm(String searchterm) {
        this.searchterm = searchterm;
    }

    public String search(){
        if(searchterm==null || searchterm.isEmpty()){
            this.ownerList = ownerDao.getAll();
        } else {
            try {
                this.ownerList = ownerDao.search(searchterm);
            } catch (Exception e){
                this.ownerList = ownerDao.getAll();
            }
        }
        return "owners.xhtml";
    }

    public String getNewOwnerForm(){
        this.owner = new Owner();
        return "newOwner.xhtml";
    }

    public String saveNewOwner(){
        ownerDao.addNew(this.owner);
        this.ownerList = ownerDao.getAll();
        return "owners.xhtml";
    }

    public String showOwner(long id){
        this.owner = ownerDao.findById(id);
        return "showOwner.xhtml";
    }

    public String getEditForm(){
        return "editOwner.xhtml";
    }

    public String saveEditedOwner(){
        ownerDao.update(this.owner);
        this.ownerList = ownerDao.getAll();
        return "showOwner.xhtml";
    }

    public String delete(long id){
        ownerDao.delete(id);
        this.ownerList = ownerDao.getAll();
        return "owners.xhtml";
    }

    public String getAddNewPetForm(){
        this.pet = new Pet();
        return "addNewPet.xhtml";
    }

    public List<PetType> getAllPetTypes(){
        return petTypeDao.getAll();
    }

    public String addNewPet(){
        PetType petType = petTypeDao.findById(this.petTypeId);
        this.pet.setType(petType);
        this.owner.addPet(this.pet);
        petDao.addNew(this.pet);
        ownerDao.update(this.owner);
        return "showOwner.xhtml";
    }

    public String editPetForm(long petId){
        this.pet = petDao.findById(petId);
        this.petTypeId = this.pet.getType().getId();
        return "editPet.xhtml";
    }

    public String saveEditedPet(){
        PetType petType = petTypeDao.findById(this.petTypeId);
        this.pet.setType(petType);
        petDao.update(this.pet);
        long ownerId = this.owner.getId();
        this.owner = this.ownerDao.findById(ownerId);
        return "showOwner.xhtml";
    }

    public String addVisitToPetForm(long petId){
        this.pet = petDao.findById(petId);
        this.petTypeId = this.pet.getType().getId();
        this.visit = new Visit();
        return "addVisitToPet.xhtml";
    }

    public String saveVisit(){
        visitDao.addNew(this.visit);
        this.visit.setPet(this.pet);
        this.pet.addVisit(this.visit);
        petDao.update(this.pet);
        long ownerId = this.owner.getId();
        this.owner = this.ownerDao.findById(ownerId);
        return "showOwner.xhtml";
    }

}
