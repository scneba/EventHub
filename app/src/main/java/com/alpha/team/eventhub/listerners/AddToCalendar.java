package com.alpha.team.eventhub.listerners;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import com.alpha.team.eventhub.entities.Event;
import com.alpha.team.eventhub.exceptions.EventHubException;
import com.alpha.team.eventhub.utils.GeneralHelpers;

import java.util.Calendar;
import java.util.Date;

import static android.provider.CalendarContract.Events;

/**
 * Created by clasence on 01,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 * <p>
 * Class handles addition of event to user's Calendar
 */
public class AddToCalendar implements Strategy {
    private Date dateFrom = null;
    private Date dateTo = null;
    private Event event;
    private Activity context;

    @Override
    public void execute(Activity context, View v, Event event) {
        this.event = event;
        this.context = context;

        setUpDates();

    }

    /**
     * Convert the String dates to Date format
     */
    private void setUpDates() {

        try {
            dateFrom = GeneralHelpers.formatDate(event.getDateFrom());

            dateTo = GeneralHelpers.formatDate(event.getDateTo());

            Log.e("datefrom", dateFrom.toString());
            Log.e("dateTo", dateTo.toString());
            if (dateFrom != null && dateTo != null) {

                buildCalendarIntent(context);

            }

        } catch (EventHubException e) {
            e.printStackTrace();
        }
    }


    /**
     * Build the intent for calendar setup
     * @param context
     */
    private void buildCalendarIntent(final Activity context) {
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(dateFrom);
        long startMillis = calendarFrom.getTimeInMillis();

        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(dateTo);
        long endMillis = calendarTo.getTimeInMillis();

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendarFrom.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendarTo.getTimeInMillis())
                .putExtra(Events.TITLE, event.getEventName())
                .putExtra(Events.DESCRIPTION, event.getDescription())
                .putExtra(Events.EVENT_LOCATION, event.getAddress())
                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
                .putExtra(Events.HAS_ALARM, 1)
                .putExtra(Events.RRULE, "FREQ=DAILY");

        context.startActivity(intent);
    }


}
