package ru.startandroid.apartmentrentals;

public class ApartmentItem {

    private String mId;
    private String mAmountUSD;
    private String mCurrencyUSD;
    private String mAmountBYN;
    private String mCurrencyBYN;
    private String mAmountBYR;
    private String mCurrencyBYR;
    private String mRentType;
    private String mAddress;
    private String mPhotoUrl;
    private String mWebUrl;

    public String getWebUrl() {
        return mWebUrl;
    }

    public void setWebUrl(String webUrl) {
        mWebUrl = webUrl;
    }

    public String getAmountUSD() {
        return mAmountUSD;
    }

    public void setAmountUSD(String amountUSD) {
        mAmountUSD = amountUSD;
    }

    public String getCurrencyUSD() {
        return mCurrencyUSD;
    }

    public void setCurrencyUSD(String currencyUSD) {
        mCurrencyUSD = currencyUSD;
    }

    public String getAmountBYN() {
        return mAmountBYN;
    }

    public void setAmountBYN(String amountBYN) {
        mAmountBYN = amountBYN;
    }

    public String getCurrencyBYN() {
        return mCurrencyBYN;
    }

    public void setCurrencyBYN(String currencyBYN) {
        mCurrencyBYN = currencyBYN;
    }

    public String getAmountBYR() {
        return mAmountBYR;
    }

    public void setAmountBYR(String amountBYR) {
        mAmountBYR = amountBYR;
    }

    public String getCurrencyBYR() {
        return mCurrencyBYR;
    }

    public void setCurrencyBYR(String currencyBYR) {
        mCurrencyBYR = currencyBYR;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getRentType() {
        return mRentType;
    }

    public void setRentType(String rentType) {
        mRentType = rentType;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

}
