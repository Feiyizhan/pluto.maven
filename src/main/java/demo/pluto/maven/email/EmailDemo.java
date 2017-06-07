package demo.pluto.maven.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;





import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

import sun.security.krb5.internal.NetClient;
import demo.pluto.maven.util.FileUtil;


public class EmailDemo {
    ApplicationContext context;

    JavaMailSender javaMailSender;
    //@Resource(name = "configProperties")
    //private Properties properties;

    public EmailDemo() {
        // 获得xml文件环境
        context = new ClassPathXmlApplicationContext("email/email.xml");
        // 取得配置
        javaMailSender = (JavaMailSender) context.getBean("javaMailSender");
    }


    public void sendSimpleMailMessage() {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("CHINA MTO SYSTEM <cn-mto-sys@mmm.com>");
        mail.setTo("xmlde@vip.qq.com");
        mail.setSubject("testEmail");

        String text = "测试邮件";
        mail.setText(text);
        javaMailSender.send(mail);

    }

    public void sendHtmlMailMessage() {
        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        try {
            // 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用 multipart模式
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);

            // 设置收件人，寄件人
            //messageHelper.setFrom("徐明龙 <pxu3@mmm.com>");
            messageHelper.setFrom(new InternetAddress("cn-mto-sys@mmm.com", "CHINA MTO SYSTEM"));
            messageHelper.setTo("pxu3@mmm.com");
            messageHelper.setSubject("测试邮件中嵌套图片!！");
            // true 表示启动HTML格式的邮件
            messageHelper.setText("<html><head></head><body><h1>hello!!zhangjian</h1><img src=\"cid:aaa\"/></body></html>", true);
            FileSystemResource img = new FileSystemResource(FileUtil.getFile("image/left.jpg"));
            messageHelper.addInline("aaa", img);
            javaMailSender.send(mailMessage);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void sendTemplateMail() {
        boolean result = false;
        // 邮件类型
        //int type = warrantyEmail.getType();
        // 邮件主题
        String subject = "";
        // 收件人
        List<String> receivers = new ArrayList<String>();
        receivers.add("18121295313@163.com");
        
        // 抄送人
        List<String> ccs = new ArrayList<String>();
        ccs.add("pxu3@mmm.com");
        // 秘密抄送人
        List<String> bccs = new ArrayList<String>();
        // 邮件模板
        String template = "template/email/SubmitNotice.vm";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 申请人名称
            //String applyName = warrantyEmail.getApplyName();

            // 根据邮件类型设置主题及模板
            String tempSubject = "";

            subject = "模版邮件测试";


            helper.setSubject(subject);
            
//            String fromName = "徐明龙 <pxu3@mmm.com>";
            String fromName = "交通安全系统部";
//            fromName = MimeUtility.encodeText(fromName);
            System.out.println(fromName);
//            helper.setFrom(new InternetAddress(fromName+" <pxu3@mmm.com>"));
//            helper.setFrom(new InternetAddress("pxu3@mmm.com",fromName));
//            helper.setFrom("pxu3@mmm.com",fromName);
            helper.setFrom(new InternetAddress("xmlde@vip.qq.com",fromName,"utf-8"));
            
//            helper.setFrom("交通安全系统部  <EWCS-TSSD@mmm.com>");
//            helper.setFrom("Pluto Xu <pxu3@mmm.com>");
            if (receivers != null && receivers.size() > 0) {
                String[] receiverArray = receivers.toArray(new String[receivers.size()]);
                helper.setTo(receiverArray);
            }

            if (ccs != null && ccs.size() > 0) {
                String[] ccArray = ccs.toArray(new String[ccs.size()]);
                helper.setCc(ccArray);
            }

            if (bccs != null && bccs.size() > 0) {
                String[] bccArray = bccs.toArray(new String[bccs.size()]);
                helper.setBcc(bccArray);
            }

            // 邮件内容参数
            VelocityContext context = new VelocityContext();
            // 项目区域
            context.put("projectRegion", "项目区域");
            // 项目名称
            context.put("projectName", "项目名称");
            // 申请日期
            context.put("submitDate", "2016-11-22");
            // 质保书编号
            context.put("requestId", "TSC0000000001");
            // URL
            context.put("url","http://www.baudu.com");

            helper.setText(resolveContent(context, template), true);


            javaMailSender.send(message);


        } catch (MailException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String resolveContent(VelocityContext context, String templatePath) {
        String content = null;
        StringWriter writer = null;
        try {
            Velocity.init(loadProperties("velocity.properties"));
            writer = new StringWriter();
            Template template = Velocity.getTemplate(templatePath);
            template.merge(context, writer);
            
            content = writer.toString();
            System.out.println("Mail Text = " + content);

            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return content;
    }

    public Properties loadProperties(String resName) {

        Properties result = new Properties();
        try {
            result.load(new FileInputStream(FileUtil.getFile(resName)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    public static void main(String args[]){
        EmailDemo demo = new EmailDemo();
        demo.sendHtmlMailMessage();
    }
    

}
