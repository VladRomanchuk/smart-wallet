package com.eightwizards.smartwallet;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Util {

    private static Context context;

    public static String getDateString(long timeStamp, SimpleDateFormat sdf) {

        try {
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    public static String formatUSD(String nominal) {
        char c[] = nominal.toCharArray();
        String hasil = "";
        int i = c.length - 1;
        int three_char = 0;
        while (i >= 0) {
            hasil = c[i] + hasil;
            three_char++;
            if (three_char == 3) {
                hasil = hasil;
                three_char = 0;
            }
            i--;
        }
        return "$" + hasil;
    }


    public static boolean checkTransactionDateInBudget(long tglTime, BudgetItem budget) {

        long startDate = budget.getTimeStartDate();
        long endDate = budget.getTimeEndDate();

        return (tglTime <= endDate) && (tglTime >= startDate);

    }

    public static long getTimeStamp(String dateStr, SimpleDateFormat sdf) {

        try {
            return sdf.parse(dateStr).getTime();
        } catch (ParseException e) {
            return 0;
        }

    }

    public static boolean budgetIsMoreThanZero(int idBudget, long amount, Context context) {
        SQLHelper helper = new SQLHelper(context);

        BudgetItem budget = helper.getDetailBudget(idBudget);

        return (Long.parseLong(budget.getLeft()) - amount) > 0;

    }

    public static void colorizeFocusItem(boolean focus, View view) {
        if (focus) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
    }

    public static boolean checkBudget(Context context) {

        SQLHelper helper = new SQLHelper(context);
        if (helper.getDetailLastBudget() != null) {
            BudgetItem budget = helper.getDetailLastBudget();

            long nowDate = (new Date()).getTime();
            long startDate = budget.getTimeStartDate();
            long endDate = budget.getTimeEndDate();

            return (nowDate <= endDate) && (nowDate >= startDate);

        } else {
            return false;
        }

    }

    public static Date addMonths(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}
