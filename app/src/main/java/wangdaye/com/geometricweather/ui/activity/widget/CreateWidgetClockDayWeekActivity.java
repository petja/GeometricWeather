package wangdaye.com.geometricweather.ui.activity.widget;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import wangdaye.com.geometricweather.basic.GeoWidgetConfigActivity;
import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.data.entity.model.weather.Weather;
import wangdaye.com.geometricweather.service.PollingService;
import wangdaye.com.geometricweather.utils.manager.TimeManager;
import wangdaye.com.geometricweather.utils.helpter.ServiceHelper;
import wangdaye.com.geometricweather.utils.remoteView.WidgetClockDayWeekUtils;

/**
 * Create widget clock day week activity.
 * */

public class CreateWidgetClockDayWeekActivity extends GeoWidgetConfigActivity
        implements View.OnClickListener {

    private ImageView widgetCard;
    private ImageView widgetIcon;
    private TextClock widgetClock;
    private TextClock widgetTitle;
    private TextView widgetSubtitle;
    private TextView[] widgetWeeks;
    private ImageView[] widgetIcons;
    private TextView[] widgetTemps;

    private CoordinatorLayout container;

    private Switch showCardSwitch;
    private Switch blackTextSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_widget_clock_day_week);
    }

    @Override
    public View getSnackbarContainer() {
        return container;
    }

    @SuppressLint("InflateParams")
    @Override
    public void initWidget() {
        View widgetView = LayoutInflater.from(this).inflate(R.layout.widget_clock_day_week, null);
        ((ViewGroup) findViewById(R.id.activity_create_widget_clock_day_week_widgetContainer)).addView(widgetView);

        this.widgetCard = widgetView.findViewById(R.id.widget_clock_day_week_card);
        widgetCard.setVisibility(View.GONE);

        this.widgetIcon = widgetView.findViewById(R.id.widget_clock_day_week_icon);
        this.widgetClock = widgetView.findViewById(R.id.widget_clock_day_week_clock);
        this.widgetTitle = widgetView.findViewById(R.id.widget_clock_day_week_title);
        this.widgetSubtitle = widgetView.findViewById(R.id.widget_clock_day_week_subtitle);

        this.widgetWeeks = new TextView[] {
                widgetView.findViewById(R.id.widget_clock_day_week_week_1),
                widgetView.findViewById(R.id.widget_clock_day_week_week_2),
                widgetView.findViewById(R.id.widget_clock_day_week_week_3),
                widgetView.findViewById(R.id.widget_clock_day_week_week_4),
                widgetView.findViewById(R.id.widget_clock_day_week_week_5)};
        this.widgetIcons = new ImageView[] {
                widgetView.findViewById(R.id.widget_clock_day_week_icon_1),
                widgetView.findViewById(R.id.widget_clock_day_week_icon_2),
                widgetView.findViewById(R.id.widget_clock_day_week_icon_3),
                widgetView.findViewById(R.id.widget_clock_day_week_icon_4),
                widgetView.findViewById(R.id.widget_clock_day_week_icon_5)};
        this.widgetTemps = new TextView[] {
                widgetView.findViewById(R.id.widget_clock_day_week_temp_1),
                widgetView.findViewById(R.id.widget_clock_day_week_temp_2),
                widgetView.findViewById(R.id.widget_clock_day_week_temp_3),
                widgetView.findViewById(R.id.widget_clock_day_week_temp_4),
                widgetView.findViewById(R.id.widget_clock_day_week_temp_5)};

        ImageView wallpaper = findViewById(R.id.activity_create_widget_clock_day_week_wall);
        wallpaper.setImageDrawable(WallpaperManager.getInstance(this).getDrawable());

        this.container = findViewById(R.id.activity_create_widget_clock_day_week_container);

        this.showCardSwitch = findViewById(R.id.activity_create_widget_clock_day_week_showCardSwitch);
        showCardSwitch.setOnCheckedChangeListener(new ShowCardSwitchCheckListener());

        this.blackTextSwitch = findViewById(R.id.activity_create_widget_clock_day_week_blackTextSwitch);
        blackTextSwitch.setOnCheckedChangeListener(new BlackTextSwitchCheckListener());

        Button doneButton = findViewById(R.id.activity_create_widget_clock_day_week_doneButton);
        doneButton.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshWidgetView(Weather weather) {
        if (weather == null) {
            return;
        }

        String iconStyle = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(
                        getString(R.string.key_widget_icon_style),
                        "material");
        boolean dayTime = TimeManager.getInstance(this).getDayTime(this, weather, false).isDayTime();

        int imageId = WidgetClockDayWeekUtils.getWeatherIconId(
                weather,
                TimeManager.getInstance(this).getDayTime(this, weather, false).isDayTime(),
                iconStyle,
                blackTextSwitch.isChecked());
        Glide.with(this)
                .load(imageId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(widgetIcon);
        widgetSubtitle.setText(WidgetClockDayWeekUtils.getSubtitleText(weather, isFahrenheit()));

        for (int i = 0; i < 5; i ++) {
            widgetWeeks[i].setText(WidgetClockDayWeekUtils.getWeek(this, weather, i));
            widgetTemps[i].setText(WidgetClockDayWeekUtils.getTemp(weather, isFahrenheit(), i));
            Glide.with(this)
                    .load(
                            WidgetClockDayWeekUtils.getIconId(
                                    weather, dayTime, iconStyle, blackTextSwitch.isChecked(), i))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(widgetIcons[i]);
        }

        if (showCardSwitch.isChecked() || blackTextSwitch.isChecked()) {
            if (showCardSwitch.isChecked()) {
                widgetCard.setVisibility(View.VISIBLE);
            } else {
                widgetCard.setVisibility(View.GONE);
            }
            widgetClock.setTextColor(ContextCompat.getColor(this, R.color.colorTextDark));
            widgetTitle.setTextColor(ContextCompat.getColor(this, R.color.colorTextDark));
            widgetSubtitle.setTextColor(ContextCompat.getColor(this, R.color.colorTextDark));
            for (int i = 0; i < 5; i ++) {
                widgetWeeks[i].setTextColor(ContextCompat.getColor(this, R.color.colorTextDark));
                widgetTemps[i].setTextColor(ContextCompat.getColor(this, R.color.colorTextDark));
            }
        } else {
            widgetCard.setVisibility(View.GONE);
            widgetClock.setTextColor(ContextCompat.getColor(this, R.color.colorTextLight));
            widgetTitle.setTextColor(ContextCompat.getColor(this, R.color.colorTextLight));
            widgetSubtitle.setTextColor(ContextCompat.getColor(this, R.color.colorTextLight));
            for (int i = 0; i < 5; i ++) {
                widgetWeeks[i].setTextColor(ContextCompat.getColor(this, R.color.colorTextLight));
                widgetTemps[i].setTextColor(ContextCompat.getColor(this, R.color.colorTextLight));
            }
        }
    }

    // interface.

    // on click listener.

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_create_widget_clock_day_week_doneButton:
                SharedPreferences.Editor editor = getSharedPreferences(
                        getString(R.string.sp_widget_clock_day_week_setting),
                        MODE_PRIVATE)
                        .edit();
                editor.putBoolean(getString(R.string.key_show_card), showCardSwitch.isChecked());
                editor.putBoolean(getString(R.string.key_black_text), blackTextSwitch.isChecked());
                editor.apply();

                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                int appWidgetId = 0;
                if (extras != null) {
                    appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);
                }

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);

                ServiceHelper.startupService(this, PollingService.FORCE_REFRESH_TYPE_NORMAL_VIEW, false);
                finish();
                break;
        }
    }

    // on check changed listener(switch).

    private class ShowCardSwitchCheckListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            refreshWidgetView(getLocationNow().weather);
        }
    }

    private class BlackTextSwitchCheckListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            refreshWidgetView(getLocationNow().weather);
        }
    }
}