package whiskey.code.courses.service;

public interface UserStateService {

    void setState(Long chatId);

    boolean isWaitingForSearch(Long chatId);

    void clearState(Long chatId);

}
