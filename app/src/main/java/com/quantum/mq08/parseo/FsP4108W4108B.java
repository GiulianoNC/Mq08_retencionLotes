package com.quantum.mq08.parseo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FsP4108W4108B {
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("data")
        @Expose
        private Data__2 data;
        @SerializedName("errors")
        @Expose
        private List<Object> errors;
        @SerializedName("warnings")
        @Expose
        private List<Object> warnings;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Data__2 getData() {
            return data;
        }

        public void setData(Data__2 data) {
            this.data = data;
        }

        public List<Object> getErrors() {
            return errors;
        }

        public void setErrors(List<Object> errors) {
            this.errors = errors;
        }

        public List<Object> getWarnings() {
            return warnings;
        }

        public void setWarnings(List<Object> warnings) {
            this.warnings = warnings;
        }

    }
