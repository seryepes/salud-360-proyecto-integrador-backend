package app.service.helpers;

/**
 * Simula el envío de correos y SMS imprimiéndolos en consola.
 * En una app real aquí se conectaría un servicio de correo/SMS.
 */
public final class NotificationHelper {

    private NotificationHelper() {
    }

    public static void sendEmail(String to, String subject, String body) {
        System.out.println();
        System.out.println("  [CORREO SIMULADO] Para: " + to);
        System.out.println("  Asunto: " + subject);
        System.out.println("  " + body);
        System.out.println();
    }

    public static void sendSms(String phone, String body) {
        System.out.println();
        System.out.println("  [SMS SIMULADO] Para: " + phone);
        System.out.println("  " + body);
        System.out.println();
    }
}
