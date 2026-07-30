package com.classes.Backend.Service.mail;

public interface MailService {

    /**
     * Sends a welcome email to a newly registered institute admin.
     *
     * @param to            recipient email address
     * @param instituteName name of the institute
     * @param email         the login email shown in the body
     */
    void sendInstituteWelcomeEmail(String to, String instituteName, String email);

    /**
     * Sends an email verification code to a pending institute signup.
     *
     * @param to            recipient email address
     * @param instituteName name of the institute
     * @param code          the verification code
     */
    void sendEmailVerificationCode(String to, String instituteName, String code);
}
