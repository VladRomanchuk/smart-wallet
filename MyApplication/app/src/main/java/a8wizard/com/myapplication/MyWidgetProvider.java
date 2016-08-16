package a8wizard.com.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;

public class MyWidgetProvider extends AppWidgetProvider {
	// private static final String ACTION_CLICK = "ACTION_CLICK";
	SQLHelper helper;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
						 int[] appWidgetIds) {

		helper = new SQLHelper(context);

		// Get all ids
		ComponentName thisWidget = new ComponentName(context,
				MyWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		for (int widgetId : allWidgetIds) {

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			// Register an onClickListener
			Intent intent = new Intent(context, MainActivity.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);  // Identifies the particular widget...
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// Make the pending intent unique...
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			views.setOnClickPendingIntent(R.id.linearLayout1, pendingIntent);
			
			if (!Util.checkBudget(context)) {
				views.setTextViewText(R.id.time_row, "");
				views.setTextViewText(R.id.description_row, "Buget not set");
				views.setTextViewText(R.id.bill_row, "");
				views.setTextViewText(R.id.textView4, "");
			} else {
				views.setTextViewText(R.id.time_row, "Budget left");
				views.setTextViewText(R.id.description_row,
						Util.formatUSD(helper.getDetailLastBudget().getLeft()));
				views.setTextViewText(R.id.bill_row, Util.getDateString(helper
						.getDetailLastBudget().getTimeStartDate(),
						new SimpleDateFormat("dd/MM/yyyy kk:mm:ss")));

				views.setTextViewText(R.id.textView4, Util.getDateString(helper
						.getDetailLastBudget().getTimeEndDate(),
						new SimpleDateFormat("dd/MM/yyyy kk:mm:ss")));
			}
			// To update a label

			// Tell the AppWidgetManager to perform an update on the current app
			// widget
			appWidgetManager.updateAppWidget(widgetId, views);
		}
	}

}
