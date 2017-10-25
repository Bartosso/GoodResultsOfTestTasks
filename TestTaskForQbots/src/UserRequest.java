/**
 * Created by Eshu on 13.06.2017.
 */
public class UserRequest {
   private String fio;
   private String year;
   private String phoneNumber;
   private long chatId;
    UserRequest(String fio, String year,String phoneNumber, long chatId ){
     this.fio = fio;
     this.year = year;
     this.phoneNumber = phoneNumber;
     this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }

    public String getFio() {
        return fio;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getYear() {
        return year;
    }
}
