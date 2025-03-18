package com.Fujitsu.DeliveryApplication.Components;

import com.Fujitsu.DeliveryApplication.Entities.Station;
import com.Fujitsu.DeliveryApplication.Entities.Weather;
import com.Fujitsu.DeliveryApplication.Repositories.StationRepository;
import com.Fujitsu.DeliveryApplication.Repositories.WeatherRepository;
import com.Fujitsu.DeliveryApplication.Utils.CityStationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final StationRepository stationRepository;
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;
    private static final String WEATHER_API_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private final Map<String, Station> stationMap = new HashMap<>();

    @Override
    public void run(ApplicationArguments args) {
        if (stationRepository.count() == 0) {
            initializeStations();
        }
        loadStationsIntoMemory();
        initializeWeather();
    }

    @Scheduled(cron = "${scheduling.weather.cron}")
    public void initializeWeather() {
        try {
            String xmlData = restTemplate.getForObject(WEATHER_API_URL, String.class);
            Document doc = parseXml(xmlData);
            if (doc == null) return;

            NodeList stationList = doc.getDocumentElement().getElementsByTagName("station");
            for (int i = 0; i < stationList.getLength(); i++) {
                processStationWeather((Element) stationList.item(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processStationWeather(Element stationElement) {
        String stationName = getElementText(stationElement, "name");

        Station station = stationMap.get(stationName);
        if (station == null) {
            return;
        }

        String wmocode = getElementText(stationElement, "wmocode");
        double airTemperature = Double.parseDouble(getElementText(stationElement, "airtemperature"));
        double windSpeed = Double.parseDouble(getElementText(stationElement, "windspeed"));
        String phenomenon = getElementText(stationElement, "phenomenon");

        Weather weather = new Weather(station, wmocode, airTemperature, windSpeed, phenomenon, new Timestamp(System.currentTimeMillis()));
        weatherRepository.save(weather);
    }

    private void initializeStations() {
        CityStationMapper.getStationMappings().forEach((stationName, cityName) -> {
            stationRepository.save(new Station(stationName, cityName));
        });
    }

    private void loadStationsIntoMemory() {
        stationRepository.findAll().forEach(station -> stationMap.put(station.getStationName(), station));
    }

    private Document parseXml(String xmlData) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlData)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getElementText(Element element, String tag) {
        NodeList nodeList = element.getElementsByTagName(tag);
        return (nodeList.getLength() > 0) ? nodeList.item(0).getTextContent() : "";
    }
}
