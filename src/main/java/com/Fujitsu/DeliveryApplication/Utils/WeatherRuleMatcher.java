package com.Fujitsu.DeliveryApplication.Utils;

import com.Fujitsu.DeliveryApplication.Enums.ExtraFeeRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherRuleMatcher {
    private static final Map<String, ExtraFeeRule> WEATHER_KEYWORDS = new HashMap<>();

    static {
        WEATHER_KEYWORDS.put("snow", ExtraFeeRule.WEATHER_SNOW);
        WEATHER_KEYWORDS.put("sleet", ExtraFeeRule.WEATHER_SLEET);
        WEATHER_KEYWORDS.put("rain", ExtraFeeRule.WEATHER_RAIN);
        WEATHER_KEYWORDS.put("glaze", ExtraFeeRule.WEATHER_GLAZE);
        WEATHER_KEYWORDS.put("hail", ExtraFeeRule.WEATHER_HAIL);
        WEATHER_KEYWORDS.put("thunder", ExtraFeeRule.WEATHER_THUNDER);
    }

    public static List<ExtraFeeRule> matchWeatherRule(String weather) {
        List<ExtraFeeRule> rules = new ArrayList<>();

        for (Map.Entry<String, ExtraFeeRule> entry : WEATHER_KEYWORDS.entrySet()) {
            if (weather.contains(entry.getKey().toLowerCase())) {
                rules.add(entry.getValue());
            }
        }

        return rules;
    }
}
