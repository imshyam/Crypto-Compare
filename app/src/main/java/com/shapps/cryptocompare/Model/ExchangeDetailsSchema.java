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
        public static final String COLUMN_NAME_ACTIVE = "active";
    }

}
