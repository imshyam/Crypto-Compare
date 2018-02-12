package com.shapps.cryptocompare.Model;

import android.provider.BaseColumns;

/**
 * Created by shyam on 14/11/17.
 */

public class ExchangeDetailsSchema {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ExchangeDetailsSchema() {}


    /* Inner class that defines the table contents */
    public static class ExchangesDetailsEntry implements BaseColumns {
        public static final String TABLE_NAME = "exchanges_list";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_EX_NAME = "name";
        public static final String COLUMN_NAME_CRYPTO_CURRENCY = "crypto";
        public static final String COLUMN_NAME_CURRENCY = "curr";
        public static final String COLUMN_NAME_BUY_FEE = "0";
        public static final String COLUMN_NAME_SELL_FEE = "0";
        public static final String COLUMN_NAME_BUY = "123.4";
        public static final String COLUMN_NAME_SELL = "120.1";
        public static final String COLUMN_NAME_DATE_N_TIME = "12-11-2018";
        public static final String COLUMN_NAME_APP_ADDRESS = "google.com";
        public static final String COLUMN_NAME_WEB_ADDRESS = "google.com";
        public static final String COLUMN_NAME_ACTIVE = "active";
    }

}
