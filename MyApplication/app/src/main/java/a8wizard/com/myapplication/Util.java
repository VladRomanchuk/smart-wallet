package a8wizard.com.myapplication;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RemoteViews;

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

//    public static void refreshTimePickerAtAddTransactionFragment(){
//        AddTransactionFragment.transactionDatePickerTextView.setText(new SimpleDateFormat("kk:mm:ss").format(new Date()));
//        //AddTransactionFragment.bDatePicker.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
//    }

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

        if ((Long.parseLong(budget.getLeft()) - amount) > 0) {
            return true;
        }

        return false;
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

        // SQLHelper helper = new SQLHelper(context);
        // if (helper.getDetailLastBudget() != null) {
        // Budget budget = helper.getDetailLastBudget();
        //
        //
        // Date nowDate;
        // try {
        // nowDate = sdf.parse(sdf.format((new Date())));
        // Date startDate = sdf.parse(budget.getStartDate());
        // Date endDate = sdf.parse(budget.getEndDate());
        //
        // return (nowDate.after(startDate) && nowDate.before(endDate))
        // || (nowDate.equals(startDate))
        // || (nowDate.equals(endDate));
        // } catch (ParseException e) {
        // // TODO Auto-generated catch block
        // return false;
        // }
        //
        // } else {
        // return false;
        // }
    }

    public static void updateWidget(Context context) {
        SQLHelper helper = new SQLHelper(context);
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        ComponentName thisWidget = new ComponentName(context,
                MyWidgetProvider.class);

        remoteViews.setTextViewText(R.id.time_row, "Sisa budget");
        remoteViews.setTextViewText(R.id.description_row, Util.formatUSD(helper
                .getDetailLastBudget().getLeft()));
        remoteViews.setTextViewText(R.id.bill_row, Util.getDateString(helper
                        .getDetailLastBudget().getTimeStartDate(),
                new SimpleDateFormat("dd/MM/yyyy kk:mm:ss")));
        remoteViews.setTextViewText(R.id.textView4, Util.getDateString(helper
                        .getDetailLastBudget().getTimeEndDate(),
                new SimpleDateFormat("dd/MM/yyyy kk:mm:ss")));

        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

    }

    public static Date addMonths(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month); // minus number would decrement the days
        return cal.getTime();
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); // minus number would decrement the days
        return cal.getTime();
    }
}
