package dev.tase.gta5vehicles.scrap;

import dev.tase.gta5vehicles.consumption.FuelConsumptionEstimator;
import dev.tase.gta5vehicles.consumption.VehicleStats;
import dev.tase.gta5vehicles.entities.DriveType;
import dev.tase.gta5vehicles.entities.FuelType;
import dev.tase.gta5vehicles.entities.Vehicle;
import dev.tase.gta5vehicles.entities.VehicleCategory;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@ApplicationScoped
public class ScraperService {
    private static final Logger LOGGER = Logger.getLogger(ScraperService.class);
    private static final String STATIC_URL = "https://www.gta-db.com/gta-vehicles";

    private List<Vehicle> vehicles;

    public ScraperService() {
        this.vehicles = new ArrayList<>();
    }

    public Collection<Vehicle> startScrapping() {
        final Document doc = getDocument(STATIC_URL);
        final String pagination = "https://www.gta-db.com/gta-vehicles/?page_number=";

        String pageText = doc.select("div.page").getFirst().text();
        String[] parts = pageText.split(" ");
        int vehiclesAmountToScrap = Integer.parseInt(parts[parts.length - 1].substring(0, 2));

        for(int i = 0; i <= vehiclesAmountToScrap; i++) {
            System.out.println("Watching page number " + i);
            scrapPage(pagination + i);
        }

        return this.vehicles;
    }

    public void scrapPage(String navigationPage) {
        final Document doc = getDocument(navigationPage);
        if(doc == null) return;

        Elements rows = doc.select("div.spartadb_table_wrapper > table > tbody > tr");

        for (Element row : rows) {
            Elements cells = row.select("td");
            for (Element cell : cells) {
                Element linkElement = cell.selectFirst("a");
                if (linkElement != null) {
                    String relativeUrl = linkElement.attr("href");
                    String fullUrl = resolveFullUrl(STATIC_URL, relativeUrl);
                    scrapVehicleSinglePage(fullUrl);
                }
            }
        }
    }

    public void scrapVehicleSinglePage(String page) {
        Vehicle vehicle = new Vehicle();
        Document doc = getDocument(page);
        if (doc == null) return;

        // vehicle name
        Elements rows = doc.select("article.site-content > h1");
        vehicle.setName(rows.getFirst().text());

        System.out.println("Parsing page: " + page);
        System.out.println("Vehicle being parsed: " + vehicle.getName());
        // Get primary details
        Element vehiclePrimaryDetails;
        try {
            vehiclePrimaryDetails = doc.select("div.col-sm-12 >table>tbody").getFirst();
        } catch (NoSuchElementException e) {
            LOGGER.warn("No primary details found for vehicle: " + vehicle.getName() + " at " + page);
            return;
        }

        String vehicleClassType = ScraperUtils.getCleanText(vehiclePrimaryDetails, 0, 1);
        if (vehicleClassType != null) {
            vehicle.setCategory(VehicleCategory.from(vehicleClassType).name());
        }
        String manufc = ScraperUtils.getCleanText(vehiclePrimaryDetails, 1, 1);
        if (!ScraperUtils.isDouble(manufc)) {
            vehicle.setManufacturer(manufc);
        } else {
            vehicle.setManufacturer("Unknown");
        }

        // Get secondary details
        final Element vehicleSecondaryDetails = doc.select("div.table-rail>table>tbody").getFirst();
        final int defaultTextIndex = 1;
        vehicle.setTopSpeed(ScraperUtils.getDouble(vehicleSecondaryDetails, VehicleProperty.SPEED.getIndex(), defaultTextIndex));
        vehicle.setAcceleration(ScraperUtils.getDouble(vehicleSecondaryDetails, VehicleProperty.ACCELERATION.getIndex(), defaultTextIndex));
        vehicle.setBraking(ScraperUtils.getDouble(vehicleSecondaryDetails, VehicleProperty.BRAKING.getIndex(), defaultTextIndex));
        vehicle.setWeight(ScraperUtils.getDouble(vehicleSecondaryDetails, VehicleProperty.WEIGHT.getIndex(), defaultTextIndex));
        vehicle.setSeats(ScraperUtils.getInteger(vehicleSecondaryDetails, VehicleProperty.SEATS.getIndex(), defaultTextIndex));
        vehicle.setPrice(ScraperUtils.getBigDecimal(vehicleSecondaryDetails, VehicleProperty.BUY_PRICE.getIndex(), defaultTextIndex));
        vehicle.setDriveType(DriveType.from(vehicleSecondaryDetails.child(VehicleProperty.DRIVE_TYPE.getIndex()).child(1).text()).name());
        vehicle.setEngineHealth(1000.0);

        // calculate vehicle consumptions
        vehicle.setConsumptions(
                FuelConsumptionEstimator.calculateFuelConsumption(
                        new VehicleStats(vehicle.getCategory(), vehicle.getDriveType(), FuelType.GASOLINE.name(), vehicle.getEngineHealth(), vehicle.getWeight()),
                        false, 0.0
                )
        );
        addNewScrappedVehicle(vehicle);
    }
    public Document getDocument(String url) {
        int tries = 3;
        while (tries-- > 0) {
            try {
                return Jsoup.connect(url)
//                        .timeout(90_000)
                        .get();
            } catch (SocketTimeoutException e) {
                System.err.println("Timeout ao aceder a " + url + ". Tentativas restantes: " + tries);
                try {
                    Thread.sleep(2000);
                    if(tries == 1 ) return null;
                } catch (InterruptedException ignored) {}
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Falha ao obter documento após várias tentativas: " + url);
    }

    private String resolveFullUrl(String baseUrl, String relativeUrl) {
        try {
            return new java.net.URL(new java.net.URL(baseUrl), relativeUrl).toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL malformado: " + relativeUrl, e);
        }
    }

    private void addNewScrappedVehicle(Vehicle vehicle) {
        System.out.println("Adding new Vehicle");
        this.vehicles.add(vehicle);
    }
}
