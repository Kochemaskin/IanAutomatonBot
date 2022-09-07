package com.proj3ct.IanAutomatonBot.service;

import com.proj3ct.IanAutomatonBot.configuration.BotConfig;
import com.proj3ct.IanAutomatonBot.model.Weather;
import com.proj3ct.IanAutomatonBot.model.WeatherModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    private static final String HELP_TEXT = "This bot can show you weather in any city he knows," +
            " /n after first show bot will remember your city and next time when " +
            "you want to see weather just command /weather to chat. ";

    @Value("London")
    String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", "get a start message"));
        botCommandList.add(new BotCommand("/location", "get your location"));
        botCommandList.add(new BotCommand("/weather", "show the weather in your location"));
        botCommandList.add(new BotCommand("/help", "help information"));

        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error, bot cannot execute bots command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();

            WeatherModel weatherModel = new WeatherModel();

            switch (messageText) {
                case "/start":
                    log.info("THIS IS START BOTTOM INIT");
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/location":
                    sendMessage(chatId, getLocation());
                    break;
                case "/weather":
                    try {
                        sendMessage(chatId, Weather.getWeather(location, weatherModel));
                        log.info("weather in default city showed");
                        break;
                    } catch (IOException e) {
                        log.info("wrong city entered" + e);
                        sendMessage(chatId, "Bot doesn't know this city, sir");
                    }
                    break;

                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;

                default:
                    try {
                        sendMessage(chatId, Weather.getWeather(messageText, weatherModel));
                        log.info("default location changed to: " + messageText);
                        setLocation(messageText);
                    } catch (IOException e) {
                        sendMessage(chatId, "unsupported command, sir");
                    }
            }
        }

    }


    private void startCommandReceived(long chatId, String firstName) {
        String answer = "Hello " + firstName + ", welcome";
        log.info("replied to user: " + firstName);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error: " + e.getMessage());
        }
    }
}
