package itis.khabibullina.service;

import itis.khabibullina.config.MailConfig;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailServicesTest {
    @MockBean
    private EmailServices emailServices;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private MailConfig mailConfig;

    @BeforeEach
    public void beforeEach() {
        emailServices = new EmailServices(mailConfig, freeMarkerConfigurer);
    }

    @Test
    public void testSendEmail() throws TemplateException, IOException {
        String password = emailServices.sendEmail("test@gmail.com");
        System.out.println(password);
        Assertions.assertNotNull(password);
    }
}
