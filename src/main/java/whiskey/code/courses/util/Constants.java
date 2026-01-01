package whiskey.code.courses.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public final static String PREVIOUS = "\uD83D\uDD19 Назад";
    public final static String NEXT = "Далее ➡\uFE0F \uD83D\uDCA8";
    public final static String PAGE_CATEGORY = "pg-category_";
    public final static String PAGE_COURSE = "pg-course_";
    public final static String PAGE_COURSE_SEARCH = "pg-course-search_";
    public final static String PAGE_LESSON = "pg-lesson_";
    public final static String PAGE_LESSON_SEARCH = "pg-lesson-search_";
    public final static String LESSON = "lesson_";
    public final static String LESSON_SEARCH = "lesson-search_";
    public final static String ZERO_PAGE = "_0";
    public final static String DELIMITER_PAGE = "_";
    public final static int TOTAL_LENGTH = 20;
    public final static String FILLED_SYMBOL = "▓";
    public final static String EMPTY_SYMBOL = "▒";
    public final static String DELIMITER = " ";
    public final static String BACK_TO_CATEGORY = "\uD83C\uDFEF Назад к категориям \uD83D\uDD19";
    public final static String SEARCH_COURSE = "\uD83D\uDD0D Поиск курсов";

    // Course commands
    public static final String CREATE_COURSE = "/c_c ";
    public static final String UPDATE_COURSE_FULL = "/u_c_f ";
    public static final String UPDATE_COURSE_TITLE = "/u_c_t ";
    public static final String UPDATE_COURSE_VISIBILITY = "/u_c_v ";
    public static final String DELETE_COURSE = "/d_c ";

    // Lesson commands
    public static final String CREATE_LESSON = "/c_l ";
    public static final String UPDATE_LESSON_FULL = "/u_l_f ";
    public static final String UPDATE_LESSON_TITLE = "/u_l_t ";
    public static final String UPDATE_LESSON_COURSE = "/u_l_c ";
    public static final String UPDATE_LESSON_MESSAGE = "/u_l_m ";
    public static final String UPDATE_LESSON_ORDER = "/u_l_o ";
    public static final String DELETE_LESSON = "/d_l ";
}
