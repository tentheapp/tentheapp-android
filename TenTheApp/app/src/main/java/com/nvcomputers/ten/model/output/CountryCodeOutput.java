package com.nvcomputers.ten.model.output;

import java.util.ArrayList;

/**
 * Created by kambojRavi on 4/21/2017.
 */

public class CountryCodeOutput {
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response {
        private String message;

        private Result result;

        private String status;

        private String code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public class Result {
        private ArrayList<Country> country;

        public ArrayList<Country> getCountry() {
            return country;
        }

        public void setCountry(ArrayList<Country> country) {
            this.country = country;
        }
    }

    public class Country {
        private String countryId;

        private String countryName;

        private String countryISO;

        private String phoneCode;

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCountryISO() {
            return countryISO;
        }

        public void setCountryISO(String countryISO) {
            this.countryISO = countryISO;
        }

        public String getPhoneCode() {
            return phoneCode;
        }

        public void setPhoneCode(String phoneCode) {
            this.phoneCode = phoneCode;
        }
    }
}


