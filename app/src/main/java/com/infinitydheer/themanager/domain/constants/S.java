package com.infinitydheer.themanager.domain.constants;

public final class S {
    private S(){}

    public static class Constants{
        public static final boolean LOG_ENABLED=true;
        public static final String LOG_ID="TheManagerLog";

        public static final String INTENT_QUAD_KEY="quad_key";
        public static final String INTENT_TASKID_KEY="task_id";
        public static final String INTENT_CONVID_KEY="conv_id";
        public static final String INTENT_USER_KEY="user_key";
        public static final String INTENT_CONV_ID_CLEAN_KEY="clean_conv_id";

        public static final String INFINITY_TEXT="1512sdah";
        public static final String SUDO_INFINITY_TEXT="1512sdah_sudo";
        public static final String MASTER_PIN="271095811";

        public static final int CODE_SUCCESS=1;

        public static final String PREF_USER="userPrefs";
        public static final String PREF_USER_MAINP="bdayphone";
        public static final String PREF_LAST_SYNC="lastsync";

        public static final int REQ_AUTH_USER=2;

        public static final long SYNC_THRESHOLD=10000;

        public static final String COME_BACK_NOTIFICATION_TITLE="Come back";
        public static final String COME_BACK_NOTIFICATION_TEXT="Plan and manage your tasks";

        public static final String CLEAN_CONV_NOTIFICATION_TITLE="Scheduling tasks";
        public static final String CLEAN_CONV_NOTIFICATION_TEXT="About to be done soon...";

        public static final CharSequence CLEANCONV_CHANNEL_NAME="Cloud Cleaning";
        public static final String CLEANCONV_CHANNEL_DESCR="Allows for the background cleaning of the cloud storage space of the app.";
    }

    public static class RConst{
        public static final String COLL_CONV="casGWasd1VS";
            public static final String DOC_COLL_META="reg";
                public static final String COLL_SYNCED="succ";

        public static final String COLL_META="usage_statistics";
        public static final String K_UPASS="urn";
        public static final String K_ACCOUNT_ALLOWED="masterP";
            public static final String COLL_UPDATES="recent_views";
            public static final String COLL_PART="errcodes";

        //Key Constants
        public static final String K_K="asd";
        public static final String K_SENDER="dne";
        public static final String K_SYNCED="update";
        public static final String K_DT="errtyp";
        public static final String K_CONTENT="errmsg";
        public static final String K_ID="errdi";

        public static final String K_LATID="groupid";
        public static final String K_ONE_ID="begin";
        public static final String K_TWO_ID="end";

        public static final String K_SUPPORT="exists";

        public static final String K_CONV_LATID="gridex";
    }
}
