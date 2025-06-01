package dev.tase.gta5vehicles.control;

import dev.tase.gta5vehicles.scrap.ScraperService;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
@Startup
public class VehicleChronService {

    @Inject
    ScraperService scraperService;

    @Inject
    VehicleService vehicleService;

    @Scheduled(every = "PT5h") // corre uma vez, 10s após o arranque
    void runOnStartupWithDelay() {
        System.out.println("A iniciar scrapping (com delay)...");
        executeTask();
    }

    private void executeTask() {
        Logger log = Logger.getLogger(VehicleChronService.class);
        log.info("Starting cron service");
        scraperService.startScrapping().forEach(vehicleService::createVehicle);
    }
}
