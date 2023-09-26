package com.example.project_parking_management.Calculate;

public class Distance {
    public static final int R = 6371;

    public static double getDistance(double longtitude1, double latitude1, double longtitude2, double latitude2) {
        double dLat = (float) (Math.toRadians(latitude2 - latitude1));
        double dLong = (float) (Math.toRadians(longtitude2 - longtitude1));
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }
}


