package com.user.identity.service;

import com.user.identity.controller.dto.request.SupportRequest;
import com.user.identity.controller.dto.request.UserCreationRequest;
import com.user.identity.event.kafka.NotificationEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailNotificationKafka {
    KafkaTemplate<String, Object> kafkaTemplate;

    public void sendVerificationEmail(UserCreationRequest request, String urlEmailToken) {
        NotificationEvent notificationEvent = buildEmailNotification(request, urlEmailToken);
        kafkaTemplate.send("notification-delivery", notificationEvent);
    }

    public NotificationEvent buildEmailNotification(UserCreationRequest request, String urlEmailToken) {
        String emailBody = generateEmailBody(request, urlEmailToken);
        return NotificationEvent.builder()
                .chanel("EMAIL")
                .recipient(request.getEmail())
                .subject("Email Verification")
                .body(emailBody)
                .build();
    }
    public NotificationEvent buildEmailNotificationSupport(SupportRequest request) {
        String emailBody = generateEmailBodySupport(request);
        return NotificationEvent.builder()
                .chanel("EMAIL")
                .recipient(request.getEmail())
                .subject("Thư phản hồi ý kiến khách hàng")
                .body(emailBody)
                .build();
    }
    public String generateEmailBody(UserCreationRequest request, String verificationUrl) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: Arial, sans-serif;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      background-color: #f4f4f4;\n" +
                "    }\n" +
                "    .email-container {\n" +
                "      max-width: 600px;\n" +
                "      margin: 20px auto;\n" +
                "      background: #ffffff;\n" +
                "      border-radius: 8px;\n" +
                "      overflow: hidden;\n" +
                "      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "    }\n" +
                "    .header {\n" +
                "      background-color: #4CAF50;\n" +
                "      color: #ffffff;\n" +
                "      text-align: center;\n" +
                "      padding: 20px;\n" +
                "      font-size: 24px;\n" +
                "      font-weight: bold;\n" +
                "    }\n" +
                "    .content {\n" +
                "      padding: 20px;\n" +
                "      text-align: left;\n" +
                "      color: #333333;\n" +
                "    }\n" +
                "    .content p {\n" +
                "      font-size: 16px;\n" +
                "      line-height: 1.6;\n" +
                "    }\n" +
                "    .cta-button {\n" +
                "      display: block;\n" +
                "      width: fit-content;\n" +
                "      margin: 20px auto;\n" +
                "      padding: 10px 20px;\n" +
                "      text-align: center;\n" +
                "      background-color: #4CAF50;\n" +
                "      color: #ffffff;\n" +
                "      text-decoration: none;\n" +
                "      font-size: 16px;\n" +
                "      font-weight: bold;\n" +
                "      border-radius: 4px;\n" +
                "      transition: background-color 0.3s ease;\n" +
                "    }\n" +
                "    .cta-button:hover {\n" +
                "      background-color: #45a049;\n" +
                "    }\n" +
                "    .footer {\n" +
                "      text-align: center;\n" +
                "      font-size: 12px;\n" +
                "      color: #666666;\n" +
                "      padding: 10px 20px;\n" +
                "      background-color: #f4f4f4;\n" +
                "      border-top: 1px solid #e0e0e0;\n" +
                "    }\n" +
                "    .footer a {\n" +
                "      color: #4CAF50;\n" +
                "      text-decoration: none;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"email-container\">\n" +
                "    <div class=\"header\">\n" +
                "      Verify Your Email\n" +
                "    </div>\n" +
                "    <div class=\"content\">\n" +
                "      <p>Hello " + request.getEmail() + ",</p>\n" +
                "      <p>Thank you for signing up! Please click the button below to verify your email address and activate your account:</p>\n" +
                "      <a href=\"http://ec2-54-252-160-78.ap-southeast-2.compute.amazonaws.com:3000/verify-email/" + verificationUrl + "\" class=\"cta-button\">Verify Email</a>\n" + "      <p>If you didn't create an account with us, please ignore this email.</p>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "      <p>Need help? <a href=\"mailto:nextlife@odayok.com\">Contact Support</a></p>\n" +
                "      <p>&copy; 2024 NextLife. All rights reserved.</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>\n";
    }

    public String generateEmailBodySupport(SupportRequest request) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"vi\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Xin lỗi vì sự bất tiện này</title>\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: Arial, sans-serif;\n" +
                "      line-height: 1.6;\n" +
                "      background-color: #f9f9f9;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      color: #333;\n" +
                "    }\n" +
                "\n" +
                "    .container {\n" +
                "      max-width: 600px;\n" +
                "      margin: 20px auto;\n" +
                "      background: #fff;\n" +
                "      border-radius: 8px;\n" +
                "      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "      padding: 20px 30px;\n" +
                "      border: 1px solid #e0e0e0;\n" +
                "    }\n" +
                "\n" +
                "    h1 {\n" +
                "      font-size: 24px;\n" +
                "      color: #007BFF;\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "\n" +
                "    p {\n" +
                "      margin: 15px 0;\n" +
                "    }\n" +
                "\n" +
                "    .highlight {\n" +
                "      font-weight: bold;\n" +
                "      color: #007BFF;\n" +
                "    }\n" +
                "\n" +
                "    .footer {\n" +
                "      margin-top: 30px;\n" +
                "      text-align: center;\n" +
                "      font-size: 14px;\n" +
                "      color: #888;\n" +
                "    }\n" +
                "\n" +
                "    .footer a {\n" +
                "      color: #007BFF;\n" +
                "      text-decoration: none;\n" +
                "    }\n" +
                "\n" +
                "    .footer a:hover {\n" +
                "      text-decoration: underline;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <h1>Cảm Ơn</h1>\n" +
                "    <p><strong>Kính gửi: <span class=\"highlight\">"+request.getFullName()+"</span></strong></p>\n" +
                "    <p>Trước tiên, chúng tôi xin gửi lời cảm ơn chân thành tới Quý khách vì đã dành thời gian chia sẻ ý kiến và phản hồi về sự cố mà Quý khách đã gặp phải liên quan đến <span class=\"highlight\">"+request.getSubject()+"</span>.</p>\n" +
                "    <p>Chúng tôi rất lấy làm tiếc khi <span class=\"highlight\">"+request.getSubject()+"</span> đã khiến trải nghiệm của Quý khách chưa được như mong đợi. Phản hồi của Quý khách là một nguồn thông tin vô cùng quý giá để chúng tôi cải thiện dịch vụ/sản phẩm trong tương lai.</p>\n" +
                "    <p>Đội ngũ của chúng tôi đã tiếp nhận thông tin và đang tiến hành <span class=\"highlight\">[mô tả bước xử lý: kiểm tra, khắc phục, hoặc liên hệ trực tiếp]</span>. Chúng tôi cam kết sẽ thông báo sớm nhất đến Quý khách ngay khi có kết quả xử lý.</p>\n" +
                "    <p>Một lần nữa, chúng tôi xin chân thành cảm ơn sự thấu hiểu và đồng hành của Quý khách. Nếu cần thêm hỗ trợ, Quý khách vui lòng liên hệ với chúng tôi qua email <span class=\"highlight\">dacviethuynh@gmail.com</span>.</p>\n" +
                "    <p>Trân trọng,</p>\n" +
                "    <p><strong>NEXTROOM</strong></p>\n" +
                "    <div class=\"footer\">\n" +
                "      <p>Để biết thêm thông tin, vui lòng truy cập <a href=\"http://ec2-54-252-160-78.ap-southeast-2.compute.amazonaws.com:3000/\">trang web của chúng tôi</a>.</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>\n";
    }
}
