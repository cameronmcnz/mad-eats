package com.eatrading.api.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eatrading.api.entities.Client;
import com.eatrading.api.entities.Order;
import com.eatrading.api.objects.Asset;
import com.eatrading.api.objects.Holding;
import com.eatrading.api.objects.Instrument;
import com.eatrading.api.objects.OrderRequest;
import com.eatrading.api.objects.OrderResponse;
import com.eatrading.api.objects.Status;
import com.eatrading.api.repository.ClientRepository;
import com.eatrading.api.dto.Quote;

@Service
public class OrderProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);
    private static final double MAX_PRICE_VARIANCE_PERCENT = 5.0; // 5% variance tolerance
    
    private final ClientRepository clientRepository;
    private final QuoteService quoteService;

    public OrderProcessor(ClientRepository clientRepository, QuoteService quoteService) {
        this.clientRepository = clientRepository;
        this.quoteService = quoteService;
    }

    private OrderResponse validateBuy(Order order) {
        OrderResponse resp = new OrderResponse();
        try {
            // Get current market price
            Quote quote = quoteService.getQuote(order.getAsset().getSymbol());
            double marketPrice = quote.getPrice();
            double orderPrice = order.getPrice().doubleValue();
            
            // Check if order price is too far from market price
            double variance = Math.abs((orderPrice - marketPrice) / marketPrice) * 100;
            
            if (variance > MAX_PRICE_VARIANCE_PERCENT) {
                logger.warn("Buy order variance too high: {} vs market {}. Variance: {}%", 
                    orderPrice, marketPrice, variance);
                resp.setStatusCode(Status.REJECTED);
                return resp;
            }
            
            logger.info("Buy order validated. Order price: {}, Market price: {}, Variance: {}%", 
                orderPrice, marketPrice, variance);
            resp.setStatusCode(Status.ACCEPTED);
        } catch (Exception e) {
            logger.error("Error validating buy order: {}", e.getMessage());
            resp.setStatusCode(Status.REJECTED);
        }
        return resp;
    }

    private OrderResponse validateSell(Order order) {
        OrderResponse resp = new OrderResponse();
        try {
            // Get current market price
            Quote quote = quoteService.getQuote(order.getAsset().getSymbol());
            double marketPrice = quote.getPrice();
            double orderPrice = order.getPrice().doubleValue();
            
            // Check if order price is too far from market price
            double variance = Math.abs((orderPrice - marketPrice) / marketPrice) * 100;
            
            if (variance > MAX_PRICE_VARIANCE_PERCENT) {
                logger.warn("Sell order variance too high: {} vs market {}. Variance: {}%", 
                    orderPrice, marketPrice, variance);
                resp.setStatusCode(Status.REJECTED);
                return resp;
            }
            
            logger.info("Sell order validated. Order price: {}, Market price: {}, Variance: {}%", 
                orderPrice, marketPrice, variance);
            resp.setStatusCode(Status.ACCEPTED);
        } catch (Exception e) {
            logger.error("Error validating sell order: {}", e.getMessage());
            resp.setStatusCode(Status.REJECTED);
        }
        return resp;
    }

    private OrderResponse executeOrder(Order order) {
        Optional<Client> clientOptional = clientRepository.findById(order.getClientId());
        
        if (!clientOptional.isPresent()) {
            OrderResponse resp = new OrderResponse();
            resp.setStatusCode(Status.REJECTED);
            return resp;
        }
        
        Client client = clientOptional.get();
        
        Holding newHolding = new Holding(order.getAsset(), order.getQuantity());
        Asset cashAsset = new Asset("USD", "US DOLLAR", Instrument.CASH);
        Holding cashHolding = new Holding(cashAsset, newHolding.getPurchasedValue());
        
        if (order.isBuy()) {
            client.removeHolding(cashHolding);
            client.addHolding(newHolding);
        } else {
            client.removeHolding(newHolding);
            client.addHolding(cashHolding);  
        }
        
        clientRepository.save(client);
        
        OrderResponse resp = new OrderResponse();
        resp.setStatusCode(Status.FILLED);

        return resp;
    }

    public OrderResponse process(OrderRequest request) {
        OrderResponse response;
        Order currentOrder = request.getOrder();
        currentOrder.setStatus(Status.SUBMITTED);

        if (request.getOrder().isBuy()) {
            response = validateBuy(request.getOrder());
        }
        else {
            response = validateSell(request.getOrder());
        }

        if (response.getStatusCode() == Status.REJECTED) {
            // write order to DB
            return response;
        }

        currentOrder.setStatus(Status.ACCEPTED);
        response = executeOrder(request.getOrder());

        if (response.getStatusCode() == Status.FILLED) {
            currentOrder.setStatus(Status.FILLED);
        }

        return response;
    }

}
