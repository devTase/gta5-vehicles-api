package dev.tase.gta5vehicles.scrap;

import dev.tase.gta5vehicles.scrap.control.ScraperService;
import org.junit.jupiter.api.Test;

public class ScraperServiceTest {

    @Test
    public void testScrapPage() {
        ScraperService scraperService = new ScraperService();
        scraperService.startScrapping();
    }
}
