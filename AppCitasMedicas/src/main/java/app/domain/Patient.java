package app.domain;

import app.domain.enums.AccountStateEnum;
import app.domain.enums.DocumentTypeEnum;
import app.domain.enums.EpsEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO del paciente. Cada atributo sale de una historia de usuario:
 * - HU-01: documento, nombre, fecha de nacimiento, correo, teléfono, EPS, política de datos.
 * - HU-02: contraseña, intentos fallidos, bloqueado hasta.
 * - HU-05: dirección, código y cambios pendientes de verificar, historial de cambios.
 */
public class Patient extends Person {

    private String email;
    private String phone;
    private String address;
    private EpsEnum eps;
    private String password; // se guarda cifrada (hash), nunca en texto plano
    private boolean dataPolicyAccepted;
    private AccountStateEnum accountState;
    private String verificationCode;
    private String pendingEmail;
    private String pendingPhone;
    private int failedLoginAttempts;
    private LocalDateTime lockedUntil;
    private List<ChangeLog> changeHistory = new ArrayList<>();

    public Patient() {
    }

    public Patient(DocumentTypeEnum documentType, String documentNumber, String fullName, LocalDate birthDate,
                   String email, String phone, String address, EpsEnum eps,
                   String password, boolean dataPolicyAccepted) {
        super(documentType, documentNumber, fullName, birthDate);
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.eps = eps;
        this.password = password;
        this.dataPolicyAccepted = dataPolicyAccepted;
        this.accountState = AccountStateEnum.ACTIVE;
        this.failedLoginAttempts = 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public EpsEnum getEps() {
        return eps;
    }

    public void setEps(EpsEnum eps) {
        this.eps = eps;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDataPolicyAccepted() {
        return dataPolicyAccepted;
    }

    public void setDataPolicyAccepted(boolean dataPolicyAccepted) {
        this.dataPolicyAccepted = dataPolicyAccepted;
    }

    public AccountStateEnum getAccountState() {
        return accountState;
    }

    public void setAccountState(AccountStateEnum accountState) {
        this.accountState = accountState;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getPendingEmail() {
        return pendingEmail;
    }

    public void setPendingEmail(String pendingEmail) {
        this.pendingEmail = pendingEmail;
    }

    public String getPendingPhone() {
        return pendingPhone;
    }

    public void setPendingPhone(String pendingPhone) {
        this.pendingPhone = pendingPhone;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public List<ChangeLog> getChangeHistory() {
        return changeHistory;
    }

    public void setChangeHistory(List<ChangeLog> changeHistory) {
        this.changeHistory = changeHistory;
    }
}
