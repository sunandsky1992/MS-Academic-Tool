package Common;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ss on 16-4-28.
 */
public class Constants {
    public static RequestMethod DEFAULT_REQUEST_METHOD = RequestMethod.GET;

    public static final boolean NEED_FEEDBACK_OR_NOT = true;

    public static final String EMPTY_STRING = "";

    public static final boolean OUTPUT_ALLOWED = true;

    public static final int CONNECTION_TIMEOUT = 3000;

    public static final int READ_TIMEOUT = 30000;

    public static final String ENCODING = "UTF-8";

    public static final String AUID_TYPE = "AuId";

    public static final String ID_TYPE = "Id";

    public static final String CID_TYPE = "CId";

    public static final String FID_TYPE = "FId";

    public static final String JID_TYPE = "JId";

    public static final String AFD_TYPE = "AfId";

    public static final String RID_TYPE = "RId";
}
