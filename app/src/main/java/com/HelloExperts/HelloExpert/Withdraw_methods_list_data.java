package com.HelloExperts.HelloExpert;

public class Withdraw_methods_list_data {
    private int withdraw_method_id;
    private String account_title;
    private String source_name;
    private String PaypalEmail;
    private String Status;

    public Withdraw_methods_list_data(int withdraw_method_id, String account_title, String source_name, String paypalEmail, String status) {
        this.withdraw_method_id = withdraw_method_id;
        this.account_title = account_title;
        this.source_name = source_name;
        PaypalEmail = paypalEmail;
        Status = status;

    }


    public int getWithdraw_method_id() {
        return withdraw_method_id;
    }

    public void setWithdraw_method_id(int withdraw_method_id) {
        this.withdraw_method_id = withdraw_method_id;
    }

    public String getAccount_title() {
        return account_title;
    }

    public void setAccount_title(String account_title) {
        this.account_title = account_title;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getPaypalEmail() {
        return PaypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        PaypalEmail = paypalEmail;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


}
