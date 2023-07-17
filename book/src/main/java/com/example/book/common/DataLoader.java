package com.example.book.common;


import com.example.book.bot.Bot;
import com.example.book.model.Channel;
import com.example.book.model.User;
import com.example.book.repository.ChannelRepository;
import com.example.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Value("${botToken}")
    private String botToken;

    @Value("${botUsername}")
    private String botUsername;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String initMode;

    private final UserRepository userRepository;
    private final ChannelRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        if (initMode.equals("create")) {
           userRepository.save(new User(134365554L, "Feruza", "admin"));
           bookRepository.save(new Channel("Saodat asri qissalari", "Саодат асри қиссалари" ,"https://t.me/+RJq5N1ebVLsxMmQy",50000.0));
           bookRepository.save(new Channel("Payg’ambarlar tarixi", "Пайғамбарлар тарихи" ,"https://t.me/+7ShbZl2YKG4yOTgy",50000.0));
           bookRepository.save(new Channel("Alkimyogar", "Алкимёгар" ,"https://t.me/+mfsa-6o9qRwyN2My",50000.0));
           bookRepository.save(new Channel("Haqiqiy omad kaliti yohud savdogarlar ustozi", "Ҳақиқий омад калити ёҳуд савдогарлар устози" ,"https://t.me/+9Ta8RTLnkHM2ZjMy", 50000.0));
           bookRepository.save(new Channel("Asmauul Husnaa", "Асмауул Ҳуснаа" ,"https://t.me/+D6coKivdB402ZGIy", 50000.0));
           bookRepository.save(new Channel("J.Rumiy. Masnaviy 40 rivoyatga sharh", "Ж.Румий. Маснавий 40 ривоятга шарҳ" ,"https://t.me/+N_N-SVwwQX04Y2Qy", 50000.0));
        }

        try {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(new Bot(botToken,botUsername,userRepository, bookRepository));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
