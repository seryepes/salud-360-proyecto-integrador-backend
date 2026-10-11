package app.domain;

import app.domain.enums.DocumentTypeEnum;

import java.time.LocalDate;

/**
 * Datos de identidad que comparte cualquier persona del sistema.
 * Es abstracta porque nunca se crea una "Person" sola: se crea un Patient
 * (y más adelante podrían existir Doctor o Admin que también hereden de aquí).
 */
public abstract class Person {

    private DocumentTypeEnum documentType;
    private String documentNumber;
    private String fullName;
    private LocalDate birthDate;

    public Person() {
    }

    public Person(DocumentTypeEnum documentType, String documentNumber, String fullName, LocalDate birthDate) {
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    public DocumentTypeEnum getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeEnum documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
