package com.springboot.inventory.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.inventory.dto.InventoryTable;
import com.springboot.inventory.service.InventoryService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(maxAge = 3600)
public class InventoryController {
	
	@Autowired
	InventoryService inventoryService;
		
	Logger logger = LoggerFactory.getLogger(InventoryController.class);
		
  
	@PostMapping(value = "/addInventory")
	public Map<String, Object> addInventory(@RequestBody InventoryTable userData) throws Exception {
		Map<String, Object> response = new HashMap();

		String status = inventoryService.addInventory(userData);

		if (status.equalsIgnoreCase("Success")) {
			response.put("ResponseCode", "200");
			response.put("ResponseText", "Inventory has been successfully added");
		} else {
			response.put("ResponseCode", "400");
			response.put("ResponseText", "Failed to add inventory");
		}
		logger.info("Add inventory response:{", response);

		return response;
	}

	@DeleteMapping(value = "/removeInventory/{itemId}/")
	public Map<String, Object> removeInventory(@PathVariable String itemId) throws Exception {
		Map<String, Object> response = new HashMap();

		String status = inventoryService.removeInventory(itemId);

		if (status.equalsIgnoreCase("Success")) {
			response.put("ResponseCode", "204");
			response.put("ResponseText", "Inventory has been removed successfully");
		} else {
			response.put("ResponseCode", "400");
			response.put("ResponseText", "Failed to remove inventory");
		}
		logger.info("Remove Inventory response:{}", response);

		return response;
	}


	@GetMapping(value = "/getInventory")
	public Map<String, Object> viewInventory(@RequestParam(name = "itemId", required = true) String itemId)
			throws Exception {
		Map<String, Object> response = new HashMap();

		InventoryTable inventoryData = inventoryService.viewInventory(itemId);

		if (!CollectionUtils.isEmpty((Collection<?>) inventoryData)) {
			response.put("ResponseCode", "200");
			response.put("ResponseText", "Success");
			response.put("ResponseBody", inventoryData);
		} else {
			response.put("ResponseCode", "300");
			response.put("ResponseText", "Failed");
		}
		logger.info("ViewInventory response:{}", response);

		return response;
	}
}

