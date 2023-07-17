package com.example.matteomobile.config;

import com.example.matteomobile.models.Mobile;
import com.example.matteomobile.repos.MobileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase
{
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDB(MobileRepository mobRepo)
    {
        Mobile iphone13 = Mobile.builder().brand("Apple").model("iphone 13")
                .isAvailable(true).technology("GSM / CDMA / HSPA / EVDO / LTE")._2gBands("1900 MHz")._3gBands("850 MHz")._4gBands("2300 MHz").build();
        Mobile iphone12 = Mobile.builder().brand("Apple").model("iphone 12")
                .isAvailable(true).technology("GSM / CDMA / HSPA / EVDO / LTE / 5G")._2gBands("1900 MHz")._3gBands("850 MHz")._4gBands("2300 MHz").build();
        Mobile iphone11 = Mobile.builder().brand("Apple").model("iphone 11")
                .isAvailable(true).technology("GSM / CDMA / HSPA / EVDO / LTE / 5G")._2gBands("1900 MHz")._3gBands("850 MHz")._4gBands("2300").build();
        Mobile iphoneX = Mobile.builder().brand("Apple").model("iphone X")
                .isAvailable(true).technology("GSM / HSPA / LTE")._2gBands("1900 MHz")._3gBands("850 MHz")._4gBands("2300 MHz").build();
        Mobile nokia3310 = Mobile.builder().brand("Nokia").model("3310")
                .isAvailable(true).technology("GSM")._2gBands("GSM 900 / 1800")._3gBands("")._4gBands("").build();


        return args -> {
            log.info("Preloading " + mobRepo.save(
                    Mobile.builder().brand("Samsung").model("Galaxy S9")
                            .isAvailable(true).technology("GSM / CDMA / HSPA / EVDO / LTE")._2gBands("1900 MHz")._3gBands("850 MHz")._4gBands("2300 MHz").build()));
            log.info("Preloading " + mobRepo.save(
                    Mobile.builder().brand("Samsung").model("Galaxy S8")
                            .isAvailable(true).technology("GSM / CDMA / HSPA / EVDO / LTE")._2gBands("1900 MHz")._3gBands("850 MHz")._4gBands("2300 MHz").build()));
            log.info("Preloading " + mobRepo.save(
                    Mobile.builder().brand("Samsung").model("Galaxy S8")
                            .isAvailable(true).technology("GSM / CDMA / HSPA / EVDO / LTE")._2gBands("1900 MHz")._3gBands("850 MHz")._4gBands("2300 MHz").build()));
            log.info("Preloading " + mobRepo.save(
                    Mobile.builder().brand("Oneplus").model("9")
                            .isAvailable(true).technology("GSM / CDMA / HSPA / EVDO / LTE")._2gBands("1900 MHz")._3gBands("850 MHz")._4gBands("2300 MHz").build()));
            log.info("Preloading " + mobRepo.save(
                    Mobile.builder().brand("Motorola").model("Nexus 6")
                            .isAvailable(true).technology("GSM / CDMA / HSPA / EVDO / LTE")._2gBands("1900 MHz")._3gBands("850 MHz")._4gBands("2300 MHz").build()));
            log.info("Preloading " + mobRepo.save(iphone13));
            log.info("Preloading " + mobRepo.save(iphone12));
            log.info("Preloading " + mobRepo.save(iphone11));
            log.info("Preloading " + mobRepo.save(iphoneX));
            log.info("Preloading " + mobRepo.save(nokia3310));

        };
    }

}
