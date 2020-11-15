package com.infinitydheer.themanager.domain.constants;

import android.provider.BaseColumns;

public final class OfflineTableConstants {
    public static class TaskMeta implements BaseColumns {
        public static final String TABLE_NAME="task_meta";
        public static final String COL_NAME="task_name";
        public static final String COL_QUAD="task_quad";
        public static final String COL_DONE="task_done";
        public static final String COL_DUE_DATE="task_due_date";
    }

    public static class RazgoOV implements BaseColumns{
        public static final String TABLE_NAME="razgo_ov";
        public static final String COL_PARTNER="partner";
        public static final String COL_LAST="last_updt";
        public static final String COL_LAST_SENDER="last_sender";
        public static final String COL_LASTTIME="last_time";
        public static final String COL_K="k";
    }

    public static class RazgoOne implements BaseColumns{
        public static final String COL_CONTENT="content";
        public static final String COL_DT="datetime";
        public static final String COL_K="k";
        public static final String COL_SENDER="sender";
    }

    public static class RazgoUpdates implements BaseColumns{
        public static final String TABLE_NAME="razgo_updates";
        public static final String COL_CONVID="conv_id";
        public static final String COL_CONTENT="up_content";
        public static final String COL_DATETIME="up_datetime";
        public static final String COL_K="up_k";
    }
}
