package com.example.book.bot;

import com.example.book.model.Channel;
import com.example.book.model.User;
import com.example.book.repository.ChannelRepository;
import com.example.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUsername;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private Channel channel = new Channel();

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        User user = getCurrentUser(update);
        String langCode = user.getLanguageCode();
        if (update.hasMessage()) {
            Message message = update.getMessage();
            int step = user.getStep();
            if (message.hasText()) {
                String text = message.getText();

                if (user.getRole().equals("user")) {

                        if (langCode == null)
                            sendTextMessage(user.setStep(0), "Tilni tanlang!\n\nТилни танланг!");
                        else if (text.equalsIgnoreCase("/start")) {
                            sendTextMessage(user.setStep(0), "Tilni tanlang!\n\nТилни танланг!");
                        }
                    switch (step) {
                        case 1 -> {
                            if (text.equalsIgnoreCase("Kanalar ro'yxati") || text.equalsIgnoreCase("Каналар рўйхати"))
                                sendTextMessage(user.setStep(2),user.getLanguageCode().equals("uz") ? "O'zingizga kerak bo'lgan kanalni tanlang:" : "Ўзингизга керак бўлган канални танланг:");
//                                if (user.getLanguageCode().equals("uz")) {
//                                    sendTextMessage(user.setStep(2), "O'zingizga kerak bo'lgan kanalni tanlang:");
//                                } else if (user.getLanguageCode().equals("kr")) {
//                                    sendTextMessage(user.setStep(5), "Ўзингизга керак бўлган канални танланг:");
//                                }
                        }
                        case 2 -> {
                            List<Channel> all = channelRepository.findAll();
                            for (Channel channel : all) {
                                if (channel.getName().equals(text)) {
                                    user.setChannelLink(channel.getLink());
                                    sendTextMessage(user.setStep(3), user.getLanguageCode().equals("uz") ? "Kanalga qo'shilish uchun\n\n" + channel.getPrice() + " so'm to'lov qilishingiz kerak.\nTo'lovni tasdiqlash uchun chek rasmini yuboring❗\uFE0F\n\n" + "Bizni karta raqam:\n" + channel.getCard() :
                                                                                                         ( "Каналга қўшилиш учун \n\n" + channel.getPrice() + " сўм тўлов қилишингиз керак. \nТўловни тасдиқлаш учун чек расмини юборинг❗\uFE0F\n\n" + "Бизни карта рақам:\n" + channel.getCard()));
                                }
                            }
                        }
                        case 3 -> {

                                if(text.equals("Orqaga ⬅") || text.equals("Орқага ⬅")){
                                     sendTextMessage(user.setStep(1), user.getLanguageCode().equals("uz") ?"Kanalar ro'yxati tugmasini bosing \uD83D\uDC47" : "Каналар рўйхати тугмасини босинг \uD83D\uDC47");
                                 } else if (!text.equalsIgnoreCase("/start")) {
                                    sendTextMessage(user, user.getLanguageCode().equals("uz") ? "Iltimos rasm yuboring" : "Илтимос расм юборинг");
                                }
                        }
                        case 4 -> {
                            if (text.equals("Kanalga qo'shilish")){
                                sendTextMessage(user.setStep(3), user.getLanguageCode().equals("uz") ?"Raxmat" : "Raxmat1");
                            }
                        }
                    }
                }

                else {
                    switch (step) {
                        case 0 -> sendTextMessage(user.setStep(1), "Xush kelibsiz!");
                        case 1 -> {
                            switch (text) {
                                case "Kanal qo'shish ➕" -> sendTextMessage(user.setStep(2), "Kanal nomini kiriting");
                                case "Kanalni o'chirish \uD83D\uDDD1" ->
                                        sendTextMessage(user.setStep(5), "O'chirish kerak bolgan kanalni tanlang");
                                case "Kanal nomini o'zgartirish ✏" ->
                                        sendTextMessage(user.setStep(6), "O'zgarishi kerak bolgan kanalni tanlang");
                                case "Kanal linkini o'zgartirish ✏" ->
                                        sendTextMessage(user.setStep(8), "O'zgarishi kerak bolgan kanalni tanlang");
                                case "Kanal narxini o'zgartirish ✏" ->
                                        sendTextMessage(user.setStep(10), "O'zgarishi kerak bolgan kanalni tanlang");
                                case "Kanallar haqida malumot ℹ" ->
                                        sendTextMessage(user.setStep(1), String.valueOf(channelRepository.findAll()));

                            }
                        }
                        case 2 -> {
                            channel.setName(text);
                            sendTextMessage(user.setStep(3), "Kanal linkini jo'nating");
                        }
                        case 3 -> {
                            channel.setLink(text);
                            sendTextMessage(user.setStep(4), "Kanal narxi");
                        }
                        case 4 -> {
                            channel.setPrice(Double.valueOf(text));
                            channelRepository.save(channel);
                            sendTextMessage(user.setStep(1), "Kanal qo'shildi");
                            channel = new Channel();
                        }
                        case 5 -> {
                            channelRepository.deleteByName(text);
                            sendTextMessage(user.setStep(1), "Kanal o'chirildi");
                        }
                        case 6 -> {
                            channel = channelRepository.findByName(text);
                            sendTextMessage(user.setStep(7), "Kanalning yangi nomini kiriting");
                        }
                        case 7 -> {
                            channel.setName(text);
                            channelRepository.save(channel);
                            sendTextMessage(user.setStep(1), "Kanal nomi o'zgartirildi");
                        }
                        case 8 -> {
                            channel = channelRepository.findByName(text);
                            sendTextMessage(user.setStep(9), "Kanalning yangi linkini kiriting");
                        }
                        case 9 -> {
                            channel.setLink(text);
                            channelRepository.save(channel);
                            sendTextMessage(user.setStep(1), "Kanal linki o'zgartirildi");
                        }
                        case 10 -> {
                            channel = channelRepository.findByName(text);
                            sendTextMessage(user.setStep(11), "Kanalning yangi narxini kiriting");
                        }
                        case 11 -> {
                            channel.setPrice(Double.valueOf(text));
                            channelRepository.save(channel);
                            sendTextMessage(user.setStep(1), "Kanal narxi o'zgartirildi");
                        }
                    }
                }
            } else if (message.hasPhoto()) {
                if (user.getRole().equals("user"))
                    if (step == 3) {
                        sendTextMessage(user, user.getLanguageCode().equals("uz") ? "Malumotlar adminga yuborildi. Javob kelishini kuting" : ("Малумотлар админга юборилди. Жавоб келишини кутинг"));
                        User admin = userRepository.findAdmin();
                        sendForwardMessage(admin.getId(), user.getId(), message.getMessageId());
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(admin.getId());
                        sendMessage.setText("Ism: " + user.getFirstName() + "\nTasdiqlaysizmi?");

                        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();
                        List<InlineKeyboardButton> inlineRow = new ArrayList<>();

                        InlineKeyboardButton cancel = new InlineKeyboardButton();
                        cancel.setText("Bekor qilish ❌");
                        cancel.setCallbackData(user.getId() + "_false");
                        inlineRow.add(cancel);

                        InlineKeyboardButton accept = new InlineKeyboardButton();
                        accept.setText("Tasdiqlash ✅");
                        accept.setCallbackData(user.getId() + "_true");
                        inlineRow.add(accept);
                        inlineRows.add(inlineRow);
                        inlineKeyboardMarkup.setKeyboard(inlineRows);

                        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
            }
        }


        else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            if (user.getRole().equals("user")) {
                if (data.equals("uz"))
                    user.setLanguageCode("uz");
                else user.setLanguageCode("kr");
                sendTextMessage(user.setStep(1), data.equals("uz") ? "Siz lotin tilini tanladingiz\n\nKanallarga a'zo bo'lish uchun\nKanalar ro'yxati tugmasini bosing \uD83D\uDC47" : "Сиз кирил тилини танладингиз\n\nКаналларга аъзо бўлиш учун\nКаналар рўйхати тугмасини босинг \uD83D\uDC47");
            }

            else {
                String[] s = data.split("_");
                User user1 = userRepository.findById(Long.parseLong(s[0])).get();
                if (s[1].equals("true")) {
                    sendTextMessage(user1.setStep(4), user1.getLanguageCode().equals("uz") ? "Admin tasdiqladi ✅" : "Админ тасдиқлади ✅");

                } else {
                    sendTextMessage(user1, user1.getLanguageCode().equals("uz") ? "Admin tasdiqlamadi!" : "Админ тасдиқламади!");
                }
                deleteMessage(user.getId(), callbackQuery.getMessage().getMessageId());
            }
        }

        userRepository.save(user);
    }


    private ReplyKeyboard getReplyKeyboard(User user) {
        int step = user.getStep();
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();
        markup.setKeyboard(rows);
        if (user.getRole().equals("user")) {
            switch (step) {
                case 0 -> {
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();
                    List<InlineKeyboardButton> inlineRow = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText("Lotincha \uD83C\uDDFA\uD83C\uDDFF");
                    button.setCallbackData("uz");
                    inlineRow.add(button);
                    InlineKeyboardButton button1 = new InlineKeyboardButton();
                    button1.setText("Кирилча \uD83C\uDDFA\uD83C\uDDFF");
                    button1.setCallbackData("kr");
                    inlineRow.add(button1);
                    inlineRows.add(inlineRow);
                    inlineKeyboardMarkup.setKeyboard(inlineRows);
                    return inlineKeyboardMarkup;
                }
                case 1 -> {
                    KeyboardRow row = new KeyboardRow();
                    row.add(user.getLanguageCode().equals("uz") ? "Kanalar ro'yxati" : "Каналар рўйхати ");
                    rows.add(row);
                }
                case 2 -> getAllChannels(rows);
                case 3 -> {
                    KeyboardRow row = new KeyboardRow();
                    row.add(user.getLanguageCode().equals("uz") ? "Orqaga ⬅" : "Орқага ⬅");
                    rows.add(row);
                }
                case 4 -> {
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();
                    List<InlineKeyboardButton> inlineRow = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText("Kanalga qo'shilish");
                    button.setCallbackData("uz");
                    button.setUrl(user.getChannelLink());
                    inlineRow.add(button);
                    inlineRows.add(inlineRow);
                    inlineKeyboardMarkup.setKeyboard(inlineRows);
                    return inlineKeyboardMarkup;
                }
                case 5 -> {
                    getAllChannelsKr(rows);
                }
            }
        } else {
            switch (step) {
                case 1 -> {
                    KeyboardRow row = new KeyboardRow();
                    row.add("Kanal qo'shish ➕");
                    row.add("Kanalni o'chirish \uD83D\uDDD1");
                    rows.add(row);
                    row = new KeyboardRow();
                    row.add("Kanal nomini o'zgartirish ✏");
                    row.add("Kanal linkini o'zgartirish ✏");
                    rows.add(row);
                    row = new KeyboardRow();
                    row.add("Kanal narxini o'zgartirish ✏");
                    row.add("Kanallar haqida malumot ℹ");
                    rows.add(row);

                }
                case 5, 6, 8, 10 ->
                        getAllChannels(rows);

            }
        }
        return markup;
    }

    private void getAllChannels(List<KeyboardRow> rows) {
        int count = 0;
        KeyboardRow row = new KeyboardRow();
        for (Channel book : channelRepository.findAll()) {
            row.add(book.getName());
            if (count % 2 == 0) {
                rows.add(row);
                row = new KeyboardRow();
            }
            count++;
        }
        rows.add(row);
    }

    private void getAllChannelsKr(List<KeyboardRow> rows) {
        int count = 0;
        KeyboardRow row = new KeyboardRow();
        for (Channel book : channelRepository.findAll()) {
            row.add(book.getNameKr());
            if (count % 2 == 0) {
                rows.add(row);
                row = new KeyboardRow();
            }
            count++;
        }
        rows.add(row);
    }

    private User getCurrentUser(Update update) {
        org.telegram.telegrambots.meta.api.objects.User from;
        if (update.hasMessage()) from = update.getMessage().getFrom();
        else from = update.getCallbackQuery().getFrom();
        Long id = from.getId();
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElseGet(() -> userRepository.save(new User(id, from.getFirstName(), "user")));
    }

    private void sendForwardMessage(Long toChatId, Long fromChatId, int messageId) {
        ForwardMessage forwardMessage = new ForwardMessage();
        forwardMessage.setChatId(toChatId);
        forwardMessage.setMessageId(messageId);
        forwardMessage.setFromChatId(fromChatId);
        try {
            execute(forwardMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendTextMessage(User user, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getId());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(getReplyKeyboard(user));
        sendMessage.setParseMode("html");
        sendMessage.setDisableWebPagePreview(true);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteMessage(Long chatId, int messageId) {
        DeleteMessage delete = new DeleteMessage();
        delete.setChatId(chatId);
        delete.setMessageId(messageId);
        try {
            execute(delete);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void restartBot(Update update) {
        User user = getCurrentUser(update);
        sendTextMessage(user.setStep(1), "Welcome to the bot!");

        userRepository.save(user);
    }
}