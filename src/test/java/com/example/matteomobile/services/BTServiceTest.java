package com.example.matteomobile.services;

import com.example.matteomobile.models.Customer;
import com.example.matteomobile.models.OrderResponse;
import com.example.matteomobile.models.ReturnItemResponse;
import com.example.matteomobile.rest.MobileController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = MobileController.class)
@WithMockUser
public class BTServiceTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BTService btService;

    private Customer mockCustomer1 = Customer.builder()
            .id(1).name("Matteo").surname("Daniele").build();

    private DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    final String bookingDateStr = "2023-07-15 17:30:02";
    LocalDateTime bookingDate = LocalDateTime.parse(bookingDateStr, FORMATTER);


    @Test
    public void bookMobile() throws Exception
    {
        OrderResponse mockOrderResponse = OrderResponse.builder()
                .brand("Samsung")
                .model("Galaxy S8").available(true)
                .bookingDate(bookingDate).customer(mockCustomer1)
                ._2g_bands("1900 MHz")
                ._3g_bands("850 MHz")
                ._4g_bands("2300 MHz")
                .technology("GSM / CDMA / HSPA / EVDO / LTE").build();

        String exampleOrderRequest = "{\"customerName\":\"Matteo\",\"customerSurname\":\"Daniele\",\"brand\":\"Samsung\",\"model\":\"Galaxy S8\"}";

        bookingDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        // invoke service to respond back with mockOrderResponse
        Mockito.when(btService.bookDevice(Mockito.any())).thenReturn(mockOrderResponse);
        //Send OrderRequest as body to /orders
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/orders")
                .accept(MediaType.APPLICATION_JSON).content(exampleOrderRequest)
                .contentType(MediaType.APPLICATION_JSON);;

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = """
        {"deviceName":null,"brand":"Samsung","model":"Galaxy S8","available":true,"technology":"GSM / CDMA / HSPA / EVDO / LTE","bookingDate":"$bookDate","customer":{"id":1,"name":"Matteo","surname":"Daniele"},"_2g_bands":"1900 MHz","_3g_bands":"850 MHz","_4g_bands":"2300 MHz"}"""
                .replace("$bookDate", bookingDate.toString());

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        //assertEquals("http://localhost:8080/orders",
        //response.getHeader(HttpHeaders.LOCATION));
        String actual = response.getContentAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void returnMobile() throws Exception
    {
        final LocalDateTime returnDate = bookingDate.plusMonths(1);
        final Long itemId = 2L;
        final Long orderId = 1L;
        //Request
        String exampleReturnDeviceRequest = "{\"itemId\":"+ itemId +"}";
        //Response
        ReturnItemResponse mockResponse =
                ReturnItemResponse.builder()
                .returnDate(returnDate).itemId(itemId).brand("Samsung")
                        .model("Galaxy S8").available(true).build();
        // invoke service to respond back with mockOrderResponse
        Mockito.when(btService.returnDevice(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(mockResponse);
        //Send ReturnRequest as body to /orders
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/orders/{id}",orderId)
                .content(exampleReturnDeviceRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                //to check a given string in the response body
                .andExpect(jsonPath("$.returnDate".toString()).value(returnDate.toString()))
                .andExpect(jsonPath("$.itemId").value(itemId))
                .andExpect(jsonPath("$.brand").value("Samsung"))
                .andExpect(jsonPath("$.model").value("Galaxy S8"))
                .andExpect(jsonPath("$.available").value(true));

    }
}
