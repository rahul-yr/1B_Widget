package example.a1widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class SampleWidgetProvider extends AppWidgetProvider {

    private static final String FOOD_PREF = "food_prefs";
    private static final String WIDGETID = "widget_id";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        // Creating the set of data using arrayList
        ArrayList<Special> specials = new ArrayList<>();
        // specials.add(new Special(R.string.artichokes,R.drawable.artichokes));
        specials.add(new Special(R.string.pizza, R.drawable.pizza));
        specials.add(new Special(R.string.quinoa_salad, R.drawable.quinoa_salad));
        specials.add(new Special(R.string.stuffed_mushrooms, R.drawable.stuffed_mushrooms));

        SharedPreferences sharedPreferences = context.getSharedPreferences(FOOD_PREF, Context.MODE_PRIVATE);

        for (int appWidgetId : appWidgetIds) {

            int currentItemNum = sharedPreferences.getInt(WIDGETID.concat("_" + appWidgetId), 0);
            if (currentItemNum == (specials.size() - 1)) {
                currentItemNum = 0;
            } else {
                currentItemNum++;
            }
            Special temp = specials.get(currentItemNum);
            Log.i("SampleWidgetProvider ", "WP : " + currentItemNum + " widgetId " + appWidgetId);
            sharedPreferences.edit().putInt(WIDGETID.concat("_" + appWidgetId), currentItemNum)
                    .apply();
            // creating Remote Action
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sample_widget_provider);
            views.setTextViewText(R.id.appwidget_text, context.getString(temp.getTextId()));
            views.setImageViewResource(R.id.appwidget_image, temp.getImageId());
            // It's time to set the click listener for a button for that
            // need to create a pending event
            // Create boiler plate code for updating Pending events
            Intent intent = new Intent(context, SampleWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // get broadcast is used for the App widgets click events which needs to be displayed
            //inside app widget itself
            // getActivity() inplace of getBroadcast() is used to show any activity for an User
            //Setting a listener for a button
            views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent);

            // Setting onClick listener for image view also
            Intent intent1 = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);
            views.setOnClickPendingIntent(R.id.appwidget_image, pendingIntent1);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

