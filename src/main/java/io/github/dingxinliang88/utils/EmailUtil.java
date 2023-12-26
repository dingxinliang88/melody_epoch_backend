package io.github.dingxinliang88.utils;

import io.github.dingxinliang88.config.EmailConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import static io.github.dingxinliang88.constants.EmailConstant.*;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    private static JavaMailSender mailSender;

    private static EmailConfigProperties emailConfigProperties;

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        EmailUtil.mailSender = mailSender;
    }

    @Autowired
    public void setEmailConfigProperties(EmailConfigProperties emailConfigProperties) {
        EmailUtil.emailConfigProperties = emailConfigProperties;
    }


    /**
     * 发送邮件
     *
     * @param emailTo 邮箱
     * @param captcha 验证码
     */
    public static void sendEmail(String emailTo, String captcha) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        // 组装邮件内容
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setSubject(EMAIL_SUBJECT);
        messageHelper.setText(buildEmailContent(EMAIL_HTML_CONTENT_PATH, captcha), true);
        messageHelper.setTo(emailTo);
        messageHelper.setFrom(PROCESS_CN_TITLE + "<" + emailConfigProperties.getFrom() + ">");
        mailSender.send(message);
    }

    @SuppressWarnings("SameParameterValue")
    private static String buildEmailContent(String emailHtmlPath, String captcha) {
        // 加载邮件模板
        ClassPathResource resource = new ClassPathResource(emailHtmlPath);

        StringBuilder buffer = new StringBuilder();
        String line;
        try (InputStream in = resource.getInputStream();
             BufferedReader fileReader = new BufferedReader(new InputStreamReader(in))) {
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            logger.error("读取邮件模板失败" + e.getMessage());
        }
        return MessageFormat.format(buffer.toString(), captcha,
                EMAIL_TITLE, PROCESS_CN_TITLE, PLATFORM_RESPONSIBLE_PERSON);
    }
}
