package com.example.matteomobile.services;

import com.example.matteomobile.exceptions.MobileNotFoundException;
import com.example.matteomobile.exceptions.OrderNotFoundException;
import com.example.matteomobile.models.*;
import com.example.matteomobile.repos.CustomerRepository;
import com.example.matteomobile.repos.MobileRepository;
import com.example.matteomobile.repos.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BTService {
    private final OrderRepository ordRepo;
    private final MobileRepository mobRepo;
    private final CustomerRepository custRepo;



    BTService(OrderRepository ordRepo, MobileRepository mobRepo, CustomerRepository custRepo)
    {
        this.ordRepo = ordRepo;
        this.mobRepo = mobRepo;
        this.custRepo = custRepo;
    }

    public List<Mobile> allMobiles() { return mobRepo.findAll(); }
    public Mobile createNobile(Mobile newMobile) { return mobRepo.save(newMobile); }

    public Mobile getMobile(Long id)
    { return mobRepo.findById(id)
            .orElseThrow(() -> new MobileNotFoundException(id));
    }

    public Mobile replaceMobile(Mobile newMobile, Long id) {
        return mobRepo.findById(id)
                .map(mobile -> {
                    mobile.setAvailable(newMobile.isAvailable());
                    return mobRepo.save(mobile);
                })
                .orElseGet(() -> {
                    newMobile.setId(id);
                    return mobRepo.save(newMobile);
                });
    }
    public void deleteMobile(Long id) { mobRepo.deleteById(id);}

    public boolean checkAvailability(String brand, String model)
    {
        return mobRepo.findAll().stream()
                .filter(mobile -> mobile.getBrand().equals(brand) &&
                        mobile.getModel().equals(model) &&
                        mobile.isAvailable())
                .findAny().isPresent();
    }
    public OrderResponse bookDevice(OrderRequest request)
    {

        //Find any available and return
        Optional<Mobile> anyMobOptional = mobRepo.findAll().stream()
                .filter(mobile -> mobile.getBrand().equals(request.getBrand()) &&
                            mobile.getModel().equals(request.getModel()) &&
                            mobile.isAvailable())
                .findAny();

        Mobile anyMobExisting;
        if(anyMobOptional.isPresent()) {
            anyMobExisting = anyMobOptional.get();
            Customer customer = Customer.builder().name(request.getCustomerName()).surname(request.getCustomerSurname()).build();
            custRepo.save(customer);

            LocalDateTime bookingDate = LocalDateTime.now();

            Set<Mobile> mobs = new HashSet<>();
            anyMobExisting.setAvailable(false);
            mobRepo.save(anyMobExisting);
            mobs.add(anyMobExisting);

            Order order = Order.builder().bookingDate(bookingDate).customer(customer)
                    .mobiles(mobs).build();
            ordRepo.save(order);

            return OrderResponse.builder().brand(anyMobExisting.getBrand())
                    .model(anyMobExisting.getModel()).available(!anyMobExisting.isAvailable())
                    .bookingDate(bookingDate).customer(customer).build();
        } else {
            anyMobExisting = mobRepo.findAll().stream()
                    .filter(mobile -> mobile.getBrand().equals(request.getBrand()) &&
                            mobile.getModel().equals(request.getModel()) &&
                            !mobile.isAvailable())
                    .findAny().get();

            Optional<Order> optOrder = ordRepo.findAll().stream()
                    .filter( o -> o.getMobiles().stream()
                            .anyMatch(m -> m.getId() == anyMobExisting.getId())).findAny();
            Order order=optOrder.get();

            return OrderResponse.builder().brand(anyMobExisting.getBrand())
                    .model(anyMobExisting.getModel()).available(!anyMobExisting.isAvailable())
                    .bookingDate(order.getBookingDate()).customer(order.getCustomer())
                    .build();
        }

    }

    public ReturnItemResponse returnDevice(Long orderId, Long mobileId)
    {
        ReturnItemResponse response;
        Optional<Order> optOrder = ordRepo.findById(orderId);

        Order order = optOrder.orElseThrow(() -> new OrderNotFoundException(orderId));

        Optional<Mobile> optMobile = order.getMobiles().stream()
                    .filter(m -> m.getId()==mobileId)
                    .map(mob -> {
                        mob.setAvailable(true);
                        return mob;
                    })
                    .findAny();

        Mobile mobile = optMobile.orElseThrow(() -> new MobileNotFoundException(mobileId));

        mobRepo.save(mobile);
        order.setReturnDate(LocalDateTime.now());
        ordRepo.save(order);

        response = ReturnItemResponse.builder()
                .returnDate(order.getReturnDate())
                .itemId(mobile.getId())
                .brand(mobile.getBrand())
                .model(mobile.getModel())
                .available(mobile.isAvailable()).build();
        return response;
    }
}
