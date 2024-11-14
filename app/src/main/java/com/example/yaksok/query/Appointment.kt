package com.example.yaksok.query

import android.annotation.SuppressLint
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.toObject

data class Appointment(
    val name: String = "",
    val geoPoint: GeoPoint = GeoPoint(0.0, 0.0),
    val time: Timestamp = Timestamp.now(),
    val memberIds: List<String> = emptyList()
)

class AppointmentQuery {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private val db = FirebaseFirestore.getInstance()
        private val appointmentsCollection = db.collection("appointments")

        fun createAppointment(
            name: String,
            geoPoint: GeoPoint,
            time: Timestamp,
            memberIds: List<String>,
            callBack: (Boolean, String?, String?) -> Unit
        ) {
            appointmentsCollection.add(Appointment(name, geoPoint, time, memberIds))
                .addOnSuccessListener {
                    callBack(true, it.id, "Create Success!")
                }
                .addOnFailureListener {
                    callBack(false, null, it.toString())
                }
        }

        fun getAppointment(
            appointmentId: String,
            callBack: (Boolean, Appointment?, String?) -> Unit
        ) {
            appointmentsCollection.document(appointmentId).get()
                .addOnSuccessListener {
                    callBack(true, it.toObject<Appointment>(), "Get Success!")
                }
                .addOnFailureListener {
                    callBack(false, null, it.toString())
                }
        }

        fun getAppointmentsWithUserId(
            memberId: String,
            callBack: (Boolean, Map<String, Appointment>?, String?) -> Unit
        ) {
            appointmentsCollection.whereArrayContains("userIds", memberId).get()
                .addOnSuccessListener { appointments ->
                    if (appointments.size() == 0) {
                        callBack(false, null, "No Appointments!")
                    } else {
                        val appointmentsList = HashMap<String, Appointment>()
                        appointments.forEach { appointmentsList[it.id] = it.toObject<Appointment>() }
                        callBack(true, appointmentsList.toMap(), "Get Success!")
                    }
                }
                .addOnFailureListener {
                    callBack(false, null, it.toString())
                }
        }

        fun deleteAppointment(
            appointmentId: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            appointmentsCollection.document(appointmentId).delete()
                .addOnSuccessListener {
                    callBack(true, "Delete Success!")
                }
                .addOnFailureListener {
                    callBack(false, it.toString())
                }
        }

        fun updateAppointment(
            appointmentId: String,
            name: String? = null,
            geoPoint: GeoPoint? = null,
            time: Timestamp? = null,
            memberIds: List<String>? = null,
            callBack: (Boolean, String?) -> Unit
        ) {
            appointmentsCollection.document(appointmentId).get()
                .addOnSuccessListener {
                    val originAppointment = it.toObject<Appointment>()
                    appointmentsCollection.document(appointmentId).update(
                        "name", name ?: originAppointment?.name,
                        "geoPoint", geoPoint ?: originAppointment?.geoPoint,
                        "time", time ?: originAppointment?.time,
                        "userIds", memberIds ?: originAppointment?.memberIds
                    )
                        .addOnSuccessListener {
                            callBack(true, "Update Success!")
                        }
                        .addOnFailureListener { e ->
                            callBack(false, e.toString())
                        }
                }
                .addOnFailureListener {
                    callBack(false, it.toString())
                }
        }

        fun addAppointmentMember(
            appointmentId: String,
            memberId: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            appointmentsCollection.document(appointmentId).get()
                .addOnSuccessListener {
                    val originAppointment = it.toObject<Appointment>()
                    val memberIds = originAppointment?.memberIds?.toMutableList()
                    memberIds?.add(memberId)

                    appointmentsCollection.document(appointmentId).update("userIds", memberIds)
                        .addOnSuccessListener {
                            callBack(true, "Add Success!")
                        }
                        .addOnFailureListener { e ->
                            callBack(false, e.toString())
                        }
                }
                .addOnFailureListener {
                    callBack(false, it.toString())
                }
        }

        fun deleteAppointmentMember(
            appointmentId: String,
            memberId: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            appointmentsCollection.document(appointmentId).get()
                .addOnSuccessListener {
                    val originAppointment = it.toObject<Appointment>()
                    val memberIds = originAppointment?.memberIds?.toMutableList()
                    if (memberIds?.remove(memberId) == true) {
                        appointmentsCollection.document(appointmentId).update("userIds", memberIds)
                            .addOnSuccessListener {
                                callBack(true, "Delete Success!")
                            }
                            .addOnFailureListener { e ->
                                callBack(false, e.toString())
                            }
                    } else {
                        callBack(false, "Cannot find member Id!")
                    }
                }
                .addOnFailureListener {
                    callBack(false, it.toString())
                }
        }
    }
}