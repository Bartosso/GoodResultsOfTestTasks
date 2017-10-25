/**
 * Created by Eshu on 13.06.2017.
 */
import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    String fio = null;
    String year = null;
    boolean creatingRequest = false;
    String phoneNumber = null;
    String adminChatId = "360494142";
    String priveGroupLink = "el testo";
    private int countAn = 0;


    @Override
    public String getBotUsername() {
        return "Bartosso_bot";
    }

    @Override
    public String getBotToken() {
        return "393376695:AAGzzmMvDs5opvDF1AcHui4pLnYtD4PuXec";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null ) {

            if (message.hasText() && message.getText().equals("/start")){
                creatingRequest = true;
                countAn = 0;
            }
            if(creatingRequest){
                createReques(update);
            }
            else
                sendMsg(message, "Я не знаю что ответить на это");
        }
        if(update.hasCallbackQuery()){
            String callackData[] = update.getCallbackQuery().getData().split(" ");
            if(callackData[0].equals("No")){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("К сожалению вы слабое звено");
            sendMessage.setChatId(callackData[1]);
                try {
                    sendMessage(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if(callackData[0].equals("Yes")){
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(priveGroupLink);
                sendMessage.setChatId(callackData[1]);
                try {
                    sendMessage(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    private void createReques(Update update){

    switch (countAn){
        case 0: sendMsg(update.getMessage(),"Здравствуйте! Представтесь пожалуйста (В формате Ф.И.О.)");
        countAn++;
        break;
        case 1: fio = update.getMessage().getText();
        sendMsg(update.getMessage(),"Ваш год рождения?");
        countAn++;

        break;
        case 2: year = update.getMessage().getText();
        countAn++;
            //
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            KeyboardButton button1 = new KeyboardButton("Дать номер").setRequestContact(true);
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(button1);
            List<KeyboardRow> rows = new ArrayList<>();
            rows.add(keyboardRow);
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setKeyboard(rows);
            replyKeyboardMarkup.setKeyboard(rows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            sendMessage.setText("А так же ваш номер (нажимать на кнопку)");
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            //

        break;

        case 3:
            if(update.getMessage().getContact() != null){
                phoneNumber = update.getMessage().getContact().getPhoneNumber();
                creatingRequest = false;
                countAn = 0;
              sendMsgToAdmin(new UserRequest(fio,year,phoneNumber,update.getMessage().getChatId()));
                fio = null;
                year = null;
                phoneNumber = null;
                sendMsg(update.getMessage(), "Ожидаем решения");
            }
            else {
                sendMsg(update.getMessage(),"Вы не дали свой номер");
            }

        break;
    }

    }


    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);

            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

    }


    public void sendMsgToAdmin(UserRequest userRequest){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(adminChatId);
        sendMessage.setText("Запрос" + "\nФИО: " + userRequest.getFio() + "\nГод рождения: " + userRequest.getYear() + "\nНомер телефона: "
        + userRequest.getPhoneNumber());
//
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton yes = new InlineKeyboardButton("Yes");
        InlineKeyboardButton no = new InlineKeyboardButton("No");
        yes.setCallbackData("Yes" + " " + userRequest.getChatId());
        no.setCallbackData("No" + " " + userRequest.getChatId());
        row.add(yes);
        row.add(no);
        keyboard.add(row);
        markup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(markup);
//
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}

