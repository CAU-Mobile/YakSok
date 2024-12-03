package com.example.yaksok.query

import com.example.yaksok.feature.place.data.model.LatLng
import com.google.firebase.firestore.GeoPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class Utilities {
    companion object {
        private const val R = 6371.0 // Earth radius in kilometers

        fun getDistance(g1: GeoPoint, g2: GeoPoint): Double {
            val lat1Rad = Math.toRadians(g1.latitude)
            val lon1Rad = Math.toRadians(g1.longitude)
            val lat2Rad = Math.toRadians(g2.latitude)
            val lon2Rad = Math.toRadians(g2.longitude)

            val dLat = lat2Rad - lat1Rad
            val dLon = lon2Rad - lon1Rad

            val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            return R * c // Distance in kilometers
        }

        fun getDistance(l1: LatLng, l2: LatLng): Double {
            val lat1Rad = Math.toRadians(l1.latitude)
            val lon1Rad = Math.toRadians(l1.longitude)
            val lat2Rad = Math.toRadians(l2.latitude)
            val lon2Rad = Math.toRadians(l2.longitude)

            val dLat = lat2Rad - lat1Rad
            val dLon = lon2Rad - lon1Rad

            val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            return R * c // Distance in kilometers
        }

        fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val lat1Rad = Math.toRadians(lat1)
            val lon1Rad = Math.toRadians(lon1)
            val lat2Rad = Math.toRadians(lat2)
            val lon2Rad = Math.toRadians(lon2)

            val dLat = lat2Rad - lat1Rad
            val dLon = lon2Rad - lon1Rad

            val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            return R * c // Distance in kilometers
        }

        fun convertLatLngToGeoPoint(l: LatLng): GeoPoint {
            return GeoPoint(l.latitude, l.longitude)
        }

        fun convertGeoPointToLatLng(g: GeoPoint): LatLng {
            return LatLng(g.latitude, g.longitude)
        }
    }
}