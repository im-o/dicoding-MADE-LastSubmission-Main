package com.stimednp.aplikasimoviecataloguesub4.addingmethod;

import android.content.Context;

import com.stimednp.aplikasimoviecataloguesub4.R;

/**
 * Created by rivaldy on 8/3/2019.
 */

public class AllMyStrings {
    public String getNoInet(Context context) {
        return context.getResources().getText(R.string.str_no_internet).toString();
    }

    public String getTryAgain(Context context) {
        return context.getResources().getText(R.string.str_try_again).toString();
    }

    public String getRecon(Context context) {
        return context.getResources().getText(R.string.str_reconnect).toString();
    }

    public String getWrongNet(Context context) {
        return context.getResources().getText(R.string.str_wrong_net).toString();
    }

    public String getWrongErr(Context context) {
        return context.getResources().getText(R.string.str_error_wrong).toString();
    }
}
