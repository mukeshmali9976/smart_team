package com.techmali.smartteam.horizontalcalendar;

import java.util.Date;
/**
 * Created by Mali on 6/28/2017.
 */

public abstract class HorizontalCalendarListener {


    public abstract void onDateSelected(Date date, int position);

    public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy){}

    public boolean onDateLongClicked(Date date, int position){
        return false;
    }
}
