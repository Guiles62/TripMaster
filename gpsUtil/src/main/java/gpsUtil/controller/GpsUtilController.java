package gpsUtil.controller;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import gpsUtil.service.GpsUtilService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import gpsUtil.model.User;

public class GpsUtilController {

    GpsUtilService gpsUtilService;

    public GpsUtilController(GpsUtilService gpsUtilService) {
        this.gpsUtilService = gpsUtilService;
    }

    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(getUser(userName));
        return JsonStream.serialize(visitedLocation.location);
    }

    private User getUser(String userName) {
        return gpsUtilService.getUser(userName);
    }
}
