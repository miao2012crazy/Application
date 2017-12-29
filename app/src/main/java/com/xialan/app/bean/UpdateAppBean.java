package com.xialan.app.bean;

/**
 * Created by Administrator on 2017/12/17.
 */

public class UpdateAppBean {

    /**
     * code : S
     * data : {"version_code":"13","file_path":"goumei_mobile_app_rev20171130_13.apk"}
     */

    private String code;
    private DataBean data;
    private String err_msg;

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * version_code : 13
         * file_path : goumei_mobile_app_rev20171130_13.apk
         */
        private String ios;

        public String getIos() {
            return ios;
        }

        public void setIos(String ios) {
            this.ios = ios;
        }
        private String version_code;
        private String file_path;

        public String getVersion_code() {
            return version_code;
        }

        public void setVersion_code(String version_code) {
            this.version_code = version_code;
        }

        public String getFile_path() {
            return file_path;
        }

        public void setFile_path(String file_path) {
            this.file_path = file_path;
        }
    }
}
