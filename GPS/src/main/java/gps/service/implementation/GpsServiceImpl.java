package gps.service.implementation;

import gps.service.IgpsService;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GpsServiceImpl implements IgpsService {

    /**
     * Util from the librairie that has been configured
     */
    private final GpsUtil gpsUtil = new GpsUtil();

    /**
     * Logger that's going to inform of the situation on each action
     */
    private final Logger logger = LogManager.getLogger(GpsServiceImpl.class);

    /**
     * Method for finding a user with his id
     * @param userId
     * @return the location of a user
     */
    public VisitedLocation getUserLocation(UUID userId) {
        logger.info("The user using the following id:" + userId + "is here " + gpsUtil.getUserLocation(userId));
        return gpsUtil.getUserLocation(userId);
    }

    /**
     * Method to get a list of attractions
     * @return a list of attractions
     */
    public List<Attraction> getAttractions() {
        logger.info("This is a list of all attractions: " + gpsUtil.getAttractions());
        return gpsUtil.getAttractions();
    }
}
