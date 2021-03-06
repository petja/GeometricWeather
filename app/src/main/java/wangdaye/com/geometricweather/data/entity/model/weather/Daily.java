package wangdaye.com.geometricweather.data.entity.model.weather;

import android.content.Context;

import wangdaye.com.geometricweather.data.entity.result.NewDailyResult;
import wangdaye.com.geometricweather.data.entity.table.weather.DailyEntity;
import wangdaye.com.geometricweather.utils.helpter.WeatherHelper;

/**
 * Daily.
 * */

public class Daily {

    public String date;
    public String week;
    public String[] weathers;
    public String[] weatherKinds;
    public int[] temps;
    public String[] windDirs;
    public String[] windSpeeds;
    public String[] windLevels;
    public int[] windDegrees;
    public String[] astros;
    public int[] precipitations;

    public Daily() {}

/*
    Daily buildDaily(FWResult.Weathers weather) {
        date = weather.date;
        week = weather.week.replace("星期", "周");
        weathers = new String[] {
                weather.weather, weather.weather};
        weatherKinds = new String[] {
                WeatherHelper.getFWeatherKind(weathers[0]),
                WeatherHelper.getFWeatherKind(weathers[1])};
        temps = new int[] {
                Integer.parseInt(weather.temp_day_c),
                Integer.parseInt(weather.temp_night_c)};
        windDirs = new String[] {weather.wd, weather.wd};
        windSpeeds = new String[] {"", ""};
        windLevels = new String[] {weather.ws, weather.ws};
        astros = new String[] {
                weather.sun_rise_time,
                weather.sun_down_time};
        precipitations = new int[] {-1, -1};
        return this;
    }
    
    Daily buildDaily(HefengResult.HeWeather.DailyForecast forecast) {
        date = forecast.date;
        week = HefengWeather.getWeek(forecast.date, true);
        weathers = new String[] {
                forecast.cond.txt_d,
                forecast.cond.txt_n};
        weatherKinds = new String[] {
                WeatherHelper.getHefengWeatherKind(forecast.cond.code_d),
                WeatherHelper.getHefengWeatherKind(forecast.cond.code_n)};
        temps = new int[] {
                Integer.parseInt(forecast.tmp.max),
                Integer.parseInt(forecast.tmp.min)};
        windDirs = new String[] {forecast.wind.dir, forecast.wind.dir};
        windSpeeds = new String[] {forecast.wind.spd + "km/h", forecast.wind.spd + "km/h"};
        windLevels = new String[] {forecast.wind.sc, forecast.wind.sc};
        astros = new String[] {
                forecast.astro.sr,
                forecast.astro.ss};
        precipitations = new int[] {-1, -1};
        return this;
    }
*/

    public Daily buildDaily(Context c, NewDailyResult.DailyForecasts forecast) {
        date = forecast.Date.split("T")[0];
        week = WeatherHelper.getWeek(c, date);
        weathers = new String[] {
                forecast.Day.IconPhrase,
                forecast.Night.IconPhrase};
        weatherKinds = new String[] {
                WeatherHelper.getNewWeatherKind(forecast.Day.Icon),
                WeatherHelper.getNewWeatherKind(forecast.Night.Icon)};
        temps = new int[] {
                (int) forecast.Temperature.Maximum.Value,
                (int) forecast.Temperature.Minimum.Value};
        windDirs = new String[] {
                forecast.Day.Wind.Direction.Localized,
                forecast.Night.Wind.Direction.Localized};
        windSpeeds = new String[] {forecast.Day.Wind.Speed.Value + "km/h", forecast.Night.Wind.Speed.Value + "km/h"};
        windLevels = new String[] {
                WeatherHelper.getWindLevel(c, forecast.Day.Wind.Speed.Value),
                WeatherHelper.getWindLevel(c, forecast.Night.Wind.Speed.Value)};
        windDegrees = new int[] {
                forecast.Day.Wind.Direction.Degrees,
                forecast.Night.Wind.Direction.Degrees};
        astros = new String[] {
                forecast.Sun.Rise.split("T")[1].split(":")[0]
                        + ":" + forecast.Sun.Rise.split("T")[1].split(":")[1],
                forecast.Sun.Set.split("T")[1].split(":")[0]
                        + ":" + forecast.Sun.Set.split("T")[1].split(":")[1]};
        precipitations = new int[] {forecast.Day.PrecipitationProbability, forecast.Night.PrecipitationProbability};
        return this;
    }

    Daily buildDaily(DailyEntity entity) {
        date = entity.date;
        week = entity.week;
        weathers = new String[] {
                entity.daytimeWeather,
                entity.nighttimeWeather};
        weatherKinds = new String[] {
                entity.daytimeWeatherKind,
                entity.nighttimeWeatherKind};
        temps = new int[] {
                entity.maxiTemp,
                entity.miniTemp};
        windDirs = new String[] {entity.daytimeWindDir, entity.nighttimeWindDir};
        windSpeeds = new String[] {entity.daytimeWindSpeed, entity.nighttimeWindSpeed};
        windLevels = new String[] {entity.daytimeWindLevel, entity.nighttimeWindLevel};
        windDegrees = new int[] {entity.daytimeWindDegree, entity.nighttimeWindDegree};
        astros = new String[] {
                entity.sunrise,
                entity.sunset};
        precipitations = new int[] {entity.daytimePrecipitations, entity.nighttimePrecipitations};
        return this;
    }
}
