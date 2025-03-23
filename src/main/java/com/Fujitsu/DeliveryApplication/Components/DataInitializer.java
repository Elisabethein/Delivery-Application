package com.Fujitsu.DeliveryApplication.Components;

import com.Fujitsu.DeliveryApplication.Entities.*;
import com.Fujitsu.DeliveryApplication.Enums.City;
import com.Fujitsu.DeliveryApplication.Enums.ExtraFeeRuleName;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import com.Fujitsu.DeliveryApplication.Repositories.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final StationRepository stationRepository;
    private final WeatherRepository weatherRepository;
    private final VehicleRepository vehicleRepository;
    private final BaseFeeRepository baseFeeRepository;
    private final ExtraFeeRepository extraFeeRepository;
    private final ExtraFeeVehicleMappingRepository extraFeeVehicleMappingRepository;
    private final RestTemplate restTemplate;
    private static final String WEATHER_API_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private final Map<String, Station> stationMap = new HashMap<>();

    /**
     * Initializes the database with default data if it is empty
     * Weather data is always added
     */
    @Override
    public void run(ApplicationArguments args) {
        if (stationRepository.count() == 0) {
            initializeStations();
        }
        if (vehicleRepository.count() == 0) {
            initializeVehicles();
        }
        if (baseFeeRepository.count() == 0) {
            initializeBaseFees();
        }
        if (extraFeeRepository.count() == 0) {
            initializeExtraFees();
        }
        if (extraFeeVehicleMappingRepository.count() == 0) {
            initializeExtraFeeVehicleMappings();
        }
        loadStationsIntoMemory();
        initializeWeather();
    }

    private void initializeExtraFeeVehicleMappings() {
        List<ExtraFee> extraFees = extraFeeRepository.findAll();
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<ExtraFeeVehicleMapping> mappings = new ArrayList<>();

        for (ExtraFee extraFee : extraFees) {
            for (Vehicle vehicle : vehicles) {
                boolean shouldMap = switch (extraFee.getRuleName()) {
                    case TEMPERATURE_LOW, TEMPERATURE_BETWEEN, WEATHER_SNOW, WEATHER_SLEET, WEATHER_RAIN, WEATHER_GLAZE, WEATHER_HAIL, WEATHER_THUNDER ->
                            vehicle.getVehicleType() == VehicleType.Bike || vehicle.getVehicleType() == VehicleType.Scooter;
                    case WIND_BETWEEN, WIND_HIGH ->
                            vehicle.getVehicleType() == VehicleType.Bike;
                };

                if (shouldMap) {
                    mappings.add(ExtraFeeVehicleMapping.builder().extraFee(extraFee).vehicle(vehicle).build());
                }
            }
        }

        extraFeeVehicleMappingRepository.saveAll(mappings);
    }


    private void initializeVehicles() {
        List<VehicleType> vehicleTypes = List.of(VehicleType.Car, VehicleType.Scooter, VehicleType.Bike);
        vehicleTypes.forEach(vehicleType -> vehicleRepository.save(Vehicle.builder().vehicleType(vehicleType).build()));
    }

    private void initializeExtraFees() {
        String errorMessage = "Usage of selected vehicle type is forbidden";

        ExtraFee[] extraFees = {
                ExtraFee.builder().ruleName(ExtraFeeRuleName.TEMPERATURE_LOW).minValue(-999.0).maxValue(-10.0).fee(1.0).build(),
                ExtraFee.builder().ruleName(ExtraFeeRuleName.TEMPERATURE_BETWEEN).minValue(-10.0).maxValue(0.0).fee(0.5).build(),
                ExtraFee.builder().ruleName(ExtraFeeRuleName.WIND_BETWEEN).minValue(10.0).maxValue(20.0).fee(0.5).build(),
                ExtraFee.builder().ruleName(ExtraFeeRuleName.WIND_HIGH).minValue(20.0).maxValue(999.0).fee(0.0).errorMessage(errorMessage).build(),
                ExtraFee.builder().ruleName(ExtraFeeRuleName.WEATHER_SNOW).minValue(-999.0).maxValue(999.0).fee(1.0).build(),
                ExtraFee.builder().ruleName(ExtraFeeRuleName.WEATHER_SLEET).minValue(-999.0).maxValue(999.0).fee(1.0).build(),
                ExtraFee.builder().ruleName(ExtraFeeRuleName.WEATHER_RAIN).minValue(-999.0).maxValue(999.0).fee(0.5).build(),
                ExtraFee.builder().ruleName(ExtraFeeRuleName.WEATHER_GLAZE).minValue(-999.0).maxValue(999.0).fee(0.0).errorMessage(errorMessage).build(),
                ExtraFee.builder().ruleName(ExtraFeeRuleName.WEATHER_HAIL).minValue(-999.0).maxValue(999.0).fee(0.0).errorMessage(errorMessage).build(),
                ExtraFee.builder().ruleName(ExtraFeeRuleName.WEATHER_THUNDER).minValue(-999.0).maxValue(999.0).fee(0.0).errorMessage(errorMessage).build()
        };

        extraFeeRepository.saveAll(List.of(extraFees));
    }

    private void initializeBaseFees() {
        List<BaseFee> baseFees = new ArrayList<>();

        Map<City, Map<VehicleType, Double>> baseFeeMap = Map.of(
                City.Tallinn, Map.of(
                        VehicleType.Car, 4.0,
                        VehicleType.Scooter, 3.5,
                        VehicleType.Bike, 3.0
                ),
                City.Tartu, Map.of(
                        VehicleType.Car, 3.5,
                        VehicleType.Scooter, 3.0,
                        VehicleType.Bike, 2.5
                ),
                City.Pärnu, Map.of(
                        VehicleType.Car, 3.0,
                        VehicleType.Scooter, 2.5,
                        VehicleType.Bike, 2.0
                )
        );

        baseFeeMap.forEach((city, vehicleFees) ->
                vehicleFees.forEach((vehicle, fee) ->
                        baseFees.add(BaseFee.builder().city(city).vehicleType(vehicle).fee(fee).build())
                )
        );

        baseFeeRepository.saveAll(baseFees);
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

        Weather weather = Weather.builder()
                .station(station)
                .WMO(wmocode)
                .temperature(airTemperature)
                .windSpeed(windSpeed)
                .weatherPhenomenon(phenomenon)
                .observationTime(new Timestamp(System.currentTimeMillis()))
                .build();
        weatherRepository.save(weather);
    }

    private void initializeStations() {
        CityStationMapper.getStationMappings().forEach((stationName, cityName) -> {
            stationRepository.save(Station.builder().stationName(stationName).cityName(cityName).build());
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
