package com.estel.cashmoovsubscriberapp.model;

import java.util.ArrayList;

public class GenderModel {

    ArrayList<Gender> genderArrayList = new ArrayList<>();

    public GenderModel(ArrayList<Gender> genderArrayList) {
        this.genderArrayList = genderArrayList;
    }

    public ArrayList<Gender> getGenderArrayList() {
        return genderArrayList;
    }

    public void setGenderArrayList(ArrayList<Gender> genderArrayList) {
        this.genderArrayList = genderArrayList;
    }

    public static class Gender {
        public int id;
        public String code;
        public String type;
        public String status;
        public String creationDate;

        public Gender(int id, String code, String type, String status, String creationDate) {
            this.id = id;
            this.code = code;
            this.type = type;
            this.status = status;
            this.creationDate = creationDate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }
    }
}