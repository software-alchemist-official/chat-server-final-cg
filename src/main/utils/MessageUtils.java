package main.utils;

import main.model.Message;

import static java.util.Objects.isNull;

public class MessageUtils {
    public static void validateMessage(Message message) {
        validateContent(message.getContent());
        validateUserId(message.getUserId());
    }

    private static void validateContent(String content) {
        if (isNull(content) || content.isEmpty()) {
            throw new IllegalArgumentException("El contenido del mensaje no puede ser null o vacío");
        }
    }

    private static void validateUserId(int userId) {
        if (isInvalidId(userId)) {
            throw new IllegalArgumentException("El id es inválido");
        }
    }

    public static void validateMessageId(int messageId) {
        if (isInvalidId(messageId)) {
            throw new IllegalArgumentException("El id: " + messageId + " es inválido");
        }
    }

    public static boolean isInvalidId(int userId) {
        return userId <= 0;
    }
}
