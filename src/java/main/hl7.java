/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author degla
 */
public class hl7 {
    String complaint = "None", presentIllness = "None", pastIllnes = "None", infection = "None"
            , pregnancy = "None", socialHistory = "None", familyHistory = "None",
            allergy = "None", reviewSymptoms = "None", exam = "None", vitals = "None", visitSummary = "None";
    Patient p;
    public hl7(){}
    public hl7(Patient p){
        this.p = p;
    }
    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getPresentIllness() {
        return presentIllness;
    }

    public void setPresentIllness(String presentIllness) {
        this.presentIllness = presentIllness;
    }

    public String getPastIllnes() {
        return pastIllnes;
    }

    public void setPastIllnes(String pastIllnes) {
        this.pastIllnes = pastIllnes;
    }

    public String getInfection() {
        return infection;
    }

    public void setInfection(String infection) {
        this.infection = infection;
    }

    public String getPregnancy() {
        return pregnancy;
    }

    public void setPregnancy(String pregnancy) {
        this.pregnancy = pregnancy;
    }

    public String getSocialHistory() {
        return socialHistory;
    }

    public void setSocialHistory(String socialHistory) {
        this.socialHistory = socialHistory;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getReviewSymptoms() {
        return reviewSymptoms;
    }

    public void setReviewSymptoms(String reviewSymptoms) {
        this.reviewSymptoms = reviewSymptoms;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public Patient getP() {
        return p;
    }

    public void setP(Patient p) {
        this.p = p;
    }

    public String getVitals() {
        return vitals;
    }

    public void setVitals(String vitals) {
        this.vitals = vitals;
    }

    public String getVisitSummary() {
        return visitSummary;
    }

    public void setVisitSummary(String visitSummary) {
        this.visitSummary = visitSummary;
    }

}
