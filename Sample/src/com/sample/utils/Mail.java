package com.sample.utils;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {

	public static boolean sendMail(String contents) throws MessagingException{

		final String username = "twinklebenedict@gmail.com";
		final String password = "kavalakkal100";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

//		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("twinklebenedict@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("akash.termors@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText(contents);
			Transport.send(message);
			System.out.println("Done");
			return true;

//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
//		return false;
	}

}
