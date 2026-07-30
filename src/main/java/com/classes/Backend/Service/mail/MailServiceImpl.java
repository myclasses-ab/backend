package com.classes.Backend.Service.mail;

import java.nio.charset.StandardCharsets;
import java.time.Year;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${myclasses.mail.from:MyClasses <support@myclasses.co.in>}")
    private String fromAddress;

    @Value("${myclasses.mail.support-email:support@myclasses.co.in}")
    private String supportEmail;

    @Value("${myclasses.mail.brand-color:#4f46e5}")
    private String brandColor;

    @Value("${myclasses.mail.console-url:#}")
    private String consoleUrl;

    @Value("${myclasses.mail.logo-url:}")
    private String logoUrl;

    @Override
    @Async("mailTaskExecutor")
    public void sendInstituteWelcomeEmail(String to, String instituteName, String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setReplyTo(supportEmail);
            helper.setSubject("Welcome to MyClasses, " + instituteName + "!");
            helper.setText(buildWelcomeHtml(instituteName, email), true);

            mailSender.send(message);
            log.info("Welcome email sent to {} for institute {}", to, instituteName);
        } catch (MailException | MessagingException ex) {
            // Never fail signup because of an email error.
            log.error("Failed to send welcome email to {}: {}", to, ex.getMessage(), ex);
        }
    }

    @Override
    @Async("mailTaskExecutor")
    public void sendEmailVerificationCode(String to, String instituteName, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setReplyTo(supportEmail);
            helper.setSubject("Verify your email for MyClasses");
            helper.setText(buildEmailVerificationHtml(instituteName, code), true);

            mailSender.send(message);
            log.info("Email verification code sent to {}", to);
        } catch (MailException | MessagingException ex) {
            log.error("Failed to send email verification code to {}: {}", to, ex.getMessage(), ex);
        }
    }

    private String buildWelcomeHtml(String instituteName, String email) {
        String escapedInstituteName = escapeHtml(instituteName);
        String escapedEmail = escapeHtml(email);
        String escapedSupportEmail = escapeHtml(supportEmail);
        String escapedBrandColor = escapeHtml(brandColor);
        String escapedConsoleUrl = escapeHtml(consoleUrl);
        String escapedLogoUrl = escapeHtml(logoUrl);
        String year = String.valueOf(Year.now().getValue());

        String logoBlock = logoUrl != null && !logoUrl.isBlank()
                ? "<img src=\"" + escapedLogoUrl + "\" alt=\"MyClasses\" width=\"140\" style=\"max-width:140px; height:auto; display:block; margin:0 auto;\" />"
                : "<div style=\"font-size:24px; font-weight:800; color:" + escapedBrandColor + ";\">MyClasses</div>";

        String template = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                  <meta name="color-scheme" content="light" />
                  <meta name="supported-color-schemes" content="light" />
                  <title>Welcome to MyClasses</title>
                  <style>
                    @media only screen and (max-width: 600px) {
                      .container { width: 100% !important; padding: 0 16px !important; }
                      .inner { padding: 24px !important; }
                      .hero { padding: 40px 24px !important; }
                      .feature-cell { display: block !important; width: 100% !important; padding: 12px 0 !important; }
                      .btn { width: 100% !important; display: block !important; text-align: center !important; }
                    }
                  </style>
                </head>
                <body style="margin:0; padding:0; background-color:#f3f4f6; font-family:'Segoe UI',Roboto,Helvetica,Arial,sans-serif; -webkit-font-smoothing:antialiased;">
                  <table role="presentation" width="100%" cellspacing="0" cellpadding="0" border="0" style="background-color:#f3f4f6;">
                    <tr>
                      <td align="center" style="padding:0;">
                        <table role="presentation" class="container" width="600" cellspacing="0" cellpadding="0" border="0" style="max-width:600px; width:600px; background-color:#ffffff; overflow:hidden; box-shadow:0 10px 40px rgba(0,0,0,0.08);">
                          <tr>
                            <td style="padding:32px 40px 16px; text-align:center;" class="inner">
                              {{logoBlock}}
                            </td>
                          </tr>
                          <tr>
                            <td class="hero" style="padding:48px 40px; text-align:center; background:linear-gradient(135deg, {{brandColor}} 0%, #7c3aed 100%); color:#ffffff;">
                              <h1 style="margin:0 0 12px; font-size:32px; font-weight:800; line-height:1.2;">Welcome aboard, {{instituteName}}!</h1>
                              <p style="margin:0; font-size:18px; line-height:1.6; opacity:0.95;">Your institute is now part of the MyClasses family.</p>
                            </td>
                          </tr>
                          <tr>
                            <td class="inner" style="padding:40px;">
                              <p style="margin:0 0 16px; font-size:16px; line-height:1.6; color:#374151;">
                                Hi <strong>{{instituteName}}</strong>,
                              </p>
                              <p style="margin:0 0 24px; font-size:16px; line-height:1.6; color:#4b5563;">
                                Thank you for signing up on <strong>MyClasses</strong>. We’re thrilled to help you reach more students, manage your institute profile, and grow your coaching business — all from one simple console.
                              </p>
                              <table role="presentation" cellspacing="0" cellpadding="0" border="0" style="margin:32px 0;">
                                <tr>
                                  <td>
                                    <a href="{{consoleUrl}}" class="btn" style="display:inline-block; padding:14px 32px; background-color:{{brandColor}}; color:#ffffff; text-decoration:none; border-radius:10px; font-weight:700; font-size:16px;">Go to Your Console</a>
                                  </td>
                                </tr>
                              </table>
                              <p style="margin:0 0 8px; font-size:15px; line-height:1.5; color:#4b5563;">
                                Logged in as: <strong>{{email}}</strong>
                              </p>
                              <p style="margin:0; font-size:15px; line-height:1.5; color:#6b7280;">
                                If you have any questions, just reply to this email or reach out to <a href="mailto:{{supportEmail}}" style="color:{{brandColor}}; text-decoration:none; font-weight:600;">{{supportEmail}}</a>.
                              </p>
                            </td>
                          </tr>
                          <tr>
                            <td class="inner" style="padding:0 40px 40px;">
                              <table role="presentation" width="100%" cellspacing="0" cellpadding="0" border="0">
                                <tr>
                                  <td class="feature-cell" width="33%" style="padding:0 8px; text-align:center; vertical-align:top;">
                                    <div style="width:48px; height:48px; margin:0 auto 12px; background-color:#eef2ff; border-radius:12px; line-height:48px; font-size:22px;">🎓</div>
                                    <h3 style="margin:0 0 6px; font-size:15px; font-weight:700; color:#111827;">Build Profile</h3>
                                    <p style="margin:0; font-size:13px; color:#6b7280; line-height:1.5;">Showcase courses, faculty, results & facilities.</p>
                                  </td>
                                  <td class="feature-cell" width="33%" style="padding:0 8px; text-align:center; vertical-align:top;">
                                    <div style="width:48px; height:48px; margin:0 auto 12px; background-color:#eef2ff; border-radius:12px; line-height:48px; font-size:22px;">📈</div>
                                    <h3 style="margin:0 0 6px; font-size:15px; font-weight:700; color:#111827;">Get Leads</h3>
                                    <p style="margin:0; font-size:13px; color:#6b7280; line-height:1.5;">Connect with students actively looking for coaching.</p>
                                  </td>
                                  <td class="feature-cell" width="33%" style="padding:0 8px; text-align:center; vertical-align:top;">
                                    <div style="width:48px; height:48px; margin:0 auto 12px; background-color:#eef2ff; border-radius:12px; line-height:48px; font-size:22px;">⚡</div>
                                    <h3 style="margin:0 0 6px; font-size:15px; font-weight:700; color:#111827;">Grow Fast</h3>
                                    <p style="margin:0; font-size:13px; color:#6b7280; line-height:1.5;">Use insights and featured listings to stand out.</p>
                                  </td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                          <tr>
                            <td style="padding:0 40px;">
                              <div style="height:1px; background-color:#e5e7eb;"></div>
                            </td>
                          </tr>
                          <tr>
                            <td class="inner" style="padding:32px 40px; text-align:center;">
                              <p style="margin:0 0 8px; font-size:13px; color:#9ca3af;">
                                MyClasses · Empowering Education
                              </p>
                              <p style="margin:0; font-size:12px; color:#9ca3af; line-height:1.5;">
                                © {{year}} MyClasses. All rights reserved.<br />
                                Need help? Contact us at <a href="mailto:{{supportEmail}}" style="color:{{brandColor}}; text-decoration:none;">{{supportEmail}}</a>
                              </p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """;

        return template
                .replace("{{logoBlock}}", logoBlock)
                .replace("{{brandColor}}", escapedBrandColor)
                .replace("{{instituteName}}", escapedInstituteName)
                .replace("{{email}}", escapedEmail)
                .replace("{{supportEmail}}", escapedSupportEmail)
                .replace("{{consoleUrl}}", escapedConsoleUrl)
                .replace("{{year}}", year);
    }

    private String buildEmailVerificationHtml(String instituteName, String code) {
        String escapedInstituteName = escapeHtml(instituteName);
        String escapedCode = escapeHtml(code);
        String escapedSupportEmail = escapeHtml(supportEmail);
        String escapedBrandColor = escapeHtml(brandColor);
        String escapedLogoUrl = escapeHtml(logoUrl);
        String year = String.valueOf(Year.now().getValue());

        String logoBlock = logoUrl != null && !logoUrl.isBlank()
                ? "<img src=\"" + escapedLogoUrl + "\" alt=\"MyClasses\" width=\"140\" style=\"max-width:140px; height:auto; display:block; margin:0 auto;\" />"
                : "<div style=\"font-size:24px; font-weight:800; color:" + escapedBrandColor + ";\">MyClasses</div>";

        String template = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                  <meta name="color-scheme" content="light" />
                  <meta name="supported-color-schemes" content="light" />
                  <title>Verify your email</title>
                  <style>
                    @media only screen and (max-width: 600px) {
                      .container { width: 100% !important; padding: 0 16px !important; }
                      .inner { padding: 24px !important; }
                      .hero { padding: 40px 24px !important; }
                    }
                  </style>
                </head>
                <body style="margin:0; padding:0; background-color:#f3f4f6; font-family:'Segoe UI',Roboto,Helvetica,Arial,sans-serif; -webkit-font-smoothing:antialiased;">
                  <table role="presentation" width="100%" cellspacing="0" cellpadding="0" border="0" style="background-color:#f3f4f6;">
                    <tr>
                      <td align="center" style="padding:0;">
                        <table role="presentation" class="container" width="600" cellspacing="0" cellpadding="0" border="0" style="max-width:600px; width:600px; background-color:#ffffff; overflow:hidden; box-shadow:0 10px 40px rgba(0,0,0,0.08);">
                          <tr>
                            <td style="padding:32px 40px 16px; text-align:center;" class="inner">
                              {{logoBlock}}
                            </td>
                          </tr>
                          <tr>
                            <td class="hero" style="padding:48px 40px; text-align:center; background:linear-gradient(135deg, {{brandColor}} 0%, #7c3aed 100%); color:#ffffff;">
                              <h1 style="margin:0 0 12px; font-size:30px; font-weight:800; line-height:1.2;">Verify your email</h1>
                              <p style="margin:0; font-size:16px; line-height:1.6; opacity:0.95;">Use the code below to complete your institute signup.</p>
                            </td>
                          </tr>
                          <tr>
                            <td class="inner" style="padding:40px; text-align:center;">
                              <p style="margin:0 0 16px; font-size:16px; line-height:1.6; color:#374151; text-align:left;">
                                Hi <strong>{{instituteName}}</strong>,
                              </p>
                              <p style="margin:0 0 32px; font-size:16px; line-height:1.6; color:#4b5563; text-align:left;">
                                Thanks for signing up your institute on <strong>MyClasses</strong>. Please enter the following 6-digit verification code to confirm your email address.
                              </p>
                              <div style="margin:32px 0; padding:24px; background-color:#f9fafb; border-radius:12px; border:1px dashed #d1d5db; display:inline-block;">
                                <p style="margin:0 0 8px; font-size:13px; color:#6b7280; text-transform:uppercase; letter-spacing:1px;">Verification code</p>
                                <p style="margin:0; font-size:36px; font-weight:800; letter-spacing:8px; color:#111827;">{{code}}</p>
                              </div>
                              <p style="margin:24px 0 0; font-size:14px; line-height:1.5; color:#6b7280;">
                                This code expires in 10 minutes. If you didn't request this, please ignore this email or contact <a href="mailto:{{supportEmail}}" style="color:{{brandColor}}; text-decoration:none; font-weight:600;">{{supportEmail}}</a>.
                              </p>
                            </td>
                          </tr>
                          <tr>
                            <td style="padding:0 40px;">
                              <div style="height:1px; background-color:#e5e7eb;"></div>
                            </td>
                          </tr>
                          <tr>
                            <td class="inner" style="padding:32px 40px; text-align:center;">
                              <p style="margin:0 0 8px; font-size:13px; color:#9ca3af;">
                                MyClasses · Empowering Education
                              </p>
                              <p style="margin:0; font-size:12px; color:#9ca3af; line-height:1.5;">
                                © {{year}} MyClasses. All rights reserved.<br />
                                Need help? Contact us at <a href="mailto:{{supportEmail}}" style="color:{{brandColor}}; text-decoration:none;">{{supportEmail}}</a>
                              </p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """;

        return template
                .replace("{{logoBlock}}", logoBlock)
                .replace("{{brandColor}}", escapedBrandColor)
                .replace("{{instituteName}}", escapedInstituteName)
                .replace("{{code}}", escapedCode)
                .replace("{{supportEmail}}", escapedSupportEmail)
                .replace("{{year}}", year);
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#039;");
    }
}
