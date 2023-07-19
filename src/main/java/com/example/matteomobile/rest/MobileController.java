package com.example.matteomobile.rest;

import com.example.matteomobile.models.*;
import com.example.matteomobile.services.BTService;
import com.example.matteomobile.services.FonoQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class MobileController
{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String ORDERS = "/orders";

    private final String MOBILES = "/mobiles";
    @Autowired
    BTService service;

    @Autowired
    FonoQueryService fonoService;

    @PostMapping(ORDERS)
    Mono<OrderResponse> bookMobile(@RequestBody OrderRequest request) {

        long start1 = System.currentTimeMillis();

        //Reactive approach
        return service.bookDevice(request)
                        .zipWith(fonoService.postForDeviceInfo(request.getModel(), request.getBrand()))
                                .map(tuple2 -> {
                                    OrderResponse response = tuple2.getT1();
                                    response.setTechnology(tuple2.getT2().getTechnology());
                                    response.set_2g_bands(tuple2.getT2().get_2g_bands());
                                    response.set_3g_bands(tuple2.getT2().get_3g_bands());
                                    response.set_4g_bands(tuple2.getT2().get_4g_bands());

                                    long end1 = System.currentTimeMillis();
                                    logger.info("Book Mobile - Elapsed Time in milli seconds: "+ (end1-start1));

                                    return response;
                                });
    }

    @GetMapping(MOBILES + "/{id}")
    Mobile one(@PathVariable Long id) { return service.getMobile(id); }

    @PutMapping(ORDERS + "/{id}")
    ReturnItemResponse returnMobile(@RequestBody ReturnItemRequest request, @PathVariable Long id)
    {
        return service.returnDevice(id, request.getItemId());
    }

    @GetMapping(MOBILES)
    List<Mobile> all() { return service.allMobiles(); }

    @PostMapping(MOBILES)
    Mobile createMobile(@RequestBody Mobile newMobile)
    {
        return service.createNobile(newMobile);
    }

    @PutMapping(MOBILES +"/{id}")
    Mobile replaceMobile(@RequestBody Mobile newMobile, @PathVariable Long id)
    {
        return service.replaceMobile(newMobile, id);
    }

    @DeleteMapping(MOBILES +"/{id}")
    void deleteMobile(@PathVariable Long id) { service.deleteMobile(id);}

}
