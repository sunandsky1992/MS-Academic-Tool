package Common;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ss on 16-4-28.
 */
public class Constants {
    public static RequestMethod DEFAULT_REQUEST_METHOD = RequestMethod.POST;

    public static final boolean NEED_FEEDBACK_OR_NOT = true;

    public static final String EMPTY_STRING = "";

    public static final boolean OUTPUT_ALLOWED = true;

    public static final int CONNECTION_TIMEOUT = 3000;

    public static final int READ_TIMEOUT = 30000;

    public static final String ENCODING = "UTF-8";
}
